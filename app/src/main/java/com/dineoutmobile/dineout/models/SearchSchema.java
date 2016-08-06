package com.dineoutmobile.dineout.models;


import com.dineoutmobile.dineout.R;

import java.util.ArrayList;

public class SearchSchema {

    public enum FilterStates {
        NEUTRAL,
        SUPPORTED
    }

    public static class Filters {

        public FilterStates state;
        public transient int descriptionResId;
        public transient int resource;
        public static transient ArrayList<Filters> all = new ArrayList<>();

        Filters(int description, int resource) {
            this.state = FilterStates.NEUTRAL;
            this.descriptionResId = description;
            this.resource = resource;

            for( int i=0; i < all.size(); i++ )
                if( all.get( i ).resource == resource ) {
                    all.clear();
                    break;
                }
            all.add( this );
        }


        public void flipState() {
            if( state == FilterStates.NEUTRAL )         state = FilterStates.SUPPORTED;
            else                                        state = FilterStates.NEUTRAL;
        }
    }
    public Filters wifi = new Filters( R.string.restaurant_services_wifi, R.drawable.ic_wifi );
    public Filters privateRooms = new Filters( R.string.restaurant_services_private_rooms, R.drawable.ic_private_room );
    public Filters banquet = new Filters( R.string.restaurant_services_banquet, R.drawable.ic_banquet);
    //public Filters shipping = new Filters( R.string.restaurant_services_shipping, R.drawable.ic_shipping );
    public Filters creditCard = new Filters( R.string.restaurant_services_credit_card, R.drawable.ic_credit_card );
    public Filters parking = new Filters( R.string.restaurant_services_parking, R.drawable.ic_parking );
    public Filters insideSeating = new Filters( R.string.restaurant_services_inside_seating, R.drawable.ic_inside );
    public Filters outsideSeating = new Filters( R.string.restaurant_services_outside_seating, R.drawable.ic_nature );
    public Filters smokingAreas = new Filters( R.string.restaurant_services_smoking_area, R.mipmap.ic_smoking_area );
    public Filters smokeFreeAreas = new Filters( R.string.restaurant_services_smoke_free_area, R.mipmap.ic_smoke_free_area );


    public String searchQuery;
}
