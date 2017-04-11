package com.nader.starfeeds.data;

import android.content.Context;

import com.nader.starfeeds.components.Feed;
import com.nader.starfeeds.components.FeedFacebookLink;
import com.nader.starfeeds.components.FeedFacebookImage;
import com.nader.starfeeds.components.FeedFacebookText;
import com.nader.starfeeds.components.FeedFacebookVideo;
import com.nader.starfeeds.components.FeedInstagramImage;
import com.nader.starfeeds.components.FeedInstagramVideo;
import com.nader.starfeeds.components.FeedTwitterLink;
import com.nader.starfeeds.components.FeedTwitterImage;
import com.nader.starfeeds.components.FeedTwitterText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Nader on 12-Feb-17.
 */

public class InternalJsonFileData {
    Context context;
    JSONParserListener listener;

    public InternalJsonFileData(Context context, JSONParserListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = context.getAssets().open("json.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return ;
        }

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
        }
        catch(JSONException e){
            e.printStackTrace();
        }


        listener.onResult(feeds);
    }
    interface JSONParserListener{
        void onResult(ArrayList<Feed> feeds);
    }
}