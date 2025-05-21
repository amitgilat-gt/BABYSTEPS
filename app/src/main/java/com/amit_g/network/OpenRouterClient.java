package com.amit_g.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Singleton client class to provide access to the OpenRouter API
public class OpenRouterClient {

    private static final String BASE_URL = "https://openrouter.ai/api/"; // Base URL for API requests
    private static OpenRouterApi api; // Single instance of the API interface

    // Returns a singleton instance of OpenRouterApi
    public static OpenRouterApi getApi() {
        if (api == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            api = retrofit.create(OpenRouterApi.class);
        }
        return api;
    }
}
