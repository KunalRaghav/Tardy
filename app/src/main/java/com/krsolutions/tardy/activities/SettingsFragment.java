package com.krsolutions.tardy.activities;


import android.os.Bundle;

import com.krsolutions.tardy.R;

import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.app_pref);
    }
}
