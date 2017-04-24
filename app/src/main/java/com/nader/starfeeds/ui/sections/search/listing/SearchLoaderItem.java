package com.nader.starfeeds.ui.sections.search.listing;

import com.nader.starfeeds.data.componenets.Loader;

/**
 * Created by Nader on 19-Apr-17.
 */

public class SearchLoaderItem extends SearchListingItem {
    private final Loader loader;

    public SearchLoaderItem(Loader loader) {
        this.loader = loader;
        setType(ListingSearchType.LOADER);
    }

    public Loader getLoader() {
        return loader;
    }
}
