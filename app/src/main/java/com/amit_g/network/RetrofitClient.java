package com.amit_g.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://openrouter.ai/api/"; // Correct base URL
    private static final String API_KEY = "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"; // Replace with your actual API key
    private static Retrofit retrofit;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
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
                    .client(okHttpClient)  // Use the client with the interceptor
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
