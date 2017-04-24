package com.nader.starfeeds.ui.sections.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.nader.starfeeds.Configuration.Configuration;
import com.nader.starfeeds.R;
import com.nader.starfeeds.settings.SettingsActivity;
import com.nader.starfeeds.ui.sections.explore.ExploreFragment;
import com.nader.starfeeds.ui.sections.suggestions.FragmentSuggestions;
import com.nader.starfeeds.ui.sections.user_feeds.FeedsListFragment;
import com.nader.starfeeds.ui.sections.search.SearchCelebritiesActivity;

import java.util.ArrayList;
import java.util.List;


public class MainViewActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private Toolbar mtoolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initTabLayout();

    }

    private void initToolbar() {
        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        setTitle("StarFeeds");
        mtoolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }
//initializing TabLayout and setting it up with the viewpager
    private void initTabLayout(){
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_explore_black_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_group_add_white_24dp);
    }
//Setting up viewpager with its adapter
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(2);
        adapter.addFragment(new FeedsListFragment(),"");
        adapter.addFragment(new ExploreFragment(), "");
        adapter.addFragment(new FragmentSuggestions(),"");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
return false;
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivityForResult(intent, Configuration.AUTHENTICATION_REQUEST);
        }
        if(id==R.id.action_search){

            Intent intent = new Intent(this, SearchCelebritiesActivity.class);
            startActivityForResult(intent, Configuration.AUTHENTICATION_REQUEST);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Configuration.AUTHENTICATION_REQUEST){
            setResult(Configuration.AUTHENTICATION_REQUEST);
            finish();
        }
    }
}