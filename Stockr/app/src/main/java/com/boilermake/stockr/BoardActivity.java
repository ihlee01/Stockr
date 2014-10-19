package com.boilermake.stockr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;


public class BoardActivity extends Activity {

    private SharedPreferences mPrefs;
    //private List<DashboardItem> messages;
    //private ArrayAdapter<DashboardItem> adapter;
    private ListView listView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        context = this;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.board, menu);

        mPrefs = getSharedPreferences("data",0);

        /*messages = null;
        ByteArrayInputStream byteInputStream;
        ObjectInputStream objectInputStream;

        try {
            String encodedString = mPrefs.getString("messages", null);
            byte[] input = Base64.decode(encodedString, Base64.DEFAULT);
            byteInputStream = new ByteArrayInputStream(input);
            objectInputStream = new ObjectInputStream(byteInputStream);
            messages = (ArrayList<DashboardItem>)objectInputStream.readObject();
            objectInputStream.close();

        } catch(IOException e) {
            e.printStackTrace();
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }

        listView = (ListView)findViewById(R.id.dashBoardList);
        listView.setAdapter(new DashboardListAdapter(context, R.layout.dashboard_item, messages));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
*/
        return true;
    }
/*
    private class DashboardListAdapter extends ArrayAdapter<DashboardItem> {
        private int resource;
        private LayoutInflater inflater;
        private Context context;

        public DashboardListAdapter(Context context, int resource, List<DashboardItem> items) {
            super(context,resource,items);
            this.resource = resource;
            this.inflater = LayoutInflater.from(context);
            this.context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = (RelativeLayout)inflater.inflate(resource,null);
            DashboardItem item = getItem(position);
            TextView info = (TextView)convertView.findViewById(R.id.info);
            info.setText(item.getInformation());

            return convertView;
        }
    }

*/
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
