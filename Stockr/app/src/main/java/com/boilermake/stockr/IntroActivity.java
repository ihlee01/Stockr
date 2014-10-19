package com.boilermake.stockr;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import com.google.android.gcm.GCMRegistrar;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class IntroActivity extends Activity {

    Handler handler;
    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Full page logo screen - remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_intro);

        mPrefs = getSharedPreferences("data",0); // initialize SharedPreferences
        List<BoardItem> messages = new ArrayList<BoardItem>();
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

        registerGCM(); // register GCM and get registration ID

        // Delay for 1 sec
        handler = new Handler();
        handler.postDelayed(irun, 1200);

        //Log.e("RegisterID",GCMRegistrar.getRegistrationId(this));
    }

    Runnable irun = new Runnable() {
        public void run() {
            Intent i = new Intent(IntroActivity.this, BoardActivity.class);
            startActivity(i);
            finish();

            // Screen fade in & out effect
            overridePendingTransition(android.R.anim.fade_in, R.anim.out);
        }
    };

    public void registerGCM() {
        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);

        final String regID = GCMRegistrar.getRegistrationId(this);

        if(regID.equals("")) {
            GCMRegistrar.register(this,"817225698371"); // project number
        } else {
            Log.e("id",regID);
            SharedPreferences.Editor edit = mPrefs.edit();
            edit.putString("RegID",regID);
            edit.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.intro, menu);
        return true;
    }

    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(irun);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
