package com.dineoutmobile.dineout.util;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class Util {

    public static final class Tags {

        public static final String SHARED_PREFS_NAME = "cache";
        public static final String SHARED_PREFS_LANGUAGE_LOCALE = "lang_locale";
        public static final String SHARED_PREFS_SHOW_AS_GRID = "show_grid";
        public static final String SHARED_PREFS_USER_NAME = "user_name";
        public static final String SHARED_PREFS_USER_PHONE = "user_phone";
        public static final String SHARED_PREFS_RESERVE_PEOPLE = "reserve_people";
        public static final String SHARED_PREFS_RESERVE_DATE = "reserve_date";
        public static final String SHARED_PREFS_RESERVE_TIME = "reserve_time";
        public static final String BUNDLE_RESTAURANT_ID = "rest_id";
        public static final String BUNDLE_RESTAURANT_NAME = "rest_name";
        public static final String BUNDLE_RESTAURANT_COORDINATE_LAT = "rest_cord_lat";
        public static final String BUNDLE_RESTAURANT_COORDINATE_LNG = "rest_cord_lng";
        public static final String RESTAURANT_LIST_FRAGMENT = "rest_list_f";
        public static final String RESTAURANT_GRID_FRAGMENT = "rest_grid_f";
        public static final String NEARBY_PLACES_FRAGMENT = "nearby_f";
        public static final String RESERVED_RESTAURANTS_FRAGMENT = "reserved_f";
        public static final String GOOGLE_MAPS_FRAGMENT = "maps_f";
        public static final String SAVED_STATE_FRAGMENT = "saved_f";
        public static final String SAVED_STATE_SEARCH = "saved_s";
    }

    public static int calculateRatingColor( float rating ) {

        /// rating is scaled from 1 to 5 => color has to be scaled accordingly
        /// in HSV Hue is changed from 0 to 120 { 0 = RED, 120 = GREEN }
        return Color.HSVToColor( new float[] {30*rating - 30, 0.55f, 0.9f} );
    }

    public static int dpToPx( float dp, Context context ) {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }
    public static int getWindowWidth( Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }
    public static int getWindowHeight( Context context ) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }


    public static void hideKeyboard( AppCompatActivity activity ) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public static void showKeyboard( AppCompatActivity activity ) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        }
    }



    public static void showDialog( Context context, String title, String message ) {
        new AlertDialog
                .Builder( context, AlertDialog.THEME_DEVICE_DEFAULT_DARK  )
                .setTitle( title )
                .setMessage( message ).show();
    }


    ///////////////////////////////// URL Helpers //////////////////////////////////////////////////
    public static void openUrlInBrowser( Context context, String url ) {
        if( url == null || url.isEmpty() )   return;
        Intent browserIntent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
        context.startActivity( browserIntent );
    }
    public static void writeFeedback( Context context ) {

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_SUBJECT, "DineOut Feedback");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"XPNInc@gmail.com"});
        try {
            context.startActivity(Intent.createChooser(i, "Choose an Email client:"));
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "There are no Email applications installed", Toast.LENGTH_SHORT).show();
        }
    }


    public static boolean isNetworkAvailable( Context context ) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
