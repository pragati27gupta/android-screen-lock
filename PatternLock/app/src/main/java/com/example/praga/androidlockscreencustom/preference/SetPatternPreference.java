package com.example.praga.androidlockscreencustom.preference;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.example.praga.androidlockscreencustom.R;
import com.example.praga.androidlockscreencustom.util.Util;

public class SetPatternPreference extends Preference implements SharedPreferences.OnSharedPreferenceChangeListener {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SetPatternPreference(Context context, AttributeSet attrs, int defStyleAttr,
                                int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public SetPatternPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SetPatternPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SetPatternPreference(Context context) {
        super(context);
    }

    @Override
    protected void onAttachedToHierarchy(PreferenceManager preferenceManager) {
        super.onAttachedToHierarchy(preferenceManager);

        PreferenceManager.getDefaultSharedPreferences(getContext())
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPrepareForRemoval() {
        super.onPrepareForRemoval();

        PreferenceManager.getDefaultSharedPreferences(getContext())
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public CharSequence getSummary() {
        Context context = getContext();
        return Util.hasPattern(context) ?
                context.getString(R.string.pref_summary_set_pattern_has) :
                context.getString(R.string.pref_summary_set_pattern_none);
    }

    @Override
    protected void onClick() {
        Util.setPatternByUser(getContext());
    }

    @Override
    public boolean shouldDisableDependents() {
        return super.shouldDisableDependents() || !Util.hasPattern(getContext());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (TextUtils.equals(key, Util.KEY_PATTERN_SHA1)) {
            notifyChanged();
            notifyDependencyChange(shouldDisableDependents());
        }
    }
}
