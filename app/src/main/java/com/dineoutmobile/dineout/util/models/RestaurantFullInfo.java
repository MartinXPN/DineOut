package com.dineoutmobile.dineout.util.models;


import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.databasehelpers.DatabaseHelper;
import com.dineoutmobile.dineout.util.Address;
import com.dineoutmobile.dineout.util.LanguageUtil;
import com.dineoutmobile.dineout.util.Util;

import java.util.ArrayList;


public class RestaurantFullInfo extends RestaurantBasicInfo {

    private transient OnDataLoadedListener listener;
    private transient static Context context;

    public String phoneNumber;
    public String shortInfo = "Information";
    public ArrayList <String> backgroundPhotoURLs = new ArrayList<>();
    public Address currentAddress = null;
    public ArrayList <Address> allAddresses = new ArrayList<>();



    public static class BasicInfo {

        public String description;
        public transient int resource;
        public transient View.OnClickListener onClickListener;
        public static transient ArrayList <BasicInfo> all = new ArrayList<>();

        BasicInfo(String description, int resource, View.OnClickListener onClickListener) {
            this.description = description;
            this.resource = resource;
            this.onClickListener = onClickListener;
            all.add( this );
        }
    }
    public BasicInfo WORKING_HOURS = new BasicInfo( "00:00 - 00:00", R.drawable.ic_time, new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialog( context.getResources().getString( R.string.working_hours ), WORKING_HOURS.description );
        }
    });
    public BasicInfo PRICE_RANGE = new BasicInfo( "100-200 $", R.drawable.ic_price, new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialog( context.getResources().getString( R.string.price_range ), PRICE_RANGE.description );
        }
    });
    public BasicInfo MUSIC = new BasicInfo("Jazz, Classic, Pop", R.drawable.ic_music, new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialog( context.getResources().getString( R.string.music ), MUSIC.description );
        }
    });
    public BasicInfo CUISINE = new BasicInfo( "Seafood, Italian, National", R.drawable.ic_restaurant_menu, new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialog( context.getResources().getString( R.string.cuisines ), CUISINE.description );
        }
    });




    public static class BasicInfoWithLinks {

        public String URL;
        public transient int descriptionResId;
        public transient int resource;
        public transient View.OnClickListener onClickListener;
        public static transient ArrayList <BasicInfoWithLinks> all = new ArrayList<>();

        BasicInfoWithLinks(int descriptionResId, int resource, View.OnClickListener onClickListener) {
            this.descriptionResId = descriptionResId;
            this.resource = resource;
            this.onClickListener = onClickListener;
            all.add( this );
        }
    }
    public BasicInfoWithLinks MENU = new BasicInfoWithLinks ( R.string.menu, R.drawable.ic_restaurant_menu, new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Util.openUrlInBrowser( context, MENU.URL );
        }
    });
    public BasicInfoWithLinks FEEDBACKS = new BasicInfoWithLinks( R.string.feedbacks, R.drawable.ic_feedback_white, new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Util.openUrlInBrowser( context, FEEDBACKS.URL );
        }
    });
    public BasicInfoWithLinks WEBSITE = new BasicInfoWithLinks( R.string.restaurant_website, R.drawable.ic_link, new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Util.openUrlInBrowser( context, WEBSITE.URL );
        }
    });




    public static class Services {

        public boolean isSupported;
        public transient int descriptionResId;
        public transient int resource;
        public static transient ArrayList <Services> all = new ArrayList<>();

        Services(boolean isSupported, int description, int resource) {
            this.isSupported = isSupported;
            this.descriptionResId = description;
            this.resource = resource;
            all.add( this );
        }
    }
    public Services WIFI = new Services( true, R.string.restaurant_services_wifi, R.drawable.ic_wifi );
    public Services PRIVATE_ROOMS = new Services( true,R.string.restaurant_services_private_rooms, R.drawable.ic_private_room );
    public Services FOURSHET = new Services( true, R.string.restaurant_services_fourshet, R.drawable.ic_fourshet );
    public Services SHIPPING = new Services( true, R.string.restaurant_services_shipping, R.drawable.ic_shipping );
    public Services CREDIT_CARD = new Services( true, R.string.restaurant_services_credit_card, R.drawable.ic_credit_card );
    public Services PARKING = new Services( true, R.string.restaurant_services_parking, R.drawable.ic_parking );
    public Services OUTSIDE_SEATING = new Services( true, R.string.restaurant_services_outside_seating, R.drawable.ic_nature );
    public Services SMOKING_AREAS = new Services( true, R.string.restaurant_services_smoking_area, R.mipmap.ic_smoking_area );
    public Services SMOKE_FREE_AREAS = new Services( true, R.string.restaurant_services_smoke_free_area, R.mipmap.ic_smoke_free_area);





    public RestaurantFullInfo( Context context ) {
        Log.d( "RestaurantFI", "created" );
        RestaurantFullInfo.context = context;
        listener = (OnDataLoadedListener) context;

        backgroundPhotoURLs.clear();
        backgroundPhotoURLs.add( "https://www.lenordik.com/app/assets/media/generated/55afa22ff1c80bb4097cb1349324a8fc1320719438_gallery_gallery.jpg?1335469974" );
        backgroundPhotoURLs.add( "http://www.julios.co.za/wp-content/uploads/2012/10/restaurant.jpeg" );
        backgroundPhotoURLs.add( "http://restaurant.business.brookes.ac.uk/images/slideshow/restaurant.jpg" );
        backgroundPhotoURLs.add( "http://www.maiyango.com/images/homeselect/restaurant-002.jpg" );
    }
    public interface OnDataLoadedListener {
        void onDataLoaded();
    }



    protected static void showDialog( String title, String message ) {
        new AlertDialog.Builder( context, AlertDialog.THEME_DEVICE_DEFAULT_DARK  )
                .setTitle( title )
                .setMessage( message ).show();
    }


    public void loadRestaurantWholeInfo( long id ) {
        this.id = id;

        LoadRestaurantWholeInfoTask loadRestaurantWholeInfoTask = new LoadRestaurantWholeInfoTask();
        loadRestaurantWholeInfoTask.execute();
    }
    public void loadRestaurantAddressInfo() {

        LoadRestaurantAddressInfoTask loadRestaurantAddressInfoTask = new LoadRestaurantAddressInfoTask();
        loadRestaurantAddressInfoTask.execute();
    }


    class LoadRestaurantWholeInfoTask extends AsyncTask<Object, Object, Object> {

        /// db is single-tone
        private transient DatabaseHelper db = DatabaseHelper.getInstance( context );

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
        private transient DatabaseHelper db = DatabaseHelper.getInstance( context );

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
            loadRestaurantAddressInfo();
        }
    }
}
