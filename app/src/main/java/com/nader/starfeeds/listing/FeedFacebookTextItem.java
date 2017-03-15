package com.nader.starfeeds.listing;

import com.nader.starfeeds.components.Feed;
import com.nader.starfeeds.components.FeedFacebookImage;
import com.nader.starfeeds.components.FeedFacebookText;

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
