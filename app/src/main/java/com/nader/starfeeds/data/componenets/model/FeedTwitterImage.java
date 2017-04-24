package com.nader.starfeeds.data.componenets.model;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nasse_000 on 3/6/2017.
 */
public class FeedTwitterImage extends Feed {
    String imageUrl;

    public FeedTwitterImage(JSONObject jsonObject) throws JSONException {
        super(FeedType.TWITTER_IMAGE,
                jsonObject.getString("id"),
                jsonObject.getString("celebName"),
                jsonObject.getString("celebId"),
                jsonObject.getString("text"),
                jsonObject.getString("date"),
                jsonObject.getString("imageProfile"));
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

