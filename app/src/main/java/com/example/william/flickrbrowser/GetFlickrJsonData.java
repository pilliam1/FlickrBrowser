package com.example.william.flickrbrowser;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by William on 12/11/2016.
 */

//This class can grab the call back from GetRawData but it also creates it own interface that can pass the value to MainActivity

class GetFlickrJsonData implements GetRawData.OnDownloadComplete {
    private static final String TAG = "GetFlickrJsonData";


    //store a list of photo objects that we parse out of the JSON data
    private List<Photo> mPhotoList = null;
    //raw link
    private String mBaseUrl;
    private String mLanguage;
    private boolean mMatchAll;

    private final OnDataAvailable mCallBack;

    interface OnDataAvailable {
        void onDataAvailable(List<Photo> data, DownloadStatus status);
    }

    //constructor
    public GetFlickrJsonData(OnDataAvailable callBack, String baseUrl, String language, boolean matchAll) {
        Log.d(TAG, "GetFlickrJsonData: Called");
        mBaseUrl = baseUrl;
        mCallBack = callBack;
        mLanguage = language;
        mMatchAll = matchAll;
    }

    //MainActivity will call this method
    void executeOnSameThread(String searchCriteria) {
        Log.d(TAG, "executeOnSameThread: Starts");
        String destinationUri = createUri(searchCriteria, mLanguage, mMatchAll);

        GetRawData getRawData = new GetRawData(this);
        getRawData.execute(destinationUri);
        Log.d(TAG, "executeOnSameThread: ends");
    }

    private String createUri(String searchCriteria, String lang, boolean matchAll){
        Log.d(TAG, "createUrl: starts");

        return Uri.parse(mBaseUrl).buildUpon()
                //bouldering and climbing
                .appendQueryParameter("tags", searchCriteria)
                //tagmode for the url were parsing = ANY
                .appendQueryParameter("tagmode", matchAll ? "ALL" : "ANY")
                //lang = en-en
                .appendQueryParameter("lang", lang)
                //format = json
                .appendQueryParameter("format", "json")
                //nojsoncallback=1
                .appendQueryParameter("nojsoncallback", "1")
                .build().toString();
    }

    //overriding interface
    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete starts. Status = " + status);
        if(status == DownloadStatus.OK){
            mPhotoList = new ArrayList<>();
            try {
                JSONObject jsonData = new JSONObject(data);
                JSONArray itemsArray = jsonData.getJSONArray("items");

                //enables us to target a element within the array.
                for(int i = 0; i < itemsArray.length(); i++) {
                    JSONObject jsonPhoto = itemsArray.getJSONObject(i);
                    String title = jsonPhoto.getString("title");
                    String author = jsonPhoto.getString("author");
                    String authorId = jsonPhoto.getString("author_id");
                    String tags = jsonPhoto.getString("tags");

                    //accesing media object in the items array
                    JSONObject jsonMedia = jsonPhoto.getJSONObject("media");
                    String photoUrl = jsonMedia.getString("m");

                    //changing the image size from small to big.
                    //the small image will show in the recycler view. But when we click, it will be converted to the big image and opened in a new activity
                    String link = photoUrl.replaceFirst("_m.", "_b.");

                    //giving fields to Photo class
                    Photo photoObject = new Photo(title, author, authorId, link, tags, photoUrl);
                    mPhotoList.add(photoObject);

                    Log.d(TAG, "onDownloadComplete " + photoObject.toString());
                }
            } catch (JSONException jsone){
                jsone.printStackTrace();
                Log.e(TAG, "onDownloadComplete: Error processing Json Data " + jsone.getMessage());
                status = DownloadStatus.FAILED_OR_EMPTY;
            }
        }
        if (mCallBack != null) {
            //now inform the caller that processing is done - possibly returning null if there was an error
            mCallBack.onDataAvailable(mPhotoList, status);
        }

        Log.d(TAG, "onDownloadComplete: ends");
    }
}
