/*
    Filename: homapage.java
    Author: Bhuwan Shrestha
    StudentID: 8892146
    Date: 13th March, 2024
    Project: Trip Planner Academic Project
    Description: This file contains the homepage of the application.
 */
package com.example.loginpage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class homepage extends AppCompatActivity {

    private EditText startAddressEditText, destinationAddressEditText, budgetEditText, durationEditText;
    private Spinner modeOfTransportSpinner;
    private CheckBox wheelchairCheckbox, petFriendlyCheckBox, breakfastCheckbox, wifiCheckbox;
    boolean isAllFieldChecked = false;

    private EditText startDateEditText;
    private EditText endDateEditText;

    TripDetailsDatabaseHelper tripDetailsDB;

    Button nextButton, getDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        //Get all the values"
        startAddressEditText = findViewById(R.id.startingAddressEditText);
        destinationAddressEditText = findViewById(R.id.destinationAddressEditText);
        budgetEditText = findViewById(R.id.budgetEditText);
        durationEditText = findViewById(R.id.durationEditText);
        modeOfTransportSpinner = findViewById(R.id.modeOfTransportSpinner);
        wheelchairCheckbox = findViewById(R.id.wheelchairAccessCheckBox);
        petFriendlyCheckBox = findViewById(R.id.petFriendlyCheckBox);
        breakfastCheckbox = findViewById(R.id.breakfastCheckBox);
        wifiCheckbox = findViewById(R.id.wifiCheckBox);
         nextButton = findViewById(R.id.nextButton);
        startDateEditText = findViewById(R.id.startingDateEditText);
        endDateEditText = findViewById(R.id.endDateEditText);

        tripDetailsDB = new TripDetailsDatabaseHelper(this);

        //On click listeners for the Date fields
        setupDatePicker(R.id.startingDateEditText, R.id.datePickerIcon);
        setupDatePicker(R.id.endDateEditText, R.id.datePickerIcon2);
        chooseActivitiesPage();

    }


    /*
    Method Name: setupDatePicker
    Parameters: int editTextId, int imageViewId
    Return: void
    Purpose: Initializes and sets up a date picker dialog to be shown when the user clicks on an EditText field or an associated ImageView icon.
             The method configures a DatePickerDialog to update the EditText with the selected date, formatted as MM/DD/YYYY.
*/

    private void setupDatePicker(int editTextId, int imageViewId) {
        EditText dateEditText = findViewById(editTextId);
        ImageView datePickerIcon = findViewById(imageViewId);

        View.OnClickListener showDatePickerListener = v -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        String date = "        " + (monthOfYear + 1) + "/" + dayOfMonth + "/" + year1;
                        dateEditText.setText(date);
                    }, year, month, day);
            datePickerDialog.show();
        };

        dateEditText.setOnClickListener(showDatePickerListener);
        datePickerIcon.setOnClickListener(showDatePickerListener);
    }


    /*
    Method Name: chooseActivitiesPage
    Parameters: None
    Return: void
    Purpose: Handles the logic for the nextButton click event on the choose activities page. It checks if all required fields are filled,
             at least one preference is checked, and a mode of transport is selected. Displays appropriate toast messages for validation failures.
             On successful validation, it saves the trip details to the database and navigates to the choose activities page.
*/

    private void chooseActivitiesPage()
    {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAllFieldChecked = CheckAllFields();
                if (isAllFieldChecked && !checkAtLeastOnePreferenceCheckBox()) {
                    Toast.makeText(getApplicationContext(), "At least one preference should be checked", Toast.LENGTH_SHORT).show();
                } else if (!isAllFieldChecked) {
                    Toast.makeText(getApplicationContext(), "All fields must be filled", Toast.LENGTH_SHORT).show();
                }
                else if(!isValidSpinnerSelected(modeOfTransportSpinner))
                {
                    Toast.makeText(getApplicationContext(), "At least one mode of transport must be selected", Toast.LENGTH_SHORT).show();
                }
                else {
                    //Prepare Data for insertion
                    String startAddress = startAddressEditText.getText().toString();
                    String destinationAddress = destinationAddressEditText.getText().toString();
                    String budget = budgetEditText.getText().toString();
                    String duration = durationEditText.getText().toString();
                    String startDate = startDateEditText.getText().toString();
                    String endDate = endDateEditText.getText().toString();
                    String transportMode = modeOfTransportSpinner.getSelectedItem().toString();
                    String preferences = getSelectedPreferences();

                    tripDetailsDB.addTripDetails(startAddress, destinationAddress, budget, duration, startDate, endDate, transportMode, preferences);

                    Intent intent = new Intent(homepage.this, chooseActivities.class);
                    // Add other data as extras
                    startActivity(intent);
                }
            }
        });
    }

    /*
    Method Name: getSelectedPreferences
    Parameters: None
    Return: String
    Purpose: Compiles a comma-separated string of selected preferences based on the state of various checkboxes.
             The method checks each preference checkbox and appends the corresponding preference text to a StringBuilder.
             A trailing comma is removed before returning the final string of preferences.
*/

    private String getSelectedPreferences() {
        StringBuilder preferences = new StringBuilder();
        if (wheelchairCheckbox.isChecked()) preferences.append("Wheelchair Access,");
        if (petFriendlyCheckBox.isChecked()) preferences.append("Pet Friendly,");
        if (breakfastCheckbox.isChecked()) preferences.append("Breakfast Included,");
        if (wifiCheckbox.isChecked()) preferences.append("WiFi Available,");
        if (preferences.length() > 0) preferences.setLength(preferences.length() - 1); // Remove the trailing comma
        return preferences.toString();
    }


