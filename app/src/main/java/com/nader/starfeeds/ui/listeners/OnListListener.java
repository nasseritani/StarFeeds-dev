package com.nader.starfeeds.ui.listeners;

import com.nader.starfeeds.data.componenets.model.Celebrity;

/**
 * Created by Nader on 16-Aug-16.
 */

public interface OnListListener {

    /**
     * Indicates the click of reloader button.
     */
    void onReloaderButtonSelected();
    void onFollowClick(String celebrity);
    void onUnFollowClick(String celebrity);

}
