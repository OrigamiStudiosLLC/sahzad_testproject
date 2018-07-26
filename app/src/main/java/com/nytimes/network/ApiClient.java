package com.nytimes.network;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import com.nytimes.utalities.Constants;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient implements Constants {
    // base url for the API Client
    private static final String BASE_URL = Constants.BASE_URL;
    // Retrofit client instance
    private static Retrofit retrofit = null;
    // Set gson lenient
    private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    // Singleton initialization of Retrofit client instance
    public static Retrofit getClient() {
        if (retrofit == null) {
            // OkHTTPClient Client initialization
            OkHttpClient httpClient = new OkHttpClient.Builder()
                    // Connection timeout 60 seconds
                    .connectTimeout(60, TimeUnit.SECONDS)
                    // Socket Read timeout 60 seconds
                    .readTimeout(60, TimeUnit.SECONDS)
                    // Socket Write timeout 500 seconds
                    .writeTimeout(500, TimeUnit.SECONDS)
                    // Add HTTP logging interceptor at body level
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build();
            retrofit = new Retrofit.Builder()
                    // Add GSON parser
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    // Set baseurl
                    .baseUrl(BASE_URL)
                    // set httpClient OkHTTPClient.
                    .client(httpClient)
                    .build();
        }
        return retrofit;
    }
}