package com.nader.starfeeds.data.error;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Nader on 17-Apr-17.
 *
 * <p>
 *     Class holding the details of an error returned after
 *     an failure in executing a API request.
 * </p>
 */
public final class ErrorResponse extends Throwable {
    private ApiError error;
    private String errorMessage;

    public ErrorResponse(ApiError error, String errorMessage) {
        this.error = error;
        this.errorMessage = errorMessage;
    }

    @NonNull
    public ApiError getError() {
        return error;
    }

    @Nullable
    public String getErrorMessage() {
        return errorMessage;
    }
}
