package com.boilermake.stockr;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.util.Base64;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yongsun on 10/18/14.
 */
public class GCMIntentService extends GCMBaseIntentService {

    private SharedPreferences mPrefs;
    private boolean activityFound;

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

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);

        mPrefs = getSharedPreferences("data", 0);

        //DB lookup -> ArrayList<Subscribe> -> sharePreference

        List<BoardItem> messages = null;
        ByteArrayInputStream byteInputStream;
        ObjectInputStream objectInputStream;

        try {
            String encodedString = mPrefs.getString("messages", null);
            byte[] input = Base64.decode(encodedString, Base64.DEFAULT);
            byteInputStream = new ByteArrayInputStream(input);
            objectInputStream = new ObjectInputStream(byteInputStream);
            messages = (ArrayList<BoardItem>)objectInputStream.readObject();
            objectInputStream.close();

        } catch(IOException e) {
            e.printStackTrace();
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }

        String msg = intent.getStringExtra("gcm");
        int subId = intent.getIntExtra("subId", 0);
        double value = intent.getDoubleExtra("value", 0.0);
        long tiestamp = intent.getLongExtra("timestamp", 0);


        // messages.add

        SharedPreferences.Editor edit = mPrefs.edit();

        ObjectOutputStream objectOutputStream = null;
        try {
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteOutputStream);
            objectOutputStream.writeObject(messages);
            byte[] output = byteOutputStream.toByteArray();
            String encodedString = Base64.encodeToString(output, Base64.DEFAULT);
            edit.putString("messages",encodedString);
            edit.commit();
        } catch(IOException e) {
            e.printStackTrace();
        }

        /*
        ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> services = activityManager.getRunningTasks(Integer.MAX_VALUE);

        activityFound = false;

        if(services.get(0).topActivity.getPackageName().toString().equalsIgnoreCase(this.getPackageName().toString())) {
            activityFound = true;
        }*/

        generateNotification(context, msg);
    }

    @Override
    protected void onError(Context context, String s) {

    }

    @Override
    protected void onRegistered(Context context, String reg_id) {
        Log.e("Register the key.(GCM INTENT_SERVICE)", reg_id);
    }

    @Override
    protected void onUnregistered(Context context, String s) {
        Log.e("Unregister the key.(GCM INTENT_SERVICE)", "Removed");
    }
}
