package com.nader.starfeeds.ui.sections.profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.nader.starfeeds.R;
import com.nader.starfeeds.data.SessionManager;
import com.nader.starfeeds.data.api.requests.RegisterUserRequest;
import com.nader.starfeeds.data.api.requests.UpdateUserRequest;
import com.nader.starfeeds.data.api.responses.ApiResponse;
import com.nader.starfeeds.data.api.responses.LoginEmailResponse;
import com.nader.starfeeds.data.componenets.Provider;
import com.nader.starfeeds.data.componenets.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.nader.starfeeds.data.componenets.Provider.EMAIL_LOGIN;
import static com.nader.starfeeds.data.componenets.Provider.Fb_LOGIN;
import static com.nader.starfeeds.data.componenets.Provider.GOOGLE_LOGIN;

public class EditProfileActivity extends AppCompatActivity {
    private EditText etName;
    private EditText etEmail;
    private TextInputLayout inputLayoutName;
    private TextInputLayout inputLayoutEmail;
    private Spinner spinnerCountries;
    private Button btnRegisterSubmit;
    private Toolbar mtoolbar;
    private User user;
    private String country = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        user = SessionManager.getInstance().getSessionUser();
        initToolbar();
        country = user.getCountry();
        initViews();
        String name = user.getName();
        String email = user.getEmail();
        etName.setText(name);
        etEmail.setText(email);
    }

    private void initViews() {

        etName = (EditText) findViewById(R.id.inputName);
        etEmail =  (EditText) findViewById(R.id.inputEmail);
        if (user.getProvider() == Fb_LOGIN || user.getProvider() == GOOGLE_LOGIN){
            etEmail.setEnabled(false);
            etName.setEnabled(false);
        }
        if (user.getEmail() == null || user.getEmail().equals("")){
            etEmail.setEnabled(false);
            etEmail.setVisibility(View.GONE);

        }
        spinnerCountries = (Spinner) findViewById(R.id.spinnerCountry);
        spinnerCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                country = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        inputLayoutName = (TextInputLayout) findViewById(R.id.inputLayoutName);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.inputLayoutEmail);
        inputLayoutName.setErrorEnabled(true);
        inputLayoutEmail.setErrorEnabled(true);
        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    validateName();
                }
                if (b){
                    inputLayoutName.setError(null);
                }
            }
        });
        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    validateEmail();
                }
                if (b){
                    inputLayoutEmail.setError(null);
                }
            }
        });


        btnRegisterSubmit = (Button) findViewById(R.id.btnRegisterSubmit);
        btnRegisterSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });

        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }

        Collections.sort(countries);
        for (String country : countries) {
            System.out.println(country);
        }

        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, countries);

        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the your spinner
        spinnerCountries.setAdapter(countryAdapter);
        int index = countries.indexOf(country);
        spinnerCountries.setSelection(index);
    }

    private void initToolbar() {
        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    /** Handles the press of the submit button, and if credentials are valid,
     * an instant of the LoginProvider class is created, which is used to call
     * registerUser method and passing the credentials to it.
     */
    private void submitForm() {
        if (validateName() && validateEmail()) {
            String name = getInputName();
            String email = getInputEmail();
            if (name.equals(user.getName())) name = null;
            if (email.equals(user.getEmail())) email = null;
            sendUpdateRequest(user.getId(), name, email, country);
        }
    }

    private void sendUpdateRequest(@NonNull String id, String name, String email, String country) {
        UpdateUserRequest apiRequest = new UpdateUserRequest();
        // create rx subscription
        Subscription loginSubscription = apiRequest.update(id, name, email, country)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ApiResponse>() {
                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        LoginEmailResponse response = (LoginEmailResponse) apiResponse;
                        User user = response.getUser();
                        handleLoginResponse(user);
                    }
                    @Override
                    public void onError(Throwable error) {
                        handleLoginError();
                    }
                });
    }

    private void handleLoginError() {
        Toast.makeText(this, "Something went wrong, try again later", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void handleLoginResponse(User newUser) {
        if (newUser != null) {
            user.updateUser(newUser);
            SessionManager.getInstance().storeSession(user, false);
            Toast.makeText(this, "Your account has been updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            handleLoginError();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    /**
     * Validates the name inserted in the name TextField,
     * if name is not valid, shows an error message on the inputLayout
     * @return true if name is valid, false otherwise.
     */
    private boolean validateName() {
        if (getInputName().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            return false;
        } else {
            inputLayoutName.setError(null);
        }

        return true;
    }

    /**
     *
     * @return the Name TextField String Trimmed.
     */
    private String getInputName() {
        return etName.getText().toString().trim();
    }
    /**
     * Validates the email inserted in the email TextField,
     * if email is not valid, shows an error message on the inputLayout
     * @return true if email is valid, false otherwise.
     */
    private boolean validateEmail() {
        String email = getInputEmail();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            return false;
        } else {
            inputLayoutEmail.setError(null);
        }

        return true;
    }

    /**
     *
     * @return the Email TextField String Trimmed.
     */
    private String getInputEmail() {
        return etEmail.getText().toString().trim();
    }

    /**
     * Checks the validity of the email according to email pattern
     * @param email is the email string being validated
     * @return true if email is valid, false otherwise.
     */
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}

