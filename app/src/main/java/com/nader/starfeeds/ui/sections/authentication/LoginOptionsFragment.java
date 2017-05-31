package com.nader.starfeeds.ui.sections.authentication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nader.starfeeds.configuration.Configuration;
import com.nader.starfeeds.R;
import com.nader.starfeeds.data.componenets.model.User;
import com.nader.starfeeds.data.LoginProvider;

/**
 * Fragment class which handles the UI of login functionality.
 */
public class LoginOptionsFragment extends Fragment implements
        View.OnClickListener,
        LoginProvider.OnLoginProviderListener {
    LoginProvider loginProvider ;
    private OnOptionsFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i(Configuration.TAG,"loginOptions");
        // Inflate the layout for this fragment
        View view= getActivity().getLayoutInflater().inflate(R.layout.fragment_login_options, container, false);
        initializeViews(view);
        loginProvider = new LoginProvider(getActivity().getBaseContext(),this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mListener.changeToolbarTitle(getString(R.string.app_name));
    }

    /**
     * Handles login button click.
     */
    public void handleLoginButtonPressed() {
        if (mListener != null) {
            mListener.onLoginSelected();
        }
    }


    /**
     * Handles register button click.
     */
    public void handleRegisterButtonPressed() {
        if (mListener != null) {
            mListener.onRegisterSelected();
        }
    }

    void initializeViews(View view) {
        view.findViewById(R.id.btnFbLogin).setOnClickListener(this);
        view.findViewById(R.id.btnGmailLogin).setOnClickListener(this);
        view.findViewById(R.id.btnEmailLogin).setOnClickListener(this);
        view.findViewById(R.id.btnRegister).setOnClickListener(this);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOptionsFragmentInteractionListener) {
            mListener = (OnOptionsFragmentInteractionListener) context;

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(Configuration.TAG,"Login options");
        if (loginProvider != null) loginProvider.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnFbLogin:
                handleFbLoginButtonPressed();
                break;
            case R.id.btnGmailLogin:
                handleGmailLoginButtonPressed();
                break;
            case R.id.btnEmailLogin:
                handleLoginButtonPressed();
                break;
            case R.id.btnRegister:
                handleRegisterButtonPressed();
                break;
        }
    }

    /**
     * Handles fb login button click.
     */
    public void handleFbLoginButtonPressed() {
        mListener.onShowProgress();
        loginProvider.loginViaFB(getActivity());
    }

    /**
     * Handles gmail login button click.
     */
    public void handleGmailLoginButtonPressed() {
        mListener.onShowProgress();
        loginProvider.loginViaGoogle(getActivity());
    }

    @Override
    public void onLoginSuccess(User user) {
        mListener.onDismissProgress();
        mListener.onSocialAccountLoginSuccess(user);
    }

    @Override
    public void onLoginFailed(String error) {
        Toast.makeText(getContext(), error,Toast.LENGTH_SHORT).show();
        mListener.onDismissProgress();
    }

    @Override
    public void onLoginCanceled() {

    }

    @Override
    public void onLogOutSuccess() {

    }

    public interface OnOptionsFragmentInteractionListener {
        /**
         * Indicates login option selected.
         */
        void onLoginSelected();
        /**
         * Indicates register option selected.
         */
        void onRegisterSelected();
        /**
         * Indicates progress taking place.
         */
        void onShowProgress();
        /**
         * Indicates current progress finished.
         */
        void onDismissProgress();

        /**
         * Triggered to change toolbar title.
         */
        void changeToolbarTitle(String title);

        /**
         * Indicates social account login succeeded.
         */
        void onSocialAccountLoginSuccess(User user);
    }
}
