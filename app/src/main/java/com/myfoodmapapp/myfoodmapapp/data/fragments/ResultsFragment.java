package com.myfoodmapapp.myfoodmapapp.data.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.myfoodmapapp.myfoodmapapp.R;
import com.myfoodmapapp.myfoodmapapp.data.DatabaseManager;
import com.myfoodmapapp.myfoodmapapp.data.model.Consumption;
import com.myfoodmapapp.myfoodmapapp.data.model.MapData;
import com.myfoodmapapp.myfoodmapapp.data.repo.ConsumptionRepo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.myfoodmapapp.myfoodmapapp.data.model.Profile.TAG;

/**
 * Created by chris on 24/8/17.
 */

public class ResultsFragment extends Fragment {

    private ProgressDialog mProgressDialog;
    Button historyBtn;
    Context ctx;

    public static ResultsFragment newInstance(){
        return  new ResultsFragment();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_results, container, false);

        // retrieve the (MapData class) data sent from the add meal fragment
        Bundle bundle = getArguments();
        final MapData mapData = (MapData) bundle.getSerializable("mapdata");


        ctx = view.getContext();
        historyBtn = view.findViewById(R.id.history_btn);

        if(mapData != null){
            populateDataFields(view, mapData);
        }

        historyBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // save mapdata to database
                // save data then navigate to the history screen
                if (saveData(mapData)){

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("mapdata", mapData);

                    // add the bundle to the results fragment
                    HistoryFragment historyFrag = new HistoryFragment();
                    historyFrag.setArguments(bundle);

                    // Redirect to history fragment with bundle of mapdata
                    getFragmentManager().beginTransaction().replace(R.id.root_layout, historyFrag).addToBackStack(null).commit();
                }
            }
        });


        return view;
    }

    public void populateDataFields(View view, MapData mapdata){

        TextView queryTxt, caloriesTxt, totalFatTxt, satFatTxt, cholestrolTxt, sodiumTxt, totcarbsTxt, dietFiberTxt, sugarsTxt, proteinTxt;

        queryTxt = view.findViewById(R.id.foodName_txt);
        caloriesTxt = view.findViewById(R.id.calories_txt);
        totalFatTxt = view.findViewById(R.id.totalFat_txt);
        satFatTxt = view.findViewById(R.id.satFat_txt);
        cholestrolTxt = view.findViewById(R.id.cholestrol_txt);
        sodiumTxt = view.findViewById(R.id.sodium_txt);
        totcarbsTxt = view.findViewById(R.id.totalCarbs_txt);
        dietFiberTxt = view.findViewById(R.id.dietFiber_txt);
        sugarsTxt = view.findViewById(R.id.sugars_txt);
        proteinTxt = view.findViewById(R.id.protein_txt);

        // set the control fields with the bundle data sent from the add meal fragment
        queryTxt.setText(mapdata.getFoods().getQuery() );

        caloriesTxt.setText(mapdata.getFoods().getNf_calories().toString());
        totalFatTxt.setText(mapdata.getFoods().getNf_total_fat().toString());
        satFatTxt.setText(mapdata.getFoods().getNf_saturated_fat().toString());
        cholestrolTxt.setText(mapdata.getFoods().getNf_cholesterol().toString());
        sodiumTxt.setText(mapdata.getFoods().getNf_sodium().toString());
        totcarbsTxt.setText(mapdata.getFoods().getNf_total_carbohydrate().toString());
        dietFiberTxt.setText(mapdata.getFoods().getNf_dietary_fiber().toString());
        sugarsTxt.setText(mapdata.getFoods().getNf_sugars().toString());
        proteinTxt.setText(mapdata.getFoods().getNf_protein().toString());
    }

    public boolean saveData(MapData mapdata){

        boolean completed = false;
        Double calories, cholesterol, totalFats;
        String queryName;

        try{
            DatabaseManager db = new DatabaseManager();

            Consumption consumption = new Consumption();
            ConsumptionRepo consrepo = new ConsumptionRepo();

            //get the data to save to the database
            calories = db.roundResults(Double.parseDouble(mapdata.getFoods().getNf_calories()));
            cholesterol = db.roundResults(Double.parseDouble(mapdata.getFoods().getNf_cholesterol()));
            totalFats = db.roundResults(Double.parseDouble(mapdata.getFoods().getNf_saturated_fat()));
            queryName = mapdata.getFoods().getQuery();
            String date = mapdata.getDate();

            consumption.setDate(date);
            consumption.setCholesterol(cholesterol);
            consumption.setCalories(calories);
            consumption.setTotalFats(totalFats);
            consumption.setQueryName(queryName);

            int id = consrepo.insert(consumption);

            if ( id > 0){
                completed = true;

                CharSequence text = "Data Saved";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(ctx, text, duration);
                toast.show();
            }



        }catch(Exception e){
            Log.d(TAG, e.toString());

            CharSequence text = "Error. Data not saved";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(ctx, text, duration);
            toast.show();
        }

        return  completed;

    }

}
