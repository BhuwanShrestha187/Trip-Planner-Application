/*
    Filename: TripActivitiesHelper.java
    Author: Bhuwan Shrestha
    StudentID: 8892146
    Date: 13th March, 2024
    Project: Trip Planner Academic Project
    Description: This file contains the Trip Activities Database.
 */
package com.example.loginpage;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TripActivitiesDatabaseHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "TripActivities.db";
    private static  final int DATABASE_VERSION = 1;

    //Trips Table
    // Trips Table
    private static final String TABLE_ACTIVITIES = "table_activities";
    private static final String ID = "id";
    private static final String ACTIVITIES = "activities";
    private static final String ADVENTURES = "adventures";
    private static final String PERCENTAGE = "percentage";

    public TripActivitiesDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*
    Method Name: onCreate
    Parameters: SQLiteDatabase db
    Return: void
    Purpose: Creates the TABLE_ACTIVITIES table in the database with the specified columns: ID (primary key), activities, adventures, and percentage.
             Executes the SQL command to create the table.
*/

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ACTIVITIES_TABLE = "CREATE TABLE " + TABLE_ACTIVITIES + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ACTIVITIES + " TEXT,"
                + ADVENTURES + " TEXT,"
                + PERCENTAGE + " TEXT" + ")";
        db.execSQL(CREATE_ACTIVITIES_TABLE);
    }

    /*
    Method Name: onUpgrade
    Parameters: SQLiteDatabase db, int oldVersion, int newVersion
    Return: void
    Purpose: Handles the database upgrade process. Drops the existing TABLE_ACTIVITIES if it exists and then calls the onCreate method to recreate it with the new schema.
*/

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITIES);
        onCreate(db);
    }

    /*
    Method Name: addDetails
    Parameters: String activities, String adventures, String percentage
    Return: void
    Purpose: Inserts a new row into the TABLE_ACTIVITIES with the provided activities, adventure type, and preference percentage.
             Utilizes ContentValues to package the data for insertion. Ensures the database connection is properly closed after the operation.
*/

    public void addDetails(String activities, String adventures, String percentage) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ACTIVITIES, activities); // Concatenated string of selected activities
        values.put(ADVENTURES, adventures); // Adventure type, assuming mapped from RadioGroup
        values.put(PERCENTAGE, percentage); // Preference percentage as a string

        db.insert(TABLE_ACTIVITIES, null, values);
        db.close(); // Closing database connection
    }

    /*
    Method Name: getDetails
    Parameters: None
    Return: List<String>
    Purpose: Retrieves and formats details of activities from the database into a list of strings. Each string represents a row in the TABLE_ACTIVITIES,
             formatted to include the ID, activities, adventure type, and preference percentage. In case of a query failure, returns an empty list.
             Ensures the database connection and cursor are properly closed after fetching the data.
*/

    public List<String> getDetails() {
        List<String> detailsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_ACTIVITIES;
        Cursor cursor;

        try {
            cursor = db.rawQuery(selectQuery, null);
        } catch (SQLException e) {
            return detailsList; // Return an empty list in case of a query failure
        }

        if (cursor.moveToFirst()) {
            do {
                // Formatting each row's data into a string
                @SuppressLint("Range") String detail = "ID: " + cursor.getInt(cursor.getColumnIndex(ID)) +
                        ", Activities: " + cursor.getString(cursor.getColumnIndex(ACTIVITIES)) +
                        ", Adventure: " + cursor.getString(cursor.getColumnIndex(ADVENTURES)) +
                        ", Preference Percentage: " + cursor.getString(cursor.getColumnIndex(PERCENTAGE));
                detailsList.add(detail);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close(); // Closing database connection
        return detailsList;
    }


}
