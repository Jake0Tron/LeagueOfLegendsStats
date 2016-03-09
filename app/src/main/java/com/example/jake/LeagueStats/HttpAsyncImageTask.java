package com.example.jake.LeagueStats;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Jake on 11/19/2015.
 */
public class HttpAsyncImageTask extends AsyncTask<ArrayList<String>, Integer, ArrayList<Bitmap>> {

    // for callback
    public AsyncImageResponse delegate = null;
    private URLBuilder urlBuilder;

    public HttpAsyncImageTask (AsyncImageResponse delegate){
        this.delegate = delegate;
    }

    public HttpAsyncImageTask (){
        this.delegate = null;
        this.urlBuilder = new URLBuilder();
    }

    // MAIN LOGIC
    @Override
    protected ArrayList<Bitmap> doInBackground(ArrayList<String>... incomingNames) {
        // arraylist to hold champion names
        ArrayList<String> urlsToGet = new ArrayList<>();

        // get ArrayList passed in
        urlsToGet = incomingNames[0];
        //Log.d("AsyncPics" , champNames.size() + " champs to get pictures for");

        //  make ArrayList of Images
        ArrayList<Bitmap> images = new ArrayList<>();

        for (int i=0; i < urlsToGet.size(); i++){
            // create URL for champion picture
            String imageUrl = urlsToGet.get(i);

            //Log.d("AsyncPics",imageUrl);

            Bitmap bmp = getImageFromURL(imageUrl);
            if (bmp != null){
                //populate ArrayList of images
                images.add(bmp);
            }
        }
        // return ArrayList of Images
        return images;
    }

    // CALLED FOR EACH IMAGE
    // Takes URL and queries it to return a Bitmap object
    public Bitmap getImageFromURL(String imageUrl){
        URL url = null;
        try {
            url = new URL(imageUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection connection  = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream is = null;
        try {
            is = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap img = BitmapFactory.decodeStream(is);
        return img;
    }

    public void setDelegate(AsyncImageResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected void onPostExecute(ArrayList<Bitmap> result){
        delegate.imageProcessFinish(result);
    }
}
