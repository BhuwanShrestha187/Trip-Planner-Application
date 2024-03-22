/*
    Filename: DatabaseHelper.java
    Author: Bhuwan Shrestha
    StudentID: 8892146
    Date: 13th March, 2024
    Project: Trip Planner Academic Project
    Description: This file contains the database requuired for the Login and sign Up page.
 */

package com.example.loginpage;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "USER_RECORD";

    private static final String TABLE_NAME = "USER_DATA";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "USERNAME";
    private static final String COL_3 = "EMAIL";
    private static final String COL_4 = "PASSWORD";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT , EMAIL TEXT, PASSWORD TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /*
    Function Name: registerUser
    Parameters: String username, String email, String password
    Return: boolean
    Purpose: Attempts to register a new user by inserting the provided username, email, and password into the database.
             Returns true if the insertion is successful, indicating the user was registered. Returns false if the insertion fails.
*/

    public boolean registerUser(String username, String email, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, username);
        values.put(COL_3, email);
        values.put(COL_4, password);

        //Now we insert the data, insert data returns long type.
        long result = db.insert(TABLE_NAME, null, values);
        if(result == -1)
        {
            return false; //Means insertion failed
        }

        else
        {
            return  true; //Data inserted successfully
        }
    }

    /*
    Function Name: checkUser
    Parameters: String username, String password
    Return: int
    Purpose: Checks if a user with the given username and password exists in the database.
             Returns 0 if a match is found (indicating success), and returns 3 if no match is found (indicating failure).
*/

    public int checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COL_1, COL_2, COL_4}; // Ensure you're selecting the necessary columns.
        String selection = COL_2 + "=?" + " and " + COL_4 + "=?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            // Since you're checking the existence by query, direct comparison is enough, no need to fetch again.
            cursor.close(); // Always close the cursor when done.
            return 0; // Success
        }
        cursor.close(); // Close the cursor to avoid memory leaks.
        return 3; // Failed to find a match.
    }

}
