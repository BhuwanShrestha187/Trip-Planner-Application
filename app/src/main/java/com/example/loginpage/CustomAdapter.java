/*
    Filename: customAdapter.java
    Author: Bhuwan Shrestha
    StudentID: 8892146
    Date: 13th March, 2024
    Project: Trip Planner Academic Project
    Description: This file acts as a Custom adapter for the places.
 */
package com.example.loginpage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.CloudMediaProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class CustomAdapter extends BaseAdapter{
    Context context; //It is a reference to the context from which the adapter was created.


    List<Place> places;
    LayoutInflater inflater; //It is used to instantiate layout XML files into their corresponding
    //objects. Meaning it turns your layout files into something that can be displayed on screen.

    public CustomAdapter (Activity context, List<Place> places)
    {
        this.context = context;
        this.places = places;
        inflater = (LayoutInflater.from(context));
        //See here you used the system resources
    }



    @Override
    public int getCount() { //Tells how many data items are in the data set.
        return places.size();
    }

    @Override
    public Object getItem(int position) { //specifies the item to be retrieved from data set.
        return places.get(position);
    }

    @Override
    public long getItemId(int position) { //returns the row id of the specified position.
        return position;
    }


    /*
        - Function getView():
        - This is important function when the GridView needs a view to display for a particular
          item. You inflate the layout for each grid item (that means create a new View object from
          XML layout file), find the ImageView within that layout, and set the Image for that
          position in the array using decodeSampledBitmapFromResource().

     */
    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.activity_gridview, null);
        //The above line means make the object of the View which ic activity_gridview

        //Now in that view we have ImageViewID right
        ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
        TextView name = convertView.findViewById(R.id.textViewPlaceName);
        String existingTextName = name.getText().toString();

        TextView location = convertView.findViewById(R.id.textViewPlaceLocation);
        String existingLocationText = location.getText().toString();

        Place place = places.get(position);

        // Set image
        Bitmap bitmap = decodeSampledBitmapFromResource(context.getResources(), place.getImageResourceId(), 100, 100);
        icon.setImageBitmap(bitmap);
        // Set name and location
        name.setText(existingTextName + " " + place.getName());
        location.setText(existingLocationText + " " + place.getLocation());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailDescrptionPlace.class);
                intent.putExtra("placeName", places.get(position).getName()); // Passing name
                intent.putExtra("imageResourceId", places.get(position).getImageResourceId()); // Passing image resource ID
                context.startActivity(intent);
            }
        });
        return  convertView;
    }

    /*
        - Function calculateSampleSize();
        This method calculates a size for image scaling.
        - It first retrieves the orignal height and width of the image. If the image dimesions are
        larger than the required dimension, then it calculates how much the image can be
        scaled down. It does it by dividing the image width and height by 2 iteratively until
        finding the largest size value that meets teh required dimensions.

        - options-> Contains the original dimension for the image.

     */
    private int caculateSampleInSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if(height > reqHeight || width > reqWidth)
        {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth)
            {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /*
        - Function decodeSampledBitmapFromResources()
        --> It uses the calculated size to load a scaled-down version of an image into the memory.
     */
    private Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; //It helps to calculate dimension without loading in memory
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = caculateSampleInSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}
