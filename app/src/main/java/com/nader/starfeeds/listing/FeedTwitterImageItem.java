package com.nader.starfeeds.listing;


import com.nader.starfeeds.data.componenets.model.Feed;
import com.nader.starfeeds.data.componenets.model.FeedTwitterImage;

/**
 * Created by Nasse_000 on 3/6/2017.
 */
public class FeedTwitterImageItem extends ListingItem {

    private final FeedTwitterImage feed;

    public FeedTwitterImageItem(FeedTwitterImage feed) {
        this.feed = feed;
        setType(ListingItemType.FeedTwitterImage);
    }

    public Feed getFeed() {
        return feed;
    }
}
