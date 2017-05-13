package com.nader.starfeeds.ui.sections.explore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.nader.starfeeds.R;
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
import com.nader.starfeeds.data.FeedsProvider;
import com.nader.starfeeds.data.NetworkErrorType;
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

public class ExploreDetailActivity extends AppCompatActivity {
    public static final String FEED = "feed";
    private FeedsListAdapter mFeedsListAdapter;
    private RecyclerView mRecyclerView;
    boolean mLoadItemsSuccess = true;
    FeedsProvider mFeedsProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_detail);
        initToolbar();
        initRecyclerView();
        //initFeedsProvider();
        //requestNewSet();
        bindFeed();
    }

    private void bindFeed() {
        Feed feed = (Feed) getIntent().getSerializableExtra(FEED);
        ListingItem listingItem = null;
        switch (feed.getFeedType()) {
            case FACEBOOK_IMAGE:
                listingItem = new FeedFacebookImageItem((FeedFacebookImage) feed);
                break;
            case FACEBOOK_VIDEO:
                listingItem = new FeedFacebookVideoItem((FeedFacebookVideo) feed);
                break;
            case FACEBOOK_TEXT:
                listingItem = new FeedFacebookTextItem((FeedFacebookText) feed);
                break;
            case TWITTER_IMAGE:
                listingItem = new FeedTwitterImageItem((FeedTwitterImage) feed);
                break;
            case TWITTER_TEXT:
                listingItem = new FeedTwitterTextItem((FeedTwitterText) feed);
                break;
            case FACEBOOK_LINK:
                listingItem = new FeedFacebookLinkItem((FeedFacebookLink) feed);
                break;
            case INSTAGRAM_IMAGE:
                listingItem = new FeedInstagramImageItem((FeedInstagramImage) feed);
                break;
            case INSTAGRAM_VIDEO:
                listingItem = new FeedInstagramVideoItem((FeedInstagramVideo) feed);
                break;
            case TWITTER_LINK:
                listingItem = new FeedTwitterLinkItem((FeedTwitterLink) feed);
                break;
        }

        mFeedsListAdapter.addNewItem(listingItem);
        mLoadItemsSuccess = true;
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext(),LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        //create new adapter
        mFeedsListAdapter = new FeedsListAdapter(null, listListener, getBaseContext(), false);
        mRecyclerView.setAdapter(mFeedsListAdapter);

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
                    mFeedsListAdapter.removeLastItem();
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
                    mFeedsListAdapter.removeLastItem();
                    addReloaderItem(fail);
                    mLoadItemsSuccess = false;
                }
            };

    private ArrayList<ListingItem> convertFeedsToItems(ArrayList<Feed> arrayFeeds) {
        //save the received array
        ArrayList<ListingItem> newListingItems = new ArrayList<>();
        for (int i = 0; i < arrayFeeds.size(); i++) {
            FeedItem a = new FeedItem(arrayFeeds.get(i));
            if(( a.getFeed().getId().matches(getIntent().getExtras().get("FeedType").toString())))
            switch (a.getFeed().getFeedType()) {
                case TWITTER_IMAGE:
                    FeedTwitterImageItem feedTwitterImageItem = new FeedTwitterImageItem((FeedTwitterImage) a.getFeed());
                    newListingItems.add(feedTwitterImageItem);
                    break;

                case FACEBOOK_IMAGE:
                    FeedFacebookImageItem feedFacebookImageItem=new FeedFacebookImageItem((FeedFacebookImage) a.getFeed());
                    newListingItems.add(feedFacebookImageItem);
                    break;

                case FACEBOOK_LINK:
                    FeedFacebookLinkItem feedFacebookLinkItem =new FeedFacebookLinkItem((FeedFacebookLink) a.getFeed());
                    newListingItems.add(feedFacebookLinkItem);
                    break;
                case INSTAGRAM_IMAGE:
                    FeedInstagramImageItem feedInstagramImageItem=new FeedInstagramImageItem((FeedInstagramImage) a.getFeed());
                    newListingItems.add(feedInstagramImageItem);
                    break;

                case TWITTER_LINK:
                    FeedTwitterLinkItem feedTwitterLinkItem =new FeedTwitterLinkItem((FeedTwitterLink) a.getFeed());
                    newListingItems.add(feedTwitterLinkItem);
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
        mFeedsListAdapter.addItems(newListingItems);

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
     * Add reloader item to the adapter.
     */
    private void addReloaderItem(NetworkErrorType error) {
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
        public void onFollowClick(String celebrity) {

        }

        @Override
        public void onUnFollowClick(String celebrity) {

        }

        @Override
        public void onImageClicked(String imageUrl) {

        }
    };

    private void reloadClicked() {
        mFeedsListAdapter.removeLastItem();
        requestNewSet();
    }


}
