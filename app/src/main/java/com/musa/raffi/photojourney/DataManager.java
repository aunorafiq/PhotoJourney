package com.musa.raffi.photojourney;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Asus on 8/21/2016.
 */

public class DataManager {
    private SQLiteDatabase db;

    public static final String TABLE_ROW_ID = "_id";
    public static final String TABLE_ROW_TITLE = "image_title";
    public static final String TABLE_ROW_URI = "image_uri";

    // version 2
    public static final String TABLE_ROW_LOCATION_LAT = "gps_location_lat";
    public static final String TABLE_ROW_LOCATION_LONG = "gps_location_long";


    private static final String DB_NAME = "wis_db";
    private static final int DB_VERSION = 2;
    private static final String TABLE_PHOTOS = "wis_table_photos";
    private static final String TABLE_TAGS = "wis_table_tags";
    private static final String TABLE_ROW_TAG1 = "tag1";
    private static final String TABLE_ROW_TAG2 = "tag2";
    private static final String TABLE_ROW_TAG3 = "tag3";

    public static final String TABLE_ROW_TAG = "tag";

    public DataManager(Context context){
        Log.d("asdfasf", "DataManager: ");
        CustomSQLiteOpenHelper helper = new CustomSQLiteOpenHelper(context);
        db = helper.getWritableDatabase();

//        (mCursor.getString(mCursor.getColumnIndex(DataManager.TABLE_ROW_URI)));
//        Cursor c = db.rawQuery("SELECT " + TABLE_ROW_ID + ", " + TABLE_ROW_TITLE + " FROM " + TABLE_PHOTOS, null);
//        c.moveToFirst();
//        Log.d("DataManager: ", c.getString(c.getColumnIndex(TABLE_ROW_URI)));

    }

    public void addPhoto(Photo photo){
        String query = "INSERT INTO " + TABLE_PHOTOS + " (" +
                TABLE_ROW_TITLE + ", " +
                TABLE_ROW_URI + ", " +
                TABLE_ROW_LOCATION_LAT + ", " +
                TABLE_ROW_LOCATION_LONG + ", " +
                TABLE_ROW_TAG1 + ", " +
                TABLE_ROW_TAG2 + ", " +
                TABLE_ROW_TAG3 + ") " +
                "VALUES (" +
                "'" + photo.getTitle() + "'" + ", " +
                "'" + photo.getStorageLocation() + "'" + ", " +
                photo.getGpsLocation().getLatitude() + ", " +
                photo.getGpsLocation().getLongitude() + ", " +
                "'" + photo.getTag1() + "'" + ", " +
                "'" + photo.getTag2() + "'" + ", " +
                "'" + photo.getTag3() + "'" +
                ");";
        Log.d("addPhoto: ", query);
        db.execSQL(query);

        query = "INSERT INTO " + TABLE_TAGS + "(" +
                TABLE_ROW_TAG + ") " +
                "SELECT '" + photo.getTag1() + "' " +
                "WHERE NOT EXISTS ( SELECT 1 FROM " +
                TABLE_TAGS +
                " WHERE " + TABLE_ROW_TAG + " = " +
                "'" + photo.getTag1() + "');";
        Log.d("addPhoto 1: ", query);
        db.execSQL(query);

        query = "INSERT INTO " + TABLE_TAGS + "(" +
                TABLE_ROW_TAG + ") " +
                "SELECT '" + photo.getTag2() + "' " +
                " WHERE NOT EXISTS ( SELECT 1 FROM " +
                TABLE_TAGS +
                " WHERE " + TABLE_ROW_TAG + " = " +
                "'" + photo.getTag2() + "');";
        Log.d("addPhoto 1: ", query);
        db.execSQL(query);

        query = "INSERT INTO " + TABLE_TAGS + "(" +
                TABLE_ROW_TAG + ") " +
                "SELECT '" + photo.getTag3() + "' " +
                " WHERE NOT EXISTS ( SELECT 1 FROM " +
                TABLE_TAGS +
                " WHERE " + TABLE_ROW_TAG + " = " +
                "'" + photo.getTag3() + "');";
        Log.d("addPhoto 1: ", query);
        db.execSQL(query);
    }

    public Cursor getTitles(){
        Cursor c = db.rawQuery("SELECT " + TABLE_ROW_ID + ", " + TABLE_ROW_TITLE + " FROM " + TABLE_PHOTOS, null);
        Log.d("getTitles: ", "SELECT " + TABLE_ROW_ID + ", " + TABLE_ROW_TITLE + " FROM " + TABLE_PHOTOS, null);
        c.moveToFirst();

        return c;
    }

    public Cursor getTitlesWithTag(String tag) {
        Cursor c = db.rawQuery("SELECT " + TABLE_ROW_ID + ", " + TABLE_ROW_TITLE +
                        " FROM " + TABLE_PHOTOS +
                        " WHERE " + TABLE_ROW_TAG1 + " = '" + tag + "' OR " + TABLE_ROW_TAG2 + " = '" + tag + "' OR " + TABLE_ROW_TAG3 + " = '" + tag + "';", null);
        c.moveToFirst();
        return c;
    }

    public Cursor getPhoto(int id){
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_PHOTOS + " WHERE " + TABLE_ROW_ID + " = " + id, null);
        c.moveToFirst();
        return c;
    }

    public Cursor getTags(){
        Cursor c = db.rawQuery("SELECT " + TABLE_ROW_ID + ", " + TABLE_ROW_TAG + " FROM " + TABLE_TAGS, null);
        c.moveToFirst();
        return c;
    }

    private class CustomSQLiteOpenHelper extends SQLiteOpenHelper{
        public CustomSQLiteOpenHelper (Context context){
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate (SQLiteDatabase db){
            String newTableQueryString = "create table "
                    + TABLE_PHOTOS + " ("
                    + TABLE_ROW_ID + " integer primary key autoincrement not null, "
                    + TABLE_ROW_TITLE + " text not null, "
                    + TABLE_ROW_URI + " text not null, "
                    + TABLE_ROW_LOCATION_LAT + " real, "
                    + TABLE_ROW_LOCATION_LONG + " real, "
                    + TABLE_ROW_TAG1 + " text not null, "
                    + TABLE_ROW_TAG2 + " text not null, "
                    + TABLE_ROW_TAG3 + " text not null" + ");";

            db.execSQL(newTableQueryString);

            newTableQueryString = "create table "
                    + TABLE_TAGS + " ("
                    + TABLE_ROW_ID
                    + " integer primary key autoincrement not null, "
                    + TABLE_ROW_TAG
                    + " text not null" + ");";
            db.execSQL(newTableQueryString);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            String addLongColumn = "ALTER TABLE "
                    + TABLE_PHOTOS
                    + " ADD "
                    + TABLE_ROW_LOCATION_LONG
                    + " rael;";
            db.execSQL(addLongColumn);

            String addLatColumn = "ALTER TABLE "
                    + TABLE_PHOTOS
                    + " ADD "
                    + TABLE_ROW_LOCATION_LAT
                    + " REAL;";
            db.execSQL(addLatColumn);
        }
    }
}
