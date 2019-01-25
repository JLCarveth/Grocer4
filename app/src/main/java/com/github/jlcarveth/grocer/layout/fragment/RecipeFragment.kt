package com.github.jlcarveth.grocer.layout.fragment

import android.app.Fragment
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.github.jlcarveth.grocer.R
import com.github.jlcarveth.grocer.layout.decoration.RecyclerViewDivider
import com.github.jlcarveth.grocer.model.RecipeItem
import com.github.jlcarveth.grocer.storage.DatabaseHandler
import com.github.jlcarveth.grocer.storage.DatabaseObserver
import com.github.jlcarveth.grocer.util.recycler.OnStartDragListener

/**
 * Created by John on 1/4/2018.
 *
 * Fragment displays all of the user's recipes in a RecyclerView
 */
class RecipeFragment : Fragment(), DatabaseObserver {

    lateinit var rv : RecyclerView
    lateinit var adapter : RecipeAdapter
    lateinit var dataset : ArrayList<RecipeItem>
    lateinit var dh : DatabaseHandler

    lateinit var emptyView : LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_recipe_list, container, false)

        rv = view.findViewById(R.id.recipe_list)
        emptyView = view.findViewById(R.id.empty_view)

        dh = DatabaseHandler(view.context)

        dataset = dh.getRecipes()

        rv.layoutManager = LinearLayoutManager(view.context)

        adapter = RecipeAdapter(dataset, this)
        rv.adapter = adapter

        val divider = ContextCompat.getDrawable(activity, R.drawable.divider)
        rv.addItemDecoration(RecyclerViewDivider(divider))

        if (dataset.isEmpty()) {
            rv.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
        } else {
            rv.visibility = View.VISIBLE
            emptyView.visibility = View.GONE
        }

        return view
    }

    /**
     * Called whenever the Database has changed data
     */
    override fun update() {
        TODO("not implemented")
    }



    companion object {
        fun newInstance() : RecipeFragment {
            val fragment = RecipeFragment()
            // We can attach data here if needed
            //fragment.arguments...
            return fragment
        }

        val TAG = "RecipeFragment"
    }
}