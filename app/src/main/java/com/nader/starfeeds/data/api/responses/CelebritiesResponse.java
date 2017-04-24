package com.nader.starfeeds.data.api.responses;

import com.nader.starfeeds.data.componenets.model.Celebrity;

import java.util.ArrayList;

/**
 * Created by Nader on 19-Apr-17.
 */

public class CelebritiesResponse implements ApiResponse {
    private ArrayList<Celebrity> celebrities;

    public CelebritiesResponse(ArrayList<Celebrity> celebrities) {
        this.celebrities = celebrities;
    }

    public ArrayList<Celebrity> getCelebrities(){
        return celebrities;
    }
}
