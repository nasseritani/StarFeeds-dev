package com.nader.starfeeds.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;


import com.nader.starfeeds.Configuration.Configuration;
import com.nader.starfeeds.components.Provider;
import com.nader.starfeeds.components.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static com.nader.starfeeds.components.Provider.EMAIL_LOGIN;

/**
 * Provides user login via different ways(Facebook Login, Google Login, Email Login)
 */
public class LoginProvider {
    private Context context;
    private OnLoginProviderListener loginListener;

    public LoginProvider(Context context, OnLoginProviderListener loginRequested) {
        this.loginListener = loginRequested;
        this.context = context;
    }


    /**
     * Method to login user via email, sends request to SessionManager to store session.
     * @param emailInput is the email of the user.
     * @param passwordInput is the password of the user.
     */
    public void loginViaEmail(String emailInput, String passwordInput) {
        User user = new User(emailInput, passwordInput);
        user.setId((int) Math.random()*100 + "");
        user.setProvider(EMAIL_LOGIN);
        SessionManager.getInstance().storeSession(user);
    }

    /**
     * Method to logout current user
     */
    public void logOut() {
        User user = SessionManager.getInstance().getSessionUser();
        Log.i(Configuration.TAG, "provider" + user.getProvider());
        if (user.getProvider() != null) {
            switch (user.getProvider()) {
                case EMAIL_LOGIN:
                    SessionManager.getInstance().clearSession();
                    loginListener.onLogOutSuccess();
                    break;
            }
        } else {
            SessionManager.getInstance().clearSession();
            loginListener.onLogOutSuccess();
        }
    }

    /**
     * Handles the registration of a user.
     */
    public void registerUser(String name, String email, String password) {
        User user = new User(name, email, password);
        user.setId((int) Math.random()*100 + "");
        user.setProvider(EMAIL_LOGIN);
        SessionManager.getInstance().storeSession(user);
    }


    // CALLBACK //

        /**
        * Interface definition for callbacks to be invoked
        * when events occur in {@link LoginProvider}.
        */
    public interface OnLoginProviderListener {

         /**
         * Called once Login is successful.
         * @param user
         */
        void onLoginSuccess(User user);

        /**
         * Called once login is failed
         * @param error is the message describing the error
         */
        void onLoginFailed(String error);

        /**
         * Called once login is canceled
         */
        void onLoginCanceled();

        void onLogOutSuccess();
    }

    // API //
}
