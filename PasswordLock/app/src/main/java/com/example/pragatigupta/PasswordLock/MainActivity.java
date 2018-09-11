package com.example.pragatigupta.PasswordLock;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity
{
    EditText pass;
    Button button;
    String setPass1 = null;
    SharedPreferences mPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        startService(new Intent(this, LockScreenService.class));
        setContentView(R.layout.content_main);
        pass =(EditText)findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if(mPreferences.contains("PASSWORD"))
            button.setText("Unlock");
        else
            button.setText("Create Password");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(pass.getText())) {
                    if (mPreferences.contains("PASSWORD")) {
                        if ((pass.getText().toString()).equals(mPreferences.getString("PASSWORD", ""))) {
                            unlockScreen();
                        }
                        else{
                            pass.setText("");
                            Toast.makeText(getBaseContext(), "Wrong Password. Retry!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                        else {
                            if(!TextUtils.isEmpty(setPass1)){
                                if((pass.getText().toString()).equals(setPass1)){
                                    mPreferences.edit().putString("PASSWORD", setPass1).apply();
                                    unlockScreen();
                                }
                                else{
                                    setPass1 = null;
                                    button.setText("Create Password");
                                    Toast.makeText(getBaseContext(), "Passwords didn't match. Retry", Toast.LENGTH_LONG).show();
                                    pass.setText("");
                                }
                            }

                            else{
                                setPass1 = pass.getText().toString();
                                pass.setText("");
                                button.setText("Retype Password to Confirm");
                            }
                        }
                    }
                }
        });
    }

    public void makeFullScreen()
    {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(Build.VERSION.SDK_INT < 19)
        {
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
        else
        {
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }

    }

    public void unlockScreen()
    {
        startActivity(new Intent(this, Welcome.class));
    }

}