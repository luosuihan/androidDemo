package com.example.m1762.myapplication;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.ACTION_TIME_TICK.equals(intent.getAction())){
//            updateTime();//每一分钟更新时间
            Log.e("luosuihan", "onReceive: 时间有变");
            PowerManager pm=(PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
            wl.acquire();
            KeyguardManager km= (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
            kl.disableKeyguard();
            kl.reenableKeyguard();
            wl.release();
        }else if(intent.ACTION_TIME_CHANGED.equals(intent.getAction())){
        }
    }
}
