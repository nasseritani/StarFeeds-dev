package com.nader.starfeeds.components;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nasse_000 on 3/11/2017.
 */

    public class FeedFacebookVideo extends Feed {
        String videoUrl;

        public FeedFacebookVideo(JSONObject jsonObject) throws JSONException {
            super(FeedType.FACEBOOK_VIDEO,jsonObject.getString("id"),jsonObject.getString("celebName"),jsonObject.getString("text"),jsonObject.getString("date"),jsonObject.getString("imageProfile"));
            try {
                if (jsonObject.has("videoUrl")) this.videoUrl = jsonObject.getString("videoUrl");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public String getVideoUrl() {
            return videoUrl;
        }

    }


