package com.nader.starfeeds.data.api.requests;

import android.support.annotation.NonNull;

import com.nader.starfeeds.data.api.ApiConfig;
import com.nader.starfeeds.data.api.responses.ApiResponse;
import com.nader.starfeeds.data.api.responses.CelebritiesResponse;
import com.nader.starfeeds.data.componenets.model.Celebrity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import rx.Single;

/**
 * Created by Nader on 20-Apr-17.
 */

public class CelebritiesFollowedRequest extends ApiRequest {

    public Single<ApiResponse> fetchCelebFollowing(@NonNull String id) {
        // base url
        String url;
        url = ApiConfig.getApiHost() + "/user-following/" + id;
        // create request
        return sendGetRequest(url);
    }

    @Override
    protected ApiResponse processSuccessResult(String result) throws Exception {
        ArrayList<Celebrity> celebrities = new ArrayList<>();
        try {
            if (result != null) {
                JSONObject jsonResult = new JSONObject(result);
                JSONArray data = jsonResult.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonObject = data.getJSONObject(i);
                    Celebrity celebrity = new Celebrity(jsonObject);
                    celebrities.add(celebrity);
                }
            }
        } catch (Exception e){
            celebrities = null;
        }
        return new CelebritiesResponse(celebrities);
    }

}