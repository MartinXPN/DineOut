package com.dineoutmobile.dineout.util;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.util.TypedValue;
import android.widget.Toast;

public class Util {

    public static final class Tags {

        public static final String SHARED_PREFS_NAME = "cache";
        public static final String SHARED_PREFS_LANGUAGE_LOCALE = "lang_locale";
        public static final String SHARED_PREFS_SHOW_AS_GRID = "show_grid";
        public static final String BUNDLE_RESTAURANT_ID = "rest_id";
        public static final String BUNDLE_RESTAURANT_NAME = "rest_name";
        public static final String BUNDLE_RESTAURANT_COORDINATE_LAT = "rest_cord_lat";
        public static final String BUNDLE_RESTAURANT_COORDINATE_LNG = "rest_cord_lng";
        public static final String RESTAURANT_LIST_FRAGMENT = "rest_grid_f";
        public static final String NEARBY_PLACES_FRAGMENT = "nearby_f";
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


    ///////////////////////////////// URL Helpers //////////////////////////////////////////////////
    public static void openUrlInBrowser( Context context, String url ) {
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
    public static String getImageURL(String imageName) {
        if(imageName == null || imageName.isEmpty())    return "http://dineoutmobile.com/images/placeholder.png";
        else                                            return "http://dineoutmobile.com/images/" + imageName ;
    }
}
