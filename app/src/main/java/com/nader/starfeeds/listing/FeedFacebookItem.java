package com.nader.starfeeds.listing;

import com.nader.starfeeds.components.Feed;
import com.nader.starfeeds.components.FeedFacebookLink;

/**
 * Created by Nasse_000 on 3/6/2017.
 */
public class FeedFacebookItem extends ListingItem {
    private final FeedFacebookLink feed;

    public FeedFacebookItem(FeedFacebookLink feed) {
        this.feed = feed;
        setType(ListingItemType.FeedFacebook);
    }

    public Feed getFeed() {
        return feed;
    }
}
