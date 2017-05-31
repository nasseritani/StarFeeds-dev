package com.nader.starfeeds;

import android.app.Application;

import com.nader.starfeeds.configuration.Configuration;

/**
 * Created by Nader on 07-Sep-16.
 */

/**
 * Main Activity class which initializes application base components.
 */
public class StarFeeds extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
         Configuration.setContext(getApplicationContext());
    }


}
