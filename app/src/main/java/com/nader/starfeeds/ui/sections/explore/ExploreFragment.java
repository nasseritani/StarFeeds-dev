package com.nader.starfeeds.ui.sections.explore;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nader.starfeeds.R;

import com.nader.starfeeds.data.api.requests.ExploreFeedsRequest;
import com.nader.starfeeds.data.api.responses.ApiResponse;
import com.nader.starfeeds.data.api.responses.UserFeedsResponse;
import com.nader.starfeeds.data.componenets.model.Feed;
import com.nader.starfeeds.data.componenets.model.FeedFacebookLink;
import com.nader.starfeeds.data.componenets.model.FeedFacebookImage;
import com.nader.starfeeds.data.componenets.model.FeedTwitterImage;
import com.nader.starfeeds.data.componenets.model.FeedTwitterLink;
import com.nader.starfeeds.data.componenets.Loader;
import com.nader.starfeeds.data.componenets.Reloader;
import com.nader.starfeeds.data.NetworkErrorType;
import com.nader.starfeeds.listing.FeedFacebookImageItem;
import com.nader.starfeeds.listing.FeedFacebookLinkItem;
import com.nader.starfeeds.listing.FeedItem;
import com.nader.starfeeds.listing.FeedTwitterImageItem;
import com.nader.starfeeds.listing.FeedTwitterLinkItem;
import com.nader.starfeeds.listing.ListingItem;
import com.nader.starfeeds.listing.LoaderItem;
import com.nader.starfeeds.listing.ReloaderItem;
import com.nader.starfeeds.ui.listeners.OnListListener;

import java.util.ArrayList;

import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Nasse_000 on 3/11/2017.
 */
