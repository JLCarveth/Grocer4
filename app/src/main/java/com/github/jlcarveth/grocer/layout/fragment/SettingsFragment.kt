package com.github.jlcarveth.grocer.layout.fragment

import android.os.Bundle
import android.preference.PreferenceFragment
import com.github.jlcarveth.grocer.R

/**
 * Created by John on 11/28/2017.
 */
class SettingsFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)
    }
}