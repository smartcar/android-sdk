package com.smartcar.sdk.rpc.ble

import com.smartcar.sdk.rpc.JsonRpcRequest
import com.smartcar.sdk.rpc.JsonRpcResult
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
@SerialName("startScan")
data class StartScanRequest(
    val params: JsonObject
) : JsonRpcRequest()

@Serializable
@SerialName("stopScan")
data class StopScanRequest(
    val params: JsonObject
) : JsonRpcRequest()

@Serializable
class SuccessResult : JsonRpcResult()

@Serializable
@SerialName("readCharacteristic")
data class ReadCharacteristicRequest(
    val params: CharacteristicParams
) : JsonRpcRequest()

@Serializable
data class ReadCharacteristicResult(
    val value: String
) : JsonRpcResult()

@Serializable
@SerialName("writeCharacteristic")
data class WriteCharacteristicRequest(
    val params: WriteCharacteristicParams
) : JsonRpcRequest() {
    @Serializable
    data class WriteCharacteristicParams(
        val address: String,
        val serviceUUID: String,
        val characteristicUUID: String,
        val value: String,
    )
}

@Serializable
@SerialName("startNotifications")
data class StartNotificationsRequest(
    val params: CharacteristicParams
) : JsonRpcRequest()

@Serializable
@SerialName("stopNotifications")
data class StopNotificationsRequest(
    val params: CharacteristicParams
) : JsonRpcRequest()

@Serializable
data class CharacteristicParams(
    val address: String,
    val serviceUUID: String,
    val characteristicUUID: String,
)

@Serializable
@SerialName("connectGATT")
data class ConnectRequest(
    val params: AddressParams
) : JsonRpcRequest()

@Serializable
@SerialName("disconnectGATT")
data class DisconnectRequest(
    val params: AddressParams
) : JsonRpcRequest()

@Serializable
data class AddressParams(
    val address: String,
)

@Serializable
data class DeviceInfoRequest(
    val jsonrpc: String = "2.0",
    val method: String,
    val params: DeviceInfoParams,
) {
    @Serializable
    data class DeviceInfoParams(
        val name: String,
        val address: String,
        val rssi: Int,
        val serviceData: Map<String, String>,
        val manufacturerData: Map<Int, String>,
        val advertisedServiceUUIDs: List<String>,
    )
}

@Serializable
data class NotifyRequest(
    val jsonrpc: String = "2.0",
    val method: String,
    val params: NotifyParams,
) {
    @Serializable
    data class NotifyParams(
        val address: String,
        val serviceUUID: String,
        val characteristicUUID: String,
        val value: String,
    )
}
