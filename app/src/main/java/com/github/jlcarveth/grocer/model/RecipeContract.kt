package com.github.jlcarveth.grocer.model

import android.provider.BaseColumns

/**
 * Created by John on 1/4/2018.
 *
 * This class handles both the representation of a recipe in our software model,
 * as well as the representation of the data in database schema.
 *
 * TODO : Storing images in the Database, or at least a reference to them.
 */
class RecipeContract : BaseColumns {
    companion object {
        val TABLE_NAME = "recipes"
        val _ID = "id"
        val COLUMN_NAME = "name"
        val COLUMN_ING = "ingredients"
        val COLUMN_DIR = "directions"
        val COLUMN_PREP = "prep"    // Prep time
        val COLUMN_COOK = "cook"    // Cooking time
        val COLUMN_TAGS = "tags"
        val COLUMN_NOTES = "notes"
        val COLUMN_RATING = "rating"

        val SQL_CREATE_ENTRIES = "CREATE TABLE $TABLE_NAME (" +
                "$_ID INTEGER PRIMARY KEY," +
                "$COLUMN_NAME TEXT, $COLUMN_ING TEXT, $COLUMN_DIR TEXT," +
                "$COLUMN_PREP INTEGER, $COLUMN_COOK INTEGER, $COLUMN_TAGS TEXT," +
                "$COLUMN_NOTES TEXT, $COLUMN_RATING INTEGER"

        val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}

class RecipeItem(val name : String,
                 val ingredients : Array<String>,
                 val directions : Array<String>,
                 val prepTime : MinuteTime,
                 val cookTime : MinuteTime,
                 val tags : Array<String>,
                 val notes : String) {
    var rating = 0
        set(value) {
            if (0 > value || value > 5) {
                throw IllegalArgumentException("rAtINg mUst bE bEtwEEn 0-5")
            }
            field = value
        }

    // A preset ID. This will be overwritten when the data is fetched from the DB
    var id = -99

    /**
     * Get's the total time the recipe takes (prep + cook times)
     */
    fun getTotalTime() : MinuteTime {
        return cookTime + prepTime
    }

    override fun toString() : String {
        return name
    }
}