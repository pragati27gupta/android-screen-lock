package com.example.praga.androidlockscreencustom;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.praga.androidlockscreencustom.preference.ClearPatternPreference;

public class PatternLockFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences_pattern_lock);
    }


    @Override
    public void onDisplayPreferenceDialog(android.support.v7.preference.Preference preference) {
        if (!ClearPatternPreference.onDisplayPreferenceDialog(this, preference)) {
            super.onDisplayPreferenceDialog(preference);
        }
    }
}
