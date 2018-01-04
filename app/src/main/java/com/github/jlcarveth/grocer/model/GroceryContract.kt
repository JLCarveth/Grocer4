package com.github.jlcarveth.grocer.model

import android.provider.BaseColumns

/**
 * Created by John on 11/10/2017.
 *
 * Our Model class for both the Grocery database table and the Grocery Item
 */
class GroceryEntry : BaseColumns {
    companion object {
        val TABLE_NAME = "groceries"
        val _ID = "id"
        val COLUMN_NAME = "name"
        val COLUMN_NOTE = "note"
        val COLUMN_QTY = "qty"
        val COLUMN_CHKD = "checked"

        val SQL_CREATE_ENTRIES = "CREATE TABLE $TABLE_NAME (" +
                "$_ID INTEGER PRIMARY KEY," +
                "$COLUMN_NAME TEXT, $COLUMN_NOTE TEXT, $COLUMN_QTY TEXT, $COLUMN_CHKD INTEGER)"

        val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"
    }

}

class GroceryItem(val name : String, val note : String, val qty : String, var checked : Boolean) : Comparable<GroceryItem>{
    var id : Long = -99

    /**
     * Our object will need to be sorted by lists later.
     * Groceries are sorted alphabetically by name
     */
    override fun compareTo(other: GroceryItem): Int = (this.name.compareTo(other.name))

    override fun toString(): String = "GroceryItem{name:$name, note:$note, qty:$qty}"
}