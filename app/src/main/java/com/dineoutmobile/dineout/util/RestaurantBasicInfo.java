package com.dineoutmobile.dineout.util;


import android.graphics.Bitmap;


public class RestaurantBasicInfo {

    public long id = -1;                                        /// database id for better access
    public String name = "Name";                                /// name of the restaurant
    public String description = "Description";                  /// short description of the restaurant
    public float rating = 5.0f;                                 /// cumulative average rating of the restaurant
    public Bitmap logo;                                         /// logo of the restaurant
}
