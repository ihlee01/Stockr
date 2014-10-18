package com.boilermake.stockr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;


public class SubsActivity extends Activity {

    Button instant_button;
    Button spatial_button;
    Button time_button;
    Button greater_button;
    Button lesser_button;
    Button subs_butotn;
    EditText input_value;
    ListView company_list;
    ArrayAdapter<Company> adapter;
    ArrayList<Company> companies;

    LinearLayout company_layout;


    int type = 0;
    int association = 0;
    String company_id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subs);
        Animation slideup = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideup);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.subs_layout);

        layout.startAnimation(slideup);
        layout.setVisibility(View.VISIBLE);

        //Components initializing
        instant_button = (Button) findViewById(R.id.instant_button);
        spatial_button = (Button) findViewById(R.id.spatial_button);
        time_button = (Button) findViewById(R.id.time_button);
        greater_button = (Button) findViewById(R.id.greater_button);
        lesser_button = (Button) findViewById(R.id.lesser_button);
        input_value = (EditText) findViewById(R.id.input_value);
        subs_butotn = (Button) findViewById(R.id.subs_button);
        company_list = (ListView) findViewById(R.id.company_list);
        company_layout = (LinearLayout) findViewById(R.id.company_layout);

        //Populate companies
        company_layout.setVisibility(View.GONE);
        companies = new ArrayList<Company>();
        if(companies.size() == 0) {
            populateCompany();
        }

        //Event Listener
        input_value.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                input_value.setHint("");
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

                company_layout.setVisibility(View.VISIBLE);
                generateList(company_list, R.layout.company_item_radio);
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

                company_layout.setVisibility(View.VISIBLE);
                generateList(company_list, R.layout.company_item_radio);

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

                company_layout.setVisibility(View.VISIBLE);
                generateList(company_list, R.layout.company_item_radio);

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
        subs_butotn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Send data to server here...


            }
        });
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
        setListViewHeightBasedOnChildren(view);
        view.setAdapter(swing);
    }
    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, WindowManager.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }





    private class MyListAdapter extends ArrayAdapter<Company> implements Filterable {
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
        public int getCount() { return list.size(); }

        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            if(rowView == null) {
                LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(resource, parent, false);
            }
            final Company curCompany = list.get(position);
            String name = curCompany.getName();
            ImageView company_image = (ImageView)rowView.findViewById(R.id.company_image);
            TextView company_name = (TextView)rowView.findViewById(R.id.company_name);
            final RadioButton company_radio = (RadioButton)rowView.findViewById(R.id.company_radio);

           /* //Set company image
            Resources res = getResources();
            String image_name = curCompany.getName().toLowerCase();
            int resID = res.getIdentifier(image_name, "drawable", getPackageName());
            Drawable drawable = res.getDrawable(resID);
            company_image.setImageDrawable(drawable);
*/
            company_name.setText(name);

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //check the radio button
                    company_radio.setChecked(true);
                    company_id = curCompany.getId();
                }
            });

            return rowView;
        }
        private class CompanyFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                charSequence = charSequence.toString().toLowerCase();
                if(charSequence == null || charSequence.length() == 0) {
                    results.values = original_list;
                    results.count = original_list.size();
                }
                else {
                    List<Company> result = new ArrayList<Company>();
                    for(Company company : original_list) {
                        if(company.getName().toLowerCase().contains(charSequence)) {
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
                if(filterResults.count == 0) {
                    list = original_list;
                    notifyDataSetInvalidated();
                    //defaultView.setVisibility(View.VISIBLE);
                    company_list.setVisibility(View.GONE);
                }
                else {
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
