/*
    Filename: finalPage.java
    Author: Bhuwan Shrestha
    StudentID: 8892146
    Date: 13th March, 2024
    Project: Trip Planner Academic Project
    Description: This file contains the final page of the project.
 */
package com.example.loginpage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;
import android.net.Uri;
import androidx.core.content.FileProvider;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class finalPage extends AppCompatActivity {

    private TextView reviewTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_page);

        RatingBar ratingBar = findViewById(R.id.ratingBar);
        reviewTextView = findViewById(R.id.reviewThankYouText);

        // Set a listener on the rating bar
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(fromUser) { // Check if the rating change was initiated by the user
                    reviewTextView.setVisibility(View.VISIBLE); // Show the thank you message
                }
            }
        });

        findViewById(R.id.downloadButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFormatChoiceDialog();
            }
        });
    }


    /*
    Method Name: showFormatChoiceDialog
    Parameters: None
    Return: void
    Purpose: Displays a dialog allowing the user to choose between PDF and DOC formats.
             Depending on the user's choice, it either generates and downloads a PDF or a DOC document.
*/

    private void showFormatChoiceDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Choose a format")
                .setItems(new String[]{"PDF", "DOC"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            // User chose PDF
                            generateAndDownloadPdf();
                        } else {
                            // User chose DOC
                            generateAndDownloadDoc();
                        }
                    }
                })
                .show();
    }


    /*
    Method Name: generateAndDownloadPdf
    Parameters: None
    Return: void
    Purpose: Generates a PDF document containing trip details fetched from the database, saves it to external storage, and opens it for viewing.
             The method creates a PDF file with a list of trip activities and their details, utilizing the iText PDF library for document creation and manipulation.
             After generating the document, it shares or views the PDF through an intent, providing read access to the file via a FileProvider.
*/

    private void generateAndDownloadPdf() {
        try {
            // Define file path for the PDF
            String filePath = getExternalFilesDir(null) + "/TripDetails.pdf";
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Fetch trip details
            TripActivitiesDatabaseHelper db = new TripActivitiesDatabaseHelper(this);
            ArrayList<String> detailsList = (ArrayList<String>) db.getDetails();

            // Create a list in the PDF
            com.itextpdf.layout.element.List list = new List()
                    .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                    .setFontSize(12);

            // Add details to the list
            for (String detail : detailsList) {
                list.add(new ListItem(detail));
            }

            document.add(list);
            document.close();

            // Share or view the generated PDF
            File pdfFile = new File(filePath);
            Uri pdfUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", pdfFile);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(pdfUri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(intent, "Open PDF"));

        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    private void generateAndDownloadDoc() {
       //Gnerate DownloadDoc
    }

    public void openFacebook(View view) {
        openUrl("https://www.facebook.com");
    }

    public void openInstagram(View view) {
        openUrl("https://www.instagram.com");
    }

    public void openGmail(View view) {
        openUrl("https://mail.google.com");
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}