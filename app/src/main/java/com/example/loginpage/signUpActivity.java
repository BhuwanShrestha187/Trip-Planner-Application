/*
    Filename: signUpActivity.java
    Author: Bhuwan Shrestha
    StudentID: 8892146
    Date: 13th March, 2024
    Project: Trip Planner Academic Project
    Description: This file contains the Sign Up handling codes.
 */
package com.example.loginpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class signUpActivity extends AppCompatActivity {

    private Button signUpButton;
    private EditText usernameEditText, passwordEditText, emailEditText;
    private DatabaseHelper myDB;


    /*
    Method Name: onCreate
    Parameters: Bundle savedInstanceState
    Return: void
    Purpose: Initializes the activity's layout and views. Retrieves references to UI elements such as signUpButton, usernameEditText, passwordEditText, and emailEditText.
             Creates an instance of the DatabaseHelper to interact with the database and invokes the insertUser method to handle user registration.
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpButton  = findViewById(R.id.signUpButton);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        emailEditText = findViewById(R.id.emailEditText);

        myDB = new DatabaseHelper(this);
        insertUser();
    }

    /*
    Method Name: insertUser
    Parameters: None
    Return: void
    Purpose: Sets an onClickListener for the signUpButton to handle user registration. It validates the username to ensure it doesn't contain numbers,
             checks that the email and password fields are not empty, and then attempts to register the user using the registerUser method of the myDB object.
             Displays a toast message indicating whether the registration was successful or encountered an error. Upon successful registration,
             navigates the user back to the MainActivity and finishes the current activity.
*/

    private void insertUser() {
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validating the username..........
                String username = usernameEditText.getText().toString().trim();
                String regexPattern = ".*\\d+.*";

                if(username.isEmpty())
                {
                    usernameEditText.setError("Username must be provided!!!");
                    return;
                }

                //Check if user has entered number in the username field
                if(username.matches(regexPattern))
                {
                    usernameEditText.setError("Username must not contain numbers!!!");
                    return;
                }

                //..............Validating Email...........................
                String email = emailEditText.getText().toString().trim();
                if(email.isEmpty())
                {
                    emailEditText.setError("Email must be provided!!!");
                    return;
                }

                //................. Validating Password .........................
                String password = passwordEditText.getText().toString().trim();
                if(password.isEmpty())
                {
                    passwordEditText.setError("Password must be provided!!!");
                    return;
                }
                boolean isInserted = myDB.registerUser(username, email, password);

                if(isInserted)
                {
                    Toast.makeText(signUpActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(signUpActivity.this, MainActivity.class));
                    finish();
                }

                else
                {
                    Toast.makeText(signUpActivity.this, "Registration Error!!!!!!. Try Again!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}