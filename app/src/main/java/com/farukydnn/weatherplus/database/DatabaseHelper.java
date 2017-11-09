package com.farukydnn.weatherplus.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.farukydnn.weatherplus.modelview.CityWeatherModelView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();

    private static DatabaseHelper dbInstance;

    private static final String DATABASE_NAME = "cityDB";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CITIES = "cities";
    private static final String ROW_ID = "_id";
    private static final String ROW_DATA = "data";

    public static final int CURRENT_LOCATION_CURSOR_INDEX = 1;
    private static int CURSOR_INDEX = CURRENT_LOCATION_CURSOR_INDEX;


    public static synchronized DatabaseHelper getInstance(Context contex) {
        if (dbInstance == null) {
            dbInstance = new DatabaseHelper(contex);
        }

        return dbInstance;
    }

    public static synchronized int getCursorIndex() {
        return ++CURSOR_INDEX;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_CITIES
                + "("
                + ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ROW_DATA + " TEXT NOT NULL"
                + ");";

        Log.d(TAG, "onCreate: SQL = " + sql);

        db.execSQL(sql);

        sql = "INSERT INTO " + TABLE_CITIES
                + "(" + ROW_DATA + ")"
                + " VALUES ('-1');";

        Log.d(TAG, "onCreate: SQL = " + sql);

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = "onUpgare: SQL = DROP TABLE IF EXISTS " + TABLE_CITIES;

        Log.d(TAG, "onUpgarade: SQL = " + sql);

        db.execSQL(sql);

        onCreate(db);
    }

    public synchronized void addCity(final CityWeatherModelView city) {
        Log.d(TAG, "Creating new city record on db");

        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(ROW_DATA, new GsonBuilder().create().toJson(city));

        db.insert(TABLE_CITIES, null, cv);

        db.close();
    }

    public synchronized void updateCity(final CityWeatherModelView city) {
        Log.d(TAG, "Updating an existing city data on db " + city.getCityName());

        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(ROW_DATA, new GsonBuilder().create().toJson(city));

        String cursorIndex = Integer.toString(city.getCursorIndex());

        db.update(TABLE_CITIES, cv, ROW_ID + " = ?", new String[]{cursorIndex});

        db.close();
    }

    public synchronized void deleteCity(final CityWeatherModelView city) {
        Log.d(TAG, "Deleting city data from db " + city.getCityName());

        SQLiteDatabase db = getWritableDatabase();

        String cursorIndex = Integer.toString(city.getCursorIndex());

        db.delete(TABLE_CITIES, ROW_ID + " = ?", new String[]{cursorIndex});

        db.close();
    }

    public synchronized void restoreCity(final CityWeatherModelView city) {
        Log.d(TAG, "Restore deleted city from db " + city.getCityName());

        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(ROW_ID, city.getCursorIndex());
        cv.put(ROW_DATA, new GsonBuilder().create().toJson(city));

        db.insert(TABLE_CITIES, null, cv);
        db.close();
    }

    public synchronized void fillCityList(final List<CityWeatherModelView> weatherList) {

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_CITIES, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            if (!cursor.getString(1).equals("-1")) {
                Gson gson = new GsonBuilder().create();
                CityWeatherModelView model =
                        gson.fromJson(cursor.getString(1), CityWeatherModelView.class);

                CURSOR_INDEX = model.getCursorIndex();

                weatherList.add(model);
            }
        }

        cursor.close();
        db.close();
    }
}
