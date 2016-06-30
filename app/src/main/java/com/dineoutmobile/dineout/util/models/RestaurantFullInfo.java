package com.dineoutmobile.dineout.util.models;


import android.util.Log;

import com.dineoutmobile.dineout.R;

import java.util.ArrayList;


public class RestaurantFullInfo extends RestaurantBasicInfo {

    public String phoneNumber;
    public String shortDescription = "Information";
    public ArrayList <String> backgroundPhotoURLs = new ArrayList<>();
    public Address currentAddress = null;
    public ArrayList <Address> allAddresses = new ArrayList<>();



    public class BasicInfo {

        public String description;
        public transient int backgroundResId;
        public transient int titleResourceId;

        BasicInfo(String description, int titleResourceId, int backgroundResId) {
            this.description = description;
            this.backgroundResId = backgroundResId;
            this.titleResourceId = titleResourceId;
        }
    }
    public BasicInfo workingHours = new BasicInfo( "10:00 - 23:00", R.string.working_hours, R.drawable.ic_time);
    public BasicInfo priceRange = new BasicInfo( "100-200 $", R.string.price_range, R.drawable.ic_price);
    public BasicInfo music = new BasicInfo("Jazz, Classic, Pop", R.string.music, R.drawable.ic_music);
    public BasicInfo cuisine = new BasicInfo( "Seafood, Italian, National", R.string.cuisines, R.drawable.ic_restaurant_menu );
    /// Awful, terrible method... The worst thing in this project
    public ArrayList<BasicInfo> getAllBasicInfo() {
        ArrayList <BasicInfo> result = new ArrayList<>();
        result.add( new BasicInfo( workingHours.description, R.string.working_hours, R.drawable.ic_time ) );
        result.add( new BasicInfo( priceRange.description, R.string.price_range, R.drawable.ic_price ) );
        result.add( new BasicInfo( music.description, R.string.music, R.drawable.ic_music ) );
        result.add( new BasicInfo( cuisine.description, R.string.cuisines, R.drawable.ic_restaurant_menu ) );
        return result;
    }




    public class BasicInfoWithLinks {

        public String URL;
        public transient int descriptionResId;
        public transient int backgroundResId;

        BasicInfoWithLinks( String URL, int descriptionResId, int backgroundResId) {
            this.URL = URL;
            this.descriptionResId = descriptionResId;
            this.backgroundResId = backgroundResId;
        }
    }
    public BasicInfoWithLinks menu = new BasicInfoWithLinks ( "", R.string.menu, R.drawable.ic_restaurant_menu);
    public BasicInfoWithLinks feedbacks = new BasicInfoWithLinks( "", R.string.feedbacks, R.drawable.ic_feedback_white);
    public BasicInfoWithLinks website = new BasicInfoWithLinks( "", R.string.restaurant_website, R.drawable.ic_link);
    /// Awful, terrible method... The worst thing in this project
    public ArrayList <BasicInfoWithLinks> getAllBasicInfoWithLinks() {
        ArrayList <BasicInfoWithLinks> result = new ArrayList<>();
        result.add( new BasicInfoWithLinks( menu.URL, R.string.menu, R.drawable.ic_restaurant_menu ) );
        result.add( new BasicInfoWithLinks( feedbacks.URL, R.string.feedbacks, R.drawable.ic_feedback_white ) );
        result.add( new BasicInfoWithLinks( website.URL, R.string.restaurant_website, R.drawable.ic_link ) );
        return result;
    }




    public class Services {

        public boolean isSupported = true;
        public transient int descriptionResId;
        public transient int resource;

        Services( boolean isSupported, int description, int resource) {
            this.isSupported = isSupported;
            this.descriptionResId = description;
            this.resource = resource;
        }
    }
    public Services wifi = new Services( true, R.string.restaurant_services_wifi, R.drawable.ic_wifi );
    public Services privateRooms = new Services( true, R.string.restaurant_services_private_rooms, R.drawable.ic_private_room );
    public Services banquet = new Services( true, R.string.restaurant_services_banquet, R.drawable.ic_banquet);
    //public Services shipping = new Services( true, R.string.restaurant_services_shipping, R.drawable.ic_shipping );
    public Services creditCard = new Services( true, R.string.restaurant_services_credit_card, R.drawable.ic_credit_card );
    public Services parking = new Services( true, R.string.restaurant_services_parking, R.drawable.ic_parking );
    public Services insideSeating = new Services( true, R.string.restaurant_services_inside_seating,R.drawable.ic_inside );
    public Services outsideSeating = new Services( true, R.string.restaurant_services_outside_seating, R.drawable.ic_nature );
    public Services smokingAreas = new Services( true, R.string.restaurant_services_smoking_area, R.mipmap.ic_smoking_area );
    public Services smokeFreeAreas = new Services( true, R.string.restaurant_services_smoke_free_area, R.mipmap.ic_smoke_free_area);
    public ArrayList <Services> getAllServices() {
        ArrayList <Services> result = new ArrayList<>();
        result.add( new Services( wifi.isSupported, R.string.restaurant_services_wifi, R.drawable.ic_wifi ) );
        result.add( new Services( privateRooms.isSupported, R.string.restaurant_services_private_rooms, R.drawable.ic_private_room ) );
        result.add( new Services( banquet.isSupported, R.string.restaurant_services_banquet, R.drawable.ic_banquet ) );
        result.add( new Services( creditCard.isSupported, R.string.restaurant_services_credit_card, R.drawable.ic_credit_card ) );
        result.add( new Services( parking.isSupported, R.string.restaurant_services_parking, R.drawable.ic_parking ) );
        result.add( new Services( insideSeating.isSupported, R.string.restaurant_services_inside_seating, R.drawable.ic_inside ) );
        result.add( new Services( outsideSeating.isSupported, R.string.restaurant_services_outside_seating, R.drawable.ic_nature ) );
        result.add( new Services( smokingAreas.isSupported, R.string.restaurant_services_smoking_area, R.mipmap.ic_smoking_area ) );
        result.add( new Services( smokeFreeAreas.isSupported, R.string.restaurant_services_smoke_free_area, R.mipmap.ic_smoke_free_area ) );
        return result;
    }




    public RestaurantFullInfo() {
        Log.d( "RestaurantFI", "created" );
        backgroundPhotoURLs.clear();
        backgroundPhotoURLs.add( "https://www.lenordik.com/app/assets/media/generated/55afa22ff1c80bb4097cb1349324a8fc1320719438_gallery_gallery.jpg?1335469974" );
        backgroundPhotoURLs.add( "http://www.julios.co.za/wp-content/uploads/2012/10/restaurant.jpeg" );
        backgroundPhotoURLs.add( "http://restaurant.business.brookes.ac.uk/images/slideshow/restaurant.jpg" );
        backgroundPhotoURLs.add( "http://www.maiyango.com/images/homeselect/restaurant-002.jpg" );
    }
}
