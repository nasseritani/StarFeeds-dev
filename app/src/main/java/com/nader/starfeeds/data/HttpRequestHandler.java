package com.nader.starfeeds.data;

import android.os.AsyncTask;
import android.util.Log;

import com.nader.starfeeds.data.componenets.model.Feed;
import com.nader.starfeeds.data.componenets.model.FeedFacebookImage;
import com.nader.starfeeds.data.componenets.model.FeedFacebookLink;
import com.nader.starfeeds.data.componenets.model.FeedFacebookText;
import com.nader.starfeeds.data.componenets.model.FeedFacebookVideo;
import com.nader.starfeeds.data.componenets.model.FeedInstagramImage;
import com.nader.starfeeds.data.componenets.model.FeedInstagramVideo;
import com.nader.starfeeds.data.componenets.model.FeedTwitterImage;
import com.nader.starfeeds.data.componenets.model.FeedTwitterLink;
import com.nader.starfeeds.data.componenets.model.FeedTwitterText;

import org.json.JSONArray;
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
import java.util.ArrayList;


/**
 * Created by Nader on 16-Apr-17.
 */

public class HttpRequestHandler extends AsyncTask<String, Void, String> {
    JSONParserListener listener;

    // constructor
    public HttpRequestHandler(JSONParserListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... strings) {
        return executeUrl(strings[0]);
    }

    private String executeUrl(String url) {
        Log.i("TAG","get");
        URL _url;
        HttpURLConnection urlConnection;
        InputStream is = null;
        String output = "";

        try {
            _url = new URL(url);
            urlConnection = (HttpURLConnection) _url.openConnection();
        }
        catch (MalformedURLException e) {
            Log.e("JSON Parser", "Error due to a malformed URL " + e.toString());
            return null;
        }
        catch (IOException e) {
            Log.e("JSON Parser", "IO error " + e.toString());
            return null;
        }

        try {
            is = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder total = new StringBuilder(is.available());
            String line;
            while ((line = reader.readLine()) != null) {
                total.append(line).append('\n');
            }
            output = total.toString();
        }
        catch (IOException e) {
            Log.e("JSON Parser", "IO error " + e.toString());
            output = null;
        }
        finally{
            urlConnection.disconnect();
        }

        return output;
    }

    @Override
    protected void onPostExecute(String json) {

        ArrayList<Feed> feeds = new ArrayList<>();
        try {
            if (json != null) {
                JSONArray data =new JSONArray(json);
                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonObject = data.getJSONObject(i);
                    String feedType = jsonObject.getString("feedType");
                    Feed feed = null;
                    switch (feedType){
                        case "twitter_photo":
                            feed=new FeedTwitterImage(jsonObject);
                            feeds.add(feed);
                            break;
                        case "twitter_twitter_text":
                            feed=new FeedTwitterText(jsonObject);
                            feeds.add(feed);
                            break;
                        case "twitter_shared_story":
                            feed=new FeedTwitterLink(jsonObject);
                            feeds.add(feed);
                            break;
                        case "facebook_added_photos":
                            feed=new FeedFacebookImage(jsonObject);
                            feeds.add(feed);
                            break;
                        case "facebook_shared_story":
                            feed=new FeedFacebookLink(jsonObject);
                            feeds.add(feed);
                            break;
                        case "facebook_facebook_text":
                            feed=new FeedFacebookText(jsonObject);
                            feeds.add(feed);
                            break;
                        case"Instagram_photo":
                            feed=new FeedInstagramImage(jsonObject);
                            feeds.add(feed);
                            break;
                        case"Instagram_video":
                            feed=new FeedInstagramVideo(jsonObject);
                            feeds.add(feed);
                            break;
                        case "facebook_added_video":
                            feed=new FeedFacebookVideo(jsonObject);
                            feeds.add(feed);
                            break;
                    }
                    //   feeds.add(feed);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listener.onResult(feeds);
    }

    public interface JSONParserListener{
        void onResult(ArrayList<Feed> feeds);
}

}