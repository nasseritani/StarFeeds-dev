package com.nader.starfeeds.data.componenets.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nasse_000 on 3/31/2017.
 */
public class FeedInstagramImage extends Feed {
    String imageUrl;
    String count;
    public FeedInstagramImage(JSONObject jsonObject) throws JSONException {
        super(FeedType.INSTAGRAM_IMAGE,jsonObject.getString("id"),jsonObject.getString("celebName"),jsonObject.getString("celebId"),jsonObject.getString("text"),jsonObject.getString("date"),jsonObject.getString("imageProfile"));
        try {
            if (jsonObject.has("imageUrl")) this.imageUrl = jsonObject.getString("imageUrl");
            if(jsonObject.has("count")) this.count=jsonObject.getString("count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public String getCount() {
        return count;
    }
}
