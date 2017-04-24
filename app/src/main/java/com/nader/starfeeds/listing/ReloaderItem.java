package com.nader.starfeeds.listing;


import com.nader.starfeeds.data.componenets.Reloader;

/**
 * Created by Nader on 28-Oct-16.
 */

public class ReloaderItem extends ListingItem {
    private final Reloader reloader;

    public ReloaderItem(Reloader reloader) {
        this.reloader = reloader;
        setType(ListingItemType.Reloader);
    }

    public Reloader getReloader() {
        return reloader;
    }
}
