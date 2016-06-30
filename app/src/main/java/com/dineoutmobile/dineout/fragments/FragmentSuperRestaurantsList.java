package com.dineoutmobile.dineout.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.adapters.AdapterSuperRestaurantList;
import com.dineoutmobile.dineout.util.LanguageUtil;

public class FragmentSuperRestaurantsList
        extends     Fragment
        implements  SwipeRefreshLayout.OnRefreshListener {


    SwipeRefreshLayout refreshLayout;
    AdapterSuperRestaurantList adapter;

    private boolean isRefreshLayoutSpinning = false;
    private Menu menu;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu( true );
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
