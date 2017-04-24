package com.nader.starfeeds.data.api.responses;

/**
 * Created by Nader on 22-Apr-17.
 */

public class PostRequestResponse implements ApiResponse {
    boolean isRequestSuccessful;

    public PostRequestResponse(boolean isRequestSuccessful) {
        this.isRequestSuccessful = isRequestSuccessful;
    }

    public boolean isRequestSuccesful() {
        return isRequestSuccessful;
    }
}
