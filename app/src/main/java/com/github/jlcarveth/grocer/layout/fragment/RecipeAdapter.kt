package com.github.jlcarveth.grocer.layout.fragment

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import com.github.jlcarveth.grocer.R
import com.github.jlcarveth.grocer.model.RecipeItem
import com.github.jlcarveth.grocer.storage.DatabaseHandler

/**
 * Created by John on 1/4/2018.
 *
 */
class RecipeAdapter(private val values : ArrayList<RecipeItem>,
                    private val fragment : RecipeFragment) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    val dh = DatabaseHandler(fragment.activity)

    private val TAG = "RecipeAdapter"

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) : ViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.recipe_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        if (holder == null) {
            return
        }

        val item = values[position]
        holder.bind(item, dh)

    }

    override fun getItemCount(): Int {
        return values.count()
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        // We add the view elements to a single entry here...
        val recipeName : TextView = view.findViewById(R.id.recipe_name)
        val ratingBar : RatingBar = view.findViewById(R.id.recipe_rating)
        val recipeTags : TextView = view.findViewById(R.id.recipe_tags)

        val recipeAction : ImageButton = view.findViewById(R.id.recipe_action)

        /**
         * Called to bind data to the ViewHolder
         */
        fun bind(item : RecipeItem, db : DatabaseHandler) {
            recipeName.text = item.name
            ratingBar.rating = item.rating.toFloat()
            recipeTags.text = item.tags.toString()

            recipeAction.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    // When clicked, the button adds all ingredients to the shopping list.
                    Toast.makeText(v!!.context, "", Toast.LENGTH_LONG).show()
                    //db.insertGroceryItem()
                }

            })
        }

    }
}
