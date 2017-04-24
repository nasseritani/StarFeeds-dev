package com.nader.starfeeds.ui.sections.celebrity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.nader.starfeeds.R;
import com.nader.starfeeds.data.NetworkErrorType;
import com.nader.starfeeds.data.api.requests.CelebrityFeedsRequest;
import com.nader.starfeeds.data.api.requests.CelebrityRequest;
import com.nader.starfeeds.data.api.responses.ApiResponse;
import com.nader.starfeeds.data.api.responses.CelebrityResponse;
import com.nader.starfeeds.data.api.responses.UserFeedsResponse;
import com.nader.starfeeds.data.componenets.model.Celebrity;
import com.nader.starfeeds.data.componenets.model.Feed;
import com.nader.starfeeds.data.componenets.model.FeedFacebookImage;
import com.nader.starfeeds.data.componenets.model.FeedFacebookLink;
import com.nader.starfeeds.data.componenets.model.FeedFacebookText;
import com.nader.starfeeds.data.componenets.model.FeedFacebookVideo;
import com.nader.starfeeds.data.componenets.model.FeedInstagramImage;
import com.nader.starfeeds.data.componenets.model.FeedInstagramVideo;
import com.nader.starfeeds.data.componenets.model.FeedTwitterImage;
import com.nader.starfeeds.data.componenets.model.FeedTwitterLink;
import com.nader.starfeeds.data.componenets.model.FeedTwitterText;
import com.nader.starfeeds.data.componenets.Loader;
import com.nader.starfeeds.data.componenets.Reloader;
import com.nader.starfeeds.listing.CelebrityProfileItem;
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
import com.nader.starfeeds.ui.sections.user_feeds.FeedsListAdapter;

import java.util.ArrayList;

