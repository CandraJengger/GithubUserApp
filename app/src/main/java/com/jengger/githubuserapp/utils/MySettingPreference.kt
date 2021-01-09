package com.jengger.githubuserapp.utils

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.jengger.githubuserapp.R


class MySettingPreference : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var switchReminderPreference : SwitchPreferenceCompat
    private lateinit var alarmReminder: AlarmReminder
    private lateinit var reminder: String

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings_preference)

        alarmReminder = AlarmReminder()

        reminder = getString(R.string.reminder_key)
        switchReminderPreference = findPreference<SwitchPreferenceCompat>(reminder) as SwitchPreferenceCompat

        val sharedPreference = preferenceManager.sharedPreferences
        switchReminderPreference.isChecked = sharedPreference.getBoolean(reminder, false)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == reminder) {
            if (sharedPreferences != null) {
                switchReminderPreference.isChecked = sharedPreferences.getBoolean(reminder, false)
            }
        }

        val state = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(reminder, false)

        setAlarmReminder(state)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    private fun setAlarmReminder(state: Boolean) {
        if (state) {
            context?.let {
                alarmReminder.setRepeatingReminder(it)
            }
        } else {
            context?.let {
                alarmReminder.cancelAlarm(it)
            }
        }
    }
}