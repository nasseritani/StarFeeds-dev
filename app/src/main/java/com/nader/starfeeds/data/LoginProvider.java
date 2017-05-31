package com.nader.starfeeds.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nader.starfeeds.configuration.Configuration;
import com.nader.starfeeds.data.api.requests.LoginEmailRequest;
import com.nader.starfeeds.data.api.requests.LoginFacebookRequest;
import com.nader.starfeeds.data.api.requests.LoginGoogleRequest;
import com.nader.starfeeds.data.api.requests.RegisterUserRequest;
import com.nader.starfeeds.data.api.responses.ApiResponse;
import com.nader.starfeeds.data.api.responses.LoginEmailResponse;
import com.nader.starfeeds.data.componenets.Provider;
import com.nader.starfeeds.data.componenets.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.nader.starfeeds.data.componenets.Provider.EMAIL_LOGIN;

/**
 * Provides user login via different ways(Facebook Login, Google Login, Email Login)
 */
public class LoginProvider implements FacebookCallback<LoginResult>,
        GoogleApiClient.OnConnectionFailedListener  {
    private Context context;
    private OnLoginProviderListener loginListener;
    // fb
    private CallbackManager callbackManager;
    // google
    private static final int RC_GOOGLE_LOGIN = 9001;
    private static GoogleApiClient mGoogleApiClient;
    private GoogleSignInResult result;

    public LoginProvider(Context context, OnLoginProviderListener loginRequested) {
        this.loginListener = loginRequested;
        this.context = context;
        FacebookSdk.sdkInitialize(context);
    }

    /**
     * Method to login user via email, sends request to SessionManager to store session.
     * @param emailInput is the email of the user.
     * @param passwordInput is the password of the user.
     */
    public void loginViaEmail(String emailInput, String passwordInput) {
       sendEmailLoginRequest(emailInput, passwordInput);
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
    public void registerUser(String name, String email, String password, String country) {
        sendRegisterRequest(name, email, password, country);
    }

    // FACEBOOK //

    /**
     * Requests to login the user using the Facebook SDK.
     * The result will be handled in the callbackManager.
     */
    public void loginViaFB(Activity activity){
        // checks if an accessToken already exists
        if(AccessToken.getCurrentAccessToken() != null ){
            Log.i("logged","Logged in");
        }
        callbackManager = CallbackManager.Factory.create();
        //requests fblogin with public profile and user friends permissions
        LoginManager.getInstance().registerCallback(callbackManager,this);
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile","user_friends"));
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        // request fb login
        loginFacebookUser(loginResult.getAccessToken());

        //Handles the result of login and changes the JSON Object to User Object with equivalent values.
        GraphRequest.newMeRequest(
                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject json, GraphResponse response) {
                        if (response.getError() != null) {
                            // handle error
                            Log.i(Configuration.TAG,"Fb Login Error ERROR");
                        } else {
                            Log.i(Configuration.TAG,json.toString());
                            User user;
                            try {
                                String name = null;
                                String email = null;
                                String id = json.getString("id");
                                if (!json.isNull("name") ) name = json.getString("name");
                                if (!json.isNull("email") ) email = json.getString("email");
                                sendFacebookLoginRequest(id, email, name);
                            } catch (JSONException e) {
                                Log.i(Configuration.TAG, " n: " + e.toString());
                                user = new User("", "", "");
                            }
                        }
                    }

                }).executeAsync();
    }

    /**
     * Handles activity results in case of social login.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(Configuration.TAG,"login provider "+requestCode+"    "+resultCode);
        if (requestCode == RC_GOOGLE_LOGIN) {
            handleGoogleLoginResult(data);
        }
        else {
            Log.i(Configuration.TAG,"result");
            if(callbackManager != null)  callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Handles the data received by google login activity result.
     * @param data
     */
    private void handleGoogleLoginResult(Intent data) {
        if (data != null) {
            result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result != null) {
                try {
                    GoogleSignInAccount account = result.getSignInAccount();
                    if (account!=null) {
                        User user = new User(result.getSignInAccount().getDisplayName(), result.getSignInAccount().getEmail(), "");
                        GoogleSignInAccount signInResult = result.getSignInAccount();
                        String id = signInResult.getId();
                        String name = signInResult.getGivenName();
                        String email = signInResult.getEmail();
                        Log.i(Configuration.TAG, "Google login success");
                        if (id != null)
                        sendGoogleLoginRequest(id, name, email);
                        else{
                            Toast.makeText(context,"Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                        /* SessionManager.getInstance().storeSession(user, false);
                        loginListener.onLoginSuccess(user);*/
                    }
                } catch (Exception e) {
                    loginListener.onLoginFailed(e.getMessage());
                }
            }
            //logout
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            }
        }
    }

    /**
     * Triggers the google login process.
     * @param fragmentActivity
     */
    public void loginViaGoogle(FragmentActivity fragmentActivity) {
        // check if not initialized previously
        if (mGoogleApiClient == null) {
            // init sign in objects
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .enableAutoManage(fragmentActivity, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
            Log.i(Configuration.TAG,"result null");
        }
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        fragmentActivity.startActivityForResult(signInIntent, RC_GOOGLE_LOGIN);
    }



    @Override
    public void onCancel() {
        if (loginListener != null) {
            loginListener.onLoginCanceled();
        }
    }

    @Override
    public void onError(FacebookException error) {
        loginListener.onLoginFailed(error.toString());
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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


    private void loginFacebookUser(AccessToken accessToken) {
    }

    // API //

    private void sendEmailLoginRequest(@NonNull String email, @NonNull final String password) {
        LoginEmailRequest apiRequest = new LoginEmailRequest();
        // create rx subscription
        Subscription loginSubscription = apiRequest.login(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ApiResponse>() {
                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        LoginEmailResponse response = (LoginEmailResponse) apiResponse;
                        User user = response.getUser();
                        if (user != null) {
                            handleLoginResponse(user, EMAIL_LOGIN, false);
                        } else{
                            handleLoginError("Wrong email/password");
                        }
                    }
                    @Override
                    public void onError(Throwable error) {
                        handleLoginError("Wrong email/password");
                    }
                });
    }

    private void sendRegisterRequest(@NonNull String name, @NonNull String email,
                                     @NonNull final String password, @NonNull final String country) {
        RegisterUserRequest apiRequest = new RegisterUserRequest();
        // create rx subscription
        Subscription loginSubscription = apiRequest.register(name, email, password, country)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ApiResponse>() {
                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        LoginEmailResponse response = (LoginEmailResponse) apiResponse;
                        User user = response.getUser();
                        handleLoginResponse(user, EMAIL_LOGIN, true);
                    }
                    @Override
                    public void onError(Throwable error) {
                        handleLoginError("Email already registered");
                    }
                });
    }


    private void sendFacebookLoginRequest(@NonNull String id,String email, final String name) {
        LoginFacebookRequest apiRequest = new LoginFacebookRequest();
        // create rx subscription
        Subscription loginSubscription = apiRequest.login(id, email, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ApiResponse>() {
                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        LoginEmailResponse response = (LoginEmailResponse) apiResponse;
                        User user = response.getUser();
                        handleLoginResponse(user,Provider.Fb_LOGIN, false);
                    }
                    @Override
                    public void onError(Throwable error) {
                        handleLoginError("Something went wrong, try again later");
                    }
                });
    }

    private void sendGoogleLoginRequest(@NonNull String id, final String name, String email) {
        LoginGoogleRequest apiRequest = new LoginGoogleRequest();
        // create rx subscription
        Subscription loginSubscription = apiRequest.login(id, email, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ApiResponse>() {
                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        LoginEmailResponse response = (LoginEmailResponse) apiResponse;
                        User user = response.getUser();
                        handleLoginResponse(user,Provider.GOOGLE_LOGIN, false);
                    }
                    @Override
                    public void onError(Throwable error) {
                        handleLoginError("Something went wrong, try again later");
                    }
                });
    }


    private void handleLoginError(String message) {
        loginListener.onLoginFailed(message);
    }

    private void handleLoginResponse(User user, Provider type, boolean isLogin) {
        if (user != null) {
            user.setProvider(type);
            SessionManager.getInstance().storeSession(user, isLogin);
            loginListener.onLoginSuccess(user);
        }
        else{
            loginListener.onLoginFailed("Login Failed");
        }
    }

}
