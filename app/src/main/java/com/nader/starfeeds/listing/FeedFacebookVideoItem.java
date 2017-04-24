package com.nader.starfeeds.listing;


import com.nader.starfeeds.data.componenets.model.Feed;
import com.nader.starfeeds.data.componenets.model.FeedFacebookVideo;

/**
 * Created by Nasse_000 on 3/11/2017.
 */
public class FeedFacebookVideoItem extends ListingItem {
    private final FeedFacebookVideo feed;

    public FeedFacebookVideoItem(FeedFacebookVideo feed) {
        this.feed = feed;
        setType(ListingItemType.FeedFacebookVideo);
    }

    public Feed getFeed() {
        return feed;
    }
}
