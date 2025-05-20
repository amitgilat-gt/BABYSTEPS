// File: com/amit_g/network/OpenRouterApi.java
package com.amit_g.network;


import com.amit_g.dto.R1Request;
import com.amit_g.dto.R1Response;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface OpenRouterApi {

    @Headers({
            "Authorization: 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", // REPLACE THIS SAFELY
            "Content-Type: application/json"
    })
    @POST("v1/chat/completions")
    Call<R1Response> getPercentile(@Body R1Request request);
}
