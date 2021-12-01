package com.huawei.agc.gifttracker.data.gifts.impl

import android.content.ContentValues
import com.huawei.agc.gifttracker.data.Result
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import com.huawei.agc.gifttracker.data.DbHelper
import com.huawei.agc.gifttracker.data.gifts.GiftsRepository
import com.huawei.agc.gifttracker.data.holiday.HolidaysRepository
import com.huawei.agc.gifttracker.data.recipient.RecipientsRepository
import com.huawei.agc.gifttracker.data.successOr
import com.huawei.agc.gifttracker.model.Gift
import com.huawei.agc.gifttracker.model.Recipient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalGiftsRepository(private val dbHelper: DbHelper,
                           private val holidaysRepository: HolidaysRepository,
                           private val recipientRepository: RecipientsRepository) : GiftsRepository {

    override suspend fun getGift(giftId: String?, context: Context): Result<Gift> {
        return withContext(Dispatchers.IO) {
            val cursor = dbHelper.getObjectByID(Gift.TABLE, giftId)
            val gifts = giftsFromCursor(cursor, context)
            dbHelper.close()
            if (gifts.size != 1) {
                Result.Error(IllegalArgumentException("Gift not found"))
            } else {
                Result.Success(gifts[0])
            }
        }
    }

    override suspend fun getGifts( context: Context): Result<MutableList<Gift>> {
        return withContext(Dispatchers.IO) {
            val cursor = dbHelper.getAllObjects(Gift.TABLE)
            val gifts = giftsFromCursor(cursor, context)
            dbHelper.close()
            if (gifts.size < 1) {
                Result.Error(IllegalArgumentException("Gifts not found"))
            } else {
                Result.Success(gifts)
            }
        }
    }

    override suspend fun insertGift(gift: Gift): Result<Gift> {

        return withContext(Dispatchers.IO) {
            val db = dbHelper.writableDatabase
            val values = valuesFromGift(gift)

            val newRowId = db?.insert(Gift.TABLE, null, values)
            gift.id = newRowId.toString()
            dbHelper.close()
            Result.Success(gift)
        }
    }

    override suspend fun updateGift(gift: Gift): Result<Gift> {
        return withContext(Dispatchers.IO) {
            val db = dbHelper.writableDatabase
            val values = valuesFromGift(gift)

            val selection = "${BaseColumns._ID} LIKE ?"
            val selectionArgs = arrayOf(gift.id)
            db.update(
                Gift.TABLE,
                values,
                selection,
                selectionArgs)
            dbHelper.close()
            Result.Success(gift)
        }
    }

    override suspend fun deleteGift(gift: Gift): Result<Gift> {
        return withContext(Dispatchers.IO) {
            dbHelper.deleteObject(Gift.TABLE, gift.id)
            dbHelper.close()
            Result.Success(gift)
        }
    }

    private suspend fun giftsFromCursor(cursor: Cursor, context: Context): MutableList<Gift> {
        val gifts = mutableListOf<Gift>()
        with(cursor) {
            while (moveToNext()) {
                val gift = Gift
                gift.id = getString(getColumnIndexOrThrow(BaseColumns._ID))
                gift.title = getString(getColumnIndexOrThrow(Gift.TITLE))
                gift.url = getString(getColumnIndexOrThrow(Gift.URL))

                val recipientId = getString(getColumnIndexOrThrow(Gift.RECIPIENT))
                if (recipientId != "0") {
                    when (val result = recipientRepository.getRecipient(recipientId, context)) {
                        is Result.Success -> gift.recipient = result.data
                        is Result.Error -> TODO()
                    }
                }

                val holidayId = getString(getColumnIndexOrThrow(Gift.HOLIDAY))
                if (holidayId != "0") {
                    when (val result = holidaysRepository.getHoliday(holidayId, context)) {
                        is Result.Success -> gift.holiday = result.data
                        is Result.Error -> TODO()
                    }
                }

                gifts.add(gift)
            }
        }
        cursor.close()
        return gifts
    }

    private fun valuesFromGift(gift: Gift): ContentValues {
        var recipientId = "0"
        if (gift.recipient.id != "") {
            recipientId = gift.recipient.id
        }
        var holidayId = "0"
        if (gift.holiday.id != "") {
            holidayId = gift.holiday.id
        }

        val values = ContentValues().apply {
            put(Gift.TITLE, gift.title)
            put(Gift.URL, gift.url)
            put(Gift.RECIPIENT, recipientId)
            put(Gift.HOLIDAY, holidayId)
        }
        return values
    }
}