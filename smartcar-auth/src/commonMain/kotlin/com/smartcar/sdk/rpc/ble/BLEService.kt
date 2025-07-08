package com.smartcar.sdk.rpc.ble

import com.juul.kable.Advertisement
import com.juul.kable.NotConnectedException
import com.juul.kable.Peripheral
import com.juul.kable.Scanner
import com.juul.kable.WriteType
import com.juul.kable.characteristicOf
import com.smartcar.sdk.bridge.ContextBridge
import com.smartcar.sdk.bridge.WebViewBridge
import com.smartcar.sdk.bridge.isBonded
import com.smartcar.sdk.bridge.isPeerRemovedState
import com.smartcar.sdk.bridge.requestMtuIfAvailable
import com.smartcar.sdk.rpc.JsonRpcRequest
import com.smartcar.sdk.rpc.JsonRpcResult
import com.smartcar.sdk.rpc.RPCInterface
import com.smartcar.sdk.rpc.RpcException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalStdlibApi::class, ExperimentalUuidApi::class)
class BLEService(
    private val context: ContextBridge,
    webView: WebViewBridge,
) : RPCInterface(
    "SmartcarSDKBLE",
    webView,
    SerializersModule {
        // Register request classes
        polymorphic(JsonRpcRequest::class) {
            subclass(StartScanRequest::class)
            subclass(StopScanRequest::class)
            subclass(ConnectRequest::class)
            subclass(DisconnectRequest::class)
            subclass(ReadCharacteristicRequest::class)
            subclass(WriteCharacteristicRequest::class)
            subclass(StartNotificationsRequest::class)
            subclass(StopNotificationsRequest::class)
        }
        // Register result classes
        polymorphic(JsonRpcResult::class) {
            subclass(SuccessResult::class)
            subclass(ConnectResult::class)
            subclass(ReadCharacteristicResult::class)
        }
    }
) {
    private var scanJob: Job? = null
    private var peripherals = mutableMapOf<String, Peripheral>()

    private val notificationJobs = mutableMapOf<NotificationKey, Job>()
    data class NotificationKey(
        val address: String,
        val serviceUuid: Uuid,
        val characteristicUuid: Uuid
    )

    override suspend fun handleRequest(request: JsonRpcRequest): JsonRpcResult {
        return when (request) {
            is StartScanRequest -> {
                when (context.getBLEAvailability()) {
                    Availability.BluetoothOff ->
                        throw RpcException(-32011, "Bluetooth turned off")
                    Availability.PermissionDenied ->
                        throw RpcException(-32012, "Bluetooth permission not granted")
                    Availability.Unknown ->
                        throw RpcException(-32013, "Bluetooth not available")
                    Availability.LocationServicesDisabled ->
                        throw RpcException(-32014, "Location services disabled")
                    Availability.Available -> {}
                }

                scanJob = scope.launch {
                    val scanner = Scanner {}
                    scanner.advertisements
                        .catch { }
                        .collect { advertisement ->
                            onAdvertisement(advertisement)
                            if (!peripherals.contains(advertisement.identifier.toString())) {
                                peripherals[advertisement.identifier.toString()] = Peripheral(advertisement)
                            }
                        }
                }
                SuccessResult()
            }
            is StopScanRequest -> {
                scanJob?.cancelAndJoin()
                SuccessResult()
            }
            is ConnectRequest -> {
                val peripheral = getPeripheral(request.params.address)
                try {
                    peripheral.connect()
                } catch (e: NotConnectedException) {
                    // Handle "Peer removed pairing information" on iOS
                    val st = peripheral.state.value
                    if (st.isPeerRemovedState()) {
                        throw RpcException(-32015, "Peer removed pairing information")
                    }
                    throw e
                }

                // Negotiate and return MTU
                peripheral.requestMtuIfAvailable()
                val mtu = peripheral.maximumWriteValueLengthForType(WriteType.WithResponse)

                // Return service discovery results
                val services = peripheral.services.value!!.map { s ->
                    ConnectResult.Service(
                        s.serviceUuid.toString(),
                        s.characteristics.map { c ->
                            ConnectResult.Characteristic(
                                c.characteristicUuid.toString(),
                                c.properties.value
                            )
                        }
                    )
                }

                ConnectResult(mtu, services)
            }
            is DisconnectRequest -> {
                val peripheral = getPeripheral(request.params.address)
                peripheral.disconnect()
                // Remove peripheral state
                peripheral.close()
                peripherals.remove(request.params.address)
                SuccessResult()
            }
            is ReadCharacteristicRequest -> {
                val peripheral = getPeripheral(request.params.address)
                val characteristic = characteristicOf(
                    Uuid.parse(request.params.serviceUUID),
                    Uuid.parse(request.params.characteristicUUID)
                )
                val bytes = peripheral.read(characteristic)
                ReadCharacteristicResult(bytes.toHexString())
            }
            is WriteCharacteristicRequest -> {
                val peripheral = getPeripheral(request.params.address)
                val characteristic = characteristicOf(
                    Uuid.parse(request.params.serviceUUID),
                    Uuid.parse(request.params.characteristicUUID)
                )
                val bytes = request.params.value.hexToByteArray()
                peripheral.write(characteristic, bytes, WriteType.WithResponse)
                SuccessResult()
            }
            is StartNotificationsRequest -> {
                val peripheral = getPeripheral(request.params.address)
                val characteristic = characteristicOf(
                    Uuid.parse(request.params.serviceUUID),
                    Uuid.parse(request.params.characteristicUUID)
                )

                // Create a unique key for this notifications subscription.
                val key = NotificationKey(
                    request.params.address,
                    Uuid.parse(request.params.serviceUUID),
                    Uuid.parse(request.params.characteristicUUID)
                )

                // Ignore if already observing
                if (notificationJobs.contains(key)) {
                    throw RpcException(-32099, "Already observing characteristic")
                }

                // Launch a coroutine to collect notifications and store its Job.
                val observationEstablished = CompletableDeferred<Unit>()
                val job = peripheral.observe(characteristic) {
                    observationEstablished.complete(Unit)
                }.catch {
                    observationEstablished.completeExceptionally(
                        RpcException(-32099, it.message)
                    )
                }.onEach { value ->
                    val req = NotifyRequest(
                        method = "notify",
                        params = NotifyRequest.NotifyParams(
                            address = request.params.address,
                            serviceUUID = request.params.serviceUUID,
                            characteristicUUID = request.params.characteristicUUID,
                            value = value.toHexString(),
                        )
                    )
                    sendToWebView(Json.encodeToString(req))
                }.launchIn(peripheral.scope)

                // Keep track of notification job
                notificationJobs[key] = job
                job.invokeOnCompletion {
                    notificationJobs.remove(key)
                }

                // Wait until the observation has started.
                observationEstablished.await()
                SuccessResult()
            }
            is StopNotificationsRequest -> {
                // Build the same key used in StartNotificationsRequest.
                val key = NotificationKey(
                    request.params.address,
                    Uuid.parse(request.params.serviceUUID),
                    Uuid.parse(request.params.characteristicUUID)
                )

                // Retrieve and cancel the corresponding notifications job, if it exists.
                notificationJobs[key]?.cancel()
                SuccessResult()
            }
            else -> {
                throw IllegalArgumentException()
            }
        }
    }

    private fun getPeripheral(address: String): Peripheral {
        return peripherals[address]
            ?: throw RpcException(-32099, "Peripheral not found for address: $address")
    }

    private fun onAdvertisement(advertisement: Advertisement) {
        // Extract device name (fallback to empty if null)
        val deviceName = advertisement.name.orEmpty()

        // Convert identifier (Uuid on Apple, MAC address on Android, etc.) to string
        val deviceAddress = advertisement.identifier

        // Extract RSSI
        val rssi = advertisement.rssi

        // Gather Manufacturer Data
        val manufacturerDataMap = mutableMapOf<Int, String>()
        advertisement.manufacturerData?.let {
            manufacturerDataMap[it.code] = it.data.toHexString()
        }

        // Gather Service Data
        val serviceDataMap = mutableMapOf<String, String>()
        advertisement.uuids.forEach { uuid ->
            advertisement.serviceData(uuid)?.let { data ->
                serviceDataMap[uuid.toString()] = data.toHexString()
            }
        }

        // Extract advertised service UUIDs
        val advertisedServiceUUIDs = advertisement.uuids.map { it.toString() }

        // Create DeviceInfoRequest instance
        val deviceInfo = DeviceInfoRequest(
            method = "deviceInfo",
            params = DeviceInfoRequest.DeviceInfoParams(
                name = deviceName,
                address = deviceAddress.toString(),
                rssi = rssi,
                manufacturerData = manufacturerDataMap,
                serviceData = serviceDataMap,
                advertisedServiceUUIDs = advertisedServiceUUIDs,
                isBonded = advertisement.isBonded,  // only available on Android
            )
        )

        // Send to WebView
        val jsonResponse = Json.encodeToString(deviceInfo)
        sendToWebView(jsonResponse)
    }

    override fun dispose() {
        super.dispose()
        peripherals.values.forEach {
            it.scope.launch {
                it.disconnect()
                it.close()
            }
        }
        scanJob?.cancel()
    }
}