public class ExploreFragment extends Fragment {
    private ExploreListAdapter mExploreListAdapter;
    private int mThreshold = 0;
    private int mItemsCount;
    private RecyclerView mRecyclerView;
    boolean mLoadItemsSuccess = true;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean isLoading = false;
    CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.row_explore, container, false);
        mRecyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeToRefresh);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mExploreListAdapter = new ExploreListAdapter(null, listListener, getActivity());//create new adapter
        mRecyclerView.setAdapter(mExploreListAdapter);
        // track scrolling
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    //check for scroll down
                    // get last visible position
                    int lastVisiblePosition = gridLayoutManager.findLastVisibleItemPosition();
                    //checks if current position of bindView equals mThreshold
                    if (lastVisiblePosition >= mThreshold && mLoadItemsSuccess) {
                        //requestNewSet();
                    }
                }
            }
        });
        requestExploreFeeds("10");
        return v;
    }

    /**
     * Fetches user feeds.
     */
    private void requestExploreFeeds(@NonNull String userId) {
        if (isLoading) return;
        // set flag
        isLoading = true;
        // todo change user id
        addLoaderItem();
        ExploreFeedsRequest apiRequest = new ExploreFeedsRequest();
        // create rx subscription
        Subscription exploreFeedsSubscription = apiRequest.fetchExploreFeeds(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ApiResponse>() {
                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        UserFeedsResponse userFeedsResponse = (UserFeedsResponse) apiResponse;
                        ArrayList<Feed> items = userFeedsResponse.getUserFeeds();
                        handleNewDataFeeds(items);
                    }

                    @Override
                    public void onError(Throwable error) {
                        handleDataError();
                    }
                });
        // add subscription to compositeSubscription
        compositeSubscription.add(exploreFeedsSubscription);
    }

    private void handleDataError() {
        mExploreListAdapter.removeLastItem();
        addReloaderItem(NetworkErrorType.API_FAIL);
        mLoadItemsSuccess = false;
        isLoading = false;
    }

    //testing
    private void handleNewDataFeeds(ArrayList<Feed> arrayFeeds) {
        Log.i("nn", "complete");
        mExploreListAdapter.removeLastItem();
        mLoadItemsSuccess = true;
        mSwipeRefreshLayout.setRefreshing(false);
        int currentCount = arrayFeeds.size();
        // convert new articles to items array
        ArrayList<ListingItem> newListingItems = convertFeedsToItems(arrayFeeds);
        //add new items to mRecyclerView
        mExploreListAdapter.addNewItems(newListingItems);
        // update mThreshold
        if (mItemsCount == 0) {
            mThreshold += currentCount / 2;
        } else {
            mThreshold += currentCount;
        }
        mItemsCount += currentCount;
        isLoading = false;
    }

    private ArrayList<ListingItem> convertFeedsToItems(ArrayList<Feed> arrayFeeds) {
        //save the received array
        ArrayList<ListingItem> newListingItems = new ArrayList<>();
        for (int i = 0; i < arrayFeeds.size(); i++) {
            FeedItem a = new FeedItem(arrayFeeds.get(i));
            Log.d("type", ":" + a.getType());
            switch (a.getFeed().getFeedType()) {
               case TWITTER_IMAGE:
                    FeedTwitterImageItem feedTwitterImageItem = new FeedTwitterImageItem((FeedTwitterImage) a.getFeed());
                    newListingItems.add(feedTwitterImageItem);
                    break;
               /* case TWITTER_TEXT:
                    FeedTwitterTextItem feedTwitterTextItem=new FeedTwitterTextItem((FeedTwitterText) a.getFeed());
                    newListingItems.add(feedTwitterTextItem);
                    break;*/
                case FACEBOOK_IMAGE:
                    FeedFacebookImageItem feedFacebookImageItem=new FeedFacebookImageItem((FeedFacebookImage) a.getFeed());
                    newListingItems.add(feedFacebookImageItem);
                    break;
               /* case FACEBOOK_VIDEO:
                    FeedFacebookVideoItem feedFacebookVideoItem=new FeedFacebookVideoItem((FeedFacebookVideo) a.getFeed());
                    newListingItems.add(feedFacebookVideoItem);
                    break*/
               /* case FACEBOOK_TEXT:
                    FeedFacebookTextItem feedFacebookTextItem=new FeedFacebookTextItem((FeedFacebookText) a.getFeed());
                    newListingItems.add(feedFacebookTextItem);
                    break;*/
                case FACEBOOK_LINK:
                    FeedFacebookLinkItem feedFacebookLinkItem =new FeedFacebookLinkItem((FeedFacebookLink) a.getFeed());
                    newListingItems.add(feedFacebookLinkItem);
                    break;
                case TWITTER_LINK:
                    FeedTwitterLinkItem feedTwitterLinkItem =new FeedTwitterLinkItem((FeedTwitterLink) a.getFeed());
                    newListingItems.add(feedTwitterLinkItem);
                    break;

            }
        }
        return newListingItems;

    }



    //testing
    private void handleNewDataFeeds(ArrayList<Feed> arrayFeeds, int currentCount) {
        // convert new articles to items array
        ArrayList<ListingItem> newListingItems = convertFeedsToItems(arrayFeeds);
        //add new items to mRecyclerView
        mExploreListAdapter.addNewItems(newListingItems);
        // update mThreshold
        if (mItemsCount == 0) {
            mThreshold += currentCount / 2;
        } else {
            mThreshold += currentCount;
        }
        mItemsCount += currentCount;
    }
    /**
     * Adds a loader item to the adapter.
     */
    private void addLoaderItem() {
        //create new loader
        Loader loader = new Loader();
        //create new loaderItem with loader
        LoaderItem loaderItem = new LoaderItem(loader);
        //add loaderItem to arrayList
        mExploreListAdapter.addNewItem(loaderItem);
    }
    /**
     * Add reloader item to the adapter.
     */
    private void addReloaderItem(NetworkErrorType error){
        Reloader reloader = new Reloader(error);
        ReloaderItem reloaderItem = new ReloaderItem(reloader);
        mExploreListAdapter.addNewItem(reloaderItem);
    }
    OnListListener listListener = new OnListListener() {

        @Override
        public void onReloaderButtonSelected() {
            reloadClicked();
        }
    };

    private void reloadClicked() {
        mExploreListAdapter.removeLastItem();
        requestExploreFeeds("10");
    }

}