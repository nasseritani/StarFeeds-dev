package com.nader.starfeeds.ui.tools.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.nader.starfeeds.R;
import com.nader.starfeeds.components.Feed;
import com.nader.starfeeds.components.FeedFacebookLink;
import com.nader.starfeeds.components.FeedFacebookImage;
import com.nader.starfeeds.components.FeedFacebookText;
import com.nader.starfeeds.components.FeedFacebookVideo;
import com.nader.starfeeds.components.FeedInstagramImage;
import com.nader.starfeeds.components.FeedTwitterLink;
import com.nader.starfeeds.components.FeedTwitterImage;
import com.nader.starfeeds.components.FeedTwitterText;
import com.nader.starfeeds.components.Loader;
import com.nader.starfeeds.components.Reloader;
import com.nader.starfeeds.data.FeedsProvider;
import com.nader.starfeeds.data.NetworkErrorType;
import com.nader.starfeeds.listing.FeedFacebookImageItem;
import com.nader.starfeeds.listing.FeedFacebookItem;
import com.nader.starfeeds.listing.FeedFacebookTextItem;
import com.nader.starfeeds.listing.FeedFacebookVideoItem;
import com.nader.starfeeds.listing.FeedInstagramImageItem;
import com.nader.starfeeds.listing.FeedItem;
import com.nader.starfeeds.listing.FeedTwitterImageItem;
import com.nader.starfeeds.listing.FeedTwitterItem;
import com.nader.starfeeds.listing.FeedTwitterTextItem;
import com.nader.starfeeds.listing.ListingItem;
import com.nader.starfeeds.listing.LoaderItem;
import com.nader.starfeeds.listing.ReloaderItem;
import com.nader.starfeeds.ui.listeners.OnListListener;
import com.nader.starfeeds.ui.tools.Adapters.CelebrityAdapter;

import java.util.ArrayList;

public class CelebrityActivity extends AppCompatActivity {
    private CelebrityAdapter mCelebritiesListAdapter;
    private int mThreshold = 0;
    private int mItemsCount;
    private RecyclerView mRecyclerView;
    boolean mLoadItemsSuccess = true;
    FeedsProvider mFeedsProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celebrity);
        initToolbar();
     initRecyclerView();
        initFeedsProvider();
        requestNewSet();

    }
    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext(),LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        //create new adapter
        mCelebritiesListAdapter = new CelebrityAdapter(null, listListener, getBaseContext());
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
                        requestNewSet();
                    }
                }
            }
        });
    }


    /**
     * Create new ArticlesProvider instance.
     */
   private void initFeedsProvider() {
        mFeedsProvider = new FeedsProvider(feedsProviderListener, 6);
    }
    private void initToolbar() {
        Toolbar mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        setTitle(getIntent().getExtras().get("Celebname").toString());
        mtoolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private FeedsProvider.OnFeedsProviderListener feedsProviderListener =
            new FeedsProvider.OnFeedsProviderListener() {
                @Override
                public void onComplete(ArrayList<Feed> arrayFeeds, int currentCount) {
                    Log.i("nn", "complete");
                    mCelebritiesListAdapter.removeLastItem();
                    handleNewDataFeeds(arrayFeeds, currentCount);
                    mLoadItemsSuccess = true;
                }

                @Override
                public void onStateChanged(boolean loading) {
                    if (loading) {
                        addLoaderItem();
                    }

                }

                @Override
                public void onFailed(NetworkErrorType fail) {
                    Log.i("nn", "failed");
                    mCelebritiesListAdapter.removeLastItem();
                    addReloaderItem(fail);
                    mLoadItemsSuccess = false;
                }
            };

    private ArrayList<ListingItem> convertFeedsToItems(ArrayList<Feed> arrayFeeds) {
        //save the received array
        ArrayList<ListingItem> newListingItems = new ArrayList<>();
        for (int i = 0; i < arrayFeeds.size(); i++) {
            FeedItem a = new FeedItem(arrayFeeds.get(i));
            Log.d("type", ":" + a.getType());
            if (a.getFeed().getCelebName().matches(getIntent().getExtras().get("Celebname").toString()))//Matching the celebname to get their profile
                switch (a.getFeed().getFeedType()) {
                    case TWITTER_IMAGE:
                        FeedTwitterImageItem feedTwitterImageItem = new FeedTwitterImageItem((FeedTwitterImage) a.getFeed());
                        newListingItems.add(feedTwitterImageItem);
                        break;
                    case TWITTER_TEXT:
                        FeedTwitterTextItem feedTwitterTextItem=new FeedTwitterTextItem((FeedTwitterText) a.getFeed());
                        newListingItems.add(feedTwitterTextItem);
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
                    case FACEBOOK:
                        FeedFacebookItem feedFacebookItem=new FeedFacebookItem((FeedFacebookLink) a.getFeed());
                        newListingItems.add(feedFacebookItem);
                        break;
                    case INSTAGRAM_IMAGE:
                        FeedInstagramImageItem feedInstagramImageItem=new FeedInstagramImageItem((FeedInstagramImage) a.getFeed());
                        newListingItems.add(feedInstagramImageItem);
                        break;
                /*case INSTAGRAM_VIDEO:
                    FeedInstagramVideoItem feedInstagramVideoItem=new FeedInstagramVItem((FeedInstagramImage) a.getFeed());
                    newListingItems.add(feedInstagramImageItem);
                    break;
                    */
                    case TWITTER:
                        FeedTwitterItem feedTwitterItem=new FeedTwitterItem((FeedTwitterLink) a.getFeed());
                        newListingItems.add(feedTwitterItem);
                        break;
                }
        }
        return newListingItems;

    }



    /**
     * Requests a new set of data.
     */
    private void requestNewSet() {
        Log.i("nn", "request set");
        if (mFeedsProvider != null) {
            mFeedsProvider.requestNextFeedsSet();
        }
    }


    //testing
    private void handleNewDataFeeds(ArrayList<Feed> arrayFeeds, int currentCount) {
        // convert new articles to items array
        ArrayList<ListingItem> newListingItems = convertFeedsToItems(arrayFeeds);
        //add new items to mRecyclerView
        mCelebritiesListAdapter.addNewItems(newListingItems);
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
        requestNewSet();
    }


}
