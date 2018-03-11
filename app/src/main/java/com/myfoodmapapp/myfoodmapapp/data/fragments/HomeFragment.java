package com.myfoodmapapp.myfoodmapapp.data.fragments;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.aware.PublishConfig;
import android.os.Bundle;

import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.myfoodmapapp.myfoodmapapp.R;
import com.myfoodmapapp.myfoodmapapp.data.DatabaseManager;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
//1
public class HomeFragment extends Fragment {

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
    Button addMealBtn,healthHistoryBtn,profileDetailsBtn,nutHistoryBtn;
    ImageView imageView;
    String path = "";

    @Override
   public void onStart(){
        super.onStart();

        imageView = getView().findViewById(R.id.imageView);
        DatabaseManager db = new DatabaseManager();
        // Check if the user has a profile
        String selectProfileQuery = "SELECT  * FROM " + "profile";

        int count = db.countTableRecords("profile");
        if(count == 0){
            // redirect to profile page
            getFragmentManager().beginTransaction().replace(R.id.root_layout, new UserProfileFragment()).addToBackStack(null).commit();
        }else{

            Cursor cursor = null;

            try{
                cursor = db.openDatabase().rawQuery(selectProfileQuery, null);

                // Retrieve the Data from the Profile table

                if (cursor.moveToFirst()) {

                    path = cursor.getString(cursor.getColumnIndex("ImagePath"));
                }
            } finally {

                cursor.close();
            }

            File imgFile = new File(String.valueOf(path));

            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(String.valueOf(path));
                imageView.setImageBitmap(myBitmap);
            }else{
                imageView.setImageResource(R.drawable.user_profile);
            }
        }

        db.closeDatabase();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_home, container, false);

        addMealBtn = view.findViewById(R.id.button_addMeal);
        healthHistoryBtn = view.findViewById(R.id.button_healthHistory);
        profileDetailsBtn = view.findViewById(R.id.button_profile);
        nutHistoryBtn = view.findViewById(R.id.button_nutHistory);

        addMealBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.root_layout, new AddMealFragment()).addToBackStack(null).commit();
            }
        });

        healthHistoryBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.root_layout, new UserHealthFragment()).addToBackStack(null).commit();
            }
        });

        profileDetailsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.root_layout, new UserProfileFragment()).addToBackStack(null).commit();
            }
        });

        nutHistoryBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.root_layout, new HistoryFragment()).addToBackStack(null).commit();
            }
        });

        return view;
    }
}
