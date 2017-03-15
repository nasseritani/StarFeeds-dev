package com.nader.starfeeds;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.nader.starfeeds.Configuration.Configuration;
import com.nader.starfeeds.data.SessionManager;

public class LogOutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //clear session
                SessionManager.getInstance().clearSession();
                setResult(Configuration.AUTHENTICATION_REQUEST);
                finish();
            }
        });

    }

}
