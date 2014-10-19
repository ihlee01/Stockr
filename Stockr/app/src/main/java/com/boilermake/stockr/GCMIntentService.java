package com.boilermake.stockr;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Base64;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Yongsun on 10/18/14.
 */
public class GCMIntentService extends GCMBaseIntentService {

    private SharedPreferences mPrefs;
    private boolean activityFound;
    static String path = Environment.getExternalStorageDirectory() + "/SUBSdata/subs.dat";


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
        int subId = Integer.parseInt(intent.getStringExtra("subId"));
        double value = Double.parseDouble(intent.getStringExtra("value"));
        long timestamp = Long.parseLong(intent.getStringExtra("timestamp"));



        HashMap<Integer, Subscribe> read_map = readSubsMap();
        Subscribe sub_obj = read_map.get(subId);

        BoardItem bitem = new BoardItem(sub_obj.getSymbol(), sub_obj.getType(), value, timestamp, sub_obj.getAssociation(), sub_obj.getValue());
        messages.add(bitem);

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

    public static void saveSubsMap(HashMap<Integer,Subscribe> o){
        File f = new File(path);
        File store = new File(Environment.getExternalStorageDirectory() + "/SUBSdata");
        if(!store.exists()){
            store.mkdirs();
            Log.e("MKDIR", "TRUE");
        }

        try{
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f)); //Select where you wish to save the file...
            oos.writeObject(o); // write the class as an 'object'
            oos.flush(); // flush the stream to insure all of the information was written to 'save.bin'
            oos.close();// close the stream
            Log.d("SUBS", "Intent: Map saved");

        }catch(Exception e){
            e.printStackTrace();
            Log.d("SUBS", "save error: " + e.getMessage());
        }
    }

    public static HashMap<Integer,Subscribe> readSubsMap(){
        try{
            File f = new File(path);
            Log.d("SUBS", "Intent: Map read");
            return (HashMap<Integer,Subscribe>) new ObjectInputStream(new FileInputStream(f)).readObject();
        }catch(FileNotFoundException e){
            Log.d("SUBS","FNE");
            return new HashMap<Integer,Subscribe>();
        }catch(Exception ex){
            Log.d("SUBS","Null");
            ex.printStackTrace();
            return new HashMap<Integer,Subscribe>();
        }
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
