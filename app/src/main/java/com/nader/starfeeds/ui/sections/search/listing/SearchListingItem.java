package com.nader.starfeeds.ui.sections.search.listing;

import com.nader.starfeeds.data.componenets.model.Celebrity;

/**
 * Created by Nader on 23-Aug-16.
 */

public class SearchListingItem {
    protected ListingSearchType type;
    protected Celebrity celebrity;

    public Celebrity getCelebrity() {
        return celebrity;
    }

    public ListingSearchType getType() {
        return type;
    }

    protected void setType(ListingSearchType type) {
        this.type = type;
    }
}
