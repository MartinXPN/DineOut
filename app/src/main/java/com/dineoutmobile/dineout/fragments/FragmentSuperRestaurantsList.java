package com.dineoutmobile.dineout.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.adapters.AdapterSuperRestaurantList;
import com.dineoutmobile.dineout.databasehelpers.DataTransferAPI;
import com.dineoutmobile.dineout.util.LanguageUtil;
import com.dineoutmobile.dineout.util.models.RestaurantBasicInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentSuperRestaurantsList
        extends     Fragment
        implements  SwipeRefreshLayout.OnRefreshListener,
                    AdapterSuperRestaurantList.OnDataRequestedListener {


    SwipeRefreshLayout refreshLayout;
    private String TAG = "FragSuperRestList";
    private boolean isRefreshLayoutSpinning = false;
    private Menu menu;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d( TAG, "onCreate" );
        super.onCreate(savedInstanceState);
        setHasOptionsMenu( true );
//        setRetainInstance( true ); TODO
        loadRestaurants();
    }

    public void setLanguage( LanguageUtil.Language language ) {

        menu.findItem(R.id.action_language).setIcon( language.iconResource );

        if( LanguageUtil.getLanguage( getActivity() ) == language )
            return;
        LanguageUtil.setLanguage( language, getActivity() );

        /// recreate the whole activity
        getActivity().recreate();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate( R.menu.activity_choose_restaurant, menu );
        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);

        /// update current language
        setLanguage( LanguageUtil.getLanguage( getActivity() ) );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if( id == R.id.action_language_arm )    { setLanguage( LanguageUtil.Language.HY );          return true; }
        if( id == R.id.action_language_rus )    { setLanguage( LanguageUtil.Language.RU );          return true; }
        if( id == R.id.action_language_eng )    { setLanguage( LanguageUtil.Language.EN );          return true; }
        return super.onOptionsItemSelected(item);
    }



    //////////////////////////////// GET DATA ///////////////////////////////////////////
    protected LanguageUtil.Language currentLanguage;
    protected AdapterSuperRestaurantList adapter;
    protected ArrayList<RestaurantBasicInfo> restaurants = new ArrayList<>();



    @Override
    public ArrayList<RestaurantBasicInfo> getRestaurants() {
        return restaurants;
    }

    public void loadRestaurants() {

        currentLanguage = LanguageUtil.getLanguage( getActivity() );

        // Retrofit needs to know how to deserialize response, for instance into JSON
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( DataTransferAPI.BASE_URL )
                .addConverterFactory( GsonConverterFactory.create() )
                .build();


        Map<String, String> options = new HashMap<>();
        options.put( "language", LanguageUtil.getLanguage( getActivity() ).locale);
        DataTransferAPI api = retrofit.create(DataTransferAPI.class);
        api.getAllRestaurantsBasicInfo(options).enqueue(new Callback<ArrayList<RestaurantBasicInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<RestaurantBasicInfo>> call, Response<ArrayList<RestaurantBasicInfo>> response) {

//                //// GSON TEST
//                final Gson gson = new GsonBuilder().setPrettyPrinting().create();
//                String json = gson.toJson( response.body() );
//                Log.d( "RESPONSE IN JSON", json );

                if( response.body() != null ) {
                    notifyDataSetChanged( response.body() );
//                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), "Success - null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RestaurantBasicInfo>> call, Throwable t) {

                Toast.makeText(getActivity(), "Failure\n" + t.toString(), Toast.LENGTH_SHORT).show();
                Log.d( "FAILURE", t.toString() );
                Log.d( "FAILURE", call.toString() );
            }
        });
    }


    public void notifyDataSetChanged( ArrayList <RestaurantBasicInfo> restaurants ) {
        this.restaurants = restaurants;
        adapter.notifyDataSetChanged();
    }


    /////////////////////////////// REFRESH /////////////////////////////////////////////
    @Override
    public void onRefresh() {
        updateViewState(ViewState.LOADING, null);
        updateViewState(ViewState.DONE, null);
    }


    private enum ViewState {
        LOADING, DONE, ERROR
    }
    private void updateViewState(ViewState state, String errorMessage) {

        switch (state) {
            case LOADING:
                isRefreshLayoutSpinning = true;
                break;
            case DONE:
                isRefreshLayoutSpinning = false;
                break;
            case ERROR:
                isRefreshLayoutSpinning = false;
                Toast.makeText( getActivity(), errorMessage, Toast.LENGTH_LONG ).show();
                break;
        }
        refreshLayout.setRefreshing( isRefreshLayoutSpinning );
    }
}
