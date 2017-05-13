package com.nader.starfeeds.ui.sections.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.nader.starfeeds.R;
import com.nader.starfeeds.data.SessionManager;
import com.nader.starfeeds.data.api.requests.CelebritiesFollowedRequest;
import com.nader.starfeeds.data.api.requests.CelebritiesSuggestionsRequest;
import com.nader.starfeeds.data.api.requests.DislikeCelebrityRequest;
import com.nader.starfeeds.data.api.requests.FollowCelebrityRequest;
import com.nader.starfeeds.data.api.requests.UnFollowCelebrityRequest;
import com.nader.starfeeds.data.api.responses.ApiResponse;
import com.nader.starfeeds.data.api.responses.CelebritiesResponse;
import com.nader.starfeeds.data.api.responses.PostRequestResponse;
import com.nader.starfeeds.data.componenets.Loader;
import com.nader.starfeeds.data.componenets.model.Celebrity;
import com.nader.starfeeds.ui.sections.celebrity.CelebrityActivity;
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


public class ProfileFragment extends Fragment {

    private CelebrityListAdapter celebrityListAdapter;
    private RecyclerView mRecyclerView;
    private Button btnEditProfile;
    boolean mLoadItemsSuccess = true;
    private boolean isLoading = false;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();
    private int currentPage;
    private int mThreshold = 0;
    private int mItemsCount;
    ProgressDialog pd ;
    private String userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        userId = SessionManager.getInstance().getSessionUser().getId();
        btnEditProfile = (Button) v.findViewById(R.id.btnEditProfile);
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEditProfileActivity();
            }
        });
        mRecyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        celebrityListAdapter = new CelebrityListAdapter(null,getActivity(), false, new CelebrityListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Celebrity celebrity) {
                if (celebrity != null) {
                    startCelebrityActivity(celebrity.getId());
                }
            }

            @Override
            public void onFollowClick(Celebrity celebrity) {

            }

            @Override
            public void onUnFollowClick(Celebrity celebrity) {
                sendUnFollowRequest(userId, celebrity);
            }

            @Override
            public void onDislikeClick(Celebrity celebrity) {

            }
        });//create new adapter
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
        requestCelebritiesFollowed(userId);
        return v;
    }

    private void startEditProfileActivity() {
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        startActivity(intent);
    }


    private void sendUnFollowRequest(@NonNull String userId,@NonNull final Celebrity celebrity) {
        pd = new ProgressDialog(getContext());
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
        Toast.makeText(getContext(),"Something Went Wrong, Try Again Later", Toast.LENGTH_SHORT).show();
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
        Intent i=new Intent(getContext(), CelebrityActivity.class);
        i.putExtra(CelebrityActivity.CELEB_ID, id);
        startActivity(i);
    }

    private void requestCelebritiesFollowed(@NonNull String userID) {
        addLoaderItem();
        CelebritiesFollowedRequest apiRequest = new CelebritiesFollowedRequest();
        // create rx subscription
        Subscription celebritySearchSubscription = apiRequest.fetchCelebFollowing(userID)
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
        if (items != null) {
            ArrayList<SearchListingItem> newListingItems = convertFeedsToItems(items);
            //add new items to mRecyclerView
            celebrityListAdapter.addNewItems(newListingItems);
        }
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
