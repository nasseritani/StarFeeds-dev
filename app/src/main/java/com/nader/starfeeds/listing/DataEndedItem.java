package com.nader.starfeeds.listing;


import com.nader.starfeeds.data.componenets.DataEnded;

/**
 * Created by Nader on 20-Apr-17.
 */

public class DataEndedItem extends ListingItem {
    private final DataEnded dataEnded;

    public DataEndedItem(DataEnded dataEnded) {
        this.dataEnded = dataEnded;
        setType(ListingItemType.ENDED);
    }

    public DataEnded getDataEnded() {
        return dataEnded;
    }
}
