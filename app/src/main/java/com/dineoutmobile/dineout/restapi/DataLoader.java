package com.dineoutmobile.dineout.restapi;


import android.util.Log;

import com.dineoutmobile.dineout.models.RestaurantOnMapSchema;
import com.dineoutmobile.dineout.models.RestaurantPreviewSchema;
import com.dineoutmobile.dineout.models.SearchSchema;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataLoader {

    private static boolean loadRestaurantsRequested = false;
    public static void loadRestaurants( String languageLocale, Integer blockStart, Long regionId, SearchSchema searchOptions ) {

        /// don't load the same thing twice
        if( loadRestaurantsRequested )
            return;
        loadRestaurantsRequested = true;


        // Retrofit needs to know how to deserialize response, for instance into JSON
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( DataTransferAPI.BASE_URL )
                .addConverterFactory( GsonConverterFactory.create() )
                .build();


        DataTransferAPI api = retrofit.create(DataTransferAPI.class);
        api.getAllRestaurants( languageLocale, blockStart, regionId, searchOptions).enqueue(new Callback<ArrayList<RestaurantPreviewSchema>>() {
            @Override
            public void onResponse(Call<ArrayList<RestaurantPreviewSchema>> call, Response<ArrayList<RestaurantPreviewSchema>> response) {
                EventBus.getDefault().post( response.body() );
                loadRestaurantsRequested = false;
            }

            @Override
            public void onFailure(Call<ArrayList<RestaurantPreviewSchema>> call, Throwable t) {
                Log.d( "FAILURE", t.toString() + '\n' + call.toString() );
                loadRestaurantsRequested = false;
            }
        });
    }



    private static boolean loadNearbyRequested = false;
    public static void loadNearbyRestaurants( String languageLocale, Float latitude, Float longitude, SearchSchema searchOptions ) {

        /// don't load the same thing twice
        if( loadNearbyRequested )
            return;
        loadNearbyRequested = true;


        // Retrofit needs to know how to deserialize response, for instance into JSON
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( DataTransferAPI.BASE_URL )
                .addConverterFactory( GsonConverterFactory.create() )
                .build();


        DataTransferAPI api = retrofit.create(DataTransferAPI.class);
        api.getNearbyRestaurants( languageLocale, latitude, longitude, searchOptions).enqueue(new Callback<ArrayList<RestaurantOnMapSchema>>() {
            @Override
            public void onResponse(Call<ArrayList<RestaurantOnMapSchema>> call, Response<ArrayList<RestaurantOnMapSchema>> response) {
                EventBus.getDefault().post( response.body() );
                loadNearbyRequested = false;
            }

            @Override
            public void onFailure(Call<ArrayList<RestaurantOnMapSchema>> call, Throwable t) {
                Log.d( "FAILURE", t.toString() + '\n' + call.toString() );
                loadNearbyRequested = false;
            }
        });
    }
}
