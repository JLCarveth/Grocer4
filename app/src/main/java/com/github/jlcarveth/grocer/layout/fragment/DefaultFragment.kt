package com.github.jlcarveth.grocer.layout.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.github.jlcarveth.grocer.R

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DefaultFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DefaultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class DefaultFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_default, container, false)
    }


    companion object {
        val TAG = "DEFAULT"

        fun newInstance(): DefaultFragment {
            val fragment = DefaultFragment()
            return fragment
        }
    }
}
