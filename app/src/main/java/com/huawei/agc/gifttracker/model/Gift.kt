package com.huawei.agc.gifttracker.model

import android.provider.BaseColumns

object Gift : BaseObject {

    override val TABLE = "gift"
    const val TITLE = "title"
    const val URL = "url"
    const val HOLIDAY = "holiday_id"
    const val RECIPIENT = "recipient_id"

    override var id: String = ""
    var title: String = ""
    var url: String = ""
    var holiday = Holiday
    var recipient = Recipient
}
