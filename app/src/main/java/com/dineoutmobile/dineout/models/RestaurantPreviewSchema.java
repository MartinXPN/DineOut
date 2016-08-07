package com.dineoutmobile.dineout.models;


public class RestaurantPreviewSchema {

    public Long restaurantId = -1L;         /// database addressId for better access
    public String restaurantName = "Name";  /// name of the restaurant
    public Float rating = 5.0f;             /// cumulative average rating of the restaurant

    public String logoURL;
    public String backgroundURL;
}
