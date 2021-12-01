package com.huawei.agc.gifttracker.model

import android.provider.BaseColumns

interface BaseObject : BaseColumns {
    val TABLE: String
    var id: String
}