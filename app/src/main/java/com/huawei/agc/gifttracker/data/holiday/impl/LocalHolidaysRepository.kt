package com.huawei.agc.gifttracker.data.holiday.impl

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import com.huawei.agc.gifttracker.data.DbHelper
import com.huawei.agc.gifttracker.data.Result
import com.huawei.agc.gifttracker.data.holiday.HolidaysRepository
import com.huawei.agc.gifttracker.model.Gift
import com.huawei.agc.gifttracker.model.Holiday
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalHolidaysRepository(private val dbHelper: DbHelper) : HolidaysRepository {
    override suspend fun getHoliday(holidayId: String?, context: Context): Result<Holiday> {
        return withContext(Dispatchers.IO) {
            val cursor = dbHelper.getObjectByID(Holiday.TABLE, holidayId)
            val holidays = holidaysFromCursor(cursor)
            dbHelper.close()
            if (holidays.size != 1) {
                Result.Error(IllegalArgumentException("Holiday not found"))
            } else {
                Result.Success(holidays[0])
            }
        }
    }

    override suspend fun getHolidays(context: Context): Result<MutableList<Holiday>> {
        return withContext(Dispatchers.IO) {
            val cursor = dbHelper.getAllObjects(Holiday.TABLE)
            val holidays = holidaysFromCursor(cursor)
            dbHelper.close()
            if (holidays.size < 1) {
                Result.Error(IllegalArgumentException("Holidays not found"))
            } else {
                Result.Success(holidays)
            }
        }
    }

    override suspend fun insertHoliday(holiday: Holiday): Result<Holiday> {
        return withContext(Dispatchers.IO) {
            val db = dbHelper.writableDatabase

            val values = ContentValues().apply {
                put(Holiday.TITLE, holiday.title)
                put(Holiday.DATE, holiday.date)
            }

            val newRowId = db?.insert(Gift.TABLE, null, values)
            holiday.id = newRowId.toString()
            dbHelper.close()
            Result.Success(holiday)
        }
    }

    override suspend fun updateHoliday(holiday: Holiday): Result<Holiday> {
        return withContext(Dispatchers.IO) {
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put(Holiday.TITLE, holiday.title)
                put(Holiday.DATE, holiday.date)
            }

            val selection = "${BaseColumns._ID} LIKE ?"
            val selectionArgs = arrayOf(holiday.id)
            db.update(
                Holiday.TABLE,
                values,
                selection,
                selectionArgs)
            dbHelper.close()
            Result.Success(holiday)
        }
    }

    override suspend fun deleteHoliday(holiday: Holiday): Result<Holiday> {
        return withContext(Dispatchers.IO) {
            dbHelper.deleteObject(Holiday.TABLE, holiday.id)
            dbHelper.close()
            Result.Success(holiday)
        }
    }

    private fun holidaysFromCursor(cursor: Cursor): MutableList<Holiday> {
        val holidays = mutableListOf<Holiday>()
        with(cursor) {
            while (moveToNext()) {
                val holiday = Holiday
                holiday.id = getString(getColumnIndexOrThrow(BaseColumns._ID))
                holiday.title = getString(getColumnIndexOrThrow(Holiday.TITLE))
                holiday.date = getString(getColumnIndexOrThrow(Holiday.DATE))
                holidays.add(holiday)
            }
        }
        cursor.close()
        return holidays
    }
}