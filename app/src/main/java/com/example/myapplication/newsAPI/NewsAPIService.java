package com.example.myapplication.newsAPI;

import com.example.myapplication.newsAPI.model.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsAPIService {
    @GET("everything")
    Call<NewsResponse> getBreakingNews(
            @Query("q") String query,
            @Query("sortBy") String sortBy,
            @Query("pageSize") int pageSize,
            @Query("apiKey") String apiKey,
            @Query("language") String language
    );
}
