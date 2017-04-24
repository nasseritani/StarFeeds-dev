package com.nader.starfeeds.listing;

import com.nader.starfeeds.data.componenets.model.Feed;
import com.nader.starfeeds.data.componenets.model.FeedFacebookLink;

/**
 * Created by Nasse_000 on 3/6/2017.
 */
public class FeedFacebookLinkItem extends ListingItem {
    private final FeedFacebookLink feed;

    public FeedFacebookLinkItem(FeedFacebookLink feed) {
        this.feed = feed;
        setType(ListingItemType.FeedFacebookLink);
    }

    public Feed getFeed() {
        return feed;
    }
}
