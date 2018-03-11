package com.myfoodmapapp.myfoodmapapp;


import android.app.Fragment;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.os.Bundle;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.myfoodmapapp.myfoodmapapp.data.DatabaseManager;
import com.myfoodmapapp.myfoodmapapp.data.fragments.AddMealFragment;
import com.myfoodmapapp.myfoodmapapp.data.fragments.HistoryFragment;
import com.myfoodmapapp.myfoodmapapp.data.fragments.HomeFragment;
import com.myfoodmapapp.myfoodmapapp.data.fragments.UserHealthFragment;
import com.myfoodmapapp.myfoodmapapp.data.fragments.UserProfileFragment;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {

    String frag = "";
    int duration = Toast.LENGTH_SHORT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final HomeFragment home = new HomeFragment();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle abDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.closed);
        drawerLayout.setDrawerListener(abDrawerToggle);
        abDrawerToggle.syncState();

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);


        updateProfileImage();

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawers();
                int id = menuItem.getItemId();

                if (id == R.id.home) {

                    getFragmentManager().beginTransaction().replace(R.id.root_layout, new HomeFragment()).addToBackStack(null).commit();
//
                    return true;
                } else if (id == R.id.profile) {

                    getFragmentManager().beginTransaction().replace(R.id.root_layout, new UserProfileFragment()).addToBackStack(null).commit();
                    return true;
                } else if (id == R.id.nutrition) {
//
                    getFragmentManager().beginTransaction().replace(R.id.root_layout, new HistoryFragment()).addToBackStack(null).commit();
                    return true;

                }else if(id==R.id.health) {
                    getFragmentManager().beginTransaction().replace(R.id.root_layout, new UserHealthFragment()).addToBackStack(null).commit();
                    return true;
                }else if(id==R.id.addMeal) {
                    getFragmentManager().beginTransaction().replace(R.id.root_layout, new AddMealFragment()).addToBackStack(null).commit();
                    return true;
                }
                else {
                    return false;
                }
            }
        });

        getFragmentManager().beginTransaction().replace(R.id.root_layout, home).commit();


    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        frag = savedInstanceState.getString("TAG");
        Log.d("TAG", "onRestoreInstanceState: " + frag);

    }

        @Override
    protected void onStart() {
        super.onStart();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the main_menu; this adds items to the action bar if it is present.
       getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Navigate to the add meal fragment on click of plus icon
        switch (item.getItemId()) {
            case R.id.addMeal:
                getFragmentManager().beginTransaction().replace(R.id.root_layout, new AddMealFragment()).addToBackStack(null).commit();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }


    // Update the profile image from the database in the Nav drawer
    public void updateProfileImage(){


        DatabaseManager db = new DatabaseManager();

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = navView.getHeaderView(0);
        final CircleImageView imageView = headerView.findViewById(R.id.img_profile);
//
        String path = db.getImagePath();
        File imgFile = new File(String.valueOf(path));

        // Check an image has been found - create a bitmap - add to view
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(String.valueOf(path));

            imageView.setImageBitmap(myBitmap);
        }

        db.closeDatabase();
    }


}
