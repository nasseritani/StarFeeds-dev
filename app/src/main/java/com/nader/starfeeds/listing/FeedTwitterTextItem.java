package com.nader.starfeeds.listing;


import com.nader.starfeeds.data.componenets.model.Feed;
import com.nader.starfeeds.data.componenets.model.FeedTwitterText;

/**
 * Created by Nasse_000 on 3/6/2017.
 */
public class FeedTwitterTextItem extends ListingItem {
    private final FeedTwitterText feed;

    public FeedTwitterTextItem(FeedTwitterText feed) {
        this.feed = feed;
        setType(ListingItemType.FeedTwitterText);
    }

    public Feed getFeed() {
        return feed;
    }
}

