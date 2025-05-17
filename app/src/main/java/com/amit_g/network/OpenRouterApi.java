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
