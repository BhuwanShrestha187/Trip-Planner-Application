/*
    Filename: TripDetailsDatabaseHelper.java
    Author: Bhuwan Shrestha
    StudentID: 8892146
    Date: 13th March, 2024
    Project: Trip Planner Academic Project
    Description: This file contains the Trip Details Database codes.
 */
package com.example.loginpage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TripDetailsDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "TripInfo.db";
    private static  final int DATABASE_VERSION = 1;

    //Trips Table
    // Trips Table
    private static final String TABLE_TRIPS = "trips";
    private static final String ID = "id";
    private static final String START_ADDRESS = "startAddress";
    private static final String DESTINATION_ADDRESS = "destinationAddress";
    private static final String BUDGET = "budget";
    private static final String DURATION = "duration";
    private static final String START_DATE = "startDate";
    private static final String END_DATE = "endDate";
    private static final String TRANSPORT_MODE = "transportMode";
    private static final String PREFERENCES = "preferences";
    public TripDetailsDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*
    Method Name: onCreate
    Parameters: SQLiteDatabase db
    Return: void
    Purpose: Creates the TABLE_TRIPS table in the database with the specified columns: ID (primary key), start address, destination address, budget, duration,
             start date, end date, transport mode, and preferences. Executes the SQL command to create the table.
*/

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TRIPS_TABLE = "CREATE TABLE " + TABLE_TRIPS + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + START_ADDRESS + " TEXT,"
                + DESTINATION_ADDRESS + " TEXT,"
                + BUDGET + " TEXT,"
                + DURATION + " TEXT,"
                + START_DATE + " TEXT,"
                + END_DATE + " TEXT,"
                + TRANSPORT_MODE + " TEXT,"
                + PREFERENCES + " TEXT" + ")";
        db.execSQL(CREATE_TRIPS_TABLE);
    }

    /*
    Method Name: onUpgrade
    Parameters: SQLiteDatabase db, int oldVersion, int newVersion
    Return: void
    Purpose: Handles the database upgrade process. Drops the existing TABLE_TRIPS if it exists and then calls the onCreate method to recreate it with the new schema.
*/

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIPS);
        onCreate(db);
    }

    /*
    Method Name: addTripDetails
    Parameters: String startAddress, String destinationAddress, String budget, String duration, String startDate, String endDate, String transportMode, String preferences
    Return: void
    Purpose: Inserts a new trip record into the TABLE_TRIPS with the provided details.
             Utilizes ContentValues to package the data for insertion. Ensures the database connection is properly closed after the operation.
*/

    public void addTripDetails(String startAddress, String destinationAddress, String budget, String duration, String startDate, String endDate, String transportMode, String preferences) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(START_ADDRESS, startAddress);
        values.put(DESTINATION_ADDRESS, destinationAddress);
        values.put(BUDGET, budget);
        values.put(DURATION, duration);
        values.put(START_DATE, startDate);
        values.put(END_DATE, endDate);
        values.put(TRANSPORT_MODE, transportMode);
        values.put(PREFERENCES, preferences);

        db.insert(TABLE_TRIPS, null, values);
        db.close();
    }

    /*
    Method Name: getAllTrips
    Parameters: None
    Return: List<String>
    Purpose: Retrieves all trips from the database and formats them into a list of strings.
             Each string represents a trip with details including ID, start address, destination address, budget, duration, start date, end date, transport mode, and preferences.
             Ensures the database connection and cursor are properly closed after fetching the data.
*/

    public List<String> getAllTrips() {
        List<String> tripList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TRIPS, new String[] { ID, START_ADDRESS, DESTINATION_ADDRESS, BUDGET, DURATION, START_DATE, END_DATE, TRANSPORT_MODE, PREFERENCES }, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                // Assuming you want to display these specific details. Adjust as needed.
                String trip = "ID: " + cursor.getString(0) + ", Start: " + cursor.getString(1) + ", End: " + cursor.getString(2) + ", Budget: " + cursor.getString(3) + ", Duration: " + cursor.getString(4) + ", Start Date: " + cursor.getString(5) + ", End Date: " + cursor.getString(6) + ", Mode: " + cursor.getString(7) + ", Preferences: " + cursor.getString(8);
                tripList.add(trip);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tripList;
    }

}
