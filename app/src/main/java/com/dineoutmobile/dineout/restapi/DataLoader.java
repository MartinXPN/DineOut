package com.dineoutmobile.dineout.restapi;


import android.util.Log;

import com.dineoutmobile.dineout.util.models.RestaurantBasicInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataLoader {

    private static boolean loadRestaurantsRequested = false;
    public static void loadRestaurants( String languageLocale ) {

        /// don't load the same thing twice
        if( loadRestaurantsRequested )
            return;
        loadRestaurantsRequested = true;


        // Retrofit needs to know how to deserialize response, for instance into JSON
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( DataTransferAPI.BASE_URL )
                .addConverterFactory( GsonConverterFactory.create() )
                .build();


        Map<String, String> options = new HashMap<>();
        options.put( "language", languageLocale );
        DataTransferAPI api = retrofit.create(DataTransferAPI.class);
        api.getAllRestaurantsBasicInfo(options).enqueue(new Callback<ArrayList<RestaurantBasicInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<RestaurantBasicInfo>> call, Response<ArrayList<RestaurantBasicInfo>> response) {
                EventBus.getDefault().post( response.body() );
                loadRestaurantsRequested = false;
            }

            @Override
            public void onFailure(Call<ArrayList<RestaurantBasicInfo>> call, Throwable t) {
                Log.d( "FAILURE", t.toString() + '\n' + call.toString() );
                loadRestaurantsRequested = false;
            }
        });
    }
}
