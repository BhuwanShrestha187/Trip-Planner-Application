/*
    Filename: MainActivity.java
    Author: Bhuwan Shrestha
    StudentID: 8892146
    Date: 13th March, 2024
    Project: Trip Planner Academic Project
    Description: This file contains the Login Page.
 */

package com.example.loginpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView signInTextView;
    Button loginButton;
    private EditText loginUsername, loginPassword;
    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.loginButton);
        signInTextView = findViewById(R.id.signInTextview);
        loginUsername = findViewById(R.id.usernameEditText);
        loginPassword = findViewById(R.id.passwordEditText);
        myDB = new DatabaseHelper(this);


        loginUser();
        signInUser();
    }

    /*
        Function Name: loginUser
        Parameters: None
        Return: void
        Purpose: Authenticates user credentials and navigates to the homepage on successful login or displays error messages.
    */

    private void loginUser() {
        //Button onclickListener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = loginUsername.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();

                if (username.isEmpty()) {
                    loginUsername.setError("Username must be provided!!!");
                    return;
                }

                if (password.isEmpty()) {
                    loginPassword.setError("Password must be provided!!!");
                    return;
                }

                int checkUserData = myDB.checkUser(username, password);
                switch (checkUserData) {
                    case 0: // Success
                        Toast.makeText(MainActivity.this, "Login Successful!!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, homepage.class));
                        finish();
                        break;
                    case 3: // Generic failure
                        Toast.makeText(MainActivity.this, "Login Failed!! Incorrect Username or Password!!!", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "Login Error. Try Again!!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    /*
    Function Name: signInUser
    Parameters: None
    Return: void
    Purpose: Directs users to the sign-up page if they do not have an account.
*/

    private void signInUser()
    {
        signInTextView.setText(Html.fromHtml("<u>Don't have account? Create a new one ? </u>" , Html.FROM_HTML_MODE_LEGACY));
        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, signUpActivity.class));
                finish();
            }
        });
    }
}