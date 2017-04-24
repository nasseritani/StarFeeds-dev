package com.nader.starfeeds.data.componenets.model;

import org.json.JSONObject;

/**
 * Created by Nader on 19-Apr-17.
 */

public class Celebrity {
    private String name;
    private String id;
    private String profileUrl;
    private String fbId;
    private String twtId;
    private String instaId;
    private boolean isFollowed;

    public Celebrity(JSONObject jsonObject) {
        try{
            if (jsonObject.has("is_followed")) this.isFollowed = jsonObject.getBoolean("is_followed");
            jsonObject = jsonObject.getJSONObject("celeb");
            if (jsonObject.has("name")) this.name = jsonObject.getString("name");
            if (jsonObject.has("id")) this.id = jsonObject.getString("id");
            if (jsonObject.has("fb_profile_url")) this.profileUrl = jsonObject.getString("fb_profile_url");
            if (jsonObject.has("fb_id")) this.fbId = jsonObject.getString("fb_id");
            if (jsonObject.has("twt_id")) this.twtId = jsonObject.getString("twt_id");
            if (jsonObject.has("insta_id")) this.instaId = jsonObject.getString("insta_id");
        } catch (Exception e){

        }
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getFbId() {
        return fbId;
    }

    public String getTwtId() {
        return twtId;
    }

    public String getInstaId() {
        return instaId;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }
}
