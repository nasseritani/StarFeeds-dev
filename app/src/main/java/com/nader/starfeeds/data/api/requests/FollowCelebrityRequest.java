package com.nader.starfeeds.data.api.requests;

import android.support.annotation.NonNull;

import com.nader.starfeeds.data.api.ApiConfig;
import com.nader.starfeeds.data.api.responses.ApiResponse;
import com.nader.starfeeds.data.api.responses.PostRequestResponse;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Single;

/**
 * Created by Nader on 22-Apr-17.
 */

public class FollowCelebrityRequest extends ApiRequest {

    public Single<ApiResponse> followCeleb(@NonNull String userId, @NonNull String celebId) {
        // base url
        String url;
        url = ApiConfig.getApiHost() + "/follow";
        // raw params //
        JSONObject rawParams = new JSONObject();
        // mandatory
        try {
            rawParams.put("user_id", userId);
            rawParams.put("celeb_id", celebId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // create request
        return sendPOSTRequest(url, rawParams);
    }

    @Override
    protected ApiResponse processSuccessResult(String result) throws Exception {
        boolean isSuccessful = false;
        try {
            if (result != null) {
                JSONObject jsonObject = new JSONObject(result);
                isSuccessful = jsonObject.getBoolean("is_successful");
            }
        } catch (Exception e){
            isSuccessful = false;
        }
        return new PostRequestResponse(isSuccessful);
    }
}
