package com.myfoodmapapp.myfoodmapapp.data;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.myfoodmapapp.myfoodmapapp.data.model.Health;
import com.myfoodmapapp.myfoodmapapp.data.model.Profile;
import com.myfoodmapapp.myfoodmapapp.data.model.Consumption;
import com.myfoodmapapp.myfoodmapapp.app.App;
import com.myfoodmapapp.myfoodmapapp.data.repo.ConsumptionRepo;
import com.myfoodmapapp.myfoodmapapp.data.repo.HealthRepo;
import com.myfoodmapapp.myfoodmapapp.data.repo.ProfileRepo;

/**
 * Created by chris on 24/8/17.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "FoodMap.db";
    private static final String TAG = DBHelper.class.getSimpleName().toString();

    // get the Context from the App class
    public DBHelper( ) {
        super(App.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create the tables for each table in the Repo folder
        db.execSQL(ProfileRepo.createTable());
        db.execSQL(HealthRepo.createTable());
        db.execSQL(ConsumptionRepo.createTable());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, String.format("SQLiteDatabase.onUpgrade(%d -> %d)", oldVersion, newVersion));

        // Drop table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS " + Consumption.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Health.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Profile.TABLE);

        onCreate(db);
    }


}
