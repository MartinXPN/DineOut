package com.dineoutmobile.dineout.util;


import android.content.Context;
import android.content.res.Configuration;

import com.dineoutmobile.dineout.R;

import java.util.HashMap;
import java.util.Locale;

public class LanguageUtil {

    private static HashMap<String, Language> localeToLanguage = new HashMap<>();
    private static Language currentLanguage = null;


    public enum Language {

        HY( "hy", R.mipmap.ic_flag_arm ),
        RU( "ru", R.mipmap.ic_flag_rus ),
        EN( "en", R.mipmap.ic_flag_eng );

        public String locale;
        public int iconResource;

        Language(String locale, int resource ) {
            this.locale = locale;
            this.iconResource = resource;
            localeToLanguage.put( locale, this );
        }
    }

    public static Language getLanguage( String locale ) {

        //Log.d( "Util", localeToLanguage.toString() );
        if( localeToLanguage == null )
            return Language.EN;
        return localeToLanguage.get( locale );
    }
    public static Language getLanguage( Context context ) {

        //Log.d( "Util", localeToLanguage.toString() );

        if( currentLanguage != null )
            return currentLanguage;

        String lang = CacheUtil.getCache( context, Util.Tags.SHARED_PREFS_LANGUAGE_LOCALE, Language.EN.locale);
        return currentLanguage = getLanguage( lang );
    }
    public static void setLanguage(Language currentLanguage, Context context) {

        LanguageUtil.currentLanguage = currentLanguage;

        /// put new language into shared-preferences
        CacheUtil.setCache( context, Util.Tags.SHARED_PREFS_LANGUAGE_LOCALE, currentLanguage.locale);

        /// update the language of the whole application
        Configuration config = context.getResources().getConfiguration();
        Locale locale = new Locale(currentLanguage.locale);
        Locale.setDefault(locale);
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
}
