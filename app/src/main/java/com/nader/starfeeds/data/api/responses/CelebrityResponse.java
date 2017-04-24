package com.nader.starfeeds.data.api.responses;

import com.nader.starfeeds.data.componenets.model.Celebrity;

/**
 * Created by Nader on 24-Apr-17.
 */

public class CelebrityResponse implements ApiResponse {
    Celebrity celebrity;

    public CelebrityResponse(Celebrity celebrity) {
        this.celebrity = celebrity;
    }

    public Celebrity getCelebrity() {
        return celebrity;
    }
}
