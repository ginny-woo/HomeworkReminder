package com.jeongwoo.johnc.homeworkreminder;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class AlarmService extends BroadcastReceiver {

        public static String CHANNEL_ID = "CHANNEL_ID";
        public Context c;

        @Override
        public void onReceive(Context context, Intent intent) {
            String[] values = intent.getExtras().getStringArray("key");

            c=context;

            Intent i = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle("Homework Assignment Due at " + values[2])
                    .setContentText(values[0] + " is due soon so you finish it up!")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            builder.notify();

        }

}