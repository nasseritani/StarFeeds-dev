package com.nader.starfeeds.listing;

import com.nader.starfeeds.data.componenets.model.Celebrity;
import com.nader.starfeeds.data.componenets.model.Feed;

/**
 * Created by Nader on 24-Apr-17.
 */

public class CelebrityProfileItem extends ListingItem {

    private final Celebrity celebrity;

    public CelebrityProfileItem(Celebrity celebrity) {
        this.celebrity = celebrity;
        setType(ListingItemType.CELEBRITY_PROFILE);
    }

    public Celebrity getCelebrityProfile() {
        return celebrity;
    }
}
