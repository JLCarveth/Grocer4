package com.github.jlcarveth.grocer.model

import android.provider.BaseColumns

/**
 * Created by John on 1/3/2018.
 * TODO:
 * - Add Picture(s)
 * - Serving sizes
 */

class RecipeContract : BaseColumns {
    companion object {
        val TABLE_NAME = "recipes"
        val COLUMN_NAME = "name"
        val COLUMN_ING = "ingredients"
        val COLUMN_DIR = "directions"
        val COLUMN_PREP = "prep_time"
        val COLUMN_COOK = "cook_time"
        val COLUMN_TAGS = "tags"
    }
}
class RecipeItem(val name : String,
                 val ingredients : Array<String>,
                 val directions : Array<String>,
                 val prepTime : MinuteTime,
                 val cookTime : MinuteTime,
                 val tags : Array<String>,
                 notes : String) {

    fun getTotalTime() : MinuteTime {
        return prepTime + cookTime
    }
}