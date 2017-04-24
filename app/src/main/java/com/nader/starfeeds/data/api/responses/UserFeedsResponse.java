package com.nader.starfeeds.data.api.responses;

import com.nader.starfeeds.data.componenets.model.Feed;

import java.util.ArrayList;

/**
 * Created by Nader on 17-Apr-17.
 */

public class UserFeedsResponse implements ApiResponse {
    private ArrayList<Feed> userFeeds;

    public UserFeedsResponse(ArrayList<Feed> userFeeds) {
        this.userFeeds = userFeeds;
    }

    public ArrayList<Feed> getUserFeeds(){
        return userFeeds;
    }
}
