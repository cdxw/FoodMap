package com.myfoodmapapp.myfoodmapapp.data.repo;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.myfoodmapapp.myfoodmapapp.data.DatabaseManager;
import com.myfoodmapapp.myfoodmapapp.data.model.Health;

/**
 * Created by chris on 25/8/17.
 */

public class HealthRepo {

    private Health health;

    public HealthRepo(){
        health = new Health();
        //contract = new FoodMapSchemaContract();
    }

    public static String createTable(){
        return "CREATE TABLE " + Health.TABLE  + "("
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Health.Key_UserId + " INTEGER,"
                + Health.Key_Date + " TEXT,"
                + Health.Key_Height + " DOUBLE,"
                + Health.Key_Weight + " DOUBLE,"
                + Health.Key_BMI + " DOUBLE )";
    }

    public int insert(Health health) {

        int healthId;

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        //values.put(Health.Key_HealthId, health.getHealthId());
        values.put(Health.Key_UserId, health.getUserId());
        values.put(Health.Key_Date, health.getDate());
        values.put(Health.Key_Height, health.getHeight());
        values.put(Health.Key_Weight, health.getWeight());
        values.put(Health.Key_BMI, health.getBMI());
        // Insert the new row, returning the primary key value of the new row
        healthId = (int)db.insert(Health.TABLE, null, values);
        DatabaseManager.getInstance().closeDatabase();

        return healthId;
    }



    public void delete( ) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(Health.TABLE,null,null);
        db.execSQL("delete from " + Health.TABLE);
        DatabaseManager.getInstance().closeDatabase();
    }

    public Double getLatestWeight(){
        Double weight = 0.0;


        return weight;
    }
}
