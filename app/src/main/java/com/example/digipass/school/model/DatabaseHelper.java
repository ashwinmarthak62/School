package com.example.veraki.school.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by veraki on 9/7/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "school.db";
    SQLiteDatabase db;
    private static final String TABLE_PRODUCTS= "products";
    private static final String TABLE_NEWS = "newsletters";
    private static final String TABLE_ALERTS = "alerts";
    private static final String TABLE_CALENDAR = "calendar";

    private static final String TABLE_CREATE_ALERTS= "create table alerts (alert_id integer primary key not null,alert_date datetime not null , alert_message text not null);";
    private static final String TABLE_CREATE_NEWSLETTER= "create table newsletters (issue_no integer primary key not null,link text not null , preview text not null);";
    private static final String TABLE_CREATE_PRODUCTS= "create table products (product_id integer primary key not null,product_name text not null,product_price decimal not null,product_description text not null,created_at datetime not null , updated_at datetime not null);";
    private static final String TABLE_CREATE_CALENDAR= "create table calendar (id integer primary key not null,calendar_event text not null , calendar_date date not null);";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_NEWSLETTER);
        db.execSQL(TABLE_CREATE_PRODUCTS);
        db.execSQL(TABLE_CREATE_ALERTS);
        db.execSQL(TABLE_CREATE_CALENDAR);
        this.db = db;
    }

    public void insertNews(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues v = new ContentValues();
        v.put("issue_no", queryValues.get("issue_no"));
        v.put("preview", queryValues.get("preview"));
        v.put("link", queryValues.get("link"));
        try{
        database.insert("newsletters", null, v);
        }catch(SQLiteConstraintException e) {
            Log.e("SQL insert error","****problem occurred while inserting newsletters"+e.getMessage());
        }
        database.close();
    }
    public void insertCalendar(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id", queryValues.get("id"));
        values.put("calendar_event", queryValues.get("calendar_event"));
        values.put("calendar_date", queryValues.get("calendar_date"));
        try {
            database.insert("calendar", null, values);
        }catch(SQLiteConstraintException e) {
            Log.e("SQL insert error","****problem occurred while inserting calendar"+e.getMessage());
        }
            database.close();
    }

    public void insertAlerts(HashMap<String, String> alarm) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("alert_id",alarm.get("alert_id"));
        values.put("alert_date", alarm.get("alert_date"));
        values.put("alert_message", alarm.get("alert_message"));
        try {
            database.insertOrThrow("alerts", null, values);
        }catch(SQLiteConstraintException e) {
            Log.e("SQL insert error","****problem occurred while inserting alerts"+e.getMessage());
        }
        database.close();
    }

    public void insertProducts(HashMap<String, String> alertValues) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put("product_id",alertValues.get("product_id"));
        value.put("product_name", alertValues.get("product_name"));
        value.put("product_price",alertValues.get("product_price"));
        value.put("product_description", alertValues.get("product_description"));
        value.put("created_at", alertValues.get("created_at"));
        value.put("updated_at", alertValues.get("updated_at"));
        try{
        database.insertOrThrow("products", null, value);
        }catch(SQLiteConstraintException e){
           Log.e("SQL insert error","****problem occurred while inserting products"+e.getMessage());
        }
        database.close();
    }

    public ArrayList<HashMap<String, String>> getAllNewsLetters() {
        ArrayList<HashMap<String, String>> newList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "select  preview,link from " + TABLE_NEWS;
        SQLiteDatabase dbase = this.getWritableDatabase();
        Cursor cursor = dbase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("preview", cursor.getString(0));
                map.put("link", cursor.getString(1));
                newList.add(map);
            } while (cursor.moveToNext());
        }
        dbase.close();
        return newList;
    }

    public ArrayList<HashMap<String, String>> getAlerts() {
        ArrayList<HashMap<String, String>> warnList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "select  alert_date,alert_message from " + TABLE_ALERTS;
        SQLiteDatabase dbse = this.getWritableDatabase();
        Cursor cursor = dbse.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> mapAlert = new HashMap<String, String>();
                mapAlert.put("alert_date", cursor.getString(0));
                mapAlert.put("alert_message", cursor.getString(1));
                warnList.add(mapAlert);
            } while (cursor.moveToNext());
        }
        dbse.close();
        return warnList;
    }

    public ArrayList<HashMap<String, String>> getSchoolCalendar() {
        ArrayList<HashMap<String, String>> calendarList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "select  calendar_event,calendar_date from " + TABLE_CALENDAR;
        SQLiteDatabase dbs = this.getWritableDatabase();
        Cursor cursor = dbs.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> mapCal = new HashMap<String, String>();
                mapCal.put("calendar_event", cursor.getString(0));
                mapCal.put("calendar_date", cursor.getString(1));
                calendarList.add(mapCal);
            } while (cursor.moveToNext());
        }
        dbs.close();
        return calendarList;
    }

    public ArrayList<HashMap<String, String>> getProductDetails() {
        ArrayList<HashMap<String, String>> productList = new ArrayList<HashMap<String, String>>();
        String getQuery = "select product_name,product_description from " + TABLE_PRODUCTS;
        SQLiteDatabase dbe = this.getWritableDatabase();
          Cursor cursor = dbe.rawQuery(getQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("product_name", cursor.getString(0));
                map.put("product_description", cursor.getString(1));
                productList.add(map);
            } while (cursor.moveToNext());
        }
        dbe.close();
        return productList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String newsQuery = "DROP TABLE IF EXISTS " + TABLE_CREATE_NEWSLETTER;
        String advertsQuery = "DROP TABLE IF EXISTS " + TABLE_CREATE_PRODUCTS;
        String alertQuery = "DROP TABLE IF EXISTS " + TABLE_CREATE_ALERTS;
        String calendarQuery = "DROP TABLE IF EXISTS " + TABLE_CREATE_CALENDAR;

        db.execSQL(newsQuery);
        db.execSQL(advertsQuery);
        db.execSQL(alertQuery);
        db.execSQL(calendarQuery);
        this.onCreate(db);

    }
}
