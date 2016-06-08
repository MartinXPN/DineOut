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

    /// base url of all requests
    String BASE_URL = "http://mobile-course.herokuapp.com";


    /// options include
        // 1. language
        // 2. city
        // 3. block number -> first 100 or second 100 restaurants...
    @GET( "/request_url" )
    Call< ArrayList <RestaurantBasicInfo> > getAllRestaurantsBasicInfo( @QueryMap Map <String, String> options );


    /// options include
        // 1. language
        // 2. restaurant ID
        // 3. search options (WIFI, private rooms, etc. )
        // 4. if address is null => calculate preferred address and get its info, else => get only that address's info
    @GET( "/request_url" )
    Call <RestaurantFullInfo> getRestaurantFullInfo( @QueryMap Map <String, String> options );


    /// deviceId is the unique id of the device provided by android Settings.Secure
    @GET( "/request_url" )
    Call< ArrayList <ReservedRestaurantInfo> > getAllReservedRestaurants( @Query("device_id") String deviceId );



    // true => reservation was successful
    // false => reservation was denied ( possible causes: lack of places, etc. )
    @POST( "/request_url" )
    Call <Boolean> requestReservation( @Field("reservation_info") ReservedRestaurantInfo reservationInfo );


    // true => cancellation was successful
    // false => cancellation was denied by server
    @POST( "/request_url" )
    Call <Boolean> cancelReservation( @Field("reservation_info") ReservedRestaurantInfo reservationInfo );
}
