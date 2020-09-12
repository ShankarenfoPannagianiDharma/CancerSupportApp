package com.example.shankarenfo.cancermonitorapp;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Shankarenfo on 3/25/2017.
 * Builds and shows notifications as per AlarmManager.
 */
public class NotificationReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        long when = System.currentTimeMillis();
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Bundle extras = intent.getExtras();
        String title = extras.getString("Title");
        String descript = extras.getString("Descript");

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(descript)
                .setSound(sound)
                .setAutoCancel(true)
                .setWhen(when)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        nm.notify(0, mNotifyBuilder.build());
    }
}
