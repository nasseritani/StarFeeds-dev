package com.nader.starfeeds.data.api.requests;

import android.support.annotation.NonNull;

import com.nader.starfeeds.data.api.ApiConfig;
import com.nader.starfeeds.data.api.responses.ApiResponse;
import com.nader.starfeeds.data.api.responses.UserFeedsResponse;
import com.nader.starfeeds.data.componenets.model.Feed;
import com.nader.starfeeds.data.componenets.model.FeedFacebookImage;
import com.nader.starfeeds.data.componenets.model.FeedFacebookLink;
import com.nader.starfeeds.data.componenets.model.FeedFacebookText;
import com.nader.starfeeds.data.componenets.model.FeedFacebookVideo;
import com.nader.starfeeds.data.componenets.model.FeedInstagramImage;
import com.nader.starfeeds.data.componenets.model.FeedInstagramVideo;
import com.nader.starfeeds.data.componenets.model.FeedTwitterImage;
import com.nader.starfeeds.data.componenets.model.FeedTwitterLink;
import com.nader.starfeeds.data.componenets.model.FeedTwitterText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import rx.Single;

/**
 * Created by Nader on 17-Apr-17.
 */

public class UserFeedsRequest extends ApiRequest {

    public Single<ApiResponse> fetchUserFeeds(@NonNull String id, int page) {
        // base url
        String url;
        url = ApiConfig.getApiHost() + "/user-feeds/" + id +"/"+ page;
        // create request
        return sendGetRequest(url);
    }

    @Override
    protected ApiResponse processSuccessResult(String result) throws Exception {
        ArrayList<Feed> feeds = new ArrayList<>();
        try {
            if (result != null) {
                JSONObject jsonResult = new JSONObject(result);
                JSONArray jsonArray = jsonResult.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String feedType = jsonObject.getString("feedType");
                    Feed feed = null;
                    switch (feedType) {
                        case "twitter_photo":
                            feed = new FeedTwitterImage(jsonObject);
                            feeds.add(feed);
                            break;
                        case "twitter_twitter_text":
                            feed = new FeedTwitterText(jsonObject);
                            feeds.add(feed);
                            break;
                        case "twitter_shared_story":
                            feed = new FeedTwitterLink(jsonObject);
                            feeds.add(feed);
                            break;
                        case "facebook_added_photos":
                            feed = new FeedFacebookImage(jsonObject);
                            feeds.add(feed);
                            break;
                        case "facebook_shared_story":
                            feed = new FeedFacebookLink(jsonObject);
                            feeds.add(feed);
                            break;
                        case "facebook_facebook_text":
                            feed = new FeedFacebookText(jsonObject);
                            feeds.add(feed);
                            break;
                        case "facebook_mobile_status_update":
                            feed = new FeedFacebookText(jsonObject);
                            feeds.add(feed);
                            break;
                        case "instagram_image":
                            feed = new FeedInstagramImage(jsonObject);
                            feeds.add(feed);
                            break;
                        case "instagram_video":
                            feed = new FeedInstagramVideo(jsonObject);
                            feeds.add(feed);
                            break;
                        case "facebook_added_video":
                            feed = new FeedFacebookVideo(jsonObject);
                            feeds.add(feed);
                            break;
                    }
                    //   feeds.add(feed);
                }
            }
        } catch (Exception e){
            feeds = null;
        }
        return new UserFeedsResponse(feeds);
    }
}