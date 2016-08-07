package com.dineoutmobile.dineout.restapi;


import android.util.Pair;

import com.dineoutmobile.dineout.models.ReservationSchema;
import com.dineoutmobile.dineout.models.RestaurantBranchSchema;
import com.dineoutmobile.dineout.models.RestaurantOnMapSchema;
import com.dineoutmobile.dineout.models.RestaurantPreviewSchema;
import com.dineoutmobile.dineout.models.RestaurantSchema;
import com.dineoutmobile.dineout.models.SearchSchema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


/// All endpoints are defined here
public interface DataTransferAPI {

    /// base url of all requests
    String BASE_URL = "http://dineoutmobile.com";


    @GET( "/wservices/RestaurantPreviewSchema.php" )
    Call< ArrayList <RestaurantPreviewSchema> > getAllRestaurants(@Query("language") String language,
                                                                  @Query("blockStart") Integer blockStart,
                                                                  @Query("regionId") Long regionId,
                                                                  @Query("searchOptions") SearchSchema searchOptions );


    @GET ( "/wservices/nearByPlaces.php" )
    Call< ArrayList <RestaurantOnMapSchema> > getNearbyRestaurants(@Query("language") String language,
                                                                   @Query("latitude") Float latitude,
                                                                   @Query("longitude") Float longitude,
                                                                   @Query("searchOptions") SearchSchema searchOptions);


    @GET ( "/wservices/???" )
    Call< ArrayList <ReservationSchema> > getMyReservations(@Header("language") String language,
                                                            @Header("userId") String userId,
                                                            @Query("searchOptions") SearchSchema searchOptions);



    @GET( "/wservices/getRestaurantFullInfo.php" )
    Call <RestaurantSchema> getRestaurantFullInfo(@Header("language") String query,
                                                      @Query("restaurantId") Long restaurantId,
                                                      @Query("searchOptions") SearchSchema searchOptions);

    @GET( "/wservices/getRestaurantFullInfo.php" )
    Call <RestaurantSchema> getRestaurantFullInfo_Old( @QueryMap Map<String, String> options );


    @GET( "/wservices/???" )
    Call <RestaurantBranchSchema> getRestaurantBranchInfo(@Header("language") String language,
                                                          @Query("restaurantId") Long restaurantId,
                                                          @Query("branchId") Long branchId);


    /// options include
    // [Integer] (groupSize) -> Number of people
    // [String] (date) -> Date
    // [String] (time) -> Time
    // [String] (phoneNumber) -> User phone number
    // [Long] (restaurantId) -> Id of the restaurant
    // [Long] (branchId) -> Id of the reserved branch of that restaurant
    // [String] (comments) -> Additional comments (just a string… if the user has something to add)
    // Pair <wasReservationSuccessful, messageToUser>
    @POST( "/request_url" )
    Call <Pair <Boolean,String> > reserveRestaurant(@Header("userId") String userId,
                                                    @FieldMap HashMap <String, String> options);


    // Pair <wasCancellationSuccessful, messageToUser>
    @POST( "/request_url" )
    Call <Pair <Boolean,String> > cancelReservation(@Header("userId") String userId,
                                                    @Field("reservationId") Long reservationId);
}
