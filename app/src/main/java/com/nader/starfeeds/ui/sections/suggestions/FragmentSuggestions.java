package com.nader.starfeeds.ui.sections.suggestions;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nader.starfeeds.R;
import com.nader.starfeeds.data.api.requests.CelebritiesSuggestionsRequest;
import com.nader.starfeeds.data.api.responses.ApiResponse;
import com.nader.starfeeds.data.api.responses.CelebritiesResponse;
import com.nader.starfeeds.data.componenets.Loader;
import com.nader.starfeeds.data.componenets.model.Celebrity;
import com.nader.starfeeds.ui.sections.search.CelebrityListAdapter;
import com.nader.starfeeds.ui.sections.search.listing.CelebrityListingItem;
import com.nader.starfeeds.ui.sections.search.listing.SearchListingItem;
import com.nader.starfeeds.ui.sections.search.listing.SearchLoaderItem;

import java.util.ArrayList;

import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Nasse_000 on 3/31/2017.
 */
public class FragmentSuggestions extends Fragment {
    private CelebrityListAdapter celebrityListAdapter;
    private RecyclerView mRecyclerView;
    boolean mLoadItemsSuccess = true;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean isLoading = false;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();
    private int currentPage;
    private int mThreshold = 0;
    private int mItemsCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_suggestions, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //if (!isLoading)
                    //requestNewUserFeeds("10", latestPostId);
            }
        });

        mRecyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        celebrityListAdapter = new CelebrityListAdapter(null, new CelebrityListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Celebrity celebrity) {

            }

            @Override
            public void onFollowClick(Celebrity celebrity) {

            }

            @Override
            public void onUnFollowClick(Celebrity celebrity) {

            }
        }, getActivity());//create new adapter
        mRecyclerView.setAdapter(celebrityListAdapter);
        // track scrolling
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    //check for scroll down
                    // get last visible position
                    int lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();
                    //checks if current position of bindView equals mThreshold
                    if (lastVisiblePosition >= mThreshold && mLoadItemsSuccess) {
                        //requestUserFeeds("10", currentPage);
                    }
                }
            }
        });
        currentPage = 1;
        requestCelebritySuggestions("10");
        return v;
    }

    private void requestCelebritySuggestions(@NonNull String userID) {
        addLoaderItem();
        CelebritiesSuggestionsRequest apiRequest = new CelebritiesSuggestionsRequest();
        // create rx subscription
        Subscription celebritySearchSubscription = apiRequest.fetchCelebSuggestions(userID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ApiResponse>() {
                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        CelebritiesResponse celebritiesResponse = (CelebritiesResponse) apiResponse;
                        ArrayList<Celebrity> items = celebritiesResponse.getCelebrities();
                        handleNewDataCelebrities(items);
                    }

                    @Override
                    public void onError(Throwable error) {
                        handleNewDataFeedsError();
                    }
                });
        // add subscription to compositeSubscription
        compositeSubscription.add(celebritySearchSubscription);
    }

    private void handleNewDataCelebrities(ArrayList<Celebrity> items) {
        celebrityListAdapter.removeLastItem();
        ArrayList<SearchListingItem> newListingItems = convertFeedsToItems(items);
        //add new items to mRecyclerView
        celebrityListAdapter.addNewItems(newListingItems);
    }

    private ArrayList<SearchListingItem> convertFeedsToItems(ArrayList<Celebrity> celebrities) {
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
