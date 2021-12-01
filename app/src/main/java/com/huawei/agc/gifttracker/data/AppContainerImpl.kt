package com.huawei.agc.gifttracker.data

import android.content.Context
import com.huawei.agc.gifttracker.data.gifts.GiftsRepository
import com.huawei.agc.gifttracker.data.gifts.impl.LocalGiftsRepository
import com.huawei.agc.gifttracker.data.holiday.HolidaysRepository
import com.huawei.agc.gifttracker.data.holiday.impl.LocalHolidaysRepository
import com.huawei.agc.gifttracker.data.recipient.RecipientsRepository
import com.huawei.agc.gifttracker.data.recipient.impl.LocalRecipientRepository
import com.huawei.agc.gifttracker.data.user.UsersRepository
import com.huawei.agc.gifttracker.data.user.impl.LocalUsersRepository

interface AppContainer {
    val dbHelper: DbHelper
    val giftsRepository: GiftsRepository
    val usersRepository: UsersRepository
    val holidaysRepository: HolidaysRepository
    val recipientRepository: RecipientsRepository
}

class AppContainerImpl(applicationContext: Context) : AppContainer {

    override val dbHelper =  DbHelper(applicationContext)

    override val usersRepository: UsersRepository by lazy {
        LocalUsersRepository(dbHelper)
    }

    override val holidaysRepository: HolidaysRepository by lazy {
        LocalHolidaysRepository(dbHelper)
    }

    override val recipientRepository: RecipientsRepository by lazy {
        LocalRecipientRepository(dbHelper)
    }

    override val giftsRepository: GiftsRepository by lazy {
        LocalGiftsRepository(dbHelper, holidaysRepository, recipientRepository)
    }
}