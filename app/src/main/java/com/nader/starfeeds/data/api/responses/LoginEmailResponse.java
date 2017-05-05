package com.nader.starfeeds.data.api.responses;

import com.nader.starfeeds.data.componenets.model.User;

/**
 * Created by Nader on 22-Apr-17.
 */

public class LoginEmailResponse implements ApiResponse {
    private User user;

    public LoginEmailResponse(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
