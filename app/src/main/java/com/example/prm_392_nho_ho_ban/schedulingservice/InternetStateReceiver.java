package com.example.prm_392_nho_ho_ban.schedulingservice;

import static com.example.prm_392_nho_ho_ban.activity.SplashActivity.INTERNET_STATE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import com.example.prm_392_nho_ho_ban.activity.SplashActivity;

public class InternetStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isAvailable() || mobile.isAvailable()) {
            INTERNET_STATE = true;
            Log.i("Internet","Yes");
        }else{
            INTERNET_STATE = false;
            Log.i("Internet","No");
        }
    }
    }

