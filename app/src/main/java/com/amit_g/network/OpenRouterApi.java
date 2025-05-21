// File: com/amit_g/network/OpenRouterApi.java
package com.amit_g.network;

import com.amit_g.dto.R1Request;
import com.amit_g.dto.R1Response;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

// Retrofit interface for communicating with the OpenRouter API
public interface OpenRouterApi {

    // Sends a POST request to the OpenRouter API's chat completion endpoint
    // Includes authorization and content-type headers
    @Headers({
            "Authorization: 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", // WARNING: Replace with secure token management
            "Content-Type: application/json"
    })
    @POST("v1/chat/completions")
    Call<R1Response> getPercentile(@Body R1Request request); // Sends an R1Request and expects an R1Response
}