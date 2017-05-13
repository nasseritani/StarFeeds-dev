package com.nader.starfeeds.ui.sections.profile;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nader.starfeeds.R;
import com.nader.starfeeds.data.SessionManager;
import com.nader.starfeeds.data.componenets.model.User;

public class EditProfileActivity extends AppCompatActivity {
    private EditText etName;
    private EditText etEmail;
    private TextInputLayout inputLayoutName;
    private TextInputLayout inputLayoutEmail;
    private Button btnRegisterSubmit;
    private Toolbar mtoolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initToolbar();
        initViews();
        User user = SessionManager.getInstance().getSessionUser();
        String name = user.getName();
        String email = user.getEmail();
        etName.setText(name);
        etEmail.setText(email);
    }
    private void initViews() {

        etName = (EditText) findViewById(R.id.inputName);
        etEmail =  (EditText) findViewById(R.id.inputEmail);
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
            /*String name = getInputName();
            String email = getInputEmail();
            String password = getInputPassword();
            LoginProvider loginProvider = new LoginProvider(this, this);
            loginProvider.registerUser(name,email,password);
            User user = new User(name, email, password);
            mListener.onRegisterSubmitSelected(user);*/
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

