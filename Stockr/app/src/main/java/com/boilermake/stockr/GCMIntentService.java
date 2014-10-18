package com.boilermake.stockr;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

/**
 * Created by Yongsun on 10/18/14.
 */
public class GCMIntentService extends GCMBaseIntentService {

    private SharedPreferences mPrefs;

    private static void generateNotification(Context context, String message) {

        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new Notification(icon, message, when);

        String title = context.getString(R.string.app_name);


        Intent notificationIntent = new Intent(context, IntroActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context,0,notificationIntent,0);

        notification.setLatestEventInfo(context,title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, notification);


    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        String msg = intent.getStringExtra("msg");
        String msg2 = intent.getStringExtra("msg2");
        Log.e("getMessage", "getMessage:"+msg);

        generateNotification(context,msg);
        generateNotification(context,msg2);
    }

    @Override
    protected void onError(Context context, String s) {

    }

    @Override
    protected void onRegistered(Context context, String reg_id) {
        Log.e("Register the key.(GCM INTENT_SERVICE)",reg_id);
    }

    @Override
    protected void onUnregistered(Context context, String s) {
        Log.e("Unregister the key.(GCM INTENT_SERVICE)", "Removed");
    }
}
