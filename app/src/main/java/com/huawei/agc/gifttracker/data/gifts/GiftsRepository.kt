package com.huawei.agc.gifttracker.data.gifts

import com.huawei.agc.gifttracker.data.Result
import android.content.Context
import com.huawei.agc.gifttracker.model.Gift

interface GiftsRepository {

    suspend fun getGift(giftId: String?,  context: Context): Result<Gift>

    suspend fun getGifts( context: Context): Result<MutableList<Gift>>

    suspend fun insertGift(gift: Gift): Result<Gift>

    suspend fun updateGift(gift: Gift): Result<Gift>

    suspend fun deleteGift(gift: Gift): Result<Gift>
}