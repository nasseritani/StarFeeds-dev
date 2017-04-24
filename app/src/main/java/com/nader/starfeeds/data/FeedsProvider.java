package com.nader.starfeeds.data;

import android.util.Log;

import com.nader.starfeeds.data.componenets.model.Feed;
import com.nader.starfeeds.utils.InternetConnection;

import java.util.ArrayList;

/**
 * Created by Nader on 12-Feb-17.
 */

public class FeedsProvider {

    private final OnFeedsProviderListener listener;
    private boolean isLoading = false;
    private int index = 1;
    int count = 10;
    final int limit;

    public FeedsProvider(OnFeedsProviderListener listener, int limit) {
        this.listener = listener;
        this.limit = limit;
    }

    /**
     * Broadcasts a new set of Feeds.
     */
    public void requestNextFeedsSet() {
        // check if not occupied
        if (isLoading) return;
        // set flag
        isLoading = true;
        // update state
        broadcastState(isLoading);
        if (InternetConnection.checkConnection()) {
            requestFeeds();
            Log.i("nn","request");
        }
        else {
            broadcastFailed(NetworkErrorType.CONNECTION_FAIL);
            isLoading = false;
            broadcastState(isLoading);
        }
    }

    /**
     * Requests Feeds from the API.
     */
    private void requestFeeds() {
        /*InternalJsonFileData internalJsonFileData = new InternalJsonFileData(Configuration.getContext(), new InternalJsonFileData.JSONParserListener() {
            @Override
            public void onResult(ArrayList<Feed> feeds) {
                // update flag
                isLoading = false;
                // update state
                broadcastState(isLoading);
                if (feeds.size()>0){

                    Log.i("nn","broadcast complete");
                    broadcastComplete(feeds, feeds.size());
                }
                else {
                    broadcastFailed(NetworkErrorType.API_FAIL);
                }
            }

        });
        internalJsonFileData.loadJSONFromAsset();*/

        HttpRequestHandler httpRequestHandler = new HttpRequestHandler(new HttpRequestHandler.JSONParserListener() {
            @Override
            public void onResult(ArrayList<Feed> feeds) {
                // update flag
                isLoading = false;
                // update state
                broadcastState(isLoading);
                if (feeds.size()>0){
                    Log.i("nn","broadcast complete");
                    broadcastComplete(feeds, feeds.size());
                }
                else {
                    broadcastFailed(NetworkErrorType.API_FAIL);
                }
            }
        });
        httpRequestHandler.execute("http://192.168.43.209/StarFeeds/public/user-feeds/10");
    }

    public boolean isLoading() {
        return isLoading;
    }


    private void broadcastState(boolean loading) {
        if (listener != null) listener.onStateChanged(loading);
    }

    private void broadcastComplete(ArrayList<Feed> arrayFeeds, int currentCount) {
        if (listener != null) listener.onComplete(arrayFeeds, currentCount);
    }
    private void broadcastFailed(NetworkErrorType fail){
        if (listener != null) listener.onFailed(fail);

    }


    // CALLBACKS //

    public interface OnFeedsProviderListener {
        /**
         * Triggered once the requested data is broadcasted
         * back by the provider.
         *
         * @param arrayFeeds The requested data set.
         */
        void onComplete(ArrayList<Feed> arrayFeeds, int currentCount);
        //test


        /**
         * Indicates the state of the provider.
         *
         * @param loading true if currently executing,
         *                false otherwise.
         */
        void onStateChanged(boolean loading);

        /**
         * Triggered if the request for data failed.
         * @param error is the type of error.
         */
        void onFailed(NetworkErrorType error);
    }
}
