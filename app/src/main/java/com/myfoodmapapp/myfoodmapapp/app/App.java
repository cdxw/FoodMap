package com.myfoodmapapp.myfoodmapapp.app;

import android.app.Application;
import android.content.Context;

import com.myfoodmapapp.myfoodmapapp.data.DBHelper;
import com.myfoodmapapp.myfoodmapapp.data.DatabaseManager;

/**
 * Created by chris on 25/8/17.
 */

public class App extends Application {

        private static Context context;
        private static DBHelper dbHelper;

        @Override
        public void onCreate()
        {
            super.onCreate();
            context = this.getApplicationContext();
            dbHelper = new DBHelper();
            DatabaseManager.initializeInstance(dbHelper);

        }


        public static Context getContext(){
            return context;
        }

}
