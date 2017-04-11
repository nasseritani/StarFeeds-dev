package com.nader.starfeeds.components;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nasse_000 on 3/6/2017.
 */
public class FeedTwitterText extends Feed {

    public FeedTwitterText(JSONObject jsonObject) throws JSONException {
        super(FeedType.TWITTER_TEXT,jsonObject.getString("id"),jsonObject.getString("celebName"),jsonObject.getString("text"),jsonObject.getString("date"),jsonObject.getString("imageProfile"));
    }
}
