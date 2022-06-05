package com.example.prm_392_nho_ho_ban.schedulingservice;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.example.prm_392_nho_ho_ban.R;
import com.example.prm_392_nho_ho_ban.activity.EditNoteActivity;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class NotificationScheduling extends IntentService {
    private static final int TIME_VIBRATE = 1000;
    private static final String CHANNEL_ID = "PRM";
    public NotificationScheduling() {
        super(NotificationScheduling.class.getSimpleName());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onHandleIntent(Intent intent) {
        String noteTitle = intent.getStringExtra("noteTitle");
        int notificationId = intent.getIntExtra("notificationId",1);
        Log.i("Tagasd","den");
        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel =
                    new NotificationChannel(CHANNEL_ID, "Mascot Notification",
                            NotificationManager.IMPORTANCE_DEFAULT);
        }
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        manager.createNotificationChannel(notificationChannel);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.logo_icon)
                        .setContentTitle("You've been notified!")
                        .setContentText("This is your notification text.")
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setPriority(6)
                .setVibrate(new long[]{TIME_VIBRATE, TIME_VIBRATE, TIME_VIBRATE, TIME_VIBRATE,
                        TIME_VIBRATE});
        Intent intentClick = new Intent(this, EditNoteActivity.class);
        PendingIntent pending = PendingIntent.getActivity(
                this, 0, intentClick, 0);
        mBuilder.setContentIntent(pending);
        Notification notification = mBuilder.build();
        manager.notify(1, notification);

    }
}
