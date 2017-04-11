package com.nader.starfeeds.components;

import java.io.Serializable;

/**
 * Created by Nader on 12-Feb-17.
 */

public abstract class Feed implements Serializable {

    public enum FeedType{FACEBOOK_IMAGE,FACEBOOK_VIDEO, FACEBOOK_TEXT,TWITTER_IMAGE,TWITTER_TEXT,FACEBOOK, INSTAGRAM_IMAGE, INSTAGRAM_VIDEO, TWITTER}
    private FeedType feedType;
    private String id;
    private String celebName;
    private String text;
    private String date;
    private String profileImage;
    String imageUrl;
    public Feed(FeedType feedType,String id, String celebName, String text, String date,String profileImage)  {
        this.feedType = feedType;
        this.id = id;
        this.celebName = celebName;
        this.text = text;
        this.date = date;
        this.profileImage=profileImage;
    }

    public String getId() {
        return id;
    }

    public String getCelebName() {
        return celebName;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }
    //test

    public String getProfileImage() {
        return profileImage;
    }

    public FeedType getFeedType() {
        return feedType;
    }
}
