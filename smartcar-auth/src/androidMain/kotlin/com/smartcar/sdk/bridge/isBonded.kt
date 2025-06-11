package com.smartcar.sdk.bridge

import com.juul.kable.Advertisement
import com.juul.kable.PlatformAdvertisement

actual val Advertisement.isBonded: Boolean?
    get() = (this as PlatformAdvertisement).bondState == PlatformAdvertisement.BondState.Bonded
