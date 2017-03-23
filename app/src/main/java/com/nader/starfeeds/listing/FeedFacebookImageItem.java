package com.nader.starfeeds.listing;


import com.nader.starfeeds.components.Feed;
import com.nader.starfeeds.components.FeedFacebookImage;

/**
 * Created by Nader on 15-Feb-17.
 */

public class FeedFacebookImageItem extends ListingItem {

    private final FeedFacebookImage feed;

    public FeedFacebookImageItem(FeedFacebookImage feed) {
        this.feed = feed;
        setType(ListingItemType.FeedFacebookImage);
    }

    public Feed getFeed() {
        return feed;
    }
}
