package com.nader.starfeeds.ui.sections.search;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.nader.starfeeds.R;
import com.nader.starfeeds.data.api.requests.FollowCelebrityRequest;
import com.nader.starfeeds.data.api.requests.SearchCelebrityRequest;
import com.nader.starfeeds.data.api.requests.UnFollowCelebrityRequest;
import com.nader.starfeeds.data.api.requests.UserCelebritiesFollowingRequest;
import com.nader.starfeeds.data.api.responses.ApiResponse;
import com.nader.starfeeds.data.api.responses.CelebritiesResponse;
import com.nader.starfeeds.data.api.responses.PostRequestResponse;
import com.nader.starfeeds.data.componenets.model.Celebrity;
import com.nader.starfeeds.data.componenets.Loader;
import com.nader.starfeeds.ui.sections.celebrity.CelebrityActivity;
import com.nader.starfeeds.ui.sections.search.listing.CelebrityListingItem;
import com.nader.starfeeds.ui.sections.search.listing.SearchListingItem;
import com.nader.starfeeds.ui.sections.search.listing.SearchLoaderItem;

import java.util.ArrayList;

import rx.Single;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class SearchCelebritiesActivity extends AppCompatActivity {
    private Toolbar mtoolbar;
    private RecyclerView recyclerView;
    private CelebrityListAdapter celebrityListAdapter;
    CompositeSubscription compositeSubscription = new CompositeSubscription();
    ProgressDialog pd ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_expanded);
        initToolbar();
        initRecyclerView();
    }

    private void initToolbar() {
        mtoolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        celebrityListAdapter = new CelebrityListAdapter(null, new CelebrityListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Celebrity celebrity) {
                if (celebrity != null) {
                    startCelebrityActivity(celebrity.getId());
                }
            }

            @Override
            public void onFollowClick(Celebrity celebrity) {
                sendFollowRequest("10",celebrity);
            }

            @Override
            public void onUnFollowClick(Celebrity celebrity) {
                sendUnFollowRequest("10", celebrity);
            }
        }, getBaseContext());
        recyclerView.setAdapter(celebrityListAdapter);
    }

    private void sendFollowRequest(@NonNull String userId,@NonNull final Celebrity celebrity) {
        pd = new ProgressDialog(this);
        pd.setMessage("loading");
        pd.show();
        FollowCelebrityRequest apiRequest = new FollowCelebrityRequest();
        // create rx subscription
        Subscription followCelebritySubscription = apiRequest.followCeleb(userId, celebrity.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ApiResponse>() {
                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        PostRequestResponse response = (PostRequestResponse) apiResponse;
                        boolean isRequestSuccessful = response.isRequestSuccesful();
                        handleFollowResponse(isRequestSuccessful,celebrity);
                    }
                    @Override
                    public void onError(Throwable error) {
                        handleFollowError();
                    }
                });
        // add subscription to compositeSubscription
        compositeSubscription.add(followCelebritySubscription);
    }

    private void handleFollowError() {
        if (pd != null) {
            pd.dismiss();
        }
        Toast.makeText(getBaseContext(),"Something Went Wrong, Try Again Later", Toast.LENGTH_SHORT).show();
    }

    private void handleFollowResponse(boolean items, Celebrity celebrity) {
        if (pd != null) {
            pd.dismiss();
        }
        celebrity.setFollowed(items);
        celebrityListAdapter.notifyDataSetChanged();
    }

    private void sendUnFollowRequest(@NonNull String userId,@NonNull final Celebrity celebrity) {
        pd = new ProgressDialog(this);
        pd.setMessage("loading");
        pd.show();
        UnFollowCelebrityRequest apiRequest = new UnFollowCelebrityRequest();
        // create rx subscription
        Subscription unFollowCelebritySubscription = apiRequest.unFollowCeleb(userId, celebrity.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ApiResponse>() {
                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        PostRequestResponse response = (PostRequestResponse) apiResponse;
                        boolean isRequestSuccessful = response.isRequestSuccesful();
                        handleUnFollowResponse(isRequestSuccessful, celebrity);
                    }
                    @Override
                    public void onError(Throwable error) {
                        handleUnFollowError();
                    }
                });
        // add subscription to compositeSubscription
        compositeSubscription.add(unFollowCelebritySubscription);
    }

    private void handleUnFollowError() {
        if (pd != null) {
            pd.dismiss();
        }
        Toast.makeText(getBaseContext(),"Something Went Wrong, Try Again Later", Toast.LENGTH_SHORT).show();
    }

    private void handleUnFollowResponse(boolean items, Celebrity celebrity) {
        if (pd != null) {
            pd.dismiss();
        }
        // !true (successful)
        celebrity.setFollowed(!items);
        celebrityListAdapter.notifyDataSetChanged();
    }


    private void startCelebrityActivity(String id){
        Intent i=new Intent(getBaseContext(), CelebrityActivity.class);
        i.putExtra(CelebrityActivity.CELEB_ID, id);
        startActivity(i);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this,SearchActivity.class)));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                requestCelebritySearch(query,"10");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.onActionViewExpanded();
        searchView.setIconified(false);
        return true;
    }

    private void requestCelebritySearch(@NonNull String celebName, @NonNull String userId) {
        addLoaderItem();
        SearchCelebrityRequest apiRequest = new SearchCelebrityRequest();
        // create rx subscription
        Single<ApiResponse> celebsSearchSingle =  apiRequest.fetchCelebSearch(celebName, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Subscription celebritySearchSubscription = celebsSearchSingle
                .subscribe(new SingleSubscriber<ApiResponse>() {
                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        CelebritiesResponse celebritiesResponse = (CelebritiesResponse) apiResponse;
                        ArrayList<Celebrity> celebsSearchedItems = celebritiesResponse.getCelebrities();
                        handleNewDataCelebrities(celebsSearchedItems);
                    }

                    @Override
                    public void onError(Throwable error) {
                        handleNewDataFeedsError();
                    }
                });

        compositeSubscription.add(celebritySearchSubscription);
    }

    private void handleNewDataCelebrities(ArrayList<Celebrity> itemsSearchedCelebs) {
        celebrityListAdapter.removeLastItem();
        ArrayList<SearchListingItem> newListingItems = convertCelebsToItems(itemsSearchedCelebs);
        //add new itemsSearchedCelebs to mRecyclerView
        celebrityListAdapter.addNewItems(newListingItems);
    }

    private ArrayList<SearchListingItem> convertCelebsToItems(ArrayList<Celebrity> celebrities) {
        //save the received array
        ArrayList<SearchListingItem> newListingItems = new ArrayList<>();
        for (int i = 0; i < celebrities.size(); i++) {
            CelebrityListingItem celebrityListingItem = new CelebrityListingItem(celebrities.get(i));
            newListingItems.add(celebrityListingItem);
        }
        return newListingItems;
    }


    private void handleNewDataFeedsError() {
        celebrityListAdapter.removeLastItem();
    }

    /**
     * Adds a loader item to the adapter.
     */
    private void addLoaderItem() {
        //create new loader
        Loader loader = new Loader();
        //create new loaderItem with loader
        SearchLoaderItem loaderItem = new SearchLoaderItem(loader);
        //add loaderItem to arrayList
        celebrityListAdapter.addNewItem(loaderItem);
    }

}