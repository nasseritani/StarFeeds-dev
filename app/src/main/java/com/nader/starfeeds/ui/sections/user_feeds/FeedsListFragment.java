package com.nader.starfeeds.ui.sections.user_feeds;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nader.starfeeds.R;

import com.nader.starfeeds.data.SessionManager;
import com.nader.starfeeds.data.api.requests.FollowCelebrityRequest;
import com.nader.starfeeds.data.api.requests.UnFollowCelebrityRequest;
import com.nader.starfeeds.data.api.requests.UserFeedsRequest;
import com.nader.starfeeds.data.api.requests.UserNewFeedsRequest;
import com.nader.starfeeds.data.api.responses.ApiResponse;
import com.nader.starfeeds.data.api.responses.PostRequestResponse;
import com.nader.starfeeds.data.api.responses.UserFeedsResponse;
import com.nader.starfeeds.data.componenets.DataEnded;
import com.nader.starfeeds.data.componenets.model.Celebrity;
import com.nader.starfeeds.data.componenets.model.Feed;
import com.nader.starfeeds.data.componenets.model.FeedFacebookLink;
import com.nader.starfeeds.data.componenets.model.FeedFacebookImage;
import com.nader.starfeeds.data.componenets.model.FeedFacebookText;
import com.nader.starfeeds.data.componenets.model.FeedFacebookVideo;
import com.nader.starfeeds.data.componenets.model.FeedInstagramImage;
import com.nader.starfeeds.data.componenets.model.FeedInstagramVideo;
import com.nader.starfeeds.data.componenets.model.FeedTwitterLink;
import com.nader.starfeeds.data.componenets.model.FeedTwitterImage;
import com.nader.starfeeds.data.componenets.model.FeedTwitterText;
import com.nader.starfeeds.data.componenets.Loader;
import com.nader.starfeeds.data.componenets.Reloader;
import com.nader.starfeeds.data.NetworkErrorType;
import com.nader.starfeeds.listing.DataEndedItem;
import com.nader.starfeeds.listing.FeedFacebookImageItem;
import com.nader.starfeeds.listing.FeedFacebookLinkItem;
import com.nader.starfeeds.listing.FeedFacebookTextItem;
import com.nader.starfeeds.listing.FeedFacebookVideoItem;
import com.nader.starfeeds.listing.FeedInstagramImageItem;
import com.nader.starfeeds.listing.FeedInstagramVideoItem;
import com.nader.starfeeds.listing.FeedItem;
import com.nader.starfeeds.listing.FeedTwitterImageItem;
import com.nader.starfeeds.listing.FeedTwitterLinkItem;
import com.nader.starfeeds.listing.FeedTwitterTextItem;
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
 *
 * Created by Nasse_000 on 3/11/2017.
 */
/**
 * FeedsListFragment is the fragment class which lists
 * the items(Feeds(Instagram,Facebook,Twitter) in a linear way
 */

