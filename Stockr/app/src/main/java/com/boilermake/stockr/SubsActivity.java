package com.boilermake.stockr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SubsActivity extends Activity {

    Button instant_button;
    Button spatial_button;
    Button time_button;
    Button greater_button;
    Button lesser_button;
    Button subs_butotn;
    EditText input_value;
    EditText input_time;
    ListView company_list;
    ArrayAdapter<Company> adapter;
    ArrayList<Company> companies;
    ArrayList<String> company_symbols;

    LinearLayout company_layout;
    LinearLayout time_layout;

    SharedPreferences mPrefs;


    int type = 1; // 1 by default
    int association = 1; // 1 by default

    final Context context = this;

    static String path = Environment.getExternalStorageDirectory() + "/SUBSdata/subs.dat";


    MySQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subs);
        Animation slideup = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideup);
        LinearLayout layout = (LinearLayout) findViewById(R.id.subs_layout);

        layout.startAnimation(slideup);
        layout.setVisibility(View.VISIBLE);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        db = new MySQLiteHelper(this);

        //Components initializing
        instant_button = (Button) findViewById(R.id.instant_button);
        spatial_button = (Button) findViewById(R.id.spatial_button);
        time_button = (Button) findViewById(R.id.time_button);
        greater_button = (Button) findViewById(R.id.greater_button);
        lesser_button = (Button) findViewById(R.id.lesser_button);
        input_value = (EditText) findViewById(R.id.input_value);
        input_time = (EditText) findViewById(R.id.input_time);
        subs_butotn = (Button) findViewById(R.id.subs_button);
        company_list = (ListView) findViewById(R.id.company_list);
        company_layout = (LinearLayout) findViewById(R.id.company_layout);
        time_layout = (LinearLayout) findViewById(R.id.time_layout);

        company_symbols = new ArrayList<String>();

        mPrefs = getSharedPreferences("data", 0); // initialize SharedPreferences

        //Populate companies
        companies = new ArrayList<Company>();
        if (companies.size() == 0) {
            populateCompany();
        }
        generateList(company_list, R.layout.company_item_radio);
        company_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        //Event Listener
        input_value.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                input_value.setHint("");
            }
        });
        input_time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                input_time.setHint("");
            }
        });

        //1. Type Buttons
        instant_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                instant_button.setBackground(getResources().getDrawable(R.drawable.blue_shape));
                instant_button.setTextColor(getResources().getColor(R.color.light_grey));

                spatial_button.setBackgroundColor(Color.WHITE);
                spatial_button.setTextColor(getResources().getColor(R.color.heavy_grey));

                time_button.setBackgroundColor(Color.WHITE);
                time_button.setTextColor(getResources().getColor(R.color.heavy_grey));

                type = 1;

                generateList(company_list, R.layout.company_item_radio);
                time_layout.setVisibility(View.GONE);
                company_symbols.clear();
            }
        });
        spatial_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spatial_button.setBackground(getResources().getDrawable(R.drawable.blue_shape));
                spatial_button.setTextColor(getResources().getColor(R.color.light_grey));

                instant_button.setBackgroundColor(Color.WHITE);
                instant_button.setTextColor(getResources().getColor(R.color.heavy_grey));

                time_button.setBackgroundColor(Color.WHITE);
                time_button.setTextColor(getResources().getColor(R.color.heavy_grey));

                type = 2;

                generateList(company_list, R.layout.company_item_radio);
                time_layout.setVisibility(View.GONE);
                company_symbols.clear();

            }
        });
        time_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time_button.setBackground(getResources().getDrawable(R.drawable.blue_shape));
                time_button.setTextColor(getResources().getColor(R.color.light_grey));

                spatial_button.setBackgroundColor(Color.WHITE);
                spatial_button.setTextColor(getResources().getColor(R.color.heavy_grey));


                instant_button.setBackgroundColor(Color.WHITE);
                instant_button.setTextColor(getResources().getColor(R.color.heavy_grey));

                type = 3;

                generateList(company_list, R.layout.company_item_radio);
                time_layout.setVisibility(View.VISIBLE);
                company_symbols.clear();
            }
        });


        //2. Association Buttons
        greater_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                greater_button.setBackground(getResources().getDrawable(R.drawable.blue_shape));
                greater_button.setTextColor(getResources().getColor(R.color.light_grey));

                lesser_button.setBackgroundColor(Color.WHITE);
                lesser_button.setTextColor(getResources().getColor(R.color.heavy_grey));

                association = 1;
            }
        });
        lesser_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lesser_button.setBackground(getResources().getDrawable(R.drawable.blue_shape));
                lesser_button.setTextColor(getResources().getColor(R.color.light_grey));

                greater_button.setBackgroundColor(Color.WHITE);
                greater_button.setTextColor(getResources().getColor(R.color.heavy_grey));

                association = 2;
            }
        });

        //3. Subscribe button
        subs_butotn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        view.setBackgroundColor(view.getResources().getColor(R.color.green_pressed));
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        view.setBackgroundColor(view.getResources().getColor(R.color.start_green));
                        //Send data to server here...
                        Double value = 0.0;
                        int timewindow = 0;
                        int subID = mPrefs.getInt("subID", 0) + 1;

                        SharedPreferences.Editor editor = mPrefs.edit();
                        editor.putInt("subID", subID);
                        editor.commit();
                        //value
                        if (!input_value.getText().toString().equals("")) {
                            value = Double.parseDouble(input_value.getText() + "");
                        } else {
                            AlertDialog.Builder error = new AlertDialog.Builder(context);
                            error.setTitle("ERROR");
                            error.setMessage("Make sure to set the value.");
                            error.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    return;
                                }
                            });
                            error.show();
                            break;
                        }

                        //time
                        if (!input_time.getText().toString().equals("")) {
                            timewindow = Integer.parseInt(input_time.getText() + "");
                        } else {
                            if (type == 3) {
                                AlertDialog.Builder error = new AlertDialog.Builder(context);
                                error.setTitle("ERROR");
                                error.setMessage("Make sure to set the time window.");
                                error.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {

                                    }
                                });
                                error.show();
                                break;
                            } else {
                                timewindow = 0;
                            }
                        }

                        //gcm
                        String gcm = mPrefs.getString("RegID", "");
                        if (company_symbols.size() == 0) {
                            AlertDialog.Builder error = new AlertDialog.Builder(context);
                            error.setTitle("ERROR");
                            error.setMessage("Please, select at least one company.");
                            error.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    return;
                                }
                            });
                            error.show();
                            break;
                        }


                        //Generate JSON Object
                        JSONObject json = new JSONObject();

                        JSONArray symbols = new JSONArray();
                        if (type == 1 || type == 3) {
                            //When it is instant or time -> put the latest one
                            symbols.put(company_symbols.get(company_symbols.size() - 1));
                            Log.e("Symbol added for type 1 or 3", company_symbols.get(company_symbols.size() - 1));
                        } else {
                            //otherwise, put the whole list
                            for (String symbol : company_symbols) {
                                symbols.put(symbol);
                                Log.e("Symbols added for type 2", symbol);
                            }
                        }

                        try {
                            json.put("symbol", symbols);
                            json.put("value", value);
                            json.put("subId", subID);
                            json.put("gcm", gcm);
                            json.put("type", type);
                            json.put("association", association);
                            json.put("timewindow", timewindow);

                            Log.e("JSON OBJ", json.toString());

                            //Do the httpRequest
                            doPost(json);

                            HashMap<Integer, Subscribe> map = readSubsMap();


                            Subscribe new_sub = new Subscribe(subID, type, value, symbols.toString(), association, timewindow);
                            map.put(subID, new_sub);

                            saveSubsMap(map);
                        } catch (JSONException e) {
                            Log.e("JSON EXCEPTION", e.toString());
                        }

                    }
                }
                return false;
            }
        });

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
    public void populateCompany() {
        companies.add(new Company("1", "Microsoft", "MSFT"));
        companies.add(new Company("2", "IBM", "IBM"));
        companies.add(new Company("3", "Walmart", "WMT"));
        companies.add(new Company("4", "Apple", "AAPL"));
        companies.add(new Company("5", "Google", "GOOG"));
        companies.add(new Company("6", "Qualcomm", "QCOM"));
        companies.add(new Company("7", "Facebook", "FB"));
        companies.add(new Company("8", "Tesla", "TSLA"));
        companies.add(new Company("9", "LinkedIn", "LNKD"));
        companies.add(new Company("10", "Twitter", "TWTR"));
    }

    private void generateList(ListView view, int layout) {
        //defaultView.setVisibility(View.GONE);
        adapter = new MyListAdapter(getBaseContext(), layout, companies);
        SwingBottomInAnimationAdapter swing = new SwingBottomInAnimationAdapter(adapter);
        swing.setAbsListView(view);
        view.setAdapter(swing);
        view.setTextFilterEnabled(true);
    }


    private void doPost(JSONObject json) {
        try {
            Socket s = new Socket("10.184.100.240", 8001);
            PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
            pw.println(json.toString());
            s.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }


    private class MyListAdapter extends ArrayAdapter<Company> implements Filterable, Checkable {
        List<Company> list;
        List<Company> original_list;
        private CompanyFilter filter;
        private int resource;

        public MyListAdapter(Context context, int resourceId, List<Company> list) {
            super(context, resourceId, list);
            this.list = list;
            this.original_list = new ArrayList<Company>(list);
            this.resource = resourceId;
        }

        @Override
        public Filter getFilter() {
            if (filter == null) {
                filter = new CompanyFilter();
            }
            return filter;
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
            final Company curCompany = list.get(position);
            String name = curCompany.getName();
            ImageView company_image = (ImageView) rowView.findViewById(R.id.company_image);
            TextView company_name = (TextView) rowView.findViewById(R.id.company_name);
            final RadioButton company_radio = (RadioButton) rowView.findViewById(R.id.company_radio);

            //Set company image

            Resources res = getResources();
            String image_name = curCompany.getName().toLowerCase();
            int resID = res.getIdentifier(image_name, "drawable", getPackageName());
            Drawable drawable = res.getDrawable(resID);
            company_image.setImageDrawable(drawable);
            company_name.setText(name);

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //check the radio button
                    if (company_radio.isChecked()) {
                        company_radio.setChecked(false);
                        company_symbols.remove(curCompany.getSymbol());
                    } else {
                        company_radio.setChecked(true);
                        Log.e("SYMBOL", curCompany.getSymbol());
                        if (type != 0) {
                            if (type == 1 || type == 3) {
                                company_symbols.add(curCompany.getSymbol());
                            } else {
                                if (!company_symbols.contains(curCompany.getSymbol())) {
                                    company_symbols.add(curCompany.getSymbol());
                                }
                            }
                        }

                    }
                }
            });
            company_radio.setClickable(false);


            return rowView;
        }

        @Override
        public void setChecked(boolean b) {

        }

        @Override
        public boolean isChecked() {
            return false;
        }

        @Override
        public void toggle() {

        }

        private class CompanyFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                charSequence = charSequence.toString().toLowerCase();
                if (charSequence == null || charSequence.length() == 0) {
                    results.values = original_list;
                    results.count = original_list.size();
                } else {
                    List<Company> result = new ArrayList<Company>();
                    for (Company company : original_list) {
                        if (company.getName().toLowerCase().contains(charSequence)) {
                            result.add(company);
                        }
                    }
                    results.values = result;
                    results.count = result.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults.count == 0) {
                    list = original_list;
                    notifyDataSetInvalidated();
                    //defaultView.setVisibility(View.VISIBLE);
                    company_list.setVisibility(View.GONE);
                } else {
                    //defaultView.setVisibility(View.GONE);
                    company_list.setVisibility(View.VISIBLE);
                    list = (List<Company>) filterResults.values;
                    notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.subs, menu);
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
