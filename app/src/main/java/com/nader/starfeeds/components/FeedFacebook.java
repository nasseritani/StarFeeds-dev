package com.nader.starfeeds.components;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nasse_000 on 3/6/2017.
 */
public class FeedFacebook extends Feed {
    String imageUrl;

    public FeedFacebook(JSONObject jsonObject) throws JSONException {
        super(FeedType.FACEBOOK,jsonObject.getString("id"),jsonObject.getString("celebName"),jsonObject.getString("text"),jsonObject.getString("date"));
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

