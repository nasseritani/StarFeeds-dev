package com.nader.starfeeds.data.api;

/**
 * Created by Nader on 17-Apr-17.
 *
 * <p>
 *     Class holding the configuration of the API.
 * </p>
 */
public final class ApiConfig {
    public static boolean REAL_API = true;
    private static String host = "http://192.168.43.209/StarFeeds/public";

    /**
     * Returns the host address of the API.
     */
    public static String getApiHost() {
        return host;
    }
}
