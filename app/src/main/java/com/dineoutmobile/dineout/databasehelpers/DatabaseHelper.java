package com.dineoutmobile.dineout.databasehelpers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dineoutmobile.dineout.Properties;

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
    static String join_names =" left join rest_names on rest_names.id=rest_addr.id";
    static String join_rest_info = " left join rest_info on rest_info.id=rest_info.id";
    static String strComma = ",";
    static int townID = 1;
    static int countryID = 1;



    public void getRestaurantDetails(int position, RestDetails restDetails) {
        SQLiteDatabase db = getReadableDatabase();
        Log.d( Integer.toString(position), "position");
        String columns = "address,phone1,phone2,work_hours," + CalcRestNameColumn() + ",rest_info.website";
        String sql_q = "select " + columns + " from rest_addr "+join_names+join_rest_info +" where uid=" + Integer.toString(position);
        Log.d( sql_q.substring(0, 50), "QUERYYY!!!");
        Log.d( sql_q.substring(50, 100), "QUERYYY!!!");
        Log.d( sql_q.substring(100, 150), "QUERYYY!!!");
        Cursor cursor = db.rawQuery(sql_q, null);
        cursor.moveToFirst();
        SetupRestDetails(cursor, restDetails);
        cursor.close();

    }
    static public void setTownID(int id){
        townID = id;
    }
    static public void setCountryID(int id){
        countryID = id;
    }

    private void SetupRestDetails(Cursor cursor, RestDetails restDetails) {
        int col_address = 0;
        int col_phone1 = 1;
        int col_phone2 = 2;
        int col_worktime = 3;
        int col_website = 4;
        int col_restname = 4;
        restDetails.setAddress(cursor.getString(col_address));
        restDetails.setPhone1(cursor.getString(col_phone1));
        restDetails.setPhone2(cursor.getString(col_phone2));
        restDetails.setWorkTime(cursor.getString(col_worktime));
        restDetails.setWebSite(cursor.getString(col_website));
        restDetails.setAddress(cursor.getString(col_restname));

    }

    String CalcRestNameColumn(){

        return  "name_" + getLanguage();
    }

    public ArrayList<String> getRestaurantNames(ArrayList <Integer> restnum) {

        SQLiteDatabase db = getReadableDatabase();
        String col_id = "uid";
        String col_name = CalcRestNameColumn();
        String columns = col_id + strComma + col_name;
        Cursor cursor = db.rawQuery("SELECT " + columns + " FROM rest_addr "+join_names, null);
        cursor.moveToFirst();

        ArrayList <String> res = new ArrayList<>();
        //ArrayList <Integer> restnum = new ArrayList<>();
        restnum.clear();
        while( !cursor.isAfterLast() ) {
            res.add( cursor.getString( cursor.getColumnIndex( col_name ) ) );
            restnum.add(cursor.getInt(cursor.getColumnIndex(col_id)));
            cursor.moveToNext();
        }
        cursor.close();
        return res;
    }

    public ArrayList<String> getCitiesQuery(int idCountry) {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String langID = getLanguage();
        Cursor res = db.rawQuery("select name_" + langID + " from towns", null);
        int count = res.getCount();
        String ss = Integer.toString(count);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("name_en")));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    public ArrayList<String> getCuisinesQuery(int idCountry) {
        ArrayList<String> array_list = new ArrayList<String>();
        String langID = getLanguage();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select name_" + langID + " from cuisines", null);
        int count = res.getCount();
        String ss = Integer.toString(count);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("name_en")));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    public ArrayList<String> getMusicTypesQuery(int idCountry) {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String langID = getLanguage();
        Cursor res = db.rawQuery("select name_" + langID + " from cuisines", null);
        int count = res.getCount();
        String ss = Integer.toString(count);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("name_en")));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }




    private String getLanguage() {
        //return "en";
        return Properties.getInstance().getLanguage();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private static DatabaseHelper selfReference  = null;
    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1); // 1? its Database Version
        if (android.os.Build.VERSION.SDK_INT >= 17) DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        else                                        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.mContext = context;
    }
    public static DatabaseHelper getInstance() {
        // assert (selfReference != null);

        return selfReference;
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