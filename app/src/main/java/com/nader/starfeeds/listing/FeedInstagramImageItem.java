package com.nader.starfeeds.listing;

import com.nader.starfeeds.data.componenets.model.Feed;
import com.nader.starfeeds.data.componenets.model.FeedInstagramImage;

/**
 * Created by Nasse_000 on 3/31/2017.
 */
public class FeedInstagramImageItem extends ListingItem {
    private final FeedInstagramImage feed;

    public FeedInstagramImageItem(FeedInstagramImage feed) {
        this.feed = feed;
        setType(ListingItemType.FeedInstagramImage);
    }

    public Feed getFeed() {
        return feed;
    }
}
