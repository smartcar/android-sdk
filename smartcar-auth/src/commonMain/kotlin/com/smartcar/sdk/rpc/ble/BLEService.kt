package com.smartcar.sdk.rpc.ble

import co.touchlab.kermit.Logger
import com.juul.kable.Advertisement
import com.juul.kable.Peripheral
import com.juul.kable.Scanner
import com.juul.kable.characteristicOf
import com.smartcar.sdk.rpc.JsonRpcRequest
import com.smartcar.sdk.rpc.JsonRpcResult
import com.smartcar.sdk.rpc.RPCInterface
import com.smartcar.sdk.rpc.RpcException
import com.smartcar.sdk.bridge.WebViewBridge
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

class BLEService(
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
            subclass(ReadCharacteristicResult::class)
        }
    }
) {
    private var scanJob: Job? = null
    private var peripherals = mutableMapOf<String, Peripheral>()
    private var observations = mutableMapOf<String, Peripheral>()

    @ExperimentalStdlibApi
    override suspend fun handleRequest(request: JsonRpcRequest): JsonRpcResult {
        return when (request) {
            is StartScanRequest -> {
                // TODO convert to requestDevice
                scanJob = scope.launch {
                    val scanner = Scanner {}
                    scanner.advertisements.collect { advertisement ->
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
                peripheral.connect()
                // TODO: max mtu
                SuccessResult()
            }
            is DisconnectRequest -> {
                val peripheral = getPeripheral(request.params.address)
                peripheral.disconnect()
                SuccessResult()
            }
            is ReadCharacteristicRequest -> {
                val peripheral = getPeripheral(request.params.address)
                val characteristic = characteristicOf(request.params.serviceUUID,
                    request.params.characteristicUUID)
                val bytes = peripheral.read(characteristic)
                ReadCharacteristicResult(bytes.toHexString())
            }
            is WriteCharacteristicRequest -> {
                val peripheral = getPeripheral(request.params.address)
                val characteristic = characteristicOf(request.params.serviceUUID,
                    request.params.characteristicUUID)
                val bytes = request.params.value.hexToByteArray()
                peripheral.write(characteristic, bytes, com.juul.kable.WriteType.WithResponse)
                SuccessResult()
            }
            is StartNotificationsRequest -> {
                val peripheral = getPeripheral(request.params.address)
                val characteristic = characteristicOf(request.params.serviceUUID,
                    request.params.characteristicUUID)

                val observationEstablished = CompletableDeferred<Unit>()

                peripheral.launch {
                    peripheral.observe(characteristic).onStart {
                        observationEstablished.complete(Unit)
                    }.catch {
                        observationEstablished.completeExceptionally(
                            RpcException(-32099, it.message)
                        )
                    }.collect {
                        val req = NotifyRequest(
                            method = "notify",
                            params = NotifyRequest.NotifyParams(
                                address = request.params.address,
                                serviceUUID = request.params.serviceUUID,
                                characteristicUUID = request.params.characteristicUUID,
                                value=it.toHexString(),
                            )
                        )
                        sendToWebView(Json.encodeToString(req))
                    }
                }

                observationEstablished.await()
                SuccessResult()
            }
            is StopNotificationsRequest -> {
                val peripheral = getPeripheral(request.params.address)
                val characteristic = characteristicOf(request.params.serviceUUID,
                    request.params.characteristicUUID)
                //peripheral.o
                SuccessResult()
            }
            else -> {
                throw IllegalArgumentException()
            }
        }
    }

    private fun getPeripheral(address: String): Peripheral {
        return peripherals[address] ?: throw RpcException(-32099, "Peripheral not found for address: $address")
    }

    @ExperimentalStdlibApi
    fun onAdvertisement(advertisement: Advertisement) {
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
            )
        )

        // Log or print if desired
        Logger.d("BLEService") { "Found device: $deviceName - $deviceAddress" }

        // Send to WebView
        val jsonResponse = Json.encodeToString(deviceInfo)
        sendToWebView(jsonResponse)
    }

    override fun dispose() {
        super.dispose()
        peripherals.values.forEach {
            it.launch {
                it.disconnect()
                it.cancel()
            }
        }
        scanJob?.cancel()
    }
}
