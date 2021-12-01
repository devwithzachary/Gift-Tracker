package com.huawei.agc.gifttracker.data.recipient

import android.content.Context
import com.huawei.agc.gifttracker.data.Result
import com.huawei.agc.gifttracker.model.Recipient

interface RecipientsRepository {
    suspend fun getRecipient(recipientId: String?,  context: Context): Result<Recipient>

    suspend fun getRecipient( context: Context): Result<MutableList<Recipient>>

    suspend fun insertRecipient(recipient: Recipient): Result<Recipient>

    suspend fun updateRecipient(recipient: Recipient): Result<Recipient>

    suspend fun deleteRecipient(recipient: Recipient): Result<Recipient>
}