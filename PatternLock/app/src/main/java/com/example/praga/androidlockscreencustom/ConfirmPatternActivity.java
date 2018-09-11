package com.example.praga.androidlockscreencustom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.praga.androidlockscreencustom.util.PreferenceUtil;
import com.example.praga.androidlockscreencustom.util.Util;

import java.util.List;

public class ConfirmPatternActivity extends BasePatternAcitivity implements PatternView.OnPatternListener {

    private static final String KEY_NUM_FAILED_ATTEMPTS = "num_failed_attempts";

    public static final int RESULT_FORGOT_PASSWORD = RESULT_FIRST_USER;

    protected int numFailedAttempts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMessageText.setText(R.string.pl_draw_pattern_to_unlock);
        mPatternView.setInStealthMode(isStealthModeEnabled());
        mPatternView.setOnPatternListener(this);
        mLeftButton.setText(R.string.pl_cancel);
        mLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancel();
            }
        });
        mRightButton.setText(R.string.pl_forgot_pattern);
        mRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onForgotPassword();
            }
        });
        Util.announceForAccessibility(mMessageText, mMessageText.getText());

        if (savedInstanceState == null) {
            numFailedAttempts = 0;
        } else {
            numFailedAttempts = savedInstanceState.getInt(KEY_NUM_FAILED_ATTEMPTS);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_NUM_FAILED_ATTEMPTS, numFailedAttempts);
    }

    @Override
    public void onPatternStart() {

        removeClearPatternRunnable();

        // Set display mode to correct to ensure that pattern can be in stealth mode.
        mPatternView.setDisplayMode(PatternView.DisplayMode.Correct);
    }

    @Override
    public void onPatternCellAdded(List<PatternView.Cell> pattern) {}

    @Override
    public void onPatternDetected(List<PatternView.Cell> pattern) {
        if (isPatternCorrect(pattern)) {
            onConfirmed();
        } else {
            mMessageText.setText(R.string.pl_wrong_pattern);
            mPatternView.setDisplayMode(PatternView.DisplayMode.Wrong);
            postClearPatternRunnable();
            Util.announceForAccessibility(mMessageText, mMessageText.getText());
            onWrongPattern();
        }
    }

    @Override
    public void onPatternCleared() {
        removeClearPatternRunnable();
    }

    protected boolean isStealthModeEnabled() {
        return !PreferenceUtil.getBoolean(Util.KEY_PATTERN_VISIBLE,
                Util.DEFAULT_PATTERN_VISIBLE, this);
    }

    protected boolean isPatternCorrect(List<PatternView.Cell> pattern) {
        return Util.isPatternCorrect(pattern, this);
    }

    protected void onConfirmed() {
        setResult(RESULT_OK);
        finish();
    }

    protected void onWrongPattern() {
        ++numFailedAttempts;
    }

    protected void onCancel() {
        setResult(RESULT_CANCELED);
        finish();
    }

    protected void onForgotPassword() {
        startActivity(new Intent(this, ResetActivity.class));
        setResult(RESULT_FORGOT_PASSWORD);
        finish();
    }

}