import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class CelebrityActivity extends AppCompatActivity {
    public static final String CELEB_ID = "celeb_id";
    public static final String CELEB_NAME = "celeb_name";
    // views
    private TextView tvCelebName;
    private ImageView ivCelebProfileImage;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();
    private int mThreshold = 0;
    private int mItemsCount;
    private FeedsListAdapter mCelebritiesListAdapter;
    private String userId = "10";
    private String celebId;
    private String celebName;
    boolean mLoadItemsSuccess = true;
    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celebrity);
        initViews();
        Intent intent = getIntent();
        celebId = intent.getStringExtra(CELEB_ID);
        celebName = intent.getStringExtra(CELEB_NAME);
        requestCelebrity();
        requestCelebrityFeeds();
    }

    private void initViews() {
        initToolbar();
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext(),LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        //create new adapter
        mCelebritiesListAdapter = new FeedsListAdapter(null, listListener, getBaseContext());
        mRecyclerView.setAdapter(mCelebritiesListAdapter);

        // track scrolling
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {//check for scroll down
                    // get last visible position
                    int lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();
                    //checks if current position of bindView equals mThreshold
                    if (lastVisiblePosition >= mThreshold && mLoadItemsSuccess) {
                    }
                }
            }
        });
    }

    private void initToolbar() {
        Toolbar mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        setTitle(celebName);
        mtoolbar.setTitleTextColor(ContextCompat.getColor(getBaseContext(), R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
                case TWITTER_TEXT:
                    FeedTwitterTextItem feedTwitterTextItem=new FeedTwitterTextItem((FeedTwitterText) a.getFeed());
                    newListingItems.add(feedTwitterTextItem);
                    break;
                case TWITTER_LINK:
                    FeedTwitterLinkItem feedTwitterLinkItem =new FeedTwitterLinkItem((FeedTwitterLink) a.getFeed());
                    newListingItems.add(feedTwitterLinkItem);
                    break;
                case FACEBOOK_IMAGE:
                    FeedFacebookImageItem feedFacebookImageItem=new FeedFacebookImageItem((FeedFacebookImage) a.getFeed());
                    newListingItems.add(feedFacebookImageItem);
                    break;
                case FACEBOOK_VIDEO:
                    FeedFacebookVideoItem feedFacebookVideoItem=new FeedFacebookVideoItem((FeedFacebookVideo) a.getFeed());
                    newListingItems.add(feedFacebookVideoItem);
                    break;
                case FACEBOOK_TEXT:
                    FeedFacebookTextItem feedFacebookTextItem=new FeedFacebookTextItem((FeedFacebookText) a.getFeed());
                    newListingItems.add(feedFacebookTextItem);
                    break;
                case FACEBOOK_LINK:
                    FeedFacebookLinkItem feedFacebookLinkItem =new FeedFacebookLinkItem((FeedFacebookLink) a.getFeed());
                    newListingItems.add(feedFacebookLinkItem);
                    break;
                case INSTAGRAM_IMAGE:
                    FeedInstagramImageItem feedInstagramImageItem=new FeedInstagramImageItem((FeedInstagramImage) a.getFeed());
                    newListingItems.add(feedInstagramImageItem);
                    break;
                case INSTAGRAM_VIDEO:
                    FeedInstagramVideoItem feedInstagramVideoItem=new FeedInstagramVideoItem((FeedInstagramVideo) a.getFeed());
                    newListingItems.add(feedInstagramVideoItem);
                    break;
            }
        }
        return newListingItems;
    }

    /**
     * Fetches user feeds.
     */
    private void requestCelebrityFeeds() {
        if (isLoading) return;
        // set flag
        isLoading = true;
        addLoaderItem();
        CelebrityFeedsRequest apiRequest = new CelebrityFeedsRequest();
        // create rx subscription
        Subscription celebrityFeedsSubscription = apiRequest.fetchCelebrityFeeds(celebId)
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
        compositeSubscription.add(celebrityFeedsSubscription);
    }

    private void requestCelebrity() {

        CelebrityRequest apiRequest = new CelebrityRequest();
        // create rx subscription
        Subscription celebritySubscription = apiRequest.fetchCelebrity(celebId, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ApiResponse>() {
                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        CelebrityResponse celebrityResponse = (CelebrityResponse) apiResponse;
                        Celebrity celebrity = celebrityResponse.getCelebrity();
                        handleCelebrity(celebrity);
                    }

                    @Override
                    public void onError(Throwable error) {
                        handleNewDataFeedsError();
                    }
                });
        // add subscription to compositeSubscription
        compositeSubscription.add(celebritySubscription);
    }

    private void handleCelebrity(Celebrity celebrity) {
        CelebrityProfileItem celebrityListingItem = new CelebrityProfileItem(celebrity);
        mCelebritiesListAdapter.addCelebrityProfileItem(celebrityListingItem);
    }

    private void handleNewDataFeedsError() {
        mCelebritiesListAdapter.removeLastItem();
        addReloaderItem(NetworkErrorType.API_FAIL);
        mLoadItemsSuccess = false;
        isLoading = false;
    }

    //testing
    private void handleNewDataFeeds(ArrayList<Feed> arrayFeeds) {
        Log.i("nn", "complete");
        mCelebritiesListAdapter.removeLastItem();
        mLoadItemsSuccess = true;
        mSwipeRefreshLayout.setRefreshing(false);
        int currentCount = arrayFeeds.size();
        // convert new articles to items array
        ArrayList<ListingItem> newListingItems = convertFeedsToItems(arrayFeeds);
        //add new items to mRecyclerView
        mCelebritiesListAdapter.addItems(newListingItems);
        // update mThreshold
        if (mItemsCount == 0) {
            mThreshold += currentCount / 2;
        } else {
            mThreshold += currentCount;
        }
        mItemsCount += currentCount;
        isLoading = false;
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
        mCelebritiesListAdapter.addNewItem(loaderItem);
    }

    /**
     * Add reloader item to the adapter.
     */
    private void addReloaderItem(NetworkErrorType error) {
        Reloader reloader = new Reloader(error);
        ReloaderItem reloaderItem = new ReloaderItem(reloader);
        mCelebritiesListAdapter.addNewItem(reloaderItem);
    }

    OnListListener listListener = new OnListListener() {

        @Override
        public void onReloaderButtonSelected() {
            reloadClicked();
        }
    };

    private void reloadClicked() {
        mCelebritiesListAdapter.removeLastItem();
        requestCelebrityFeeds();
    }

}
