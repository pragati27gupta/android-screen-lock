package com.example.pragatigupta.pinlock;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Pragati Gupta on 11-04-2016.
 */
public class LockScreenService extends Service {

    BroadcastReceiver receiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    @SuppressWarnings("deprecation")
    public void onCreate()
    {
        KeyguardManager.KeyguardLock key;
        KeyguardManager km = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);

        key = km.newKeyguardLock("IN");
        key.disableKeyguard();

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_BOOT_COMPLETED);

        receiver = new LockScreenReceiver();
        registerReceiver(receiver, filter);


    }

}
