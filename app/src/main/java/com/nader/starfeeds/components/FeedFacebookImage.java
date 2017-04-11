package com.nader.starfeeds.components;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nader on 03-Mar-17.
 */

public class FeedFacebookImage extends Feed {
    String imageUrl;

    public FeedFacebookImage(JSONObject jsonObject) throws JSONException {
        super(FeedType.FACEBOOK_IMAGE,jsonObject.getString("id"),jsonObject.getString("celebName"),jsonObject.getString("text"),jsonObject.getString("date"),jsonObject.getString("imageProfile"));
        try {
            if (jsonObject.has("imageUrl")) this.imageUrl = jsonObject.getString("imageUrl");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
