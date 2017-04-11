package com.nader.starfeeds.listing;

import com.nader.starfeeds.components.Feed;
import com.nader.starfeeds.components.FeedTwitterLink;

/**
 * Created by Nasse_000 on 3/30/2017.
 */
public class FeedTwitterItem extends ListingItem {
    private final FeedTwitterLink feed;

    public FeedTwitterItem(FeedTwitterLink feed) {
        this.feed = feed;
        setType(ListingItemType.FeedTwitter);
    }
    public Feed getFeed() {
        return feed;
    }
}
