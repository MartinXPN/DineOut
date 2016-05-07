package com.dineoutmobile.dineout;

import android.app.Application;
import android.content.Context;

import com.dineoutmobile.dineout.util.Util;


public class ApplicationControl extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Util.setLanguage( Util.getLanguage( context ), context );
    }

    public static Context getAppContext() {
        return ApplicationControl.context;
    }
}