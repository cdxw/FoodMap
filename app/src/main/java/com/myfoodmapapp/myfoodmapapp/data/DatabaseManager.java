package com.myfoodmapapp.myfoodmapapp.data;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.myfoodmapapp.myfoodmapapp.R;
import com.myfoodmapapp.myfoodmapapp.data.fragments.UserProfileFragment;
import com.myfoodmapapp.myfoodmapapp.data.model.Profile;
import com.myfoodmapapp.myfoodmapapp.data.repo.ProfileRepo;

import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * Created by chris on 25/8/17.
 */

public class DatabaseManager {
    private Integer mOpenCounter = 0;

    private static DatabaseManager instance;
    private static SQLiteOpenHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;

    public static synchronized void initializeInstance(SQLiteOpenHelper helper) {
        if (instance == null) {
            instance = new DatabaseManager();
            mDatabaseHelper = helper;
        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(DatabaseManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }

        return instance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        mOpenCounter +=1;
        if(mOpenCounter == 1) {
            // Opening new database
            mDatabase = mDatabaseHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        mOpenCounter-=1;
        if(mOpenCounter == 0) {
            // Closing database
            mDatabase.close();

        }
    }
    public int countTableRecords( String table){

        //DatabaseManager db = new DatabaseManager();

        String countQuery = "SELECT  * FROM " + table;
        Cursor cursor = this.openDatabase().rawQuery(countQuery, null);
        int cnt = cursor.getCount();

        cursor.close();
        this.closeDatabase();

        return  cnt;
    }
    public String getImagePath(){


        String countQuery = "SELECT  * FROM " + "Profile";
        Cursor cursor = null;

        String path = "";

        try{
            cursor = this.openDatabase().rawQuery(countQuery, null);
            int cnt = cursor.getCount();
            // Retrieve the Data from the Profile table

            if (cursor.moveToFirst()) {

                path = cursor.getString(cursor.getColumnIndex("ImagePath"));
            }
        } finally {

            cursor.close();
            this.closeDatabase();
        }

        return path;
    }
    public static double roundResults(double value) {
        if(value > 0){
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            return bd.doubleValue();
        }else{
            return 0.00;
        }

    }
}
