package com.nader.starfeeds.components;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nasse_000 on 3/30/2017.
 */
public class FeedTwitterLink extends Feed {
    String imageUrl;
    String link;
    String linkDescription;


    public FeedTwitterLink(JSONObject jsonObject) throws JSONException {
        super(FeedType.TWITTER, jsonObject.getString("id"), jsonObject.getString("celebName"), jsonObject.getString("text"), jsonObject.getString("date"),jsonObject.getString("imageProfile"));
        try {
            if (jsonObject.has("imageUrl")) this.imageUrl = jsonObject.getString("imageUrl");
            if (jsonObject.has("link")) this.link = jsonObject.getString("link");
            if (jsonObject.has("linkDescription")) this.linkDescription = jsonObject.getString("linkDescription");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public String getLink() {
        return link;
    }
    public String getLinkDescription() {
        return linkDescription;
    }
}
