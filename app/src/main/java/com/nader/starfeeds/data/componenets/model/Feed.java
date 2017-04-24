package com.nader.starfeeds.data.componenets.model;

import java.io.Serializable;

/**
 * Created by Nader on 12-Feb-17.
 */

public abstract class Feed implements Serializable {

    public enum FeedType{
        FACEBOOK_IMAGE,
        FACEBOOK_VIDEO,
        FACEBOOK_TEXT,
        TWITTER_IMAGE,
        TWITTER_TEXT,
        TWITTER_LINK,
        FACEBOOK_LINK,
        INSTAGRAM_IMAGE,
        INSTAGRAM_VIDEO,
    }
    private FeedType feedType;
    private String id;
    private String celebName;
    private String celebId;
    private String text;
    private String date;
    private String profileImage;

    public Feed(FeedType feedType,String id, String celebName,String celebId,String text, String date,String profileImage)  {
        this.feedType = feedType;
        this.id = id;
        this.celebName = celebName;
        this.celebId = celebId;
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

    public String getCelebId() {
        return celebId;
    }

}
