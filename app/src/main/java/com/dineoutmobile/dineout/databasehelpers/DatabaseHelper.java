package com.dineoutmobile.dineout.databasehelpers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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


    public void getRestaurantFullInfo( String language, RestaurantFullInfo restaurant ) {

        SQLiteDatabase db = getReadableDatabase();
        String columns = "id,name_"+language +
                "," +
                "rating,address,logo_file,shortinfo,phone1,phone2,phone3, work_hours,";
        columns +=       "id_musictype, id_cuisines, menuurl, website,wifi, cubicles, furshet, shipping, acceptcards, "
                +        "parking, inoutside, smoking, nosmoking,rest_info.background_file";
        String query = "select " + columns + "  from rest_addr " +
                "left join rest_info on  rest_addr.id=rest_info.id " +
                "left join rest_names on  rest_addr.id=rest_names.id " +
                "where rest_addr.uid=26";
        Cursor cursor = db.rawQuery( "SELECT name_" + language + " FROM rest_name", null );
        cursor.moveToFirst();

        if( !cursor.isAfterLast() ) {
            restaurant.name = cursor.getString( cursor.getColumnIndex( "name_" + language ) );
            cursor.moveToNext();
        }
        cursor.close();
    }

    public ArrayList <RestaurantBasicInfo> getAllRestaurantsBasicInfo( String language ) {

        ArrayList <RestaurantBasicInfo> res = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String col_id = "id";
        String col_name = "name_"+language;
        String col_rating = "rating";
        String col_logo_file = "logo_file";
        String col_background = "background_file";
        String columns = col_id+"," + col_name +"," +col_rating +"," + col_logo_file +"," + col_background;
                String joinRestInfo = "join rest_info on rest_names.id=rest_info.id";
        Cursor cursor = db.rawQuery( "SELECT " + columns + " FROM rest_names" + joinRestInfo, null );
        cursor.moveToFirst();

        while( !cursor.isAfterLast() ) {

            RestaurantBasicInfo restaurant = new RestaurantBasicInfo();
            restaurant.id = cursor.getInt(cursor.getColumnIndex(col_id));
            restaurant.name = cursor.getString( cursor.getColumnIndex( col_name ) );
            restaurant.rating = cursor.getFloat( cursor.getColumnIndex( col_rating ) );
            restaurant.logoURL = cursor.getString( cursor.getColumnIndex( col_logo_file ) );
            restaurant.backgroundPhotoURL = cursor.getString( cursor.getColumnIndex( col_background ) );

            res.add( restaurant );
            cursor.moveToNext();
        }

        cursor.close();
        return res;
    }



    public void loadAllAdresses(long id, String language, RestaurantFullInfo info) {
        String columns  = "uid,address";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery( "SELECT " + columns + " FROM rest_addr where id=" + Long.toString(info.id) , null );
        cursor.moveToFirst();

        while( !cursor.isAfterLast() ) {
            info.allAddresses.add(cursor.getString( cursor.getColumnIndex( "address")));
            info.uids.add(cursor.getLong(cursor.getColumnIndex( "uid" )));
        }
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