package com.huawei.agc.gifttracker.data.user.impl

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import com.huawei.agc.gifttracker.data.DbHelper
import com.huawei.agc.gifttracker.data.Result
import com.huawei.agc.gifttracker.data.user.UsersRepository
import com.huawei.agc.gifttracker.model.Gift
import com.huawei.agc.gifttracker.model.Holiday
import com.huawei.agc.gifttracker.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalUsersRepository (private val dbHelper: DbHelper) : UsersRepository {
    override suspend fun getUser(userId: String?, context: Context): Result<User> {
        return withContext(Dispatchers.IO) {
            val cursor = dbHelper.getObjectByID(User.TABLE, userId)
            val users = usersFromCursor(cursor)
            dbHelper.close()
            if (users.size != 1) {
                Result.Error(IllegalArgumentException("User not found"))
            } else {
                Result.Success(users[0])
            }
        }
    }

    override suspend fun getUsers(context: Context): Result<MutableList<User>> {
        return withContext(Dispatchers.IO) {
            val cursor = dbHelper.getAllObjects(User.TABLE)
            val users = usersFromCursor(cursor)
            dbHelper.close()
            if (users.size < 1) {
                Result.Error(IllegalArgumentException("Users not found"))
            } else {
                Result.Success(users)
            }
        }
    }

    override suspend fun insertUser(user: User): Result<User> {
        return withContext(Dispatchers.IO) {
            val db = dbHelper.writableDatabase

            val values = ContentValues().apply {
                put(User.NAME, user.name)
                put(User.EMAIL, user.email)
                put(User.PASSWORD, user.password)
            }

            val newRowId = db?.insert(User.TABLE, null, values)
            user.id = newRowId.toString()
            dbHelper.close()
            Result.Success(user)
        }
    }

    override suspend fun updateUser(user: User): Result<User> {
        return withContext(Dispatchers.IO) {
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put(User.NAME, user.name)
                put(User.EMAIL, user.email)
                put(User.PASSWORD, user.password)
            }

            val selection = "${BaseColumns._ID} LIKE ?"
            val selectionArgs = arrayOf(user.id)
            db.update(
                User.TABLE,
                values,
                selection,
                selectionArgs)
            dbHelper.close()
            Result.Success(user)
        }
    }

    override suspend fun deleteUser(user: User): Result<User> {
        return withContext(Dispatchers.IO) {
            dbHelper.deleteObject(User.TABLE, User.id)
            dbHelper.close()
            Result.Success(User)
        }
    }

    private fun usersFromCursor(cursor: Cursor): MutableList<User> {
        val users = mutableListOf<User>()
        with(cursor) {
            while (moveToNext()) {
                val user = User
                user.id = getString(getColumnIndexOrThrow(BaseColumns._ID))
                user.name = getString(getColumnIndexOrThrow(User.NAME))
                user.email = getString(getColumnIndexOrThrow(User.EMAIL))
                user.password = getString(getColumnIndexOrThrow(User.PASSWORD))
                users.add(user)
            }
        }
        cursor.close()
        return users
    }
}