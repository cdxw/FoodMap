package com.myfoodmapapp.myfoodmapapp.data.fragments;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;

import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.myfoodmapapp.myfoodmapapp.R;
import com.myfoodmapapp.myfoodmapapp.data.model.Foods;
import com.myfoodmapapp.myfoodmapapp.data.model.MapData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddMealFragment extends Fragment {

    public static Context ctx;
    int duration = Toast.LENGTH_SHORT;
    private DatePickerDialog datePickerDialog;
    TextView dateTxt, load_txt;
    private String dateOfMeal = "";
    private ProgressBar mProgress;

    final String APP_ID = "";
    final String APP_KEY = "";

    Calendar myCalendar = Calendar.getInstance();

    Calendar dateSelected = Calendar.getInstance();

    public static AddMealFragment newInstance() {
        return  new AddMealFragment();
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_meal, container, false);

        mProgress = view.findViewById(R.id.mprogressbar);

//        firstBar = view.findViewById(R.id.firstBar);
//        secondBar = view.findViewById(R.id.secondBar);

        ctx = view.getContext();
        Button getData_btn;
        ImageButton addDate_btn;

        getData_btn = view.findViewById(R.id.button_getData);
        addDate_btn = view.findViewById(R.id.button_addDate);
        load_txt = view.findViewById(R.id.load_txt);

        dateTxt = view.findViewById(R.id.date_txt);

        dateTxt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        addDate_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    showDatePickerDialog();
            }
        });

        getData_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                TextView foodTxt = view.findViewById(R.id.foodName_txt);
                String query = foodTxt.getText().toString();

                if (!foodTxt.getText().toString().matches("")){

                    if(!dateTxt.getText().toString().matches("")){
                        new HttpRequestTask().execute(query);

                    }else{
                        CharSequence text = "Error. Please enter a date";
                        Toast toast = Toast.makeText(view.getContext(), text, duration);
                        toast.show();
                        return;
                    }

                }else
                {
                    CharSequence text = "Error. Please enter a query";
                    Toast toast = Toast.makeText(view.getContext(), text, duration);
                    toast.show();
                    return;
                }

            }
        });


        return view;
    }



    public void showDatePickerDialog(){

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {


        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            dateOfMeal = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
            dateTxt.setText(dateOfMeal);

        }

    };


    private class HttpRequestTask extends AsyncTask<String, Void, MapData> {
        @Override
        protected MapData doInBackground(String... strings) {



            String inputLine = "UNDEFINED";
            MapData apiData = new MapData();
            Foods foods = new Foods();
            int min = 1;
            int max = 5;

            String foodName = "UNDEFINED";

            String calories = "UNDEFINED";
            Double caloriesDouble = 0.0;
            Double totalCalories = 0.0;

            String totalFat = "UNDEFINED";
            Double fatsDouble = 0.0;
            Double totalFatDouble = 0.0;

            String satFat = "UNDEFINED";
            Double satDouble = 0.0;
            Double totalSatDouble = 0.0;

            String cholestrol = "UNDEFINED";
            Double cholDouble = 0.0;
            Double totalCholDouble = 0.0;

            String sodium = "UNDEFINED";
            Double sodiumDouble = 0.0;
            Double totalSodiumDouble = 0.0;

            String totalCarbs = "UNDEFINED";
            Double carbsDouble = 0.0;
            Double totalCarbDouble = 0.0;

            String dietFiber = "UNDEFINED";
            Double fiberDouble = 0.0;
            Double totalFiberDouble = 0.0;

            String sugars = "UNDEFINED";
            Double sugarsDouble = 0.0;
            Double totalSugarsDouble = 0.0;

            String protein = "UNDEFINED";
            Double proteinDouble = 0.0;
            Double totalProteinDouble = 0.0;

            String query = strings[0];

            if (isNetworkConnected()) {


                try {

                    final String rawurl = String.format("https://trackapi.nutritionix.com/v2/natural/nutrients/");

                    URL url = new URL(rawurl);

                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    urlConnection .setRequestMethod("POST");
                    urlConnection .setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    urlConnection .setRequestProperty("Content-Language", "en-US");

                    urlConnection .setRequestProperty("x-app-id", APP_ID);
                    urlConnection .setRequestProperty("x-app-key", APP_KEY);

                    urlConnection .setRequestProperty("x-remote-user-id", "0");

                    urlConnection .setDoOutput(true);

                    String urlParameters = "query="+ query;

                    DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                    wr.writeBytes(urlParameters);
                    wr.flush();
                    wr.close();

                    //  print out details
                    int responseCode = urlConnection.getResponseCode();
                    System.out.println("\nSending 'POST' request to URL : " + url);
                    System.out.println("Post parameters : " + urlParameters);
                    System.out.println("Response Code : " + responseCode);

                    BufferedReader in = new BufferedReader( new InputStreamReader(urlConnection.getInputStream()));

                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    try {
                        JSONObject topLevel = new JSONObject(response.toString());

                        JSONArray array = topLevel.getJSONArray("foods");

                        // iterate over the jason response string
                        // add the values for a total count of all returned nutrient counts
                        for(int i = 0 ; i < array.length() ; i++){

                            foodName = array.getJSONObject(i).getString("food_name");

                            calories = array.getJSONObject(i).getString("nf_calories");
                            caloriesDouble = castStringValues(calories);
                            totalCalories += caloriesDouble;
                            calories = castDoubleValues(totalCalories);

                            totalFat = array.getJSONObject(i).getString("nf_total_fat");
                            fatsDouble = castStringValues(totalFat);
                            totalFatDouble += fatsDouble;
                            totalFat = castDoubleValues(totalFatDouble);

                            satFat = array.getJSONObject(i).getString("nf_saturated_fat");
                            satDouble = castStringValues(satFat);
                            totalSatDouble += satDouble;
                            satFat = castDoubleValues(totalSatDouble);


                            cholestrol = array.getJSONObject(i).getString("nf_cholesterol");
                            cholDouble = castStringValues(cholestrol);
                            totalCholDouble += cholDouble;
                            cholestrol = castDoubleValues(totalCholDouble);

                            sodium = array.getJSONObject(i).getString("nf_sodium");
                            sodiumDouble = castStringValues(sodium);
                            totalSodiumDouble += sodiumDouble;
                            sodium = castDoubleValues(totalSodiumDouble);

                            totalCarbs = array.getJSONObject(i).getString("nf_total_carbohydrate");
                            carbsDouble = castStringValues(totalCarbs);
                            totalCarbDouble += carbsDouble;
                            totalCarbs = castDoubleValues(totalCarbDouble);


                            dietFiber = array.getJSONObject(i).getString("nf_dietary_fiber");
                            fiberDouble = castStringValues(dietFiber);
                            totalFiberDouble += fiberDouble;
                            dietFiber = castDoubleValues(totalFiberDouble);

                            sugars = array.getJSONObject(i).getString("nf_sugars");
                            sugarsDouble = castStringValues(sugars);
                            totalSugarsDouble += sugarsDouble;
                            sugars = castDoubleValues(totalSugarsDouble);

                            protein = array.getJSONObject(i).getString("nf_protein");
                            proteinDouble = castStringValues(protein);
                            totalProteinDouble += proteinDouble;
                            protein = castDoubleValues(totalProteinDouble);

                        }

                        // add the total values to the food object class
                        foods.setQuery(query);
                        foods.setFood_name(foodName);
                        foods.setNf_calories(calories);
                        foods.setNf_total_fat(totalFat);
                        foods.setNf_saturated_fat(satFat);
                        foods.setNf_cholesterol(cholestrol);
                        foods.setNf_sodium(sodium );
                        foods.setNf_total_carbohydrate(totalCarbs);
                        foods.setNf_dietary_fiber(dietFiber);
                        foods.setNf_sugars(sugars);
                        foods.setNf_protein(protein);

                    } catch (JSONException e){
                        e.printStackTrace();
                    }

                    // illustrate progress bar
                    // TODO: 8/10/17 Remove after presentation
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // set the class data with a date and the food object
                    apiData.setDate(dateOfMeal);
                    apiData.setFoods(foods);

                    urlConnection.disconnect();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // return the results to the post execute method
            return apiData;


        }
        @Override
        protected void onPreExecute(){
            // show loading text and progres bar
            load_txt.setText("LOADING");
            mProgress.setVisibility(getView().VISIBLE);
        }
        @Override
        protected void onPostExecute(MapData mapdata) {

            // check if the mapdata is not null before proceeding
            if(mapdata.getFoods() != null){

                Bundle bundle = new Bundle();
                bundle.putSerializable("mapdata", mapdata);

                // add the bundle to the results fragment
                ResultsFragment resultsFrag = new ResultsFragment();
                resultsFrag.setArguments(bundle);

                // Redirect to fragment with bundle of mapdata
                getFragmentManager().beginTransaction().replace(R.id.root_layout, resultsFrag).addToBackStack(null).commit();
            }else{
                new AlertDialog.Builder(ctx)
                        .setTitle("No Internet Connection")
                        .setMessage("It looks like your internet connection is off. Please turn it " +
                                "on and try again")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).setIcon(android.R.drawable.ic_dialog_alert).show();
            }
            // remove loading text and progress bar
            load_txt.setText("");
            mProgress.invalidate();
        }
    }
    // Function to cast a string to double
    private Double castStringValues(String value){

        Double castValue = 0.0;

        try{
            castValue = Double.parseDouble(value);

        }catch (NumberFormatException e) {
            // handle error
        }

        return castValue;
    }
    // Function to cast a doublke to string
    private String castDoubleValues(Double value){

        String total="UNDEFINED";

        try{
            total= String.valueOf(value);

        }catch (NumberFormatException e) {
            // handle error
        }
        return total;
    }
// check network connected
    private boolean isNetworkConnected() {

        ConnectivityManager connMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }


}



