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
            "Authorization: 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",
            "Content-Type: application/json"
    })
    @POST("v1/chat/completions")
    Call<R1Response> getPercentile(@Body R1Request request); // Sends an R1Request and expects an R1Response
}