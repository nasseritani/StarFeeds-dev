package com.nader.starfeeds.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nader.starfeeds.R;
import com.nader.starfeeds.components.Feed;
import com.nader.starfeeds.components.FeedFacebook;
import com.nader.starfeeds.components.FeedFacebookImage;
import com.nader.starfeeds.components.FeedFacebookText;
import com.nader.starfeeds.components.FeedFacebookVideo;
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
import com.nader.starfeeds.listing.FeedItem;
import com.nader.starfeeds.listing.FeedTwitterImageItem;
import com.nader.starfeeds.listing.FeedTwitterTextItem;
import com.nader.starfeeds.listing.ListingItem;
import com.nader.starfeeds.listing.LoaderItem;
import com.nader.starfeeds.listing.ReloaderItem;
import com.nader.starfeeds.ui.listeners.OnListListener;
import com.nader.starfeeds.ui.tools.FeedsListAdapter;

import java.util.ArrayList;

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
    FeedsProvider mFeedsProvider;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    /**
     * Create new ArticlesProvider instance.
     */
    private void initFeedsProvider() {
        mFeedsProvider = new FeedsProvider(feedsProviderListener, 6);
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
                    FeedFacebookItem feedFacebookItem=new FeedFacebookItem((FeedFacebook) a.getFeed());
                    newListingItems.add(feedFacebookItem);
                    break;
                case TWITTER:
                    break;
            }
        }
        return newListingItems;

    }


    /**
     * Requests a new set of data.
     */
    private void requestNewSet() {
        Log.i("nn","request set");
        if(mFeedsProvider != null){
            mFeedsProvider.requestNextFeedsSet();
        }
    }


    //testing
    private void handleNewDataFeeds(ArrayList<Feed> arrayFeeds, int currentCount) {
        // convert new articles to items array
        ArrayList<ListingItem> newListingItems = convertFeedsToItems(arrayFeeds);
        //add new items to mRecyclerView
        mFeedsListAdapter.addNewItems(newListingItems);
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
        mFeedsListAdapter.addNewItem(loaderItem);
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
    };

    private void reloadClicked() {
        mFeedsListAdapter.removeLastItem();
        requestNewSet();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_feeds_list, container, false);
        mRecyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mFeedsListAdapter = new FeedsListAdapter(null, listListener, getActivity());//create new adapter
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
                        requestNewSet();
                    }
                }
            }
        });
        initFeedsProvider();
        requestNewSet();
        return v;
    }
}