/*
    Filename: choosePlaces.java
    Author: Bhuwan Shrestha
    StudentID: 8892146
    Date: 13th March, 2024
    Project: Trip Planner Academic Project
    Description: This file contains the Choose Places that helps to populate the places.
 */
package com.example.loginpage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.GridLayoutAnimationController;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class choosePlaces extends AppCompatActivity {

    GridView simpleGrid;
    private List<Place> places = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_places);

        simpleGrid = (GridView) findViewById(R.id.simpleGridView);
        CustomAdapter customAdapter = new CustomAdapter(this, places);
        simpleGrid.setAdapter(customAdapter);
        populatePlacesList();
    }

    /*
    Function Name: populatePlacesList
    Parameters: None
    Return: void
    Purpose: Populates the list of places with predefined Place objects, each representing a specific location with a name, geographical area, and an associated drawable resource identifier for its image.
*/

    private void populatePlacesList() {
        // Add Place objects to the list
        places.add(new Place("Andes Mountain", "America ", R.drawable.andes));
        places.add(new Place("Chitwan", "Nepal", R.drawable.chitwan));
        places.add(new Place("Mount Everest", "Nepal", R.drawable.everest));
        places.add(new Place("Gosaikunda", "Nepal ", R.drawable.gosainkunda));
        places.add(new Place("Kathmandu", "Nepal", R.drawable.kathmandu));
        places.add(new Place("Kitchener", "Canada", R.drawable.kitchener));
        places.add(new Place("Beautiful Nepal", "South Asia", R.drawable.nepal));
        places.add(new Place("Pokhara", "Nepal", R.drawable.pokhara));
        places.add(new Place("Solukhumbu", "Nepal ", R.drawable.solukhumbu));
        places.add(new Place("Tilicho Lake", "Nepal", R.drawable.tilicho_lake));
        places.add(new Place("David Waterfall", "Nepal", R.drawable.waterfall));
        places.add(new Place("Waterloo", "Canada ", R.drawable.andes));

    }
}