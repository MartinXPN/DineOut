package com.dineoutmobile.dineout.util.models;


import com.dineoutmobile.dineout.R;

import java.util.ArrayList;

public class SearchInfo {

    public enum FilterStates {
        NEUTRAL,
        SUPPORTED,
        NOT_SUPPORTED
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
            else if( state == FilterStates.SUPPORTED )  state = FilterStates.NOT_SUPPORTED;
            else                                        state = FilterStates.NEUTRAL;
        }
    }
    public Filters WIFI = new Filters( R.string.restaurant_services_wifi, R.drawable.ic_wifi );
    public Filters PRIVATE_ROOMS = new Filters( R.string.restaurant_services_private_rooms, R.drawable.ic_private_room );
    public Filters BANQUET = new Filters( R.string.restaurant_services_banquet, R.drawable.ic_banquet);
    public Filters SHIPPING = new Filters( R.string.restaurant_services_shipping, R.drawable.ic_shipping );
    public Filters CREDIT_CARD = new Filters( R.string.restaurant_services_credit_card, R.drawable.ic_credit_card );
    public Filters PARKING = new Filters( R.string.restaurant_services_parking, R.drawable.ic_parking );
    public Filters INSIDE_SEATING = new Filters( R.string.restaurant_services_inside_seating, R.drawable.ic_inside );
    public Filters OUTSIDE_SEATING = new Filters( R.string.restaurant_services_outside_seating, R.drawable.ic_nature );
    public Filters SMOKING_AREAS = new Filters( R.string.restaurant_services_smoking_area, R.mipmap.ic_smoking_area );
    public Filters SMOKE_FREE_AREAS = new Filters( R.string.restaurant_services_smoke_free_area, R.mipmap.ic_smoke_free_area );


    public String searchQuery;
}
