package com.dineoutmobile.dineout;

/**
 * Created by hhovik on 4/16/16.
 */
public class Properties  {
    private String language;

    public String getLanguage() {
        if(language.isEmpty()) {
            return "en";
        }
        return language;
    }

    public void setLanguage(String l) {
        this.language = l;
    }


    private static Properties mInstance= null;

    protected Properties(){
        language = new String();
    }

    public static synchronized Properties getInstance(){
        if(null == mInstance){
            mInstance = new Properties();
        }
        return mInstance;
    }

}