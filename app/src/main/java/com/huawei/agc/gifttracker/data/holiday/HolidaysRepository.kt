package com.huawei.agc.gifttracker.data.holiday

import android.content.Context
import com.huawei.agc.gifttracker.data.Result
import com.huawei.agc.gifttracker.model.Holiday

interface HolidaysRepository {
    suspend fun getHoliday(holidayId: String?,  context: Context): Result<Holiday>

    suspend fun getHolidays( context: Context): Result<MutableList<Holiday>>

    suspend fun insertHoliday(holiday: Holiday): Result<Holiday>

    suspend fun updateHoliday(holiday: Holiday): Result<Holiday>

    suspend fun deleteHoliday(holiday: Holiday): Result<Holiday>
}