package com.github.jlcarveth.grocer.storage

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.github.jlcarveth.grocer.model.*

/**
 * Created by John on 11/10/2017.
 *
 * The Database Handler implemented in Kotlin
 * The purpose of this class is to handle all interaction with the database,
 * so the other classes can just call a function and be done.
 */
private val DBNAME : String = "grocer_db"

private var DBVERSION : Int = 3


class DatabaseHandler : SQLiteOpenHelper {
    constructor(context: Context) : super(context, DBNAME, null, DBVERSION)

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(GroceryEntry.SQL_CREATE_ENTRIES)
        db.execSQL(RecipeContract.SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(GroceryEntry.SQL_DELETE_ENTRIES)
        db.execSQL(GroceryEntry.SQL_CREATE_ENTRIES)

        db.execSQL(RecipeContract.SQL_DELETE_ENTRIES)
        db.execSQL(RecipeContract.SQL_CREATE_ENTRIES)
    }

    /**
     * Sorts the GroceryList
     */
    fun sortGroceries() {
        hideCheckedItems()
    }

    /**
     * Gets all visible Grocery items from the database
     * @return an arraylist containing GrocerItem objects
     */
    fun getGroceries() : ArrayList<GroceryItem> {
        val al = ArrayList<GroceryItem>()
        val db = this.readableDatabase

        //Where hidden is false (visible)
        val cursor : Cursor = db.rawQuery("SELECT * FROM ${GroceryEntry.TABLE_NAME} " +
                "WHERE ${GroceryEntry.COLUMN_HIDDEN} = 0 ORDER BY " +
                "${GroceryEntry.COLUMN_NAME} ASC", null)

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
     * Gets all hidden grocery items
     * @return an arraylist containing GrocerItem objects
     */
    fun getHiddenGroceryItems() : ArrayList<GroceryItem> {
        val al = ArrayList<GroceryItem>()
        val db = this.readableDatabase

        //Where hidden is true (hidden)
        val cursor = db.rawQuery("SELECT * FROM ${GroceryEntry.TABLE_NAME} " +
                "WHERE ${GroceryEntry.COLUMN_HIDDEN} = 1 ORDER BY " +
                "${GroceryEntry.COLUMN_NAME} ASC", null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(GroceryEntry._ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(GroceryEntry.COLUMN_NAME))
            val note = cursor.getString(cursor.getColumnIndexOrThrow(GroceryEntry.COLUMN_NOTE))
            val qty = cursor.getString(cursor.getColumnIndexOrThrow(GroceryEntry.COLUMN_QTY))

            val ci = cursor.getInt(cursor.getColumnIndexOrThrow(GroceryEntry.COLUMN_CHKD))
            val checked = (ci==1)

            // A Hidden Grocery Item
            val g = GroceryItem(name,note,qty,checked)
            g.id = id.toLong()
            al.add(g)
        }
        return al
    }

    /**
     * Hides the given grocery item
     * @param g the GroceryItem to hide
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
     * @param g the GroceryItem to show
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
        //hidden false by default
        values.put(GroceryEntry.COLUMN_HIDDEN, 0)

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
        values.put(GroceryEntry.COLUMN_CHKD, 1)

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
     * Well technically it would delete any of the groceries with the same name...
     * TODO: Fix that
     */
    fun deleteGroceryItem(g : GroceryItem) : Boolean {
        val db = this.writableDatabase

        val row = db.delete(GroceryEntry.TABLE_NAME,
                "${GroceryEntry.COLUMN_NAME}='${g.name}'", null)
        dataUpdated()
        return (row > 0)
    }

    /**
     * Get all recipes stored in the database
     * @return an arrayliist of all recipes
     */
    fun getRecipes() : ArrayList<RecipeItem> {
        val al = ArrayList<RecipeItem>()
        val db = this.readableDatabase

        val cursor = db.rawQuery("SELECT * FROM ${RecipeContract.TABLE_NAME}", null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(RecipeContract._ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(RecipeContract.COLUMN_NAME))
            val ingStr = cursor.getString(cursor.getColumnIndexOrThrow(RecipeContract.COLUMN_ING))
            val dirStr = cursor.getString(cursor.getColumnIndexOrThrow(RecipeContract.COLUMN_DIR))
            val prep = cursor.getInt(cursor.getColumnIndexOrThrow(RecipeContract.COLUMN_PREP))
            val cook = cursor.getInt(cursor.getColumnIndexOrThrow(RecipeContract.COLUMN_COOK))
            val tagStr = cursor.getString(cursor.getColumnIndexOrThrow(RecipeContract.COLUMN_TAGS))
            val rating = cursor.getInt(cursor.getColumnIndexOrThrow(RecipeContract.COLUMN_RATING))
            val notes = cursor.getString(cursor.getColumnIndexOrThrow(RecipeContract.COLUMN_NOTES))

            // Ingredients are stored as one string separated by commas in the DB
            val ingredients = ingStr.split(",").toTypedArray()
            // Same as ingredients.
            val directions = dirStr.split(",").toTypedArray()
            // Stored as minutes in the database (Int)
            val prepTime = MinuteTime(prep)
            val cookTime = MinuteTime(cook)
            // Same as ingredients, directions
            val tags = tagStr.split(",").toTypedArray()

            val item = RecipeItem(name, ingredients, directions, prepTime, cookTime, tags, notes)

            if (rating in 0..5) {
                item.rating = rating
            }

            item.id = id

            al.add(item)
        }
        return al
    }

    /**
     * Inserts the recipe to the database
     * @param item The Recipe to add to the database
     * @return the ID of the new entry, or -99 if it wasn't added.
     */
    fun insertRecipe(item : RecipeItem) : Long {
        val db = this.writableDatabase
        val values = ContentValues()

        //Prepare some values
        val ingredients = item.ingredients.joinToString(",")
        println(ingredients)
        val directions = item.directions.joinToString(",")
        val tags = item.tags.joinToString(",")

        val prep = item.prepTime.getTimeInMinutes()
        val cook = item.cookTime.getTimeInMinutes()

        values.put(RecipeContract.COLUMN_NAME, item.name)
        values.put(RecipeContract.COLUMN_ING, ingredients)
        values.put(RecipeContract.COLUMN_DIR, directions)
        values.put(RecipeContract.COLUMN_PREP, prep)
        values.put(RecipeContract.COLUMN_COOK, cook)
        values.put(RecipeContract.COLUMN_TAGS, tags)
        values.put(RecipeContract.COLUMN_NOTES, item.notes)
        values.put(RecipeContract.COLUMN_RATING, item.rating)

        val row : Long = db.insert(RecipeContract.TABLE_NAME, null, values)

        dataUpdated()
        return row
    }

    /**
     * Deletes the given recipe from the database
     * @param item the Recipe to delete. Object must have a non-default ID
     * @return true a row was changed because of this function call.
     */
    fun deleteRecipe(item : RecipeItem) : Boolean {
        val db = this.writableDatabase

        val row = db.delete(RecipeContract.TABLE_NAME,
                "${RecipeContract._ID}='${item.id}'", null)
        dataUpdated()
        return (row > 0)
    }

    /**
     * If the recipe ID exists in the database, the corresponding row is updated
     * @param item the Recipe to update. The Recipe object must have had an ID set.
     * @return true if the row was updated, false #rows changed != 1
     */
    fun updateRecipe(item : RecipeItem) : Boolean {
        val db = writableDatabase
        val values = ContentValues()

        //Prepare some values
        val ingredients = item.ingredients.joinToString(",")
        println(ingredients)
        val directions = item.directions.joinToString(",")
        val tags = item.tags.joinToString(",")

        val prep = item.prepTime.getTimeInMinutes()
        val cook = item.cookTime.getTimeInMinutes()

        values.put(RecipeContract.COLUMN_NAME, item.name)
        values.put(RecipeContract.COLUMN_ING, ingredients)
        values.put(RecipeContract.COLUMN_DIR, directions)
        values.put(RecipeContract.COLUMN_PREP, prep)
        values.put(RecipeContract.COLUMN_COOK, cook)
        values.put(RecipeContract.COLUMN_TAGS, tags)
        values.put(RecipeContract.COLUMN_NOTES, item.notes)
        values.put(RecipeContract.COLUMN_RATING, item.rating)

        val rows = db.update(RecipeContract.TABLE_NAME,
                values, "${RecipeContract._ID} = '${item.id}'", null)
        dataUpdated()
        return (rows == 1)
    }

    /**
     * Called within the class whenever db data has been changed
     */
    private fun dataUpdated() {
        DatabaseSubject.notifyObservers()
        close()
    }
}