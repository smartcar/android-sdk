package com.smartcar.sdk.bridge

import com.juul.kable.State
import platform.CoreBluetooth.CBErrorPeerRemovedPairingInformation

actual fun State.isPeerRemovedState(): Boolean =
    ((this as? State.Disconnected)?.status as? State.Disconnected.Status.Unknown)
        ?.status == CBErrorPeerRemovedPairingInformation.toInt()
