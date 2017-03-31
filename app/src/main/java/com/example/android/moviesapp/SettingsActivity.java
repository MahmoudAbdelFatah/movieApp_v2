package com.example.android.moviesapp;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingsActivity extends PreferenceActivity {
    private ListPreference mListPreference;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        setPreferencesFromResource(R.xml.app_preferences, s);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mListPreference = (ListPreference) getPreferenceManager().findPreference(getString(R.string.pref_movie_key));
        mListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object value) {
                String stringValue = value.toString();
                if (preference instanceof ListPreference) {
                    mListPreference = (ListPreference) preference;
                    int prefIdx = mListPreference.findIndexOfValue(stringValue);
                    if (prefIdx >= 0) {
                        preference.setSummary(mListPreference.getEntries()[prefIdx]);
                    }
                } else {
                    preference.setSummary(stringValue);
                }
                return true;
            }
        });
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
