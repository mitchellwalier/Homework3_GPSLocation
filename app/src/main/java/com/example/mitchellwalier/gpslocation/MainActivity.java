package com.example.mitchellwalier.gpslocation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public Boolean offline;
    public static final String MY_PREFS_NAME = "MyPrefsFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        offline = loadSharedPreferences(this);


        /*THE SERVICE IS NOT LOGGING! */
        /* ALSO NEED TO SET UP A BROADCAST LISTENER TO GET THE LOCATION AFTER WE FIND IT
         * NEED TO SET UP SQL DATABASE TOO
         * CREATE CUSTOM PARITOR TO SORT LOCATION BY DATE */
        Intent intent = new Intent(this, GetLocation.class);
        startService(intent);


        ListView serverListView = (ListView) findViewById(R.id.serverListView);


        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            Toolbar toolbar = (Toolbar) findViewById(R.id.customToolbar);
            setSupportActionBar(toolbar);

            if(offline) {
                changeToLocalData();
                Log.w("myApp", "Should display Local");
            } else {
                changeToServeData();
                Log.w("myApp", "Should display Service");
            }
        }

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            android.support.v4.app.Fragment please = new fragServerData();
            android.support.v4.app.Fragment please2 = new FragLocalData();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.right_fragment, please, "ServeData")
                    .replace(R.id.left_fragment, please2, "LocalData")
                    .addToBackStack(null)
                    .commit();

        }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater(); // reads XML
        inflater.inflate(R.menu.menu_main, menu); // to create

        return super.onCreateOptionsMenu(menu); // the menu

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_offline) {
            if(!offline){
                changeToLocalData();
                Log.w("myApp", "Offline is declared as " + offline + " on Option");
            }
        }
        if (item.getItemId() == R.id.action_online) {
            if(offline){
                changeToServeData();
                Log.w("myApp", "Offline is declared as " + offline + " on Option \n and we should have changed to local");
            }
        }

        /* ADD THIS ONE FOR CHANGING TO QUEARY
        if (item.getItemId() == R.id.action_quary) {
            if(offline){
                changeToQuery();
            }
        } */

        saveSharedPreferences(this, offline);

        return super.onOptionsItemSelected(item);
    }


    public static void saveSharedPreferences(Context context, Boolean temp) {
        SharedPreferences file = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = file.edit();
        editor.putBoolean("PortateView", temp);
        editor.apply();
    }

    public static Boolean loadSharedPreferences(Context context) {
        SharedPreferences file = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE);
        return file.getBoolean("PortateView", true);
    }

    public void changeToLocalData() {
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            android.support.v4.app.Fragment please = new FragLocalData();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frag_container_frame, please, "LocalData")
                    .addToBackStack(null)
                    .commit();

            offline = true;
        }
    }

    public void changeToServeData(){
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            android.support.v4.app.Fragment please = new fragServerData();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frag_container_frame, please, "ServeData")
                    .addToBackStack(null)
                    .commit();

            offline = false;

        }
    }

}
