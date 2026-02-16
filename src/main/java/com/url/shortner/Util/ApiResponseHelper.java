package com.url.shortner.Util;

import com.url.shortner.Config.BaseApiResponse;

public final class ApiResponseHelper {

    private ApiResponseHelper() {
        // prevent instantiation
    }

    public static BaseApiResponse success(Object data) {
        return new BaseApiResponse(
                true,
                "Request processed successfully",
                data
        );
    }

    public static BaseApiResponse success(Object data, String message) {
        return new BaseApiResponse(
                true,
                message,
                data
        );
    }

    public static BaseApiResponse failure(String message) {
        return new BaseApiResponse(
                false,
                message,
                null
        );
    }

    public static BaseApiResponse failure(String message, Object data) {
        return new BaseApiResponse(
                false,
                message,
                data
        );
    }
}
