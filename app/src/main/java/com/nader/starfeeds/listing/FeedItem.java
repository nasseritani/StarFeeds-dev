package com.nader.starfeeds.listing;

import com.nader.starfeeds.components.Feed;

/**
 * Created by Nader on 12-Feb-17.
 */

public class FeedItem extends ListingItem {
    final Feed feed;

    public FeedItem(Feed feed) {
        this.feed = feed;

    }

    public Feed getFeed() {
        return feed;
    }
}
