package com.nader.starfeeds.Search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.nader.starfeeds.Configuration.Configuration;
import com.nader.starfeeds.R;

public class SearchActivity extends AppCompatActivity {
TextView tvSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
       tvSearch= (TextView) findViewById(R.id.tvSearch);
        handleIntent(getIntent());
    }


    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search
            tvSearch.setText("Search query:"+query.toString());
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(Configuration.TAG,"SearchActivity");
        if (resultCode == Configuration.AUTHENTICATION_REQUEST) {
            setResult(Configuration.AUTHENTICATION_REQUEST);
            finish();
        }
    }
}
