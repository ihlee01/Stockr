package com.boilermake.stockr;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;


public class SubsActivity extends Activity {

    Button instant_button;
    Button spatial_button;
    Button time_button;
    Button greater_button;
    Button lesser_button;
    Button company_button;
    EditText input_value;

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
        company_button = (Button) findViewById(R.id.company_button);
        input_value = (EditText) findViewById(R.id.input_value);

        //Event Listener

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
            }
        });
        lesser_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lesser_button.setBackground(getResources().getDrawable(R.drawable.blue_shape));
                lesser_button.setTextColor(getResources().getColor(R.color.light_grey));

                greater_button.setBackgroundColor(Color.WHITE);
                greater_button.setTextColor(getResources().getColor(R.color.heavy_grey));

            }
        });
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
