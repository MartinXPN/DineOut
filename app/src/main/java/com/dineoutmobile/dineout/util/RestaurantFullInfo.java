package com.dineoutmobile.dineout.util;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.databasehelpers.DatabaseHelper;

import java.util.ArrayList;


public class RestaurantFullInfo extends RestaurantBasicInfo {

    private DataLoading listener;
    private static Context context;
    public String phoneNumber;
    public String shortInfo;
    public ArrayList <String> backgroundPhotoURLs = new ArrayList<>();
    public Address currentAddress = null;
    public ArrayList <Address> allAddresses = new ArrayList<>();


    public RestaurantFullInfo( Context context ) {
        Log.d( "RestaurantFI", "created" );
        RestaurantFullInfo.context = context;
        listener = (DataLoading) context;

        backgroundPhotoURLs.clear();
        backgroundPhotoURLs.add( "https://www.lenordik.com/app/assets/media/generated/55afa22ff1c80bb4097cb1349324a8fc1320719438_gallery_gallery.jpg?1335469974" );
        backgroundPhotoURLs.add( "http://www.julios.co.za/wp-content/uploads/2012/10/restaurant.jpeg" );
        backgroundPhotoURLs.add( "http://thebestoffiji.com/wp-content/uploads/2016/02/restaurants-005.jpg" );
        backgroundPhotoURLs.add( "http://www.maiyango.com/images/homeselect/restaurant-002.jpg" );
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

    public enum BasicInfo {

        WORKING_HOURS( "00:00 - 00:00", R.drawable.ic_time, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog( context.getResources().getString( R.string.working_hours ), WORKING_HOURS.description );
            }
        }),
        PRICE_RANGE( "100-200 $", R.drawable.ic_price, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog( context.getResources().getString( R.string.price_range ), PRICE_RANGE.description );
            }
        }),
        MUSIC("Jazz, Classic, Pop", R.drawable.ic_music, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog( context.getResources().getString( R.string.music ), MUSIC.description );
            }
        }),
        CUISINE( "Seafood, Italian, National", R.drawable.ic_restaurant_menu, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog( context.getResources().getString( R.string.cuisines ), CUISINE.description );
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
    public enum BasicInfoWithLinks {

        MENU( context.getResources().getString( R.string.menu ), R.drawable.ic_restaurant_menu, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.openUrlInBrowser( context, MENU.URL );
            }
        }),
        FEEDBACKS(context.getResources().getString( R.string.feedbacks ), R.drawable.ic_feedback_white, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.openUrlInBrowser( context, FEEDBACKS.URL );
            }
        }),
        WEBSITE( context.getResources().getString( R.string.restaurant_website ), R.drawable.ic_link, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.openUrlInBrowser( context, WEBSITE.URL );
            }
        });

        public String description;
        public String URL;
        public int resource;
        public View.OnClickListener onClickListener;

        BasicInfoWithLinks(String description, int resource, View.OnClickListener onClickListener) {
            this.description = description;
            this.resource = resource;
            this.onClickListener = onClickListener;
        }
    }
    public enum Services {

        WIFI( true, R.string.restaurant_services_wifi, R.drawable.ic_wifi ),
        PRIVATE_ROOMS( true,R.string.restaurant_services_private_rooms, R.drawable.ic_private_room ),
        FOURSHET( true, R.string.restaurant_services_fourshet, R.drawable.ic_fourshet ),
        SHIPPING( true, R.string.restaurant_services_shipping, R.drawable.ic_shipping ),
        CREDIT_CARD( true, R.string.restaurant_services_credit_card, R.drawable.ic_credit_card ),
        PARKING( true, R.string.restaurant_services_parking, R.drawable.ic_parking ),
        OUTSIDE_SEATING( true, R.string.restaurant_services_outside_seating, R.drawable.ic_nature ),
        SMOKING_AREAS( true, R.string.restaurant_services_smoking_area, R.mipmap.ic_smoking_area ),
        SMOKE_FREE_AREAS( true, R.string.restaurant_services_smoke_free_area, R.mipmap.ic_smoke_free_area);

        public boolean supported;
        public int descriptionResId;
        public int resource;

        Services(boolean supported, int description, int resource) {
            this.supported = supported;
            this.descriptionResId = description;
            this.resource = resource;
        }
    }




    public void loadData( long id) {
        this.id = id;

        LoadRestaurantWholeInfoTask loadRestaurantWholeInfoTask = new LoadRestaurantWholeInfoTask();
        loadRestaurantWholeInfoTask.execute();
    }
    public void loadRestaurantFullInfo() {

        LoadRestaurantAddressInfoTask loadRestaurantAddressInfoTask = new LoadRestaurantAddressInfoTask();
        loadRestaurantAddressInfoTask.execute();
    }


    class LoadRestaurantWholeInfoTask extends AsyncTask<Object, Object, Object> {

        /// db is single-tone
        private DatabaseHelper db = DatabaseHelper.getInstance( context );

        @Override
        protected Object doInBackground(Object... params) {

            Log.d( "RestaurantFI", "started to load data" );
            db.getRestaurantAllAddresses( RestaurantFullInfo.this );
            if( currentAddress == null )
                currentAddress = getPreferredAddress();
            db.getRestaurantFullInfo( LanguageUtil.getLanguage( context ).languageLocale, RestaurantFullInfo.this );
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {

            Log.d( "RestaurantFI", "finished loading data" );
            listener.onDataLoaded();
        }
    }
    class LoadRestaurantAddressInfoTask extends AsyncTask<Object, Object, Object> {

        /// db is single-tone
        private DatabaseHelper db = DatabaseHelper.getInstance( context );

        @Override
        protected Object doInBackground(Object... params) {

            Log.d( "RestaurantFI", "started to load data" );
            db.getRestaurantFullInfo( LanguageUtil.getLanguage( context ).languageLocale, RestaurantFullInfo.this );
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {

            Log.d( "RestaurantFI", "finished loading data" );
            listener.onDataLoaded();
        }
    }


    private Address getPreferredAddress() {
        return allAddresses.get( 0 );
    }
    public void setCurrentAddress( Address address ) {
        if( currentAddress != address ) {
            currentAddress = address;
            loadRestaurantFullInfo();
        }
    }
}
