package com.dineoutmobile.dineout.databasehelpers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dineoutmobile.dineout.util.Address;
import com.dineoutmobile.dineout.util.RestaurantBasicInfo;
import com.dineoutmobile.dineout.util.RestaurantFullInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


/**
 * Single-tone database helper for making experiments
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
    private static String DB_PATH = "";
    private static String DB_NAME ="restaurants.db";
    private SQLiteDatabase mDataBase;
    private final Context mContext;


    private static final class Columns {
        public static final String NAME_ID = "rest_names.id";
        public static final String INFO_ID = "rest_info.id";
        public static final String ADDRESS_ID_OF_RESTAURANT = "rest_addr.id";
        public static final String ADDRESS_ID = "uid";
        public static final String NAME = "name_";
        public static final String RATING = "rating";
        public static final String ADDRESS_NAME = "address";
        public static final String LOGO_URL = "logo_file";
        public static final String BACKGROUND_PHOTO_URL = "rest_info.background_file";
        public static final String ALL_BACKGROUNDS = "rest_addr.background_file";
        public static final String SHORT_INFO = "shortinfo";
        public static final String PHONE_NUMBER = "phone1";
        public static final String WORK_HOURS = "work_hours";
        public static final String MUSIC = "id_musictype";      /// hope this will be changed
        public static final String CUISINES = "id_cuisines";    /// hope this will be changed
        public static final String MENU_URL = "menuurl";
        public static final String WEBSITE_URL = "website";
        public static final String WIFI = "wifi";
        public static final String PRIVATE_ROOMS = "cubicles";
        public static final String FOURSHET = "furshet";
        public static final String SHIPPING = "shipping";
        public static final String CREDIT_CARD = "acceptcards";
        public static final String PARKING = "parking";
        public static final String OUTSIDE_SEATING = "inoutside";
        public static final String SMOKING_AREAS = "smoking";
        public static final String SMOKE_FREE_AREAS = "nosmoking";
    }
    private static final class Tables {
        public static final String ADDRESS = "rest_addr";
        public static final String INFO = "rest_info";
        public static final String RESTAURANT_NAMES = "rest_names";
    }


    public void getRestaurantFullInfo( String language, RestaurantFullInfo restaurant ) {

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " +
                Columns.NAME + language + "," +
                Columns.RATING + "," +
                Columns.LOGO_URL + "," +
                Columns.BACKGROUND_PHOTO_URL + "," +
                Columns.SHORT_INFO + "," +
                Columns.PHONE_NUMBER + "," +
                Columns.WORK_HOURS + "," +
                Columns.MUSIC + "," +
                Columns.CUISINES + "," +
                Columns.MENU_URL + "," +
                Columns.WEBSITE_URL + "," +
                Columns.WIFI + "," +
                Columns.PRIVATE_ROOMS + "," +
                Columns.FOURSHET + "," +
                Columns.SHIPPING + "," +
                Columns.CREDIT_CARD + "," +
                Columns.PARKING + "," +
                Columns.OUTSIDE_SEATING + "," +
                Columns.SMOKING_AREAS + "," +
                Columns.SMOKE_FREE_AREAS +

                " FROM " + Tables.ADDRESS +
                " LEFT JOIN " + Tables.INFO + " ON " + Columns.ADDRESS_ID_OF_RESTAURANT + "=" + Columns.INFO_ID +
                " LEFT JOIN " + Tables.RESTAURANT_NAMES + " ON " + Columns.ADDRESS_ID_OF_RESTAURANT + "=" + Columns.NAME_ID +
                " WHERE " +  Columns.ADDRESS_ID + "=" + restaurant.currentAddress.id + ";";

        Log.d( "RestaurantCurrentAddr", "" + restaurant.currentAddress.id );

        Cursor cursor = db.rawQuery( query, null );
        if( cursor.moveToFirst() ) {
            Log.d( "DatabaseHelper", "data is loading" );
            restaurant.name = cursor.getString( cursor.getColumnIndex( Columns.NAME + language ) );
            restaurant.rating = cursor.getFloat( cursor.getColumnIndex( Columns.RATING ) );
            restaurant.logoURL = cursor.getString( cursor.getColumnIndex( Columns.LOGO_URL ) );
            restaurant.backgroundPhotoURL = cursor.getString( cursor.getColumnIndex( Columns.BACKGROUND_PHOTO_URL ) );
            restaurant.shortInfo = cursor.getString( cursor.getColumnIndex( Columns.SHORT_INFO ) );
            restaurant.phoneNumber = cursor.getString( cursor.getColumnIndex( Columns.PHONE_NUMBER) );
            RestaurantFullInfo.BasicInfo.WORKING_HOURS.description = cursor.getString( cursor.getColumnIndex( Columns.WORK_HOURS ) );
            RestaurantFullInfo.BasicInfo.MUSIC.description = cursor.getString( cursor.getColumnIndex( Columns.MUSIC ) );
            RestaurantFullInfo.BasicInfo.CUISINE.description = cursor.getString( cursor.getColumnIndex( Columns.CUISINES ) );
            RestaurantFullInfo.BasicInfo.MENU.description = cursor.getString( cursor.getColumnIndex( Columns.MENU_URL ) );
            RestaurantFullInfo.BasicInfo.WEBSITE.description = cursor.getString( cursor.getColumnIndex( Columns.WEBSITE_URL ) );
            RestaurantFullInfo.Services.WIFI.supported = cursor.getInt( cursor.getColumnIndex( Columns.WIFI ) ) != 0;
            RestaurantFullInfo.Services.PRIVATE_ROOMS.supported = cursor.getInt( cursor.getColumnIndex( Columns.PRIVATE_ROOMS ) ) != 0;
            RestaurantFullInfo.Services.FOURSHET.supported = cursor.getInt( cursor.getColumnIndex( Columns.FOURSHET ) ) != 0;
            RestaurantFullInfo.Services.SHIPPING.supported = cursor.getInt( cursor.getColumnIndex( Columns.SHIPPING ) ) != 0;
            RestaurantFullInfo.Services.CREDIT_CARD.supported = cursor.getInt( cursor.getColumnIndex( Columns.CREDIT_CARD ) ) != 0;
            RestaurantFullInfo.Services.PARKING.supported = cursor.getInt( cursor.getColumnIndex( Columns.PARKING ) ) != 0;
            RestaurantFullInfo.Services.OUTSIDE_SEATING.supported = cursor.getInt( cursor.getColumnIndex( Columns.OUTSIDE_SEATING ) ) != 0;
            RestaurantFullInfo.Services.SMOKING_AREAS.supported = cursor.getInt( cursor.getColumnIndex( Columns.SMOKING_AREAS ) ) != 0;
            RestaurantFullInfo.Services.SMOKE_FREE_AREAS.supported = cursor.getInt( cursor.getColumnIndex( Columns.SMOKE_FREE_AREAS ) ) != 0;
            Log.d( "Name", restaurant.name );
            Log.d( "Rating", ""+restaurant.rating );
        }
        cursor.close();
    }
    public void getRestaurantAllAddresses(RestaurantFullInfo restaurantInfo) {

        SQLiteDatabase db = getReadableDatabase();
        String query =
                "SELECT " +
                        Columns.ADDRESS_ID + "," +
                        Columns.ADDRESS_NAME +
                        " FROM " + Tables.ADDRESS +
                        " WHERE " + Columns.ADDRESS_ID_OF_RESTAURANT + "=" + restaurantInfo.id + ";";


        Cursor cursor = db.rawQuery( query , null );
        if( cursor.moveToFirst() ) {
            int count = 0;
            while (!cursor.isAfterLast()) {

                Address address = new Address();
                address.id = cursor.getLong(cursor.getColumnIndex(Columns.ADDRESS_ID));
                address.name = cursor.getString(cursor.getColumnIndex(Columns.ADDRESS_NAME));
                restaurantInfo.allAddresses.add(address);
                if( ++count == 3 )break;
            }
        }

        cursor.close();
    }

    public ArrayList <RestaurantBasicInfo> getAllRestaurantsBasicInfo( String language ) {

        ArrayList <RestaurantBasicInfo> res = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query =
                "SELECT " +
                        Columns.INFO_ID + "," +
                        Columns.NAME + language + "," +
                        Columns.RATING + "," +
                        Columns.LOGO_URL + "," +
                        Columns.BACKGROUND_PHOTO_URL +

                " FROM " + Tables.RESTAURANT_NAMES +
                " JOIN " + Tables.INFO + " ON " + Columns.NAME_ID + "=" + Columns.INFO_ID + ";";


        Cursor cursor = db.rawQuery( query, null );
        if( cursor.moveToFirst() ) {
            while (!cursor.isAfterLast()) {

                RestaurantBasicInfo restaurant = new RestaurantBasicInfo();
                restaurant.id = cursor.getInt(cursor.getColumnIndex(Columns.INFO_ID));
                restaurant.name = cursor.getString(cursor.getColumnIndex(Columns.NAME + language));
                restaurant.rating = cursor.getFloat(cursor.getColumnIndex(Columns.RATING));
                restaurant.logoURL = cursor.getString(cursor.getColumnIndex(Columns.LOGO_URL));
                restaurant.backgroundPhotoURL = cursor.getString(cursor.getColumnIndex(Columns.BACKGROUND_PHOTO_URL));

                res.add(restaurant);
                cursor.moveToNext();
            }
        }

        cursor.close();
        return res;
    }





    ////////////////////////////////////////////////////////////////////////////////////////////////
    private static DatabaseHelper selfReference  = null;
    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1); // 1? its Database Version
        if (android.os.Build.VERSION.SDK_INT >= 17) DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        else                                        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.mContext = context;
    }

    public static DatabaseHelper getInstance(Context context) {
        if (selfReference == null) {
            selfReference = new DatabaseHelper(context);
        }
        return selfReference;
    }
    public void createDataBase() throws IOException
    {
        boolean mDataBaseExist = checkDataBase();
        if(!mDataBaseExist)
        {
            this.getReadableDatabase();
            this.close();

            try {
                copyDataBase();
            }
            catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    //Check that the database exists here: /data/data/your package/databases/Da Name
    public boolean checkDataBase()
    {
        File dbFile = new File(DB_PATH + DB_NAME);
        //Log.v("dbFile", dbFile + "   "+ dbFile.exists());
        return dbFile.exists();
    }

    //Copy the database from assets
    private void copyDataBase() throws IOException
    {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer))>0)
        {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    //Open the database, so we can query it
    public boolean openDataBase() throws SQLException
    {
        String mPath = DB_PATH + DB_NAME;
        //Log.v("mPath", mPath);
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        //mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return mDataBase != null;
    }

    @Override
    public synchronized void close()
    {
        if(mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}