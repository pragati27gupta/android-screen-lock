package com.example.pragatigupta.pinlock;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {

    private SharedPreferences mPreferences;
    Button b1,b2,b3,b4,b5,b6,b7,b8,b9,b10,b11,b12;
    TextView tv, tvPassMessage;
    String enteredpass="";
    String pass1 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        makeFullScreen();
        startService(new Intent(this, LockScreenService.class));
        setContentView(R.layout.content_main);
        b1=(Button)findViewById(R.id.btn1);
        b2=(Button)findViewById(R.id.btn2);
        b3=(Button)findViewById(R.id.btn3);
        b4=(Button)findViewById(R.id.btn4);
        b5=(Button)findViewById(R.id.btn5);
        b6=(Button)findViewById(R.id.btn6);
        b7=(Button)findViewById(R.id.btn7);
        b8=(Button)findViewById(R.id.btn8);
        b9=(Button)findViewById(R.id.btn9);
        b10=(Button)findViewById(R.id.btn10);
        b11=(Button)findViewById(R.id.btn11);
        b12=(Button)findViewById(R.id.btn12);
        b1.setOnClickListener((OnClickListener) this);
        b2.setOnClickListener((OnClickListener)this);
        b3.setOnClickListener((OnClickListener)this);
        b4.setOnClickListener((OnClickListener)this);
        b5.setOnClickListener((OnClickListener)this);
        b6.setOnClickListener((OnClickListener)this);
        b7.setOnClickListener((OnClickListener)this);
        b8.setOnClickListener((OnClickListener)this);
        b9.setOnClickListener((OnClickListener)this);
        b10.setOnClickListener((OnClickListener)this);
        b11.setOnClickListener((OnClickListener)this);
        b12.setOnClickListener((OnClickListener)this);
        tv=(TextView)findViewById(R.id.textView);
        tvPassMessage = (TextView)findViewById(R.id.tv_PassMessage);

        if(mPreferences.contains("PASSWORD")){
            tvPassMessage.setText("Enter Pin to Unlock");
        }
        else{
            tvPassMessage.setText("Create a Pin");
        }

    }

    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn1:
            case R.id.btn2:
            case R.id.btn3:
            case R.id.btn4:
            case R.id.btn5:
            case R.id.btn6:
            case R.id.btn7:
            case R.id.btn8:
            case R.id.btn9:
            case R.id.btn11:
                enteredpass+= ((Button)v).getText().toString() ;
                tv.setText(enteredpass);
                break;
            case R.id.btn10:
                int len = enteredpass.length();
                if(len>0)
                    enteredpass = enteredpass.substring(0, len-1);
                    tv.setText(enteredpass);
                break;
            case R.id.btn12:
                if(!TextUtils.isEmpty(enteredpass)){
                    if(!mPreferences.contains("PASSWORD")){
                        if(TextUtils.isEmpty(pass1)) {
                            pass1 = enteredpass;
                            enteredpass="";
                            tv.setText(enteredpass);
                            tvPassMessage.setText("Re-enter Pin to Confirm");
                        }
                        else {
                            if(pass1.equals(enteredpass)){
                                mPreferences.edit().putString("PASSWORD", pass1).apply();
                                unlockScreen();
                            }
                            else{
                                Toast.makeText(this, "Password Mismatch. Retry!!", Toast.LENGTH_LONG).show();
                                pass1 = "";
                                enteredpass="";
                                tv.setText(enteredpass);
                                tvPassMessage.setText("Create a Pin");
                            }
                        }
                    }
                    else {
                        if(enteredpass.equals(mPreferences.getString("PASSWORD", "")))
                            unlockScreen();
                        else{
                            //wrong password
                            Toast.makeText(MainActivity.this, "Wrong Pin", Toast.LENGTH_LONG).show();
                        }
                        tv.setText("");
                        enteredpass="";
                    }
            }
                break;
        }

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
