package com.example.prm_392_nho_ho_ban.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.prm_392_nho_ho_ban.schedulingservice.NotificationScheduling;
import com.google.firebase.auth.FirebaseAuth;

public class LogoutActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationScheduling.class);
        intent.putExtra("action", NotificationScheduling.ACTION_CANCEL_NOTIFY);
        PendingIntent pendingIntent = PendingIntent.getService(this, 1, intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, 100, pendingIntent);

        firebaseAuth.signOut();
        startActivity(new Intent(this, LoginActivity.class));
    }
}