package com.smartcar.sdk.rpc.ble

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.smartcar.sdk.rpc.JsonRpcRequest
import com.smartcar.sdk.rpc.JsonRpcResult
import com.smartcar.sdk.rpc.RPCInterface
import com.smartcar.sdk.rpc.RpcException
import com.smartcar.sdk.util.awaitMultiplePermissionsResult
import com.welie.blessed.BluetoothCentralManager
import com.welie.blessed.BluetoothPeripheral
import com.welie.blessed.WriteType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import java.security.InvalidParameterException
import java.util.UUID

class BLEService(
    private val activity: ComponentActivity,
    webView: WebView,
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
    private var central = BluetoothCentralManager(activity)

    @ExperimentalStdlibApi
    override suspend fun handleRequest(request: JsonRpcRequest): JsonRpcResult {
        return when (request) {
            is StartScanRequest -> {
                checkShit()
                central.scanForPeripherals(::onScanResult) { }
                SuccessResult()
            }
            is StopScanRequest -> {
                central.stopScan()
                SuccessResult()
            }
            is ConnectRequest -> {
                val peripheral = central.getPeripheral(request.params.address)
                central.connectPeripheral(peripheral)
                SuccessResult()
            }
            is DisconnectRequest -> {
                val peripheral = central.getPeripheral(request.params.address)
                central.cancelConnection(peripheral)
                SuccessResult()
            }
            is ReadCharacteristicRequest -> {
                val peripheral = central.getPeripheral(request.params.address)
                val serviceUUID = UUID.fromString(request.params.serviceUUID)
                val characteristicUUID = UUID.fromString(request.params.characteristicUUID)
                val bytes = peripheral.readCharacteristic(serviceUUID, characteristicUUID)
                ReadCharacteristicResult(bytes.toHexString())
            }
            is WriteCharacteristicRequest -> {
                val peripheral = central.getPeripheral(request.params.address)
                val serviceUUID = UUID.fromString(request.params.serviceUUID)
                val characteristicUUID = UUID.fromString(request.params.characteristicUUID)
                val bytes = request.params.value.hexToByteArray()
                peripheral.writeCharacteristic(serviceUUID, characteristicUUID, bytes, WriteType.WITH_RESPONSE)
                SuccessResult()
            }
            is StartNotificationsRequest -> {
                val peripheral = central.getPeripheral(request.params.address)
                val serviceUUID = UUID.fromString(request.params.serviceUUID)
                val characteristicUUID = UUID.fromString(request.params.characteristicUUID)
                peripheral.getCharacteristic(serviceUUID, characteristicUUID)?.let {
                    peripheral.observe(it) { value: ByteArray ->
                        val req = NotifyRequest(
                            method = "notify",
                            params = NotifyRequest.NotifyParams(
                                address = request.params.address,
                                serviceUUID = request.params.serviceUUID,
                                characteristicUUID = request.params.characteristicUUID,
                                value=value.toHexString(),
                            )
                        )
                        sendToWebView(Json.encodeToString(req))
                    }
                } ?: {
                    throw RpcException(-32099, "Characteristic not found")
                }
                SuccessResult()
            }
            is StopNotificationsRequest -> {
                val peripheral = central.getPeripheral(request.params.address)
                val serviceUUID = UUID.fromString(request.params.serviceUUID)
                val characteristicUUID = UUID.fromString(request.params.characteristicUUID)
                peripheral.getCharacteristic(serviceUUID, characteristicUUID)!!.let {
                    peripheral.stopObserving(it)
                }
                SuccessResult()
            }
            else -> {
                throw InvalidParameterException()
            }
        }
    }

    @SuppressLint("MissingPermission")
    @ExperimentalStdlibApi
    fun onScanResult(peripheral: BluetoothPeripheral, result: ScanResult) {
        val device = result.device
        Log.d("BLEService", "Found device: ${device.name} - ${device.address}")

        val scanRecord = result.scanRecord

        // Extract Service Data
        val serviceDataMap = mutableMapOf<String, String>()
        scanRecord?.serviceData?.let { serviceData ->
            for ((uuid, data) in serviceData) {
                serviceDataMap[uuid.toString()] = data.toHexString()
            }
        }

        // Extract Manufacturer Data
        val manufacturerDataMap = mutableMapOf<Int, String>()
        scanRecord?.manufacturerSpecificData?.let { manufacturerData ->
            for (i in 0 until manufacturerData.size()) {
                val key = manufacturerData.keyAt(i)
                val dataBytes = manufacturerData.valueAt(i)
                val dataHex = dataBytes.toHexString()
                manufacturerDataMap[key] = dataHex
            }
        }

        // Extract Advertised Service UUIDs
        val advertisedServiceUUIDs = scanRecord?.serviceUuids?.map { it.toString() } ?: emptyList()

        // Create DeviceInfo instance
        val deviceInfo = DeviceInfoRequest(method = "deviceFound",
            params = DeviceInfoRequest.DeviceInfoParams(
                name = device.name.orEmpty(),
                address = device.address,
                rssi = result.rssi,
                manufacturerData = manufacturerDataMap,
                serviceData = serviceDataMap,
                advertisedServiceUUIDs = advertisedServiceUUIDs,
            )
        )

        // Send the JSON data to the WebView
        val jsonResponse = Json.encodeToString(deviceInfo)
        sendToWebView(jsonResponse)
    }

    private suspend fun checkShit() {
        central.isBluetoothEnabled
        requestPermissions()
        // location enabled
    }

    private suspend fun requestPermissions(): Boolean {
        // Define the required permissions based on SDK version
        val requiredPermissions = mutableListOf<String>().apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                add(android.Manifest.permission.BLUETOOTH_SCAN)
                add(android.Manifest.permission.BLUETOOTH_CONNECT)
            } else {
                add(android.Manifest.permission.BLUETOOTH)
            }
            add(android.Manifest.permission.ACCESS_FINE_LOCATION)
            add(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        // Filter out the permissions that are not yet granted
        val permissionsToRequest = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }

        // Request permissions if any are missing
        return activity.awaitMultiplePermissionsResult(
            permissionsToRequest.toTypedArray()).all { it.value }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        central.close()
        super.onDestroy(owner)
    }
}
