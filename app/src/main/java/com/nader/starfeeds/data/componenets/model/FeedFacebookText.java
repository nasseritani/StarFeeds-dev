package com.nader.starfeeds.data.componenets.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nader on 03-Mar-17.
 */

public class FeedFacebookText extends Feed {

    public FeedFacebookText(JSONObject jsonObject) throws JSONException {
        super(FeedType.FACEBOOK_TEXT,jsonObject.getString("id"),jsonObject.getString("celebName"),jsonObject.getString("celebId"),jsonObject.getString("text"),jsonObject.getString("date"),jsonObject.getString("imageProfile"));
    }
}
