package com.example.william.flickrbrowser;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by William on 31/10/2016.
 */

enum DownloadStatus {IDLE, PROCESSING, NOT_INITIALIZED, FAILED_OR_EMPTY, OK}
//IDLE = not processing anything at
//NOT_INITIALIZED = havent got a valid url to download, (error condition)
//providing a string url and getting a string back
//create a enum to hold a list of download statuses
//enum is containing all the states/possibilities this class can be in
//actually so hard

class GetRawData extends AsyncTask<String, Void, String> {
    private static final String TAG = "GetRawData";

    //field to track download status
    private DownloadStatus downloadStatus;

    //constructor
    public GetRawData() {
        this.downloadStatus = DownloadStatus.IDLE;
    }

    //need to override the required Async methods

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: parameter = " + s);

    }

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        if(params == null){
            downloadStatus = DownloadStatus.NOT_INITIALIZED;
            return null;
        }

        try {
            downloadStatus = DownloadStatus.PROCESSING;
            //picking up 1st url
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int response = connection.getResponseCode();
            Log.d(TAG, "doInBackground: Response code was " + response);

            //contains data that we download ultimately
            StringBuilder result = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while(null != (line = reader.readLine())) {
                result.append(line).append("\n");
            }

            downloadStatus = DownloadStatus.OK;
            return result.toString();

        } catch(MalformedURLException e) {
            Log.e(TAG, "doInBackground: Invalid URL " + e.getMessage() );
        } catch(IOException e) {
            Log.e(TAG, "doInBackground: IO exception reading data " + e.getMessage() );
        } catch(SecurityException e){
            Log.e(TAG, "doInBackground: Security Exception. needs permission? " + e.getMessage());
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e){
                    Log.e(TAG, "doInBackground: Error closing stream " + e.getMessage());
                }
            }
        }

        downloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }
}

