package com.myfoodmapapp.myfoodmapapp.data.repo;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.myfoodmapapp.myfoodmapapp.data.DatabaseManager;

import com.myfoodmapapp.myfoodmapapp.data.model.Consumption;

import java.util.Date;

import static com.myfoodmapapp.myfoodmapapp.data.model.Profile.TAG;


/**
 * Created by chris on 25/8/17.
 */

public class ConsumptionRepo {

    private Consumption consumption;

    public ConsumptionRepo(){
        consumption = new Consumption();
    }

    public static String createTable(){
        return "CREATE TABLE " + Consumption.TABLE  + "("
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Consumption.Key_Date + " TIMESTAMP NOT NULL DEFAULT DATE,"
                + Consumption.Key_Cholesterol + " DOUBLE,"
                + Consumption.Key_Calories + " DOUBLE,"
                + Consumption.Key_TotalFats + " DOUBLE, "
                + Consumption.Key_FoodName + " TEXT )";
    }

    public int insert(Consumption consumption) {

        int mealId;

        // Gets the data repository in write mode
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(Consumption.Key_Date, consumption.getDate());
        values.put(Consumption.Key_Cholesterol, consumption.getCholesterol());
        values.put(Consumption.Key_Calories, consumption.getCalories());
        values.put(Consumption.Key_TotalFats, consumption.getTotalFats());

        // Insert the new row, returning the primary key value of the new row
        mealId = (int)db.insert(Consumption.TABLE, null, values);
        DatabaseManager.getInstance().closeDatabase();

        return mealId;
    }



    public void delete( ) {
        try{
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.delete(Consumption.TABLE,null,null);
            DatabaseManager.getInstance().closeDatabase();
            Log.d(TAG, "delete: consumption Successful");
        }catch (Exception e){
            Log.d(TAG, "delete: unSuccessful");
        }

    }


}
