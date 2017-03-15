package com.nader.starfeeds.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.nader.starfeeds.ui.tools.ItemsListAdapter;

import java.util.ArrayList;

/**
 * Created by Nasse_000 on 3/11/2017.
 */

public class OneFragment extends Fragment {
    private Toolbar mtoolbar;
    private ItemsListAdapter mItemsListAdapter;
    private int mThreshold = 0;
    private int mItemsCount;
    private RecyclerView mRecyclerView;
    boolean mLoadItemsSuccess = true;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    FeedsProvider mFeedsProvider;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private void initFeedsProvider() {
        mFeedsProvider = new FeedsProvider(feedsProviderListener, 6);
    }
 /*   private void initRecyclerView() {
  mRecyclerView = (RecyclerView)getActivity().findViewById(R.id.recyclerView);
    final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        //create new adapter
        mItemsListAdapter = new ItemsListAdapter(null, listListener, getActivity());
        mRecyclerView.setAdapter(mItemsListAdapter);

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
    */
    private FeedsProvider.OnFeedsProviderListener feedsProviderListener =
            new FeedsProvider.OnFeedsProviderListener() {
                @Override
                public void onComplete(ArrayList<Feed> arrayFeeds, int currentCount) {
                    Log.i("nn", "complete");
                    mItemsListAdapter.removeLastItem();
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
                    mItemsListAdapter.removeLastItem();
                    addReloaderItem(fail);
                    mLoadItemsSuccess = false;
                }
            };

    //testing

    private ArrayList<ListingItem> convertFeedsToItems(ArrayList<Feed> arrayFeeds) {
        //save the received array
        ArrayList<ListingItem> newListingItems = new ArrayList<>();
        for (int i = 0; i < arrayFeeds.size(); i++) {
            FeedItem a = new FeedItem(arrayFeeds.get(i));
            FeedItem a2 = null;
            // Toast.makeText(getApplicationContext(),""+arrayFeeds.get(i),Toast.LENGTH_LONG).show();
            Log.d("type", ":" + a.getType());
            switch (a.getFeed().getFeedType()) {

                case TWITTER_IMAGE:
                    //testing
                    //   Toast.makeText(getApplicationContext(), ":" + a.getFeed(), Toast.LENGTH_LONG).show();
                    FeedTwitterImageItem aItem3 = new FeedTwitterImageItem((FeedTwitterImage) a.getFeed());
                    newListingItems.add(aItem3);
                    break;
                case TWITTER_TEXT:
                    FeedTwitterTextItem aI1=new FeedTwitterTextItem((FeedTwitterText) a.getFeed());
                    newListingItems.add(aI1);
                    break;
                case FACEBOOK_IMAGE:
                    FeedFacebookImageItem aI4=new FeedFacebookImageItem((FeedFacebookImage) a.getFeed());
                    newListingItems.add(aI4);
                    break;
                case FACEBOOK_VIDEO:
                    FeedFacebookVideoItem aI5=new FeedFacebookVideoItem((FeedFacebookVideo) a.getFeed());
                    newListingItems.add(aI5);
                    break;
                case FACEBOOK_TEXT:
                    FeedFacebookTextItem aI2=new FeedFacebookTextItem((FeedFacebookText) a.getFeed());
                    newListingItems.add(aI2);
                    break;


//                case FeedFacebookImage:
                //                  Toast.makeText(getApplicationContext(), ":" + a.getFeed(), Toast.LENGTH_LONG).show();
                //                FeedFacebookImageItem aI = new FeedFacebookImageItem((FeedFacebookImage) a.getFeed());
                //              newListingItems.add(aI);
                //            break;
                //default:
//FeedItem aq=new FeedItem(a.getFeed());
                //      if(aq.getFeed().getFeedType()== Feed.FeedType.FACEBOOK_IMAGE){

                //    }

                case FACEBOOK:
                    FeedFacebookItem aI7=new FeedFacebookItem((FeedFacebook) a.getFeed());
                    newListingItems.add(aI7);
                    break;
                case TWITTER:
                    break;
            }

            //   switch (arrayFeeds.get(i)){
            //     case TWITTER_IMAGE :
            //  FeedTwitterImageItem aItem = new FeedTwitterImageItem((FeedTwitterImage)a.getFeed());
            // newListingItems.add(aItem);
            //   break;
            //  case FeedFacebookImage:
            //FeedFacebookImageItem aItem3=new FeedFacebookImageItem((FeedFacebookImage) a.getFeed());
            //    newListingItems.add(a);
            //break;

            //case TWITTER_TEXT:
            //FeedTwitterTextItem aItem2=new FeedTwitterTextItem((FeedTwitterText) a.getFeed());
            // newListingItems.add(aItem2);
            ///  break;
            //default:

        }
        return newListingItems;

    }


    /**
     * Requests a new set of data.
     */
    private void requestNewSet() {
        /*if (mArticlesProvider != null) {
            mArticlesProvider.requestNextArticlesSet();
        }*/
        //testing
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
        mItemsListAdapter.addNewItems(newListingItems);
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
        mItemsListAdapter.addNewItem(loaderItem);
    }

    /**
     * Add reloader item to the adapter.
     */
    private void addReloaderItem(NetworkErrorType error){
        Reloader reloader = new Reloader(error);
        ReloaderItem reloaderItem = new ReloaderItem(reloader);
        mItemsListAdapter.addNewItem(reloaderItem);
    }

    OnListListener listListener = new OnListListener() {

        @Override
        public void onReloaderButtonSelected() {
            reloadClicked();
        }
    };

    private void reloadClicked() {
        mItemsListAdapter.removeLastItem();
        requestNewSet();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_articles_list, container, false);
       // mRecyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        mRecyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        //create new adapter
        mItemsListAdapter = new ItemsListAdapter(null, listListener, getActivity());
        mRecyclerView.setAdapter(mItemsListAdapter);

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
                        //final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        //mRecyclerView.setLayoutManager(linearLayoutManager);
                        //mRecyclerView.setHasFixedSize(true);


                    }

                }
         //       return v;
            }
        });
        initFeedsProvider();
        requestNewSet();
        return v;
    }
}