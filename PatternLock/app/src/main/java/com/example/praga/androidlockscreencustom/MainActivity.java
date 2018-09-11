package com.example.praga.androidlockscreencustom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.praga.androidlockscreencustom.util.Util;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_CONFIRM_STARTED = "confirm_started";

    private boolean mConfirmStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mConfirmStarted = savedInstanceState.getBoolean(KEY_CONFIRM_STARTED);
        }
        if (!mConfirmStarted) {
            Util.confirmPatternIfHas(this);
            mConfirmStarted = true;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(KEY_CONFIRM_STARTED, mConfirmStarted);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Util.navigateUp(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Util.checkConfirmPatternResult(this, requestCode, resultCode)) {
            mConfirmStarted = false;
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
