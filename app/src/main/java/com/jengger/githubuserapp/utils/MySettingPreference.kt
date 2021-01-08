package com.jengger.githubuserapp.utils

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.jengger.githubuserapp.R


class MySettingPreference : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
       addPreferencesFromResource(R.xml.settings_preference)
    }
}