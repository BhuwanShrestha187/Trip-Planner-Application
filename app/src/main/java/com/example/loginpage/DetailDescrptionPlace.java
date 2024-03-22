/*
    Filename: DetailDescriptionPlace.java
    Author: Bhuwan Shrestha
    StudentID: 8892146
    Date: 13th March, 2024
    Project: Trip Planner Academic Project
    Description: This file helps in getting the detailed descriptuion of places from Wikipedia.
 */

package com.example.loginpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailDescrptionPlace extends AppCompatActivity {

    private Button choosePlace, downloadImage, skipButton;

    private String placename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_descrption_place);

        ImageView placeImage = findViewById(R.id.placeImage);
        downloadImage = findViewById(R.id.downloadButton);
        skipButton = findViewById(R.id.skipButton);

        goToFinalPage();

        downloadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int imageResourceId = getIntent().getIntExtra("imageResourceId", 0);
                if (imageResourceId != 0) {
                    // Pass the current context and placename to the AsyncTask
                    new DownloadImagesTask(DetailDescrptionPlace.this, placename).execute(imageResourceId);
                } else {
                    Toast.makeText(DetailDescrptionPlace.this, "No image to download", Toast.LENGTH_SHORT).show();
                }
            }
        });



        // Retrieve the image resource ID from the intent
        Intent intent = getIntent();
        if (intent != null) {
            placename = intent.getStringExtra("placeName");
            int imageResourceId = intent.getIntExtra("imageResourceId", 0);
            if (imageResourceId != 0) {
                placeImage.setImageResource(imageResourceId);
            }
        }

        // Execute the AsyncTask with the Wikipedia page title as parameter
        new FetchDescriptionTask().execute(placename);

    }

    /*
    Function Name: goToFinalPage
    Parameters: None
    Return: void
    Purpose: Sets up an onClickListener for the skipButton to navigate from the current activity to the final page of the application.
             Finishes the current activity after starting the new one to prevent returning to it via the back button.
*/

    private void goToFinalPage()
    {
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailDescrptionPlace.this, finalPage.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /*
    Class Name: FetchDescriptionTask
    Purpose: This AsyncTask is designed to fetch a concise description from Wikipedia for a given topic.
             It performs an HTTP request to the Wikipedia API to retrieve the summary of an article based on the provided search parameter.
    doInBackground Method:
        Parameters: String... params - An array of parameters where the first parameter is expected to be the topic for the Wikipedia search.
        Return: String - The extracted summary (extract field) from the Wikipedia API JSON response for the specified topic. Returns null if an exception occurs.
    Usage: It should be instantiated and executed with the topic as the argument to fetch its description from Wikipedia.
*/

    private class FetchDescriptionTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            // Use the Wikipedia API endpoint for the summary of the article.
            String apiUrl = "https://en.wikipedia.org/api/rest_v1/page/summary/" + params[0];
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Convert StringBuilder to String and parse it as JSON
                    JSONObject jsonResponse = new JSONObject(result.toString());
                    // Extract the "extract" field from the JSON object
                    return jsonResponse.getString("extract");
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // Update the TextView with the description extracted from the JSON response
            TextView descriptionView = findViewById(R.id.placeDescription);
            descriptionView.setText(result);
        }
    }


}