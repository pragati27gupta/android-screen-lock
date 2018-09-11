package com.example.praga.androidlockscreencustom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.praga.androidlockscreencustom.util.Util;

public class ResetActivity extends AppCompatActivity {

    Button mOkButton;
    Button mCancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.reset_pattern_activity);
        mOkButton = (Button)findViewById(R.id.ok_button);
        mCancelButton = (Button) findViewById(R.id.cancel_button);

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.clearPattern(ResetActivity.this);
                Toast.makeText(ResetActivity.this, R.string.pattern_reset, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
