package com.myfoodmapapp.myfoodmapapp.data.repo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.widget.Toast;

import com.myfoodmapapp.myfoodmapapp.data.DatabaseManager;
import com.myfoodmapapp.myfoodmapapp.data.model.Profile;

import static android.R.attr.id;

/**
 * Created by chris on 25/8/17.
 */

public class ProfileRepo {

    private Profile profile;
    //private FoodMapSchemaContract contract;


    public ProfileRepo(){
        profile = new Profile();
       //contract = new FoodMapSchemaContract();
    }


    public static String createTable(){
        return "CREATE TABLE " + Profile.TABLE + " ("
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Profile.Key_Name + " TEXT NOT NULL, "
                + Profile.Key_ImagePath + " TEXT )";

    }

    public int insert(Profile profile) {
        int userId = 0;


        try{

            // open the database connection
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            ContentValues values = new ContentValues();
            values.put(Profile.Key_Name, profile.getName());
            values.put(Profile.Key_ImagePath, profile.getImagePath());

            // Insert the new row, returning the primary key value of the new row
            userId = (int)db.insert(Profile.TABLE, null, values);

            DatabaseManager.getInstance().closeDatabase();

            Log.d("insert_COMPLETE", String.valueOf(userId));
        }catch (Exception e){
            Log.d("Insert_Error", e.toString());
        }

        return userId;
    }

    public boolean update(Profile profile, String previous){

        boolean success = false;

        try{

            // open the database connection

            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            ContentValues values = new ContentValues();
            values.put(Profile.Key_Name, profile.getName());
            values.put(Profile.Key_ImagePath, profile.getImagePath());

            String where="NAME = ?";
            // Insert the new row, returning the primary key value of the new row
            db.update(Profile.TABLE, values, where, new String[]{previous});
            DatabaseManager.getInstance().closeDatabase();

            success = true;

        }catch (Exception e){
            Log.d("Update_Error", e.toString());
        }


        return success;
    }

    public void delete( ) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(Profile.TABLE,null,null);
        //db.execSQL("delete from "+ Profile.TABLE);
        DatabaseManager.getInstance().closeDatabase();


    }


}
