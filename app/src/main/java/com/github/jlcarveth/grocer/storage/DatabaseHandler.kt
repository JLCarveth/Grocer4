package com.github.jlcarveth.grocer.storage

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.github.jlcarveth.grocer.model.GroceryEntry;
import com.github.jlcarveth.grocer.model.GroceryItem

/**
 * Created by John on 11/10/2017.
 *
 * The Database Handler implemented in Kotlin
 * The purpose of this class is to handle all interaction with the database,
 * so the other classes can just call a function and be done.
 */
private val DBNAME : String = "grocer_db";

private var DBVERSION : Int = 1;


class DatabaseHandler : SQLiteOpenHelper {
    constructor(context: Context) : super(context, DBNAME, null, DBVERSION)

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(GroceryEntry.SQL_CREATE_ENTRIES);
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(GroceryEntry.SQL_DELETE_ENTRIES)
        db.execSQL(GroceryEntry.SQL_CREATE_ENTRIES)
    }

    fun getGroceries() : ArrayList<GroceryItem> {
        val al = ArrayList<GroceryItem>()
        val db = this.readableDatabase

        val cursor : Cursor = db.rawQuery("SELECT * FROM ${GroceryEntry.TABLE_NAME}", null)

        while (cursor.moveToNext()) {
            val id = cursor.getString(cursor.getColumnIndexOrThrow(GroceryEntry._ID));
            val name = cursor.getString(cursor.getColumnIndexOrThrow(GroceryEntry.COLUMN_NAME))
            val note = cursor.getString(cursor.getColumnIndexOrThrow(GroceryEntry.COLUMN_NOTE))
            val qty = cursor.getString(cursor.getColumnIndexOrThrow(GroceryEntry.COLUMN_QTY))

            val ci = cursor.getInt(cursor.getColumnIndexOrThrow(GroceryEntry.COLUMN_CHKD))
            val checked = (ci==1)

            val g = GroceryItem(name,note,qty,checked)
            g.id = id.toLong()

            al.add(g)
        }

        return al
    }

    /**
     * Removes all entries from the grocery table
     */
    fun clearGroceries() {
        writableDatabase.delete(GroceryEntry.TABLE_NAME, null, null)
        dataUpdated()
    }

    /**
     * Inserts one item into the Database
     */
    fun insertGroceryItem(g : GroceryItem) : Boolean {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(GroceryEntry.COLUMN_NAME,g.name)
        values.put(GroceryEntry.COLUMN_NOTE,g.note)
        values.put(GroceryEntry.COLUMN_QTY, g.qty)

        val i : Int = when (g.checked) {
            true -> 1
            false -> 0
        }
        values.put(GroceryEntry.COLUMN_CHKD, i)

        val row : Long = db.insert(GroceryEntry.TABLE_NAME, null, values)
        dataUpdated()

        g.id = row

        return (row > 0)
    }

    /**
     * Sets the specified row's checked field to true
     */
    fun checkGroceryItem(g : GroceryItem) {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(GroceryEntry.COLUMN_NAME, g.name)
        values.put(GroceryEntry.COLUMN_NOTE, g.note)
        values.put(GroceryEntry.COLUMN_QTY, g.qty)
        values.put(GroceryEntry.COLUMN_CHKD, true)

        db.update(GroceryEntry.TABLE_NAME,
                values, "${GroceryEntry._ID} = '${g.id}'", null)
    }

    /**
     * Removes one item from the database
     */
    fun deleteGroceryItem(g : GroceryItem) : Boolean {
        val db = this.writableDatabase

        val row = db.delete(GroceryEntry.TABLE_NAME,
                "${GroceryEntry.COLUMN_NAME}='${g.name}'", null)
        dataUpdated()
        return (row > 0)
    }

    /**
     * Called within the class whenever db data has been changed
     */
    private fun dataUpdated() {
        DatabaseSubject.notifyObservers()
        close()
    }
}