package com.nader.starfeeds.listing;

import com.nader.starfeeds.components.Feed;
import com.nader.starfeeds.components.FeedFacebook;

/**
 * Created by Nasse_000 on 3/6/2017.
 */
public class FeedFacebookItem extends ListingItem {
    private final FeedFacebook feed;

    public FeedFacebookItem(FeedFacebook feed) {
        this.feed = feed;
        setType(ListingItemType.FeedFacebook);
    }

    public Feed getFeed() {
        return feed;
    }
}
