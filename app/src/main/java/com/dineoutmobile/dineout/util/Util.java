package com.dineoutmobile.dineout.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;

import com.dineoutmobile.dineout.R;

import java.util.HashMap;
import java.util.Locale;

public class Util {

    public static int calculateRatingColor( float rating ) {

        /// rating is scaled from 1 to 5 => color has to be scaled accordingly
        /// in HSV Hue is changed from 0 to 120 { 0 = RED, 120 = GREEN }
        return Color.HSVToColor( new float[] {30*rating - 30, 0.55f, 0.9f} );
    }

    public static final class Tags {

        public static final String SHARED_PREFS_NAME = "cache";
        public static final String SHARED_PREFS_LANGUAGE_LOCALE = "lang_locale";
        public static final String SHARED_PREFS_SHOW_AS_GRID = "show_grid";
        public static final String BUNDLE_RESTAURANT_ID = "rest_id";
        public static final String RESTAURANT_LIST_FRAGMENT = "rest_grid_f";
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


    ////////////////////////////////// LANGUAGE ////////////////////////////////////////////////////
    public enum Language {

        HY( "hy", R.mipmap.ic_flag_arm ),
        RU( "ru", R.mipmap.ic_flag_rus ),
        EN( "en", R.mipmap.ic_flag_eng );

        public String languageLocale;
        public int iconResource;

        Language(String languageLocale, int resource ) {
            this.languageLocale = languageLocale;
            this.iconResource = resource;
            localeToLanguage.put( languageLocale, this );
        }
    }


    private static HashMap <String, Language> localeToLanguage = new HashMap<>();
    private static Language currentLanguage = null;

    public static Language getLanguage( String locale ) {

        Log.d( "Util", localeToLanguage.toString() );
        if( localeToLanguage == null )
            return Language.EN;
        return localeToLanguage.get( locale );
    }
    public static Language getLanguage( Context context ) {

        Log.d( "Util", localeToLanguage.toString() );

        if( currentLanguage != null )
            return currentLanguage;

        SharedPreferences sp = context.getSharedPreferences(Tags.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        String lang = sp.getString( Tags.SHARED_PREFS_LANGUAGE_LOCALE, Language.EN.languageLocale );
        return currentLanguage = getLanguage( lang );
    }
    public static void setLanguage(Language currentLanguage, Context context) {

        Util.currentLanguage = currentLanguage;

        /// put new language into shared-preferences
        SharedPreferences sp = context.getSharedPreferences(Util.Tags.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString( Util.Tags.SHARED_PREFS_LANGUAGE_LOCALE, currentLanguage.languageLocale );
        editor.apply();

        /// update the language of the whole application
        Configuration config = context.getResources().getConfiguration();
        Locale locale = new Locale(currentLanguage.languageLocale);
        Locale.setDefault(locale);
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
}
