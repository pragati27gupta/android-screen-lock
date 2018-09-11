package com.example.praga.androidlockscreencustom.preference;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.widget.Toast;

import com.example.praga.androidlockscreencustom.R;
import com.example.praga.androidlockscreencustom.util.Util;

public class ClearPatternPreferenceDialogFragment extends PreferenceDialogFragmentCompat {
    // PreferenceDialogFragmentCompat needs a key to find its preference.
    public static ClearPatternPreferenceDialogFragment newInstance(String key) {
        ClearPatternPreferenceDialogFragment dialogFragment =
                new ClearPatternPreferenceDialogFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARG_KEY, key);
        dialogFragment.setArguments(arguments);
        return dialogFragment;
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            Activity activity = getActivity();
            Util.clearPattern(activity);
            Toast.makeText(activity, R.string.pattern_cleared, Toast.LENGTH_SHORT).show();
        }
    }
}
