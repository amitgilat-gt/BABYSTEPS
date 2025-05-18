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
