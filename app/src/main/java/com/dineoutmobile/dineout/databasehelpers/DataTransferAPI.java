package com.dineoutmobile.dineout.databasehelpers;


import com.dineoutmobile.dineout.util.models.ReservedRestaurantInfo;
import com.dineoutmobile.dineout.util.models.RestaurantBasicInfo;
import com.dineoutmobile.dineout.util.models.RestaurantFullInfo;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


/// All endpoints are defined here
public interface DataTransferAPI {

    @GET( "/request_url" )
    /// options include
        // 1. language
        // 2. city
        // 3. block number -> first 100 or second 100 restaurants...
    Call< ArrayList <RestaurantBasicInfo> > getAllRestaurantsBasicInfo( @QueryMap Map <String, String> options );

    @GET( "/request_url" )
    /// options include
        // 1. language
        // 2. restaurant ID
        // 3. search options -> if address is null => get everything, else => get only that address's info
    Call <RestaurantFullInfo> getRestaurantFullInfo( @QueryMap Map <String, String> options );


    @GET( "/request_url" )
    Call< ArrayList <ReservedRestaurantInfo> > getAllReservedRestaurants( @Query("device_id") String deviceId );


    @POST( "/request_url" )
    Call <ReservedRestaurantInfo> requestReservation( @Field("restaurant_info") ReservedRestaurantInfo reservationInfo );

    @POST( "/request_url" )
    Call <ReservedRestaurantInfo> cancelReservation( @Field("restaurant_info") ReservedRestaurantInfo reservationInfo );
}
