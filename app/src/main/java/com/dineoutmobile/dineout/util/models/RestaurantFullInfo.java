package com.dineoutmobile.dineout.util.models;


import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.util.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;


public class RestaurantFullInfo extends RestaurantBasicInfo {

    private transient static Context context;

    public String phoneNumber;
    public String shortDescription = "Information";
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
            for( int i=0; i < all.size(); i++ )
                if( all.get( i ).resource == resource ) {
                    all.clear();
                    break;
                }
            all.add( this );
        }
    }
    public BasicInfo workingHours = new BasicInfo( "00:00 - 00:00", R.drawable.ic_time, new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialog( context.getResources().getString( R.string.working_hours ), workingHours.description );
        }
    });
    public BasicInfo priceRange = new BasicInfo( "100-200 $", R.drawable.ic_price, new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialog( context.getResources().getString( R.string.price_range ), priceRange.description );
        }
    });
    public BasicInfo music = new BasicInfo("Jazz, Classic, Pop", R.drawable.ic_music, new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialog( context.getResources().getString( R.string.music ), music.description );
        }
    });
    public BasicInfo cuisine = new BasicInfo( "Seafood, Italian, National", R.drawable.ic_restaurant_menu, new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialog( context.getResources().getString( R.string.cuisines ), cuisine.description );
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

            for( int i=0; i < all.size(); i++ )
                if( all.get( i ).resource == resource ) {
                    all.clear();
                    break;
                }
            all.add( this );
        }
    }
    public BasicInfoWithLinks menu = new BasicInfoWithLinks ( R.string.menu, R.drawable.ic_restaurant_menu, new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Util.openUrlInBrowser( context, menu.URL );
        }
    });
    public BasicInfoWithLinks feedbacks = new BasicInfoWithLinks( R.string.feedbacks, R.drawable.ic_feedback_white, new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Util.openUrlInBrowser( context, feedbacks.URL );
        }
    });
    public BasicInfoWithLinks website = new BasicInfoWithLinks( R.string.restaurant_website, R.drawable.ic_link, new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Util.openUrlInBrowser( context, website.URL );
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

            for( int i=0; i < all.size(); i++ )
                if( all.get( i ).resource == resource ) {
                    all.clear();
                    break;
                }
            all.add( this );
        }
    }
    public Services wifi = new Services( true, R.string.restaurant_services_wifi, R.drawable.ic_wifi );
    public Services privateRooms = new Services( true,R.string.restaurant_services_private_rooms, R.drawable.ic_private_room );
    public Services banquet = new Services( true, R.string.restaurant_services_banquet, R.drawable.ic_banquet);
    //public Services shipping = new Services( true, R.string.restaurant_services_shipping, R.drawable.ic_shipping );
    public Services creditCard = new Services( true, R.string.restaurant_services_credit_card, R.drawable.ic_credit_card );
    public Services parking = new Services( true, R.string.restaurant_services_parking, R.drawable.ic_parking );
    public Services insideSeating = new Services( true, R.string.restaurant_services_inside_seating,R.drawable.ic_inside );
    public Services outsideSeating = new Services( true, R.string.restaurant_services_outside_seating, R.drawable.ic_nature );
    public Services smokingAreas = new Services( true, R.string.restaurant_services_smoking_area, R.mipmap.ic_smoking_area );
    public Services smokeFreeAreas = new Services( true, R.string.restaurant_services_smoke_free_area, R.mipmap.ic_smoke_free_area);





    public RestaurantFullInfo( Context context ) {
        Log.d( "RestaurantFI", "created" );
        RestaurantFullInfo.context = context;

        backgroundPhotoURLs.clear();
        backgroundPhotoURLs.add( "https://www.lenordik.com/app/assets/media/generated/55afa22ff1c80bb4097cb1349324a8fc1320719438_gallery_gallery.jpg?1335469974" );
        backgroundPhotoURLs.add( "http://www.julios.co.za/wp-content/uploads/2012/10/restaurant.jpeg" );
        backgroundPhotoURLs.add( "http://restaurant.business.brookes.ac.uk/images/slideshow/restaurant.jpg" );
        backgroundPhotoURLs.add( "http://www.maiyango.com/images/homeselect/restaurant-002.jpg" );

        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson( this );
        Log.d( "RestaurantInfo in json:", json );
    }



    protected static void showDialog( String title, String message ) {
        new AlertDialog.Builder( context, AlertDialog.THEME_DEVICE_DEFAULT_DARK  )
                .setTitle( title )
                .setMessage( message ).show();
    }
}
