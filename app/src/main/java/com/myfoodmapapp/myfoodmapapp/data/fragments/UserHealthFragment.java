package com.myfoodmapapp.myfoodmapapp.data.fragments;


import android.content.Context;
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

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.myfoodmapapp.myfoodmapapp.R;
import com.myfoodmapapp.myfoodmapapp.data.DatabaseManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.myfoodmapapp.myfoodmapapp.data.model.Profile.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserHealthFragment extends Fragment {
    DateFormat mDateFormat = null;
    Calendar mCalendar = null;
    GraphView graph;
    Boolean graphPoint = false, graphBar = false, graphLine = false;
    RadioButton graphPointRadio2, graphBarRadio2,graphLineRadio2;

    public UserHealthFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart(){
        super.onStart();
        graphPointRadio2 = getView().findViewById(R.id.radio_graphpoint2);
        graphPoint = true;
        graphPointRadio2.setChecked(true);

        getHealthdata();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                    Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_user_health, container, false);

        //radio buttons
        graphPointRadio2 = view.findViewById(R.id.radio_graphpoint2);
        graphBarRadio2 = view.findViewById(R.id.radio_graphbar2);
        graphLineRadio2 = view.findViewById(R.id.radio_graphline2);

        graph = view.findViewById(R.id.graphView);

        // radio button listeners
        graphLineRadio2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                graphLine = true;
                graphBar = false;
                graphPoint = false;
                getHealthdata();
            }
        });

        graphBarRadio2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                graphBar = true;
                graphLine = false;
                graphPoint = false;
                getHealthdata();
               // updateBarGraph();

            }
        });
        graphPointRadio2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                graphPoint = true;
                graphLine = false;
                graphBar = false;
                getHealthdata();
            }
        });
        return view;
    }
    private void getHealthdata(){

        graph.removeAllSeries();

        Date maxDate = new Date();
        Date minDate = new Date();
        Date tempMinDate = new Date();
        Date tempMaxDate = new Date();

        Date curseDate = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
        sdf.setTimeZone(TimeZone.getTimeZone("PST"));

        DatabaseManager db = new DatabaseManager();

        String[] columns ={"DATE","BMI"};

        Cursor cursor = db.openDatabase().query("Health",columns, null,null,null,null,null);

        DataPoint[] dp = new DataPoint[cursor.getCount()];


        for (int i=0; i < cursor.getCount(); i++){

            cursor.moveToNext();
            String cursorString = cursor.getString(0);

            // parse the database string date to date value
            try {
                curseDate = sdf.parse(cursorString);

            } catch (ParseException e) {
                Log.d(TAG, "parse date error: " + e.getMessage());
            }

            // add the database values to the datapoint
            dp[i] = new DataPoint(curseDate,cursor.getInt(1));

            if (curseDate.before(minDate)) {
                minDate = curseDate;
                maxDate = curseDate;

            }else if (curseDate.after(maxDate))
            {
                maxDate = curseDate;

            }
        }

        // add space between the edges of the graph by changing the range of min and max dates
        long minMillis = tempMinDate.getTime();
        long maxmillis = tempMaxDate.getTime();

        minMillis  = minMillis  + -1 *24*60*60*1000;
        maxmillis = maxmillis + 1 *24*60*60*1000;
        minDate.setTime(minMillis);
        maxDate.setTime(maxmillis);

        // close the cursor and database instances
        cursor.close();
        db.closeDatabase();

        // initialise a maximum label count depending on the orientation of the screen
        int screen = getResources().getConfiguration().orientation;

        int maxCount;

        if (screen == 1){
            maxCount = 4;
        }else{
            maxCount = 5;
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

            graph.setTitle("BMI History");
//
            graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getView().getContext()));
            graph.getGridLabelRenderer().setNumHorizontalLabels(maxCount);
//          // get time returns milliseconds
            graph.getViewport().setMinX(minDate.getTime());
            graph.getViewport().setMaxX(maxDate.getTime());

            graph.getViewport().setXAxisBoundsManual(true);


        }catch(Exception e){
            Log.d(TAG, "Error in getHealthData: " + e.getMessage());
        }
    }


}

