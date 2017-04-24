package com.nader.starfeeds.data.componenets;

import com.nader.starfeeds.data.NetworkErrorType;

/**
 * Created by Nader on 28-Oct-16.
 */

public class Reloader {
    NetworkErrorType errorType;
    public Reloader(NetworkErrorType errorType) {
        this.errorType = errorType;
    }

    public NetworkErrorType getErrorType() {
        return errorType;
    }
}
