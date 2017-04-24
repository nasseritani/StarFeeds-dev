package com.nader.starfeeds.data.api.requests;

import android.support.annotation.NonNull;

import com.nader.starfeeds.data.api.ApiHelper;
import com.nader.starfeeds.data.api.responses.ApiResponse;
import com.nader.starfeeds.data.error.ApiError;
import com.nader.starfeeds.data.error.ErrorResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import rx.Single;
import rx.SingleSubscriber;

/**
 * Created by Nader on 17-Apr-17.
 */

public abstract class ApiRequest {
    /**
     *  Enum listing the HTTP request methods used such as GET and POST.
     */
    private enum HttpRequestMethod {
        GET,
        POST,
        PUT
    }

    /**
     * Class holding the details of a request's response.
     */
    private class MyResponse {
        int status;
        public String result;

        MyResponse(int status, String result) {
            this.status = status;
            this.result = result;
        }
    }

    /**
     * Handles the execution of a GET request by attaching it to an Observable of type Single.
     * @param url The request's url.
     */
    protected Single<ApiResponse> sendGetRequest(@NonNull final String url) {
        return Single.create(new Single.OnSubscribe<ApiResponse>() {
            @Override
            public void call(SingleSubscriber<? super ApiResponse> singleSubscriber) {
                try {
                    // get response of http request
                    MyResponse response = executeUrl(url, HttpRequestMethod.GET, null);
                    // handle succeeded result
                    handleResult(response.status, response.result, singleSubscriber);
                } catch (Exception e) {
                    // call on error with bad response type
                    singleSubscriber.onError(e);
                }
            }
        });
    }

    /**
     * Handles the execution of a GET request by attaching it to an Observable of type Single.
     * @param url The request's url.
     */
    protected Single<ApiResponse> sendPOSTRequest(@NonNull final String url, final JSONObject jsonObject) {
        return Single.create(new Single.OnSubscribe<ApiResponse>() {
            @Override
            public void call(SingleSubscriber<? super ApiResponse> singleSubscriber) {
                try {
                    // get response of http request
                    MyResponse response = executeUrl(url, HttpRequestMethod.POST, jsonObject);
                    // handle succeeded result
                    handleResult(response.status, response.result, singleSubscriber);
                } catch (Exception e) {
                    // call on error with bad response type
                    singleSubscriber.onError(e);
                }
            }
        });
    }


    /**
     * Handles the result of the returned response, and callbacks through the subscriber .
     * @param status of the response.
     * @param result is the content of the response.
     * @param subscriber the subscriber of the observable.
     */
    private void handleResult(int status, String result, SingleSubscriber<? super ApiResponse> subscriber)
            throws Exception {
        // check if status 200
        if(status == 200) {
            // broadcast response object after parsing data
            ApiResponse processedResult =  processSuccessResult(result);
            //return processedResult;
            subscriber.onSuccess(processedResult);
        }
        else {
            // check if parsing error
            // get error object from raw
            try {
                JSONObject jsonError = new JSONObject(result);
                jsonError = jsonError.getJSONObject("error");
                // get error code and message
                int errorCode = jsonError.getInt("code");
                String apiErrorMessage = jsonError.getString("message");
                // check error code
                ApiError apiError = ApiHelper.getErrorFromCode(errorCode);
                // create error object
                ErrorResponse response = new ErrorResponse(apiError, apiErrorMessage);
                // broadcast it
                subscriber.onError(response);
            } catch (JSONException e){

            }
        }
    }

    /**
     * Responsible for parsing the success raw response of the API request.
     * @param result The raw data returned by the API.
     * @return Specific ApiResponse according to the type of the ApiAccess.
     */
    protected abstract ApiResponse processSuccessResult(String result) throws Exception;

    /**
     * Given a URL, establishes an HttpUrlConnection and retrieves
     * he web page content as a InputStream, which it returns as a string.
     * @param requestUrl The String url requested.
     * @param method The Http method.
     * @return response object containing the status and the stream result.
     */
    private MyResponse executeUrl(String requestUrl, HttpRequestMethod method,
                                  JSONObject jsonParams) throws Exception {
        InputStream is;
        int status;
        String strResult;
        // create request url
        URL url = new URL(requestUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setReadTimeout(10000 /* milliseconds */);
//            conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod(method != null ? method.toString() : HttpRequestMethod.GET.toString()); // get by defaut
        conn.setDoInput(true);
        // check if any params to be sen
        if (jsonParams != null && jsonParams.length() > 0) {
            // get params bytes
            final String strParams = jsonParams.toString();
            final byte[] data = strParams.getBytes("UTF-8");
            // set content type
            conn.setRequestProperty("Content-Type", "application/json");
//                conn.setRequestProperty("Content-Length", String.valueOf(data.length));
            // allow output
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            // set output
            DataOutputStream printout = new DataOutputStream(conn.getOutputStream());
            printout.write(data);
            printout.flush ();
            printout.close ();
        }
        // start the query
        conn.connect();
        status = conn.getResponseCode();
        // check if error
        if (status != 200) {
            is = conn.getErrorStream();
        } else {
            is = conn.getInputStream();
        }
        // convert the InputStream into a string
        strResult = readStream(is);
        // Makes sure that the InputStream is closed after the app is
        // finished using it.
        if (is != null) {
            is.close();
        }
        // create response and return it
        return new MyResponse(status, strResult);
    }


    /**
     * Reads an InputStream and converts it to a String.
     */
    private static String readStream(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String nextLine;
        while ((nextLine = reader.readLine()) != null) {
            sb.append(nextLine);
        }
        return sb.toString();
    }
}
