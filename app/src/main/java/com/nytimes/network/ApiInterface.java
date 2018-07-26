package com.nytimes.network;

import com.nytimes.apis.GetArticlesResponse;
import retrofit2.Call;

import retrofit2.http.GET;

import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("mostviewed/all-sections/{id}")
    Call<GetArticlesResponse> getArticlesListResponse(@Header("api-key") String apiKey,
                                                      @Path("id") int id);
}