/*
    Method Name: CheckAllFields
    Parameters: None
    Return: boolean
    Purpose: Validates if the input fields for start address, destination address, budget, and duration contain valid entries.
             Utilizes the isValidInput method for each field to determine their validity. Returns true if all fields are valid,
             otherwise returns false.
*/

    private boolean CheckAllFields()
    {
        boolean isValid = true;
        isValid &= isValidInput(startAddressEditText);
        isValid &= isValidInput((destinationAddressEditText));
        isValid &= isValidInput((budgetEditText));
        isValid &= isValidInput(durationEditText);
        return isValid;
    }

    /*
    Method Name: isValidInput
    Parameters: EditText editText
    Return: boolean
    Purpose: Checks if the input in a given EditText is valid, i.e., not empty. It trims the input to remove leading and trailing whitespace.
             Sets an error message on the EditText if the input is empty, indicating that the field is required. Returns true if the input is valid,
             otherwise returns false.
*/

    private boolean isValidInput(EditText editText)
    {
        String text = editText.getText().toString().trim();
        if(text.isEmpty())
        {
            editText.setError("This field is required");
            return false;
        }

        return true;
    }

    /*
    Method Name: isValidSpinnerSelected
    Parameters: Spinner spinner
    Return: boolean
    Purpose: Verifies if a valid selection is made in a Spinner control, assuming that the first item (at position 0) is a prompt or invalid choice.
             Returns true if the selected item's position is not 0, indicating a valid selection has been made.
*/

    private boolean isValidSpinnerSelected(Spinner spinner)
    {
        //Get the selected item position
        int selectedPosition = spinner.getSelectedItemPosition();
        //Check if a valid selection is made
        return selectedPosition != 0;
    }


    /*
    Method Name: checkAtLeastOnePreferenceCheckBox
    Parameters: None
    Return: boolean
    Purpose: Checks an array of CheckBox controls to determine if at least one preference is selected by the user.
             Iterates through each CheckBox and returns true upon finding the first checked (selected) one.
             If no CheckBox is checked, returns false, indicating no preference has been selected.
*/

    private boolean checkAtLeastOnePreferenceCheckBox()
    {
        CheckBox[] checkboxes = {petFriendlyCheckBox, wifiCheckbox, breakfastCheckbox, wheelchairCheckbox};
        for(CheckBox checkbox : checkboxes)
        {
            if(checkbox.isChecked())
            {
                return true;
            }
        }

        return false;
    }
}