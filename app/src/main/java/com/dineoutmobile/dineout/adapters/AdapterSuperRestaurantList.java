package com.dineoutmobile.dineout.adapters;


import android.content.Context;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.dineoutmobile.dineout.databasehelpers.DataTransferAPI;
import com.dineoutmobile.dineout.util.LanguageUtil;
import com.dineoutmobile.dineout.util.models.RestaurantBasicInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class AdapterSuperRestaurantList extends BaseAdapter {

    protected static ArrayList <RestaurantBasicInfo> restaurants = new ArrayList<>();
    protected Context context;
    protected LanguageUtil.Language currentLanguage;


    public AdapterSuperRestaurantList(Context context ) {

        this.context = context;
        getAllRestaurantsBasicInfo();
    }



    @Override
    public int getCount() {
        return restaurants.size();
    }

    @Override
    public Object getItem(int position) {
        return restaurants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }





    public void getAllRestaurantsBasicInfo() {

        if( LanguageUtil.getLanguage( context ) == null || ( !restaurants.isEmpty() && currentLanguage == LanguageUtil.getLanguage( context ) ) )
            return;

        currentLanguage = LanguageUtil.getLanguage( context );



        //// GSON TEST
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        // Retrofit needs to know how to deserialize response, for instance into JSON
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( DataTransferAPI.BASE_URL )
                .addConverterFactory( GsonConverterFactory.create() )
                .build();


        Map<String, String> options = new HashMap<>();
        options.put( "language", LanguageUtil.getLanguage( context ).languageLocale );
        DataTransferAPI api = retrofit.create(DataTransferAPI.class);
        api.getAllRestaurantsBasicInfo(options).enqueue(new Callback<ArrayList<RestaurantBasicInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<RestaurantBasicInfo>> call, Response<ArrayList<RestaurantBasicInfo>> response) {

                Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                String json = gson.toJson( response.body() );
                Log.d( "RESPONSE IN JSON", json );

                if( response.body() != null ) {
                    restaurants = response.body();
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RestaurantBasicInfo>> call, Throwable t) {

                Toast.makeText(context, "failure", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();
                Log.d( "FAILURE", t.toString() );
                Log.d( "FAILURE", call.toString() );
            }
        });
    }
}
