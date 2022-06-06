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
import com.example.prm_392_nho_ho_ban.bean.Note;
import com.example.prm_392_nho_ho_ban.dao.NoteDAO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Random;

@RequiresApi(api = Build.VERSION_CODES.O)
public class NotificationScheduling extends IntentService {

    private static final String ACTION_REBUILD_NOTIFY = "ReBuildNotify";
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private static final int TIME_VIBRATE = 1000;
    private static final String CHANNEL_ID = "PRM";
    public static final String ACTION_BUILD_NOTIFY = "BuildNotify";
    public static final String ACTION_CANCEL_NOTIFY = "CancelNotify";
    public NotificationScheduling() {
        super(NotificationScheduling.class.getSimpleName());
    }
    NotificationManager manager;
    NotificationChannel notificationChannel;
    @Override
    protected void onHandleIntent(Intent intent) {

         manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
         notificationChannel =
                new NotificationChannel(CHANNEL_ID, "Mascot Notification",
                        NotificationManager.IMPORTANCE_HIGH);

        String action = intent.getStringExtra("action");


        if(action.equals(ACTION_BUILD_NOTIFY)){
            String noteJson = intent.getStringExtra("noteJson");
            Note note = new Gson().fromJson(noteJson,Note.class);
            buildNotify(note,manager,notificationChannel);
            buildAlarm(note);
        }else if(action.equals(ACTION_CANCEL_NOTIFY)){
            manager.cancelAll();
        }
    }

    private void buildNotify(Note note, NotificationManager manager, NotificationChannel notificationChannel){
        manager.createNotificationChannel(notificationChannel);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.logo_icon)
                        .setContentTitle("You got a note remind")
                        .setContentText(note.getTitle())
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
        manager.notify(new Random().nextInt(10000), notification);
    }


    private static void buildAlarm(Note note){

    }

}
