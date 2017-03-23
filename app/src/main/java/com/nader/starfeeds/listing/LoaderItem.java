package com.nader.starfeeds.listing;


import com.nader.starfeeds.components.Loader;

/**
 * Created by Nader on 23-Aug-16.
 */

public class LoaderItem extends ListingItem {
    private final Loader loader;

    public LoaderItem(Loader loader) {
        this.loader = loader;
        setType(ListingItemType.Loader);
    }

    public Loader getLoader() {
        return loader;
    }
}
