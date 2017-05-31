package com.nader.starfeeds.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.nader.starfeeds.configuration.Configuration;
import com.nader.starfeeds.data.componenets.model.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Manages the session of a logged in mUser.
 */

public class SessionManager {
    private static final String KEY_IS_NEW = "new";
    private static SessionManager sessionManager = null;
    private String KEY_USER = "user";
    // objects
    private User mUser;
    private boolean mIsLoggedIn;
    private SharedPreferences mSharedPreferences;
    private boolean isNew;

    private SessionManager(){
        initSharedPreference();
        checkSessionState();
    }

    public static SessionManager getInstance(){
        if(sessionManager == null) sessionManager = new SessionManager();
        return sessionManager;
    }

    // SHARED PREF //

    /**
     * Initialize SharedPreference
     */
    private void initSharedPreference(){
        mSharedPreferences = Configuration.getContext().getSharedPreferences("SessionManager", Context.MODE_PRIVATE) ;
    }

    /**
     * Checks mIsLoggedIn in mSharedPreferences and sets mIsLoggedIn accordingly
     */
    private void checkSessionState() {
        // get logged in mUser
        mUser = getStoredUser();
        isNew = getIsNew();
        // check if valid
        mIsLoggedIn = (mUser != null && mUser.getId() != null);
    }

    /**
     *
     * @return stored mUser in sharedPreference
     */
    private User getStoredUser() {
        //Get the mUser's variables, if key is not found, will return null.
        String JSONString = mSharedPreferences.getString(KEY_USER, null);
        // create mUser object from json
        if( JSONString != null ) {
            try {
                JSONObject userJSON = new JSONObject(JSONString);
                mUser = new User(userJSON);
            } catch (JSONException e) {
                mUser = null;
            }
        }
        return mUser;
    }

    /**
     * Stores the given User in SharedPreference.
     */
    private void storeUser(User user){
        // instantiate SharedPreferences
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        // stores each attribute in a variable, if attribute is null the editor will clear the corresponding value.
        editor.putString(KEY_USER, user.toJSONString());
        // commit changes
        editor.apply();
    }

    /**
     * Stores the given User in SharedPreference.
     */
    public void storeIsNew(boolean isNew){
        // instantiate SharedPreferences
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        // stores each attribute in a variable, if attribute is null the editor will clear the corresponding value.
        editor.putBoolean(KEY_IS_NEW, isNew);
        // commit changes
        editor.apply();
    }

    /**
     * Clears the data in sharedPreference
     */
    private void clearSharedPreference() {
        //Instantiate SharedPreferences
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        //clears all saved data in SharedPreferences with tag sm_tag
        editor.clear();
        editor.apply();
    }

    private boolean getIsNew(){
        return mSharedPreferences.getBoolean(KEY_IS_NEW, false);
    }

    /**
     * Stores a new logged in session data.
     * @param user The logged in User.
     */
    public void storeSession(@NonNull User user, boolean isNew){
        //calls storeUser method with mUser as argument
        storeUser(user);
        storeIsNew(isNew);
        // validate input data
        checkSessionState();
    }

    /**
     *
     * @return User logged in if found, else returns null.
     */
    public User getSessionUser(){
        return this.mUser;
    }

    public boolean isNew(){return isNew;}

    /**
     * Clears any stored session.
     */
    public void clearSession(){
        mIsLoggedIn = false;
        mUser = null;
        clearSharedPreference();
    }

    /**
     * Returns true if logged in, false otherwise.
     */
    public boolean isLoggedIn(){
        return mIsLoggedIn;
    }

    public enum SessionState{
        LOGGED_IN,LOGGED_OUT
    }
    public interface SessionManagerListener{
        public void onStateChanged(SessionState sessionState);
    }

}
