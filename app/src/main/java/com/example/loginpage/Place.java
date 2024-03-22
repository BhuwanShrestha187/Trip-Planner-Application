/*
    Filename: Place.java
    Author: Bhuwan Shrestha
    StudentID: 8892146
    Date: 13th March, 2024
    Project: Trip Planner Academic Project
    Description: This file contains the rewquired places for the project.
 */
package com.example.loginpage;

public class Place {
    private String name;
    private String location;
    private int imageResourceId; // Drawable resource ID for the place's image

    public Place(String name, String location, int imageResourceId) {
        this.name = name;
        this.location = location;
        this.imageResourceId = imageResourceId;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}
