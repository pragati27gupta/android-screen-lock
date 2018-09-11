package com.example.praga.androidlockscreencustom.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;

import com.example.praga.androidlockscreencustom.ConfirmPatternActivity;
import com.example.praga.androidlockscreencustom.PatternView;
import com.example.praga.androidlockscreencustom.SetPatternActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Util {
    public static final String KEY_PATTERN_SHA1 = "pref_key_pattern_sha1";
    public static final String DEFAULT_PATTERN_SHA1 = null;

    public static final String KEY_PATTERN_VISIBLE = "pref_key_pattern_visible";
    public static final Boolean DEFAULT_PATTERN_VISIBLE = true;

    private Util() {}

    public static void setPattern(List<PatternView.Cell> pattern, Context context) {
        PreferenceUtil.putString(KEY_PATTERN_SHA1, patternToSha1String(pattern), context);
    }

    private static String getPatternSha1(Context context) {
        return PreferenceUtil.getString(KEY_PATTERN_SHA1, DEFAULT_PATTERN_SHA1, context);
    }

    public static boolean hasPattern(Context context) {
        return !TextUtils.isEmpty(getPatternSha1(context));
    }

    public static boolean isPatternCorrect(List<PatternView.Cell> pattern, Context context) {
        return TextUtils.equals(patternToSha1String(pattern), getPatternSha1(context));
    }

    public static void clearPattern(Context context) {
        PreferenceUtil.remove(KEY_PATTERN_SHA1, context);
    }

    public static void setPatternByUser(Context context) {
        context.startActivity(new Intent(context, SetPatternActivity.class));
    }

    public static void confirmPatternIfHas(Activity activity) {
        if (hasPattern(activity)) {
            activity.startActivityForResult(new Intent(activity, ConfirmPatternActivity.class),
                    1214);
        }
    }

    public static boolean checkConfirmPatternResult(Activity activity, int requestCode, int resultCode) {
        if (requestCode == 1214 && resultCode != Activity.RESULT_OK) {
            activity.finish();
            return true;
        } else {
            return false;
        }
    }

    private static String bytesToString(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    public static byte[] patternToBytes(List<PatternView.Cell> pattern) {
        int patternSize = pattern.size();
        byte[] bytes = new byte[patternSize];
        for (int i = 0; i < patternSize; ++i) {
            PatternView.Cell cell = pattern.get(i);
            bytes[i] = (byte)(cell.getRow() * 3 + cell.getColumn());
        }
        return bytes;
    }

    public static List<PatternView.Cell> bytesToPattern(byte[] bytes) {
        List<PatternView.Cell> pattern = new ArrayList<>();
        for (byte b : bytes) {
            pattern.add(PatternView.Cell.of(b / 3, b % 3));
        }
        return pattern;
    }

    public static String patternToString(List<PatternView.Cell> pattern) {
        return bytesToString(patternToBytes(pattern));
    }

    public static List<PatternView.Cell> stringToPattern(String string) {
        return bytesToPattern(Base64.decode(string, Base64.DEFAULT));
    }

    private static byte[] sha1(byte[] input) {
        try {
            return MessageDigest.getInstance("SHA-1").digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String patternToSha1String(List<PatternView.Cell> pattern) {
        return bytesToString(sha1(patternToBytes(pattern)));
    }

    public static void announceForAccessibility(View view, CharSequence announcement) {
        view.announceForAccessibility(announcement);
    }

    public static void navigateUp(Activity activity) {
        Intent upIntent = NavUtils.getParentActivityIntent(activity);
        if (upIntent != null) {
            if (NavUtils.shouldUpRecreateTask(activity, upIntent)) {
                // This activity is NOT part of this app's task, so create a new task
                // when navigating up, with a synthesized back stack.
                TaskStackBuilder.create(activity)
                        // Add all of this activity's parents to the back stack.
                        .addNextIntentWithParentStack(upIntent)
                        // Navigate up to the closest parent.
                        .startActivities();
            } else {
                // This activity is part of this app's task, so simply
                // navigate up to the logical parent activity.
                // According to http://stackoverflow.com/a/14792752/2420519
                //NavUtils.navigateUpTo(activity, upIntent);
                upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(upIntent);
            }
        }
        activity.finish();
    }

    public static void setActionBarDisplayUp(AppCompatActivity activity) {
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
