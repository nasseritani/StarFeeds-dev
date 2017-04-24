package com.nader.starfeeds.listing;

import com.nader.starfeeds.data.componenets.model.Feed;
import com.nader.starfeeds.data.componenets.model.FeedTwitterLink;

/**
 * Created by Nasse_000 on 3/30/2017.
 */
public class FeedTwitterLinkItem extends ListingItem {
    private final FeedTwitterLink feed;

    public FeedTwitterLinkItem(FeedTwitterLink feed) {
        this.feed = feed;
        setType(ListingItemType.FeedTwitterLink);
    }
    public Feed getFeed() {
        return feed;
    }
}
