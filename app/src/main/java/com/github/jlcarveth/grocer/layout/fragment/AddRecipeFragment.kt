package com.github.jlcarveth.grocer.layout.fragment

import android.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.github.jlcarveth.grocer.R
import com.github.jlcarveth.grocer.model.MinuteTime
import com.github.jlcarveth.grocer.util.ViewGroupUtil

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AddRecipeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AddRecipeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddRecipeFragment : Fragment() {

    lateinit var nameField : EditText
    lateinit var ingredientBtn : Button
    lateinit var directionBtn : Button
    lateinit var prepBtn : Button
    lateinit var cookBtn : Button
    lateinit var tagBtn : Button
    lateinit var noteField : EditText

    val ingredients = ArrayList<String>()
    val directions = ArrayList<String>()
    val tags = ArrayList<String>()

    lateinit var prepTime : MinuteTime
    lateinit var cookTime : MinuteTime

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_recipe, container, false)

        nameField = view.findViewById(R.id.dar_name)
        ingredientBtn = view.findViewById(R.id.dar_ingredients)
        directionBtn = view.findViewById(R.id.dar_directions)
        prepBtn = view.findViewById(R.id.dar_prep)
        cookBtn = view.findViewById(R.id.dar_cook)
        tagBtn = view.findViewById(R.id.dar_tags)
        noteField = view.findViewById(R.id.dar_notes)

        ingredientBtn.setOnClickListener {
            val led = ListEntryDialog()
            val args = Bundle()
            args.putString("title", "Ingredients")
            args.putString("message", "Input your ingredients:")

            led.arguments = args
            led.listable = object : Listable<String> {
                override fun list(data: MutableList<String>) {
                    ingredients.addAll(data)

                    // We know the dialog has been OKed
                    val tv = TextView(activity)
                    if (data.count() > 2) {
                        tv.text = "Indredients: ${data[0]}, ${data[1]}, ${data[2]}, ..."

                    } else {
                        tv.text = "Ingredients: " + data.joinToString()
                    }
                    ViewGroupUtil.replaceView(ingredientBtn, tv)
                }
            }
            led.show(fragmentManager, TAG)
        }

        directionBtn.setOnClickListener {
            val led = ListEntryDialog()
            val args = Bundle()
            args.putString("title", "Directions")
            args.putString("message", "Input your directions:")

            led.arguments = args

            led.listable = object : Listable<String> {
                override fun list(data: MutableList<String>) {
                    directions.addAll(data)

                    // Same as before!
                    val tv = TextView(activity)
                    if (data.count() > 2) {
                        tv.text = "Directions: ${data[0]}, ${data[1]}, ${data[2]}, ..."

                    } else {
                        tv.text = "Directions: " + data.joinToString()
                    }
                    ViewGroupUtil.replaceView(directionBtn, tv)
                }
            }
            led.show(fragmentManager, TAG)
        }

        prepBtn.setOnClickListener {
            val mtpd = MinuteTimePickerDialog()

            val args = Bundle()
            args.putInt("mode", MinuteTimePickerDialog.MINUTE)

            mtpd.arguments = args
            mtpd.provider = object : MinuteTime.MinuteTimeProvider {
                override fun provide(time: MinuteTime) {
                    prepTime = time
                    Log.i(TAG, "Provider called.")

                    // Still the same concept. The provide method is only called when the dialog OKs
                    val tv = TextView(activity)
                    tv.text = time.toString()

                    ViewGroupUtil.replaceView(prepBtn, tv)
                }
            }

            mtpd.show(fragmentManager, MinuteTimePickerDialog.TAG)
        }

        cookBtn.setOnClickListener {
            val mptd = MinuteTimePickerDialog()

            val args = Bundle()
            args.putInt("mode", MinuteTimePickerDialog.HOUR_AND_MINUTE)

            mptd.arguments = args
            mptd.provider = object : MinuteTime.MinuteTimeProvider {
                override fun provide(time: MinuteTime) {
                    cookTime = time
                    Log.i(TAG, "Provider called.")

                    // Still the same concept. The provide method is only called when the dialog OKs
                    val tv = TextView(activity)
                    tv.text = time.toString()

                    ViewGroupUtil.replaceView(cookBtn, tv)
                }

            }

            mptd.show(fragmentManager, MinuteTimePickerDialog.TAG)
        }

        tagBtn.setOnClickListener {
            val led = ListEntryDialog()
            val args = Bundle()
            args.putString("title", "Tags")
            args.putString("message", "Add tags to this recipe:")

            led.arguments = args

            led.listable = object : Listable<String> {
                override fun list(data: MutableList<String>) {
                    tags.addAll(data)

                    // Same as before!
                    val tv = TextView(activity)
                    if (data.count() > 2) {
                        tv.text = "Tags: ${data[0]}, ${data[1]}, ${data[2]}, ..."

                    } else {
                        tv.text = "Tags: " + data.joinToString()
                    }
                    ViewGroupUtil.replaceView(tagBtn, tv)
                }
            }

            led.show(fragmentManager, TAG)
        }

        return view
    }


    companion object {

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment AddRecipeFragment.
         */
        fun newInstance(): AddRecipeFragment {
            val fragment = AddRecipeFragment()
            val args = Bundle()

            fragment.arguments = args
            return fragment
        }

        val TAG = "AddRecipe"
    }
}
