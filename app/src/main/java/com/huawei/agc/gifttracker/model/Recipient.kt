package com.huawei.agc.gifttracker.model

object Recipient : BaseObject{
    override val TABLE = "recipient"
    const val NAME = "name"

    override var id: String = ""
    var name: String = ""
}