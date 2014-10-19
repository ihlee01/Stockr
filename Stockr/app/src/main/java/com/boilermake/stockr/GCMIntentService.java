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
    private static boolean activityFound;
    static String path = Environment.getExternalStorageDirectory() + "/SUBSdata/subs.dat";
    static String history_path = Environment.getExternalStorageDirectory() + "/SUBSdata/history.dat";

    HashMap<String, BoardItem> item_stack;
    HashMap<String, ArrayList<BoardItem>> history_map = new HashMap<String, ArrayList<BoardItem>>();


    private static void generateNotification(Context context, String message) {

        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();


        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new Notification(icon, message, when);

        String title = context.getString(R.string.app_name);


        Intent notificationIntent = null;

        notificationIntent = new Intent(context, BoardActivity.class);
        //notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context,0,notificationIntent,0);

        notification.setLatestEventInfo(context,title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        Log.e("INTENT?", "intent get triggered");

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


        int subId = Integer.parseInt(intent.getStringExtra("subId"));
        double value = Double.parseDouble(intent.getStringExtra("value"));
        long timestamp = Long.parseLong(intent.getStringExtra("timestamp"));

        Log.e("DATA?", subId+value+timestamp+"");
        Log.e("SUb", subId+"");

        HashMap<Integer, Subscribe> read_map = readSubsMap();
        Subscribe sub_obj = read_map.get(subId);

        //Message for notification
        String tmp = sub_obj.getSymbol();
        tmp = tmp.substring(1,tmp.length()-1);
        String[] symbol_list;
        if(tmp.contains(",")) {
            symbol_list = tmp.split(",");
        }
        else {
            symbol_list = new String[1];
            symbol_list[0] = tmp;
        }

        for(int i=0; i<symbol_list.length; i++) {
            symbol_list[i] = symbol_list[i].substring(1,symbol_list[i].length()-1);
        }
        String msg = "";
        if(symbol_list.length == 1) {

            //Message 1
            msg = "Alert for "+symbol_list[0];
        }
        else {
            //Message 1
            String symbols = "";
            for(int i = 0 ; i < symbol_list.length; i++) {
                symbols+= symbol_list[i];
                symbols+= ", ";
            }
            symbols = symbols.substring(0, symbols.length()-2);

            msg = "Alert for "+symbols;
        }



        Log.e("Sub_obj", sub_obj.getSymbol());


        //BoardItems are being stacked
        BoardItem bitem = new BoardItem(sub_obj.getSymbol(), sub_obj.getType(), value, timestamp, sub_obj.getAssociation(), sub_obj.getValue());
        messages.add(bitem);


        //HashMap to replace

        item_stack = new HashMap<String, BoardItem>();

        for(BoardItem item : messages) {
            item_stack.put(item.getSymbol(), item);
            Log.e("MESS", item.getSymbol());
/*
            if(history_map.get(item.getSymbol()) == null) {
                ArrayList<BoardItem> item_list = new ArrayList<BoardItem>();
                item_list.add(item);
                history_map.put(item.getSymbol(), item_list);
            }
            else {
                //update
                ArrayList<BoardItem> current_list = history_map.get(item.getSymbol());
                current_list.add(item);
                history_map.put(item.getSymbol(), current_list);
            }
*/
        }
//        saveHistoryMap(history_map);


        messages = new ArrayList<BoardItem>();
        for (BoardItem item : item_stack.values()) {
            messages.add(item);
        }




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
/*
    public static void saveHistoryMap(HashMap<String, ArrayList<BoardItem>> o) {
        File f = new File(history_path);
        File store = new File(Environment.getExternalStorageDirectory() + "/SUBSdata");
        if(!store.exists()) {
            store.mkdirs();
        }

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(o);
            oos.flush();
            oos.close();
            Log.d("HISTORY", "Intent: Map Saved for History");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("HISTORY", "Save Error: " + e.getMessage());
        }
    }
*/
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
/*
    public static HashMap<String, ArrayList<BoardItem>> readHistoryMap() {
        try {
            File f = new File(history_path);
            Log.d("HISTORY", "Intent: Map read for history");
            return (HashMap<String, ArrayList<BoardItem>>) new ObjectInputStream((new FileInputStream(f))).readObject();

        }catch(FileNotFoundException e) {
            Log.d("HISTORY", "FNE");
            return new HashMap<String, ArrayList<BoardItem>>();
        }catch(Exception ex) {
            Log.d("HISTORY", "Null");
            ex.printStackTrace();
            return new HashMap<String, ArrayList<BoardItem>>();
        }
    }
*/
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
