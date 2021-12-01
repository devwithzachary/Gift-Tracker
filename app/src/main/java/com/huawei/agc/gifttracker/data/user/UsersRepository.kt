package com.huawei.agc.gifttracker.data.user

import android.content.Context
import com.huawei.agc.gifttracker.data.Result
import com.huawei.agc.gifttracker.model.User

interface UsersRepository {

    suspend fun getUser(userId: String?,  context: Context): Result<User>

    suspend fun getUsers( context: Context): Result<MutableList<User>>

    suspend fun insertUser(user: User): Result<User>

    suspend fun updateUser(user: User): Result<User>

    suspend fun deleteUser(user: User): Result<User>
}