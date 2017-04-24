package com.nader.starfeeds.data.componenets.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nasse_000 on 3/31/2017.
 */
public class FeedInstagramVideo extends Feed {
    String videoUrl;
    String count;
    public FeedInstagramVideo(JSONObject jsonObject) throws JSONException {
        super(FeedType.INSTAGRAM_VIDEO,jsonObject.getString("id"),jsonObject.getString("celebName"),jsonObject.getString("celebId"),jsonObject.getString("text"),jsonObject.getString("date"),jsonObject.getString("imageProfile"));
        try {
            if (jsonObject.has("videoUrl")) this.videoUrl = jsonObject.getString("videoUrl");
            if(jsonObject.has("count")) this.count=jsonObject.getString("count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getCount() {
        return count;
    }
}
