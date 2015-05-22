package com.example.michalis.straining;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.michalis.straining.DataModel.DayTraining;
import com.example.michalis.straining.ListAdapter.TrainingListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    ListView listView;
    ProgressBar progressBar;
    TrainingListAdapter listAdapter ;
    ArrayList<DayTraining> trainingList = null;
    Spinner weekSpinner;
    Spinner monthSpinner;
    Spinner yearSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        weekSpinner = (Spinner) findViewById(R.id.week_spinner);
        monthSpinner = (Spinner)findViewById(R.id.month_spinner);
        yearSpinner = (Spinner) findViewById(R.id.year_spinner);
        listView = (ListView) findViewById(R.id.training_list);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        trainingList = new ArrayList<>();
        listAdapter = new TrainingListAdapter(this, R.id.training_list, trainingList);

        listView.setAdapter(listAdapter);
        listView.setVisibility(View.INVISIBLE);
        // Set Listener that start DetailActivity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DayTraining dayTraining = trainingList.get(position);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("dayTraining", dayTraining);
                startActivity(intent);
            }
        });

        int[] callingDate = getCurrentWeekTraining();
        new HttpGetDataTask().execute(callingDate[0], callingDate[1], callingDate[2]);

        weekSpinner.setSelection(callingDate[0] + 1, false);
        monthSpinner.setSelection(callingDate[1], false);
        yearSpinner.setSelection(callingDate[2] - 2013, false);

        weekSpinner.setOnItemSelectedListener(this);
        monthSpinner.setOnItemSelectedListener(this);
        yearSpinner.setOnItemSelectedListener(this);
    }

    /**
     * Returns an array
     * [0] --> current week
     * [1] --> current month
     * [2] --> current year
     * @return [current_week, current_month, current_year]
     */
    private int[] getCurrentWeekTraining(){
        Calendar c = Calendar.getInstance();
        // Find the previous Monday
        while (c.get(Calendar.DAY_OF_WEEK)!= Calendar.MONDAY)
            c.add(Calendar.DATE, -1);
        int previousMonday = c.get(Calendar.DAY_OF_MONTH);
        // Find the first Monday of the month that the previous Monday belong
        c.set(Calendar.DAY_OF_MONTH, 1);
        while (c.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY)
            c.add(Calendar.DATE, +1);
        int week = ((previousMonday - c.get(Calendar.DAY_OF_MONTH)) / 7);
        // send week, month, year
        return new int[]{week, (c.get(Calendar.MONTH) + 1), c.get(Calendar.YEAR)};
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0) {
            return;
        }
        int week = weekSpinner.getSelectedItemPosition();
        int month = monthSpinner.getSelectedItemPosition();
        int year = yearSpinner.getSelectedItemPosition();
        if (week != 0 && month!=0 && year !=0){
            new HttpGetDataTask().execute(week-1, month, year+2013);
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    /**
     * HttpGetTask
     * get Data from a fixed URL with Http GET method
     *
     * Params: week, month, year
     * Result: String (json)
     */
    private class HttpGetDataTask extends AsyncTask<Integer, Void, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(Integer... params) {
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader= null;
            if(params == null){
                return null;
            }
            try {
                // Create URL
                URL url = new URL(constructURL(params[0], params[1], params[2]));
                // Send the request
                httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                // Get the response
                InputStream inputStream = httpURLConnection.getInputStream();
                if (inputStream == null) {
                    // Nothing to do
                    return null;
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                StringBuffer stringBuffer = new StringBuffer();

                String line = null;
                // Because the response ends with comments <!-- Hosting24 Analytics Code -->
                while ( (line = bufferedReader.readLine()) != null && !line.startsWith("<!--")){
                    stringBuffer.append(line);
                }
                if (stringBuffer.length() == 0){
                    // Stream was empty
                    return null;
                }
                // Response in String Format
                return stringBuffer.toString();
            } catch (MalformedURLException e) {
                Log.e(TAG, e.toString());
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }

                if (bufferedReader != null)
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
                    }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            listView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            listAdapter.clear();
            listAdapter.addAll(extractInfoFromJson(s));
            listAdapter.notifyDataSetChanged();
        }

        /**
         * Extract Total + DayTrainings  from JSon
         * @param s
         * @return
         */
        private ArrayList<DayTraining> extractInfoFromJson(String s) {
            ArrayList<DayTraining> trainingsList = new ArrayList<DayTraining>();
            try {
                JSONObject root = new JSONObject(s);
                JSONObject request = new JSONObject(root.optString("request"));
                //Fill the List
                trainingsList.addAll(extractTotalTrainings(request));
                return trainingsList;
            } catch (JSONException e) {
                Log.e(TAG, e.toString());
            }
            return null;
        }

        /**
         * Fill the Final List with Total + DayTrainings
         * @param request
         * @return
         * @throws JSONException
         */
        private ArrayList<DayTraining> extractTotalTrainings (JSONObject request) throws JSONException {
            ArrayList<DayTraining> trainingsList = new ArrayList<DayTraining>();
            trainingsList.add(DayTraining.newInstance(request.optString("total")));
            trainingsList.addAll(extractDayTrainings(new JSONArray(request.optString("trainings"))));
            return trainingsList;
        }

        /**
         * Extracts the List of Day Trainings
         * @param trainings
         * @return
         * @throws JSONException
         */
        private ArrayList<DayTraining> extractDayTrainings(JSONArray trainings) throws JSONException {
            ArrayList<DayTraining> trainingsList = new ArrayList<DayTraining>();
            for(int i=0; i < trainings.length(); i++){
                JSONObject object = trainings.getJSONObject(i);
                trainingsList.add(DayTraining.newInstance(object.toString()));
            }
            return trainingsList;
        }

        /**
         * Constructs the URL based on the user input
         * @param week
         * @param month
         * @param year
         * @return
         */
        private String constructURL(Integer week, Integer month, Integer year) {
            Uri.Builder builder = new Uri.Builder()
                    .scheme("http")
                    .authority("straining.comlu.com")
                    .appendPath("ws")
                    .appendPath("training.php")
                    .appendQueryParameter("week", week.toString())
                    .appendQueryParameter("month",month.toString())
                    .appendQueryParameter("year", year.toString());
            return builder.toString();
        }
    }
}
