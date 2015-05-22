package com.example.michalis.straining;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.michalis.straining.DataModel.DayTraining;

/**
 * Created by Michalis on 5/12/2015.
 *
 * Display the Detail Activity
 */
public class DetailActivity extends ActionBarActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        DayTraining dayTraining = (DayTraining) intent.getSerializableExtra("dayTraining");

        Log.i(TAG, dayTraining.toString());

        // Initialize all UI Components
        TextView txtDescription = (TextView) findViewById(R.id.txt_desciption);
        TextView txtDay = (TextView) findViewById(R.id.training_number);
        TextView txtTotalKm= (TextView) findViewById(R.id.total_km);
        TextView txtTotalTime= (TextView) findViewById(R.id.total_time);
        TextView txtT1 = (TextView) findViewById(R.id.txt_t1);
        TextView txtT2 = (TextView) findViewById(R.id.txt_t2);
        TextView txtT3 = (TextView) findViewById(R.id.txt_t3);
        TextView txtT1_3 = (TextView) findViewById(R.id.txt_t1_3);
        TextView txtT2_3 = (TextView) findViewById(R.id.txt_t2_3);
        TextView txtTT = (TextView) findViewById(R.id.txt_tt);
        TextView txtTA = (TextView) findViewById(R.id.txt_ta);
        TextView txtGX = (TextView) findViewById(R.id.txt_gx);
        TextView txtGP = (TextView) findViewById(R.id.txt_gp);
        TextView txtGd = (TextView) findViewById(R.id.txt_gd);
        TextView txtTotal = (TextView) findViewById(R.id.txt_total);
        TextView txtTexniki = (TextView) findViewById(R.id.txt_texniki);
        TextView txtDrastiriotita = (TextView) findViewById(R.id.txt_drastiriotita);

        // Put values
        txtDescription.setText(dayTraining.getDescription());
        if (dayTraining.getDescription().equals(DayTraining.total)){
            txtDay.setText(DayTraining.total);
        }else {
            txtDay.setText(getResources().getStringArray(R.array.days)[(dayTraining.getDay() - 1)] + "  " + dayTraining.getDate());
        }
        txtTotalKm.setText(String.valueOf(dayTraining.getTotalKm()));
        txtTotalTime.setText(String.valueOf(dayTraining.getTime()));
        txtT1.setText(String.valueOf(dayTraining.getT1()));
        txtT2.setText(String.valueOf(dayTraining.getT2()));
        txtT3.setText(String.valueOf(dayTraining.getT3()));
        txtT1_3.setText(String.valueOf(dayTraining.getT1_3()));
        txtT2_3.setText(String.valueOf(dayTraining.getT2_3()));
        txtTT.setText(String.valueOf(dayTraining.getTt()));
        txtTA.setText(String.valueOf(dayTraining.getTa()));
        txtGX.setText(String.valueOf(dayTraining.getGx()));
        txtGP.setText(String.valueOf(dayTraining.getGp()));
        txtGd.setText(String.valueOf(dayTraining.getGd()));
        txtTotal.setText(String.valueOf(dayTraining.getNumber()));
        txtTexniki.setText(String.valueOf(dayTraining.getTexniki()));
        txtDrastiriotita.setText(String.valueOf(dayTraining.getDrastiriotita()));
    }
}
