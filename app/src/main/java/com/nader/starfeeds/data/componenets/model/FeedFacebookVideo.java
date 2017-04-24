package com.nader.starfeeds.data.componenets.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nasse_000 on 3/11/2017.
 */

public class FeedFacebookVideo extends Feed {
    String videoUrl;
    String imageUrl;

    public FeedFacebookVideo(JSONObject jsonObject) throws JSONException {
        super(FeedType.FACEBOOK_VIDEO,jsonObject.getString("id"),jsonObject.getString("celebName"),jsonObject.getString("celebId"),jsonObject.getString("text"),jsonObject.getString("date"),jsonObject.getString("imageProfile"));
        try {
            if (jsonObject.has("link")) this.videoUrl = jsonObject.getString("link");
            if (jsonObject.has("imageUrl")) this.imageUrl = jsonObject.getString("imageUrl");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

