package com.nader.starfeeds.ui.sections.authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.nader.starfeeds.configuration.Configuration;
import com.nader.starfeeds.R;
import com.nader.starfeeds.data.componenets.model.User;
import com.nader.starfeeds.data.LoginProvider;

public class AuthenticationActivity extends AppCompatActivity implements
        LoginOptionsFragment.OnOptionsFragmentInteractionListener,
        LoginFragment.OnLoginFragmentInteractionListener,
        RegisterFragment.OnRegisterFragmentInteractionListener,
        LoginProvider.OnLoginProviderListener {
    ProgressDialog pdLoader;
    Handler handler = new Handler();
    Fragment fragment;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        initPlLoader();
        initFragmentBehavior();
        initToolbar();
    }

    private void initPlLoader() {
        pdLoader = new ProgressDialog(this);
        pdLoader.setMessage("Logging you in...");
        pdLoader.setCancelable(true);
        pdLoader.setCanceledOnTouchOutside(true);
        //pdLoader.setIndeterminate(true);
    }


    private void initFragmentBehavior() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new LoginOptionsFragment();
        fragmentTransaction.add(R.id.flContainer, fragment);
        fragmentTransaction.commit();
    }

    void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("StarFeeds");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onLoginSelected() {
        Fragment LoginFragment = new LoginFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flContainer, LoginFragment, "login");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onLoggedIn(User user) {
        Log.i(Configuration.TAG, user.toString());
        Intent intent = new Intent();
        setResult(Configuration.AUTHENTICATION_REQUEST, intent);
        finish();
    }

    @Override
    public void onEmailLogInFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        pdLoader.dismiss();
    }

    @Override
    public void onRegisterSelected() {
        Fragment RegisterFragment = new RegisterFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flContainer, RegisterFragment, "registerUser");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onShowLoginProgress() {
        if (pdLoader != null) {
            pdLoader.show();
        }
    }

    @Override
    public void onDismissLoginProgress() {
        if (pdLoader != null) {
            pdLoader.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(Configuration.TAG, "on activity result " + requestCode + " " + resultCode);
        //call flContainer fragment onActivityResult
        Fragment fragment1 = fragment.getFragmentManager().findFragmentById(R.id.flContainer);
        fragment1.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRegisterSubmitSelected(User user) {
        onShowProgress();
    }

    @Override
    public void onRegisterSuccessful(User user) {
        Log.i(Configuration.TAG, user.toString());
        Intent intent = new Intent();
        setResult(Configuration.AUTHENTICATION_REQUEST, intent);
        onDismissProgress();
        finish();
    }

    @Override
    public void onShowProgress() {
        if (pdLoader != null) {
            pdLoader.show();
        }
    }

    @Override
    public void onDismissProgress() {
        pdLoader.dismiss();
    }

    @Override
    public void changeToolbarTitle(String title) {
        if (toolbar != null) toolbar.setTitle(title);
    }

    @Override
    public void onSocialAccountLoginSuccess(User user) {
        Log.i(Configuration.TAG, user.toString());
        Intent intent = new Intent();
        setResult(Configuration.AUTHENTICATION_REQUEST, intent);
        pdLoader.dismiss();
        finish();
    }

    @Override
    public void onLoginSuccess(User user) {
        Log.i(Configuration.TAG, user.toString());
        Intent intent = new Intent();
        setResult(Configuration.AUTHENTICATION_REQUEST, intent);
        pdLoader.dismiss();
        finish();
    }

    @Override
    public void onLoginFailed(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        pdLoader.dismiss();
    }

    @Override
    public void onLoginCanceled() {
    }

    @Override
    public void onLogOutSuccess() {

    }

}
