package com.nader.starfeeds.data.api;


import com.nader.starfeeds.data.error.ApiError;

import java.util.ArrayList;

/**
 * Created by Nader on 17-Apr-17.
 *
 * <p>
 *     Class providing utility methods, like generating a coma separated
 *     String for example.
 * </p>
 */
public final class ApiHelper {
    /**
     * Generates a String containing the list of items in the array, separated by a coma.
     * @param array The list of items.
     * @return The String generated if the array is valid and contains items, null otherwise.
     */
    public static String generateComaSeparatedStringFromArray(ArrayList<String> array) {
        String result;
        if(array != null && !array.isEmpty()) {
            result = array.get(0);
            if(array.size() > 1) {
                for (int i = 0; i < array.size(); i++) {
                    result = "," + result + array.get(i);
                }
            }
        }
        else {
            result = null;
        }
        return result;
    }

    /**
     * Parses the API status error and returns the matching
     * ApiError. BAD_REQUEST is the default return value.
     */
    public static ApiError getErrorFromCode(int code) {
        ApiError error = ApiError.BAD_REQUEST;
        // check if content not found
        if (code == 400) {
            error = ApiError.REQUEST_FAILED;
        } else if (code == 406) {
            error = ApiError.INPUT_MALFROMATTED;
        }
        else if(code > 400 && code <= 500) {
            error = ApiError.BAD_RESPONSE;
        }
        return error;
    }
}
