/*
    Filename: Downl;oadImageTask.java
    Author: Bhuwan Shrestha
    StudentID: 8892146
    Date: 13th March, 2024
    Project: Trip Planner Academic Project
    Description: This file contains the code for downloading the iMages.
 */
package com.example.loginpage;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DownloadImagesTask extends AsyncTask<Integer, Void, String> {
    private Context context;
    private String placename;

    public DownloadImagesTask(Context context, String placename) {
        this.context = context; // Use application context to avoid memory leaks
        this.placename = placename;
    }

    /*
    Method: doInBackground
    Purpose: Attempts to access or create a directory in the device's external storage specifically for downloads,
             using a provided resource ID to identify or manage files within this directory.
    Parameters: Integer... integers - Variable arguments where the first integer is expected to be a resource ID used within the method.
    Return: String - Returns a message indicating the success or failure of directory access or creation. It returns "Failed to create directory for downloads."
             if it cannot access or create the specified directory.
    Usage: This method is part of an AsyncTask that handles file operations in the background, such as preparing or verifying storage directories
           for file downloads or storage. The method checks for the existence of the downloads directory and attempts to create it if it does not exist.
*/

    @Override
    protected String doInBackground(Integer... integers) {
        int resourceId = integers[0];
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        if (!storageDir.exists() && !storageDir.mkdirs()) {
            return "Failed to create directory for downloads.";
        }

        String imageName = placename + ".jpg";
        File imageFile = new File(storageDir, imageName);

        try (InputStream in = context.getResources().openRawResource(resourceId);
             OutputStream out = new FileOutputStream(imageFile)) {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            // Optionally, scan the file so it appears in the gallery right away
            MediaScannerConnection.scanFile(context, new String[]{imageFile.toString()}, null, null);
            return "Image saved to Downloads folder as " + imageName;
        } catch (IOException e) {
            Log.e("DownloadImagesTask", "Error saving image", e);
            return "Error saving image.";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
    }
}
