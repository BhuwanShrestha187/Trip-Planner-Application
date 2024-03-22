/*
    Filename: chooseActivities.java
    Author: Bhuwan Shrestha
    StudentID: 8892146
    Date: 13th March, 2024
    Project: Trip Planner Academic Project
    Description: This file contains the Choose Activities Page.
 */
package com.example.loginpage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class chooseActivities extends AppCompatActivity {

    private LinearLayout linearLayoutActivitiesList;
    private final List<String> selectedActivities = new ArrayList<>();
    private Button btnSaveActivities;
    RadioGroup radioGroup;
    SeekBar seekBar;
    TextView seekBarPercentage;
    private SharedPreferences sharedPreferences;
    TripActivitiesDatabaseHelper tripActivitiesDatabaseHelper;




    /*
    Function Name: onCreate
    Parameters: Bundle savedInstanceState
    Return: void
    Purpose: Initializes the activity, sets the content view, and configures UI components like buttons, seekBar, and radioGroup.
             It also initializes the database helper and calls methods to populate the activities list, set up the save button, and configure the seekBar.
*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_activities);

        btnSaveActivities = findViewById(R.id.buttonSaveActivities);
        radioGroup = findViewById(R.id.radioGroupActivityType);
        seekBar = findViewById(R.id.seekBarPreference);
        seekBarPercentage = findViewById(R.id.textViewPreference);
        linearLayoutActivitiesList = findViewById(R.id.linearLayoutActivitiesList);

        tripActivitiesDatabaseHelper = new TripActivitiesDatabaseHelper(this);

        populateActivitiesList();
        setupSaveButton();
        setUpSeekBar();

    }

    /*
    Function Name: populateActivitiesList
    Parameters: None
    Return: void
    Purpose: Dynamically generates and adds CheckBoxes for activity options to the linearLayoutActivitiesList.
             It updates the list of selected activities based on the user's checkbox interactions.
*/

    private void populateActivitiesList() {
        String[] activities = {"Hiking", "Museum", "Beach", "City Tour", "Mountain Biking"};
        for (String activity : activities) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(activity);
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    // Add the activity to the list if checked
                    selectedActivities.add(activity);
                } else {
                    // Remove the activity from the list if unchecked
                    selectedActivities.remove(activity);
                }
            });
            linearLayoutActivitiesList.addView(checkBox);
        }
    }

    /*
    Function Name: setUpSeekBar
    Parameters: None
    Return: void
    Purpose: Configures the seekBar's behavior, updating the seekBarPercentage TextView with the current progress value as a percentage when the user adjusts the seekBar.
*/

    private void setUpSeekBar()
    {
        final String initialText = seekBarPercentage.getText().toString();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarPercentage.setText(initialText + " " + progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /*
    Function Name: isRadioButtonSelected
    Parameters: RadioGroup radioGroup
    Return: boolean
    Purpose: Checks if any RadioButton within the provided RadioGroup is selected and returns true if so; otherwise, returns false.
*/

    private boolean isRadioButtonSelected(RadioGroup radioGroup)
    {
        return radioGroup.getCheckedRadioButtonId() != -1;
    }

    /*
    Function Name: setupSaveButton
    Parameters: None
    Return: void
    Purpose: Sets up the onClickListener for btnSaveActivities. Validates that at least one activity is selected and an activity type is chosen before proceeding.
             It displays a toast message if no activity or type is selected. On successful validation, it saves the selected activities, activity type,
             and preference percentage to the database and navigates to the next screen.
*/

    private void setupSaveButton() {
        btnSaveActivities.setOnClickListener(v -> {

            if(selectedActivities.isEmpty())
            {
                Toast.makeText(this, "No adventures selected", Toast.LENGTH_SHORT).show();
            }
            else if (!isRadioButtonSelected(radioGroup)) {
                Toast.makeText(this, "No activities selected", Toast.LENGTH_SHORT).show();
            }

            else {

                // For demonstration, showing selected activities in a Toast
                Toast.makeText(this, "Selected Activities: " + String.join(", ", selectedActivities), Toast.LENGTH_LONG).show();
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedId);
                String activityType = selectedRadioButton.getText().toString(); // Assuming you want the text value of the RadioButton for activity type

                // Get the preference percentage from SeekBar
                int preferencePercentage = seekBar.getProgress();

                // Convert List of selected activities to a comma-separated String
                String selectedActivitiesStr = String.join(", ", selectedActivities);

                // Insert the data into the database
                tripActivitiesDatabaseHelper.addDetails(selectedActivitiesStr, activityType, String.valueOf(preferencePercentage));
                Intent intent = new Intent(this, choosePlaces.class); // Replace NextActivity.class with your next activity class
                startActivity(intent);

            }
        });
    }

}