public class FeedsListFragment extends Fragment {
    private FeedsListAdapter mFeedsListAdapter;
    private int mThreshold = 0;
    private int mItemsCount;
    private RecyclerView mRecyclerView;
    boolean mLoadItemsSuccess = true;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean isLoading = false;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();
    private int currentPage;
    private String latestPostId;
    private String userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    /**
     * Create new ArticlesProvider instance.
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_feeds_list, container, false);
        userId = SessionManager.getInstance().getSessionUser().getId();
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isLoading)
                    requestNewUserFeeds(userId, latestPostId);
                else{
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        mRecyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mFeedsListAdapter = new FeedsListAdapter(null, listListener, getActivity(), false);//create new adapter
        mRecyclerView.setAdapter(mFeedsListAdapter);
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
                        requestUserFeeds(userId, currentPage);
                    }
                }
            }
        });
        currentPage = 1;
        requestUserFeeds(userId, currentPage);
        return v;
    }

    private ArrayList<ListingItem> convertFeedsToItems(ArrayList<Feed> arrayFeeds) {
        //save the received array
        ArrayList<ListingItem> newListingItems = new ArrayList<>();
        for (int i = 0; i < arrayFeeds.size(); i++) {
            FeedItem feedItem = new FeedItem(arrayFeeds.get(i));
            Log.d("type", ":" + feedItem.getType());
            switch (feedItem.getFeed().getFeedType()) {
                case TWITTER_IMAGE:
                    FeedTwitterImageItem feedTwitterImageItem = new FeedTwitterImageItem((FeedTwitterImage) feedItem.getFeed());
                    newListingItems.add(feedTwitterImageItem);
                    break;
                case TWITTER_TEXT:
                    FeedTwitterTextItem feedTwitterTextItem=new FeedTwitterTextItem((FeedTwitterText) feedItem.getFeed());
                    newListingItems.add(feedTwitterTextItem);
                    break;
                case TWITTER_LINK:
                    FeedTwitterLinkItem feedTwitterLinkItem =new FeedTwitterLinkItem((FeedTwitterLink) feedItem.getFeed());
                    newListingItems.add(feedTwitterLinkItem);
                    break;
                case FACEBOOK_IMAGE:
                    FeedFacebookImageItem feedFacebookImageItem=new FeedFacebookImageItem((FeedFacebookImage) feedItem.getFeed());
                    newListingItems.add(feedFacebookImageItem);
                    break;
                case FACEBOOK_VIDEO:
                    FeedFacebookVideoItem feedFacebookVideoItem=new FeedFacebookVideoItem((FeedFacebookVideo) feedItem.getFeed());
                    newListingItems.add(feedFacebookVideoItem);
                   break;
                case FACEBOOK_TEXT:
                    FeedFacebookTextItem feedFacebookTextItem=new FeedFacebookTextItem((FeedFacebookText) feedItem.getFeed());
                    newListingItems.add(feedFacebookTextItem);
                    break;
                case FACEBOOK_LINK:
                    FeedFacebookLinkItem feedFacebookLinkItem =new FeedFacebookLinkItem((FeedFacebookLink) feedItem.getFeed());
                    newListingItems.add(feedFacebookLinkItem);
                    break;
                case INSTAGRAM_IMAGE:
                    FeedInstagramImageItem feedInstagramImageItem=new FeedInstagramImageItem((FeedInstagramImage) feedItem.getFeed());
                    newListingItems.add(feedInstagramImageItem);
                    break;
                case INSTAGRAM_VIDEO:
                    FeedInstagramVideoItem feedInstagramVideoItem=new FeedInstagramVideoItem((FeedInstagramVideo) feedItem.getFeed());
                    newListingItems.add(feedInstagramVideoItem);
                    break;
            }
        }
        return newListingItems;
    }

    /**
     * Fetches user feeds.
     */
    private void requestUserFeeds(@NonNull String userId, int page) {
        if (isLoading) return;
        // set flag
        isLoading = true;
        // todo change user id
        addLoaderItem();
        UserFeedsRequest apiRequest = new UserFeedsRequest();
        // create rx subscription
        Subscription userFeedsSubscription = apiRequest.fetchUserFeeds(userId, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ApiResponse>() {
                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        UserFeedsResponse userFeedsResponse = (UserFeedsResponse) apiResponse;
                        ArrayList<Feed> items = userFeedsResponse.getUserFeeds();
                        handleDataFeeds(items);
                    }

                    @Override
                    public void onError(Throwable error) {
                        handleDataFeedsError();
                    }
                });
        // add subscription to compositeSubscription
        compositeSubscription.add(userFeedsSubscription);
    }

    private void handleDataFeedsError() {
        isLoading = false;
        mFeedsListAdapter.removeLastItem();
        addReloaderItem(NetworkErrorType.API_FAIL);
        mLoadItemsSuccess = false;
    }

    private void handleDataFeeds(ArrayList<Feed> arrayFeeds) {
        if (arrayFeeds.size() > 0) {
            currentPage++;
            mFeedsListAdapter.removeLastItem();
            mLoadItemsSuccess = true;
            mSwipeRefreshLayout.setRefreshing(false);
            int currentCount = arrayFeeds.size();
            // convert new articles to items array
            ArrayList<ListingItem> newListingItems = convertFeedsToItems(arrayFeeds);
            //add new items to mRecyclerView
            mFeedsListAdapter.addItems(newListingItems);
            // update mThreshold
            if (mItemsCount == 0) {
                mThreshold += currentCount / 2;
            } else {
                mThreshold += currentCount;
            }
            mItemsCount += currentCount;
            isLoading = false;
            // for new feeds request
            Feed feed = mFeedsListAdapter.getItems().get(0).getFeed();
            if (feed != null) {
                latestPostId = feed.getId();
            }
        } else {
            handleEndedData();
        }
    }

    private void handleEndedData() {
        mFeedsListAdapter.removeLastItem();
        addEndedDataItem();
        mLoadItemsSuccess = false;
        isLoading = false;
    }


    /**
     * Fetches user feeds which are added after user requested feeds.
     */
    private void requestNewUserFeeds(@NonNull String userId, String postId) {
        if (isLoading) return;
        // set flag
        isLoading = true;
        UserNewFeedsRequest apiRequest = new UserNewFeedsRequest();
        // create rx subscription
        Subscription newUserFeedsSubscription = apiRequest.fetchNewUserFeeds(userId, postId)
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
                        handleNewDataFeedsError();
                    }
                });
        // add subscription to compositeSubscription
        compositeSubscription.add(newUserFeedsSubscription);
    }


    private void handleNewDataFeeds(ArrayList<Feed> items) {
        mSwipeRefreshLayout.setRefreshing(false);
        isLoading = false;
        if (items.size() > 0) {
            // for new feeds request
            latestPostId = items.get(0).getId();
            int currentCount = items.size();
            // convert new articles to items array
            ArrayList<ListingItem> newListingItems = convertFeedsToItems(items);
            //add new items to mRecyclerView
            mFeedsListAdapter.addNewItems(newListingItems);
            // update mThreshold
            if (mItemsCount == 0) {
                mThreshold += currentCount / 2;
            } else {
                mThreshold += currentCount;
            }
            mItemsCount += currentCount;
            // for new feeds request
            Feed feed = mFeedsListAdapter.getItems().get(0).getFeed();
            if (feed != null) {
                latestPostId = feed.getId();
            }
        }
    }

    private void handleNewDataFeedsError() {
        mSwipeRefreshLayout.setRefreshing(false);
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
        mFeedsListAdapter.addNewItem(loaderItem);
    }

    /**
     * Adds a loader item to the adapter.
     */
    private void addEndedDataItem() {
        //create new data ended
        DataEnded dataEnded = new DataEnded();
        //create new DataEndedItem with loader
        DataEndedItem dataEndedItem = new DataEndedItem(dataEnded);
        //add loaderItem to arrayList
        mFeedsListAdapter.addNewItem(dataEndedItem);
    }

    /**
     * Add reloader item to the adapter.
     */
    private void addReloaderItem(NetworkErrorType error){
        Reloader reloader = new Reloader(error);
        ReloaderItem reloaderItem = new ReloaderItem(reloader);
        mFeedsListAdapter.addNewItem(reloaderItem);
    }
    OnListListener listListener = new OnListListener() {

        @Override
        public void onReloaderButtonSelected() {
            reloadClicked();
        }

        @Override
        public void onFollowClick(String celebId) {

        }

        @Override
        public void onUnFollowClick(String celebId) {

        }
    };

    private void reloadClicked() {
        mFeedsListAdapter.removeLastItem();
        requestUserFeeds(userId, currentPage);
    }

}