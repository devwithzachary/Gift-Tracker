package com.huawei.agc.gifttracker.model

import android.provider.BaseColumns

object User : BaseObject {

    override val TABLE = "user"
    const val NAME = "name"
    const val EMAIL = "email"
    const val PASSWORD = "password"

    override var id: String = ""
    var name: String = ""
    var email: String = ""
    var password: String = ""
}