package com.huawei.agc.gifttracker.data.recipient.impl

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import com.huawei.agc.gifttracker.data.DbHelper
import com.huawei.agc.gifttracker.data.Result
import com.huawei.agc.gifttracker.data.recipient.RecipientsRepository
import com.huawei.agc.gifttracker.model.Gift
import com.huawei.agc.gifttracker.model.Holiday
import com.huawei.agc.gifttracker.model.Recipient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalRecipientRepository(private val dbHelper: DbHelper) : RecipientsRepository {
    override suspend fun getRecipient(recipientId: String?, context: Context): Result<Recipient> {
        return withContext(Dispatchers.IO) {
            val cursor = dbHelper.getObjectByID(Recipient.TABLE, recipientId)
            val recipients = recipientsFromCursor(cursor)
            dbHelper.close()
            if (recipients.size != 1) {
                Result.Error(IllegalArgumentException("Recipient not found"))
            } else {
                Result.Success(recipients[0])
            }
        }
    }

    override suspend fun getRecipient(context: Context): Result<MutableList<Recipient>> {
        return withContext(Dispatchers.IO) {
            val cursor = dbHelper.getAllObjects(Recipient.TABLE)
            val recipients = recipientsFromCursor(cursor)
            dbHelper.close()
            if (recipients.size < 1) {
                Result.Error(IllegalArgumentException("Holidays not found"))
            } else {
                Result.Success(recipients)
            }
        }
    }

    override suspend fun insertRecipient(recipient: Recipient): Result<Recipient> {
        return withContext(Dispatchers.IO) {
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put(Recipient.NAME, recipient.name)
            }

            val newRowId = db?.insert(Recipient.TABLE, null, values)
            recipient.id = newRowId.toString()
            dbHelper.close()
            Result.Success(recipient)
        }
    }

    override suspend fun updateRecipient(recipient: Recipient): Result<Recipient> {
        return withContext(Dispatchers.IO) {
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put(Recipient.NAME, recipient.name)
            }

            val selection = "${BaseColumns._ID} LIKE ?"
            val selectionArgs = arrayOf(recipient.id)
            db.update(
                Recipient.TABLE,
                values,
                selection,
                selectionArgs)
            dbHelper.close()
            Result.Success(recipient)
        }
    }

    override suspend fun deleteRecipient(recipient: Recipient): Result<Recipient> {
        return withContext(Dispatchers.IO) {
            dbHelper.deleteObject(Recipient.TABLE, recipient.id)
            dbHelper.close()
            Result.Success(recipient)
        }
    }

    private fun recipientsFromCursor(cursor: Cursor): MutableList<Recipient> {
        val recipients = mutableListOf<Recipient>()
        with(cursor) {
            while (moveToNext()) {
                val recipient = Recipient
                recipient.id = getString(getColumnIndexOrThrow(BaseColumns._ID))
                recipient.name = getString(getColumnIndexOrThrow(Recipient.NAME))
                recipients.add(recipient)
            }
        }
        cursor.close()
        return recipients
    }
}