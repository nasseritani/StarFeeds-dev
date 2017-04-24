package com.nader.starfeeds.listing;

import com.nader.starfeeds.data.componenets.model.Feed;
import com.nader.starfeeds.data.componenets.model.FeedFacebookText;

/**
 * Created by Nasse_000 on 3/6/2017.
 */
public class FeedFacebookTextItem extends ListingItem {

    private final FeedFacebookText feed;

    public FeedFacebookTextItem(FeedFacebookText feed) {
        this.feed = feed;
        setType(ListingItemType.FeedFaceBookText);
    }

    public Feed getFeed() {
        return feed;
    }
}
