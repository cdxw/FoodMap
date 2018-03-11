package com.myfoodmapapp.myfoodmapapp.data.fragments;


import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import com.jjoe64.graphview.series.PointsGraphSeries;
import com.myfoodmapapp.myfoodmapapp.R;
import com.myfoodmapapp.myfoodmapapp.data.DatabaseManager;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


import static com.myfoodmapapp.myfoodmapapp.data.model.Profile.TAG;

/**
 * Created by chris on 24/8/17.
 */

public class HistoryFragment extends Fragment {

    Button cholesterol_btn, fats_btn, calories_btn;

    GraphView graph;
    Boolean graphPoint = false, graphBar = false, graphLine = false;
    String query = "";
    RadioButton graphPointRadio, graphBarRadio,graphLineRadio;

    public static HistoryFragment newInstance(){
        return new HistoryFragment();
    }

    @Override
    public void onStart(){
        super.onStart();
        query = "CHOLESTEROL";
        graphPointRadio = getView().findViewById(R.id.radio_graphpoint);
        graphPoint = true;
        graphPointRadio.setChecked(true);

        // show the cholesterol
        getNutrionData(query);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_history, container, false);
        // Buttons
        calories_btn = view.findViewById(R.id.button_calories);
        fats_btn = view.findViewById(R.id.button_fats);
        cholesterol_btn = view.findViewById(R.id.button_cholesterol);

        //radio buttons
        graphPointRadio = view.findViewById(R.id.radio_graphpoint);
        graphBarRadio = view.findViewById(R.id.radio_graphbar);
        graphLineRadio = view.findViewById(R.id.radio_graphline);

        // graph
        graph = view.findViewById(R.id.graph);

        // button listeners
        cholesterol_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                query = "CHOLESTEROL";
                getNutrionData(query);
            }
        });

        calories_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                query = "CALORIES";
                getNutrionData(query);
            }
        });

        fats_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                query = "TOTALFATS";
                getNutrionData(query);
            }
        });

        // radio button listeners
        graphLineRadio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                graphLine = true;
                graphBar = false;
                graphPoint = false;
                getNutrionData(query);
            }
        });

        graphBarRadio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                graphBar = true;
                graphLine = false;
                graphPoint = false;
                getNutrionData(query);
            }
        });
        graphPointRadio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                graphPoint = true;
                graphLine = false;
                graphBar = false;
                getNutrionData(query);
            }
        });

        return view;
    }

    private void getNutrionData(String query) {

        graph.removeAllSeries();

        Date tempMinDate = new Date();
        Date tempMaxDate = new Date();
        Date maxDate = new Date();
        Date minDate = new Date();
        Date curseDate = new Date();
        int duration = Toast.LENGTH_SHORT;


        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
        sdf.setTimeZone(TimeZone.getTimeZone("PST"));


        DatabaseManager db = new DatabaseManager();

        String[] columns ={"DATE",query};

        Cursor cursor = db.openDatabase().query("Consumption",columns, null,null,null,null,null);

        if(cursor.getCount() >= 1){
            DataPoint[] dp = new DataPoint[cursor.getCount()];


            for (int i=0; i < cursor.getCount(); i++){

                cursor.moveToNext();
                String cursorString = cursor.getString(0);

                // parse the database string date to date value
                try {
                    curseDate = sdf.parse(cursorString);
                } catch (ParseException e) {
                    Log.d(TAG, "parse curse date: " + e.getMessage());
                }

                // add the database values to the datapoint
                dp[i] = new DataPoint(curseDate,cursor.getInt(1));

                // find the miniumum and max date range


                if (curseDate.before(tempMinDate)) {
                    tempMinDate = curseDate;
                    tempMaxDate = curseDate;

                }else if (curseDate.after(tempMaxDate))
                {
                    tempMaxDate = curseDate;

                }

            }

            // add space between the edges of the graph by changing the range of min and max dates
            long minMillis = tempMinDate.getTime();
            long maxmillis = tempMaxDate.getTime();

            minMillis  = minMillis  + -1 *24*60*60*1000;
            maxmillis = maxmillis + 1 *24*60*60*1000;
            minDate.setTime(minMillis);
            maxDate.setTime(maxmillis);

            cursor.close();
            db.closeDatabase();

            // get the screen orientation and assign maximum horizontal labels
            int screen = getResources().getConfiguration().orientation;

            int maxCount;

            if (screen == 1){
                maxCount = 3;
            }else{
                maxCount = 4;
            }

            if (cursor.getCount() < maxCount){
                maxCount = cursor.getCount();
            }


            try{

                if (graphBar){

                    BarGraphSeries<DataPoint> series = new BarGraphSeries<>(dp);
                    graph.addSeries(series);

                    series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
                        @Override
                        public int get(DataPoint data) {
                            return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
                        }
                    });

                }else if(graphLine){

                    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dp);
                    graph.addSeries(series);

                }else if (graphPoint){

                    PointsGraphSeries<DataPoint> series = new PointsGraphSeries<>(dp);
                    graph.addSeries(series);
                }

                graph.setTitle(query);

                graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getView().getContext()));
                graph.getGridLabelRenderer().setNumHorizontalLabels(maxCount);
//                    // get time returns milliseconds
                graph.getViewport().setMinX(minDate.getTime());
                graph.getViewport().setMaxX(maxDate.getTime());

                graph.getViewport().setXAxisBoundsManual(true);

            }catch(Exception e){
                CharSequence text = "Graph Error";
                Toast toast = Toast.makeText(getView().getContext(), text, duration);
                toast.show();
                Log.d(TAG, "Nutrition Data: " + e.getMessage());
            }
        }else{
            CharSequence text = "No Meal Data yet recorded";
            Toast toast = Toast.makeText(getView().getContext(), text, duration);
            toast.show();
        }
    }


}
