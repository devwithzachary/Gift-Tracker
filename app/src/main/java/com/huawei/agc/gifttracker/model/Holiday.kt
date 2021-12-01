package com.huawei.agc.gifttracker.model

object Holiday : BaseObject{
    override val TABLE = "holiday"
    const val TITLE = "title"
    const val DATE = "date"

    override var id: String = ""
    var title: String = ""
    var date: String = ""
}