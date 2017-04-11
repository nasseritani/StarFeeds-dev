package com.nader.starfeeds.listing;

import com.nader.starfeeds.components.Feed;

/**
 * Created by Nader on 23-Aug-16.
 */

public class ListingItem {
    protected ListingItemType type;
    protected Feed feed;
    public Feed getFeed() {
        return feed;
    }

    public ListingItemType getType() {
        return type;
    }

    protected void setType(ListingItemType type) {
        this.type = type;
    }
}
