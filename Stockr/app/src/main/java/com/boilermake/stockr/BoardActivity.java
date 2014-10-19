package com.boilermake.stockr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
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
import android.widget.Button;
import android.widget.Checkable;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class BoardActivity extends Activity {

    SharedPreferences mPrefs;
    ArrayList<BoardItem> messages;
    ListView board_list;
    ArrayAdapter<BoardItem> adapter;
    HashMap<String, Drawable> company_image_map = new HashMap<String, Drawable>();
    Button subs_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        board_list = (ListView) findViewById(R.id.board_list);
        subs_button = (Button) findViewById(R.id.subscribe_start_button);
        subs_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BoardActivity.this, SubsActivity.class);
                startActivity(i);
            }
        });

        populateMap();

    }

    public void populateMap() {
        company_image_map.put("MSFT", getResources().getDrawable(R.drawable.microsoft));
        company_image_map.put("IBM", getResources().getDrawable(R.drawable.ibm));
        company_image_map.put("WMT", getResources().getDrawable(R.drawable.walmart));
        company_image_map.put("AAPL", getResources().getDrawable(R.drawable.apple));
        company_image_map.put("GOOG", getResources().getDrawable(R.drawable.google));
        company_image_map.put("QCOM", getResources().getDrawable(R.drawable.qualcomm));
        company_image_map.put("FB", getResources().getDrawable(R.drawable.facebook));
        company_image_map.put("TSLA", getResources().getDrawable(R.drawable.tesla));
        company_image_map.put("LNKD", getResources().getDrawable(R.drawable.linkedin));
        company_image_map.put("TWTR", getResources().getDrawable(R.drawable.twitter));
    }



    private class MyListAdapter extends ArrayAdapter<BoardItem>  {
        List<BoardItem> list;
        List<BoardItem> original_list;
        private int resource;

        public MyListAdapter(Context context, int resourceId, List<BoardItem> list) {
            super(context, resourceId, list);
            this.list = list;
            this.original_list = new ArrayList<BoardItem>(list);
            this.resource = resourceId;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            if (rowView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(resource, parent, false);
            }
            final BoardItem curItem = list.get(position);
            String symbol = curItem.getSymbol();
            String type = curItem.getType();
            String association = curItem.getAssociation();
            //Data convert
            Date date = new Date(curItem.getTimestamp());
            String timestamp = date.toString();

            double value = curItem.getValue();
            double original_value = curItem.getOriginal_value();

            ImageView company_image = (ImageView) rowView.findViewById(R.id.company_image);
            TextView timestamp_view = (TextView) rowView.findViewById(R.id.timestamp);
            TextView message_1 = (TextView) rowView.findViewById(R.id.message_1);
            TextView message_2 = (TextView) rowView.findViewById(R.id.message_2);

            //Set company image
            //Symbol array to string
            String company_symbol = null;
            company_image.setImageDrawable(company_image_map.get(company_symbol));

            //Timestamp
            timestamp_view.setText(timestamp);

            //Message 1
            String msg1 = "<b>Alert</b>: "+symbol+" "+association+" than threshold("+original_value+")";


            //Message 2
            String msg2 = "<b>Current price</b>: "+value;

            message_1.setText(Html.fromHtml(msg1));
            message_2.setText(Html.fromHtml(msg2));



            return rowView;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.board, menu);

        mPrefs = getSharedPreferences("data",0);

        messages = null;
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

        return true;
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
