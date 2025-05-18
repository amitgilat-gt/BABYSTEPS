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
