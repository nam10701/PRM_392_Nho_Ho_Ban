package com.example.prm_392_nho_ho_ban.schedulingservice;

import static com.example.prm_392_nho_ho_ban.MyApplication.CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import com.example.prm_392_nho_ho_ban.R;
import com.example.prm_392_nho_ho_ban.activity.EditNoteActivity;
import com.example.prm_392_nho_ho_ban.bean.Note;
import com.google.gson.Gson;

import java.util.Random;

public class AlarmReceiver extends BroadcastReceiver {
    private static final int TIME_VIBRATE = 1000;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        Note note = retrieveNote(intent);

        Intent intentClick = new Intent(context, EditNoteActivity.class);
        PendingIntent pending = PendingIntent.getActivity(
                context, 0, intentClick, 0);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_icon)
                .setContentTitle("You got a note remind")
                .setContentText(note.getTitle())
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setPriority(6)
                .setVibrate(new long[]{TIME_VIBRATE, TIME_VIBRATE, TIME_VIBRATE, TIME_VIBRATE,
                        TIME_VIBRATE})
                .setContentIntent(pending);

        Notification notification = mBuilder.build();
        int id = new Random().nextInt(1000);
        Log.i("RECEIVER",id+"");
        manager.notify(id,notification);
    }

    private Note retrieveNote(Intent intent){
        String noteJson = intent.getStringExtra("noteJson");
        return new Gson().fromJson(noteJson,Note.class);
    }
}
