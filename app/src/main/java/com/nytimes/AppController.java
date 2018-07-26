package com.nytimes;


import android.app.Application;

import com.nytimes.network.ApiClient;
import com.nytimes.network.ApiInterface;

public class AppController extends Application {

    private static AppController mInstance;
    private static ApiInterface apiService;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        configRetrofit();
    }

    private static void configRetrofit() {
        apiService = ApiClient.getClient().create(ApiInterface.class);
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public static ApiInterface getApiService() {
        return apiService;
    }
}
