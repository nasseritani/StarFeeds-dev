package com.nader.starfeeds.data.api.requests;

import android.support.annotation.NonNull;

import com.nader.starfeeds.data.api.ApiConfig;
import com.nader.starfeeds.data.api.responses.ApiResponse;
import com.nader.starfeeds.data.api.responses.LoginEmailResponse;
import com.nader.starfeeds.data.api.responses.PostRequestResponse;
import com.nader.starfeeds.data.componenets.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Single;

/**
 * Created by Nader on 22-Apr-17.
 */

public class LoginEmailRequest extends ApiRequest {

    public Single<ApiResponse> login(@NonNull String email, @NonNull String password) {
        // base url
        String url;
        url = ApiConfig.getApiHost() + "/api/login-email";
        // raw params //
        JSONObject rawParams = new JSONObject();
        // mandatory
        try {
            rawParams.put("email", email);
            rawParams.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // create request
        return sendPOSTRequest(url, rawParams);
    }

    @Override
    protected ApiResponse processSuccessResult(String result) throws Exception {
        User user = null;
        try {
            if (result != null) {
                JSONObject jsonObject = new JSONObject(result);
                jsonObject = jsonObject.getJSONObject("data");
                user = new User(jsonObject.getJSONObject("user"));
            }
        } catch (Exception e){
            user = null;
        }
        return new LoginEmailResponse(user);
    }
}
