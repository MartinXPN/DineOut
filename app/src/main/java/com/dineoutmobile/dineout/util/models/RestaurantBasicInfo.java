package com.dineoutmobile.dineout.util.models;


public class RestaurantBasicInfo {

    public long restaurantId = -1;                              /// database addressId for better access
    public String name = "Name";                                /// name of the restaurant
    public float rating = 5.0f;                                 /// cumulative average rating of the restaurant

    public String logoURL;
    public String backgroundPhotoURL;
}
