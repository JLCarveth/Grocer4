package com.github.jlcarveth.grocer.storage

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.github.jlcarveth.grocer.model.GroceryEntry;
import com.github.jlcarveth.grocer.model.GroceryItem

/**
 * Created by John on 11/10/2017.
 *
 * The Database Handler implemented in Kotlin
 * The purpose of this class is to handle all interaction with the database,
 * so the other classes can just call a function and be done.
 *
 * TODO:
 * - Change the getGroceries method so it only gets non-hidden groceries
 * - Add a function that gets all hidden grocery entries in the DB
 */
private val DBNAME : String = "grocer_db";

private var DBVERSION : Int = 2;

class DatabaseHandler : SQLiteOpenHelper {
    constructor(context: Context) : super(context, DBNAME, null, DBVERSION)

    private val TAG : String = "DatabaseHandler"

    private val sortType : String = "ASC"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(GroceryEntry.SQL_CREATE_ENTRIES);
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(GroceryEntry.SQL_DELETE_ENTRIES)
        db.execSQL(GroceryEntry.SQL_CREATE_ENTRIES)
    }

    /**
     * Sorts the GroceryList
     */
    fun sortGroceries() {
        hideCheckedItems()
    }
    /**
     * Gets all groceries from the database that are not hidden.
     */
    fun getGroceries() : ArrayList<GroceryItem> {
        val al = ArrayList<GroceryItem>()
        val db = this.readableDatabase

        //Where hidden is false (visible)
        val cursor : Cursor = db.rawQuery("SELECT * FROM ${GroceryEntry.TABLE_NAME} " +
                "WHERE ${GroceryEntry.COLUMN_HIDDEN} = 0 ORDER BY " +
                "${GroceryEntry.COLUMN_NAME} $sortType", null)

        while (cursor.moveToNext()) {
            val id = cursor.getString(cursor.getColumnIndexOrThrow(GroceryEntry._ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(GroceryEntry.COLUMN_NAME))
            val note = cursor.getString(cursor.getColumnIndexOrThrow(GroceryEntry.COLUMN_NOTE))
            val qty = cursor.getString(cursor.getColumnIndexOrThrow(GroceryEntry.COLUMN_QTY))

            val ci = cursor.getInt(cursor.getColumnIndexOrThrow(GroceryEntry.COLUMN_CHKD))
            val checked : Boolean = when(ci) {
                1 -> true
                0 -> false
                else -> {
                    false
                }
            }

            // A Visible Grocery Item
            val g = GroceryItem(name,note,qty,checked)
            g.id = id.toInt()

            Log.d(TAG, "Item Created: $g, hidden : ${g.hidden}," +
                    " checked : ${g.checked}, ID: ${g.id}")

            al.add(g)
        }

        return al
    }

    /**
     * Gets all hidden grocery items
     */
    fun getHiddenGroceryItems() : ArrayList<GroceryItem> {
        val al = ArrayList<GroceryItem>()
        val db = this.readableDatabase

        //Where hidden is true (hidden)
        val cursor = db.rawQuery("SELECT * FROM ${GroceryEntry.TABLE_NAME} " +
                "WHERE ${GroceryEntry.COLUMN_HIDDEN} = 1 ORDER BY " +
                "${GroceryEntry.COLUMN_NAME} $sortType", null)

        while (cursor.moveToNext()) {
            val id = cursor.getString(cursor.getColumnIndexOrThrow(GroceryEntry._ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(GroceryEntry.COLUMN_NAME))
            val note = cursor.getString(cursor.getColumnIndexOrThrow(GroceryEntry.COLUMN_NOTE))
            val qty = cursor.getString(cursor.getColumnIndexOrThrow(GroceryEntry.COLUMN_QTY))

            val ci = cursor.getInt(cursor.getColumnIndexOrThrow(GroceryEntry.COLUMN_CHKD))
            val checked = (ci==1)

            // A Hidden Grocery Item
            val g = GroceryItem(name,note,qty,checked)
            g.hidden = true
            g.id = id.toInt()
            al.add(g)
        }
        return al
    }

    /**
     * Hides the given grocery item
     */
    fun hideGroceryItem(g : GroceryItem) {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(GroceryEntry.COLUMN_NAME, g.name)
        values.put(GroceryEntry.COLUMN_NOTE, g.note)
        values.put(GroceryEntry.COLUMN_QTY, g.qty)

        val i : Int = when(g.checked) {
            true -> 1
            false -> 0
        }

        values.put(GroceryEntry.COLUMN_CHKD, i)

        //Set hidden to true
        values.put(GroceryEntry.COLUMN_HIDDEN, 1)

        db.update(GroceryEntry.TABLE_NAME,
                values, "${GroceryEntry._ID} = '${g.id}'", null)
    }

    /**
     * Makes a GroceryItem visible
     */
    fun showGroceryItem(g : GroceryItem) {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(GroceryEntry.COLUMN_NAME, g.name)
        values.put(GroceryEntry.COLUMN_NOTE, g.note)
        values.put(GroceryEntry.COLUMN_QTY, g.qty)

        val i : Int = when(g.checked) {
            true -> 1
            false -> 0
        }

        values.put(GroceryEntry.COLUMN_CHKD, i)

        //Set hidden to false
        values.put(GroceryEntry.COLUMN_HIDDEN, 0)

        db.update(GroceryEntry.TABLE_NAME,
                values, "${GroceryEntry.COLUMN_NAME} = '${g.name}' " +
                "AND ${GroceryEntry.COLUMN_HIDDEN} = 1", null)
        dataUpdated()
    }

    fun showGroceryItem(s : String) {
        showGroceryItem(GroceryItem(s, "","",false))
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

        val j : Int = when (g.hidden) {
            true -> 1
            false -> 0
        }
        values.put(GroceryEntry.COLUMN_HIDDEN, j)

        val row : Long = db.insert(GroceryEntry.TABLE_NAME, null, values)
        dataUpdated()

        return (row > 0)
    }

    /**
     * Sets the specified row's checked field to true
     */
    fun checkGroceryItem(g : GroceryItem, bool : Boolean) {
        val db = this.writableDatabase
        val values = ContentValues()
        Log.d(TAG, "Checking Item... ${g.checked}, ID: ${g.id}")
        values.put(GroceryEntry.COLUMN_NAME, g.name)
        values.put(GroceryEntry.COLUMN_NOTE, g.note)
        values.put(GroceryEntry.COLUMN_QTY, g.qty)

        val i : Int = when (bool) {
            true -> 1
            false -> 0
        }

        values.put(GroceryEntry.COLUMN_CHKD, i)

        val j : Int = when (g.hidden) {
            true -> 1
            false -> 0
        }

        values.put(GroceryEntry.COLUMN_HIDDEN, j)

        db.update(GroceryEntry.TABLE_NAME,
                values, "${GroceryEntry._ID} = '${g.id}'", null)
    }

    /**
     * Sets all checked entries to hidden
     */
    fun hideCheckedItems() {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(GroceryEntry.COLUMN_HIDDEN, 1)

        db.update(GroceryEntry.TABLE_NAME, values,
                "${GroceryEntry.COLUMN_CHKD} = 1", null)

        dataUpdated()
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