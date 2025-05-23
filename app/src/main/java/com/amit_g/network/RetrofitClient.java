package com.amit_g.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Singleton Retrofit client used to send requests to the OpenRouter API
public class RetrofitClient {

    private static final String BASE_URL = "https://openrouter.ai/api/"; // Base URL for the OpenRouter API
    private static final String API_KEY = "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"; // WARNING: hardcoded key — move to secure config
    private static Retrofit retrofit;

    // Returns a singleton Retrofit instance configured with an authorization interceptor
    public static Retrofit getInstance() {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        // Adds the Authorization header to every request
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();
                            Request.Builder requestBuilder = original.newBuilder()
                                    .header("Authorization", "Bearer " + API_KEY)
                                    .method(original.method(), original.body());
                            Request request = requestBuilder.build();
                            return chain.proceed(request);
                        }
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
