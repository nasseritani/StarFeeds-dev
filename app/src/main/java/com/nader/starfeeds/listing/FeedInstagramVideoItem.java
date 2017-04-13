package com.nader.starfeeds.listing;

import com.nader.starfeeds.components.Feed;
import com.nader.starfeeds.components.FeedInstagramVideo;

/**
 * Created by Nasse_000 on 3/31/2017.
 */
public class FeedInstagramVideoItem extends ListingItem {
    private final FeedInstagramVideo feed;

    public FeedInstagramVideoItem(FeedInstagramVideo feed) {
        this.feed = feed;
        setType(ListingItemType.FeedInstagramVideo);
    }

    public Feed getFeed() {
        return feed;
    }
}
