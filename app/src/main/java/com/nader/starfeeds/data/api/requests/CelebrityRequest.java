package com.nader.starfeeds.data.api.requests;

import android.support.annotation.NonNull;

import com.nader.starfeeds.data.api.ApiConfig;
import com.nader.starfeeds.data.api.responses.ApiResponse;
import com.nader.starfeeds.data.api.responses.CelebrityResponse;
import com.nader.starfeeds.data.componenets.model.Celebrity;

import org.json.JSONObject;

import rx.Single;

/**
 * Created by Nader on 24-Apr-17.
 */

public class CelebrityRequest extends ApiRequest {

    public Single<ApiResponse> fetchCelebrity(@NonNull String celebId, @NonNull String userId) {
        // base url
        String url;
        url = ApiConfig.getApiHost() + "/celeb/" + celebId +"/" +userId;
        // create request
        return sendGetRequest(url);
    }

    @Override
    protected ApiResponse processSuccessResult(String result) throws Exception {
        Celebrity celebrity;
        try{
            JSONObject jsonObject = new JSONObject(result);
            jsonObject = jsonObject.getJSONObject("data");
            celebrity = new Celebrity(jsonObject);
        } catch (Exception e){
           celebrity = null;
        }
        return new CelebrityResponse(celebrity);
    }
}
