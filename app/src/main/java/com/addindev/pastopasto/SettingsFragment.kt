package com.addindev.pastopasto

import android.content.Intent
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.SwitchPreference
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.addindev.pastopasto.notifications.services.DailyAlarmReceiver
import com.addindev.pastopasto.notifications.services.ReleaseMovieReminder


class SettingsFragment : PreferenceFragment(), Preference.OnPreferenceChangeListener,
    Preference.OnPreferenceClickListener {

    lateinit var keyDailyReminder: String
    lateinit var keyReleaseReminder: String
    lateinit var keySettingLanguage: String

    private val dailyAlarmReceiver = DailyAlarmReceiver()
    private val releaseTodayReminder = ReleaseMovieReminder()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)

        keyDailyReminder = activity.getString(R.string.key_daily_notification)
        keyReleaseReminder = activity.getString(R.string.key_release_movie)
        keySettingLanguage = activity.getString(R.string.key_setting_language)
        val switchDailyReminder = findPreference(keyDailyReminder) as SwitchPreference
        switchDailyReminder.onPreferenceChangeListener = this
        val switchUpcomingReminder = findPreference(keyReleaseReminder) as SwitchPreference
        switchUpcomingReminder.onPreferenceChangeListener = this
        val settingLanguage = findPreference(keySettingLanguage)
        settingLanguage.onPreferenceClickListener = this
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        val key = preference.key
        if (key == keySettingLanguage) {
            val languageIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(languageIntent)
        }
        return true
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        val key = preference.key
        val isSet = newValue as Boolean

        if (key == keyDailyReminder) {
            if (isSet) {
                Log.d("IDN","Setting up daily notif...")
                dailyAlarmReceiver.setRepeatingAlarm(activity)
            } else {
                Log.d("IDN","Canceling daily notif")
                dailyAlarmReceiver.cancelAlarm(activity)
            }
        } else if (key == keyReleaseReminder) {
            if (isSet) {
                Log.d("IDN","Setting up release movie notif")
                releaseTodayReminder.setRepeatingAlarm(activity)
            } else {
                Log.d("IDN","Canceling release movie notif")
                releaseTodayReminder.cancelAlarm(activity)
            }
        }
        
        return true
    }
}

