package com.dineoutmobile.dineout.util;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.databasehelpers.DatabaseHelper;

import java.util.ArrayList;


public class RestaurantFullInfo extends RestaurantBasicInfo {

    private DataLoading listener;
    private static Context context;
    public ArrayList <Bitmap> backgroundPhotos = new ArrayList<>(); /// background pictures of the restaurant -> these are displayed only in ActivityViewRestaurant
    public ArrayList <String> addresses = new ArrayList<>();        /// addresses of the restaurant


    public RestaurantFullInfo( Context context ) {
        Log.d( "RestaurantFI", "created" );
        RestaurantFullInfo.context = context;
        listener = (DataLoading) context;
    }
    public interface DataLoading {
        void onDataLoaded();
    }



    protected static void showDialog( String title, String message ) {
        new AlertDialog.Builder( context )
                .setTitle( title )
                .setMessage( message )
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }
    protected static void openUrlInBrowser( String url ) {
        Intent browserIntent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
        context.startActivity( browserIntent );
    }

    public enum BasicInfo {

        WEBSITE( "Restaurant website", R.mipmap.ic_link, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrlInBrowser("http://www.dineoutmobile.com");
            }
        }),
        MENU( "Menu", R.mipmap.ic_restaurant_menu, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrlInBrowser("http://www.dineoutmobile.com");
            }
        }),
        CUISINE( "Seafood, Italian, National", R.mipmap.ic_restaurant_menu, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog( "Cuisines", CUISINE.description );
            }
        }),
        MUSIC("Jazz, Classic, Pop", R.mipmap.ic_music, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog( "Music", MUSIC.description );
            }
        }),
        FEEDBACKS("Feedbacks", R.mipmap.ic_feedback_white, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrlInBrowser("http://www.dineoutmobile.com");
            }
        });

        public String description;
        public int resource;
        public View.OnClickListener onClickListener;

        BasicInfo(String description, int resource, View.OnClickListener onClickListener) {
            this.description = description;
            this.resource = resource;
            this.onClickListener = onClickListener;
        }
    }
    public enum Details {

        WORKING_HOURS( true, "00:00 - 00:00", R.mipmap.ic_time ),
        PRICE_RANGE( true, "Expensive", R.mipmap.ic_price ),
        WIFI( true, "WiFi", R.mipmap.ic_wifi ),
        VIP( true, "VIP areas", R.mipmap.ic_vip ),
        FOURSHET( true, "Fourshet", R.mipmap.ic_fourshet ),
        SHIPPING( true, "Shipping", R.mipmap.ic_shipping ),
        CREDIT_CARD( true, "Accepts credit-cards", R.mipmap.ic_credit_card ),
        SMOKING_AREAS( true, "Smoking areas", R.mipmap.ic_smoking_area ),
        NO_SMOKING_AREAS( true, "Smoke-free areas", R.mipmap.ic_no_smoking_area );

        public boolean supported;
        public String description;
        public int resource;

        Details(boolean has, String description, int resource) {
            this.supported = has;
            this.description = description;
            this.resource = resource;
        }
    }




    public void loadData( long id ) {
        this.id = id;
        GetRestaurantFullInfoTask getRestaurantFullInfoTask = new GetRestaurantFullInfoTask();
        getRestaurantFullInfoTask.execute();
    }


    class GetRestaurantFullInfoTask extends AsyncTask<Object, Object, Object> {

        /// db is single-tone
        private DatabaseHelper db = DatabaseHelper.getInstance( context );

        @Override
        protected Object doInBackground(Object... params) {

            Log.d( "RestaurantFI", "started to load data" );
            db.getRestaurantFullInfo( id, Util.getLanguage( context ).languageLocale, RestaurantFullInfo.this );
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {

            Log.d( "RestaurantFI", "finished loading data" );
            listener.onDataLoaded();
        }
    }
}
