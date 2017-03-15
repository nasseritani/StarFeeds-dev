package com.nader.starfeeds.components;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nasse_000 on 3/11/2017.
 */

    public class FeedFacebookVideo extends Feed {
        String link;

        public FeedFacebookVideo(JSONObject jsonObject) throws JSONException {
            super(FeedType.FACEBOOK_VIDEO,jsonObject.getString("id"),jsonObject.getString("celebName"),jsonObject.getString("text"),jsonObject.getString("date"));
            try {
                if (jsonObject.has("link")) this.link = jsonObject.getString("link");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public String getLink() {
            return link;
        }

    }


