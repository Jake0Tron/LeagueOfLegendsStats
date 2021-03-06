package com.example.jake.LeagueStats;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/*
    This class is designed to handle all GET HttpRequests made by the application.
    URL's must be constructed outside this class according to API requirements!
    NO JSON parsing will be done here!

    Parameters:
        IN: String      // URL
        UPDATE: Void    //
        OUT: JSONObject // Return JSON to be used elsewhere

        Code sourced and altered from http://hmkcode.com/android-parsing-json-data/
*/
public class HttpAsyncVersionTask extends AsyncTask<String, Integer, JSONObject> {

    public AsyncVersionResponse delegate = null;

    public HttpAsyncVersionTask (AsyncVersionResponse delegate){
        this.delegate = delegate;
    }

    public HttpAsyncVersionTask (){
        this.delegate = null;
    }

    // LOGIC 1
    // returns JSON String retrieved from provided URL
    public static String GET(String url){
        String result = "";

        URL u = null;
        try {
            u = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = null;
        if (u != null){
            try {
                urlConnection = (HttpURLConnection) u.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (urlConnection != null){
                InputStream in = null;
                try {
                    in = new BufferedInputStream(urlConnection.getInputStream());
                }catch (Exception e){e.printStackTrace();}
                if (in != null){
                    result = convertInputStreamToString(in);
                }
                else{
                    return null;
                }

                urlConnection.disconnect();
            }
        }
        return result;
    }

    // LOGIC 2 - Type Conversion from GET
    public static String convertInputStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line;
        String result = "";
        try {
            while((line = bufferedReader.readLine()) != null)
                result += line;
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    // BEFORE execute
    @Override
    protected void onPreExecute(){
        // BEFORE
    }

    @Override
    protected void onProgressUpdate(Integer... progress){
        super.onProgressUpdate();
    }

    // Receives a URL to return JSON Object
    // http.execute(url);
    @Override
    protected JSONObject doInBackground(String... urls) {
        String get;
        get = GET(urls[0]);
        publishProgress();
        if (get != null){
            JSONObject json = null;
            try {
                json = new JSONObject(get);
            } catch (JSONException e) {}

            return json;
        }else{
            return null;
        }
    }

    public void setDelegate(AsyncVersionResponse delegate){
        this.delegate = delegate;
    }

    // http://stackoverflow.com/questions/12575068/how-to-get-the-result-of-onpostexecute-to-main-activity-because-asynctask-is-a

    // POST execute will call processFinish() and pass a JSON object back to activity to be used
    // TO RECEIVE DATA: implement the AsyncResponse interface and the processFinish method
    @Override
    protected void onPostExecute(JSONObject result){
        delegate.processVersionFinish(result);
    }


}
