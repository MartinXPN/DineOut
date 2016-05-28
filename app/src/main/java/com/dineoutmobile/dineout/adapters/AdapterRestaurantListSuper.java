package com.dineoutmobile.dineout.adapters;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;

import com.dineoutmobile.dineout.databasehelpers.DatabaseHelper;
import com.dineoutmobile.dineout.util.LanguageUtil;
import com.dineoutmobile.dineout.util.RestaurantBasicInfo;

import java.io.IOException;
import java.util.ArrayList;

public abstract class AdapterRestaurantListSuper extends BaseAdapter {

    protected static DatabaseHelper database;
    protected static ArrayList <RestaurantBasicInfo> restaurants = new ArrayList<>();
    Context context;
    LanguageUtil.Language currentLanguage;


    public AdapterRestaurantListSuper( Context context ) {

        this.context = context;

        /// DatabaseHelper is single-tone
        database = DatabaseHelper.getInstance( context );
        try                     { database.createDataBase(); }
        catch (IOException e)   { e.printStackTrace(); }

        getAllRestaurantsBasicInfo();
    }



    @Override
    public int getCount() {
        return restaurants.size();
    }

    @Override
    public Object getItem(int position) {
        return restaurants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }





    public void getAllRestaurantsBasicInfo() {

        if( LanguageUtil.getLanguage( context ) == null || ( !restaurants.isEmpty() && currentLanguage == LanguageUtil.getLanguage( context ) ) )
            return;

        currentLanguage = LanguageUtil.getLanguage( context );
        /// do loading in background because it may take a lot of time...
        /// we can't do it in UI thread
        GetAllRestaurantsBasicInfoTask getInfoTask = new GetAllRestaurantsBasicInfoTask();
        getInfoTask.execute();
    }


    class GetAllRestaurantsBasicInfoTask extends AsyncTask<Object, Object, Object> {

        @Override
        protected Object doInBackground(Object... params) {

            Log.d( "Adapter", "started to load data" );
            restaurants = database.getAllRestaurantsBasicInfo( LanguageUtil.getLanguage( context ).languageLocale );
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {

            Log.d( "Adapter", "finished loading data" );
            notifyDataSetChanged();
        }
    }
}
