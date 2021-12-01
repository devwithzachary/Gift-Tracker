package com.huawei.agc.gifttracker.data

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.huawei.agc.gifttracker.model.Gift
import com.huawei.agc.gifttracker.model.Holiday
import com.huawei.agc.gifttracker.model.Recipient
import com.huawei.agc.gifttracker.model.User

class DbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val sqlCreateGiftTable =
        "CREATE TABLE ${Gift.TABLE} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${Gift.TITLE} TEXT," +
                "${Gift.URL} TEXT," +
                "${Gift.HOLIDAY} INTEGER," +
                "${Gift.RECIPIENT} INTEGER)"

    private val sqlCreateHolidayTable =
        "CREATE TABLE ${Holiday.TABLE} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${Holiday.TITLE} TEXT," +
                "${Holiday.DATE} TEXT)"

    private val sqlCreateRecipientTable =
        "CREATE TABLE ${Recipient.TABLE} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${Recipient.NAME} TEXT)"

    private val sqlCreateUserTable =
        "CREATE TABLE ${User.TABLE} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${User.NAME} TEXT," +
                "${User.PASSWORD} TEXT," +
                "${User.EMAIL} TEXT)"

    private val sqlDeleteGiftTable = "DROP TABLE IF EXISTS ${Gift.TABLE}"
    private val sqlDeleteHolidayTable = "DROP TABLE IF EXISTS ${Holiday.TABLE}"
    private val sqlDeleteRecipientTable = "DROP TABLE IF EXISTS ${Recipient.TABLE}"
    private val sqlDeleteUserTable = "DROP TABLE IF EXISTS ${User.TABLE}"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(sqlCreateGiftTable)
        db.execSQL(sqlCreateHolidayTable)
        db.execSQL(sqlCreateRecipientTable)
        db.execSQL(sqlCreateUserTable)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(sqlDeleteGiftTable)
        db.execSQL(sqlDeleteHolidayTable)
        db.execSQL(sqlDeleteRecipientTable)
        db.execSQL(sqlDeleteUserTable)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun getObjectByID(table: String, id: String?) : Cursor {
        val db = this.readableDatabase

        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(id)
        val sortOrder = "${BaseColumns._ID} ASC"

        val cursor = db.query(table, null, selection, selectionArgs,
            null,
            null,
            sortOrder
        )

        return cursor
    }

    fun getAllObjects(table: String) : Cursor {
        val db = this.readableDatabase
        val cursor = db.query(table,
            null,
            null,
            null,
            null,
            null,
            null
        )
        return cursor
    }

    fun deleteObject(table: String, id: String?) {
        val db = this.writableDatabase
        val selection = "${BaseColumns._ID} LIKE ?"
        val selectionArgs = arrayOf(id)
        db.delete(table, selection, selectionArgs)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "GiftTracker.db"
    }
}