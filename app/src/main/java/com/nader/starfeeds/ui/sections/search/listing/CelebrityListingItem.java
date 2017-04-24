package com.nader.starfeeds.ui.sections.search.listing;

import com.nader.starfeeds.data.componenets.model.Celebrity;

/**
 * Created by Nader on 19-Apr-17.
 */

public class CelebrityListingItem extends SearchListingItem {
    private final Celebrity celebrity;

    public CelebrityListingItem(Celebrity celebrity) {
        this.celebrity = celebrity;
        setType(ListingSearchType.CELEBRITY);
    }

    public Celebrity getCelebrity() {
        return celebrity;
    }
}
