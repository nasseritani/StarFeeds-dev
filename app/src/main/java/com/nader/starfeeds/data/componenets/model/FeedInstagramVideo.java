package com.nader.starfeeds.data.componenets.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nasse_000 on 3/31/2017.
 */
public class FeedInstagramVideo extends Feed {
    private String videoUrl;
    private String imageUrl;
    private String count;
    public FeedInstagramVideo(JSONObject jsonObject) throws JSONException {
        super(FeedType.INSTAGRAM_VIDEO,jsonObject.getString("id"),jsonObject.getString("celebName"),jsonObject.getString("celebId"),jsonObject.getString("text"),jsonObject.getString("date"),jsonObject.getString("imageProfile"));
        try {
            if (jsonObject.has("link")) this.videoUrl = jsonObject.getString("link");
            if (jsonObject.has("imageUrl")) this.imageUrl = jsonObject.getString("imageUrl");
            if(jsonObject.has("count")) this.count=jsonObject.getString("count");
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

    public String getCount() {
        return count;
    }
}
