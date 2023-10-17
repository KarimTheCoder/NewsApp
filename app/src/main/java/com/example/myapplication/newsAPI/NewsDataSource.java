package com.example.myapplication.newsAPI;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.example.myapplication.data.local.database.NewsDatabase;
import com.example.myapplication.data.local.entity.Article;
import com.example.myapplication.data.local.repository.NewsRepository;
import com.example.myapplication.newsAPI.model.NewsResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NewsDataSource implements INewsDataSource{
    private static int INITIAL_ARTICLE_SIZE_FOR_ROOM = 5;
    private static final String API_KEY = "d94567c4a73f4587852b27bde5f32ef6";
    private final Application application;
    private final NewsRepository newsRepository;
    private List<Article> articlesFromAPI;
    private Call<NewsResponse> homeNewsCall;
    private NewsAPIService newsAPIService;
    private String language;
    public NewsDataSource(Application application) {

        this.application = application;
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(application);
        language = sharedPref.getString("language", "en");
        NewsDatabase database = NewsDatabase.getDatabase(application);
        newsRepository = new NewsRepository(application,database);

        initNewsAPI();

    }
    private void initNewsAPI(){

        articlesFromAPI = new ArrayList<>();
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        newsAPIService = retrofit.create(NewsAPIService.class);
        homeNewsCall = newsAPIService.getBreakingNews("Happy", "publishedAt", 100, API_KEY, language);
    }

    //Searching articles
    public void queryArticles(String query){

        cleanSearchResults();
        Call<NewsResponse> queryCall =  newsAPIService.getBreakingNews(query,"publishedAt",10,API_KEY,language );
        queryCall.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    // Handle the response here
                    List<Article> articles = response.body().getArticles();
                    // Update UI with the articles


                    for (int i = 0; i < 5; i++) {
                        articles.get(i).setSearchResult(true);
                        newsRepository.addNewsArticle(articles.get(i));
                    }


                } else {
                    // Handle error response
                    try {
                        // Log or print the error response
                        Log.e("API Error", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                // Handle network failure
            }
        });

    }
    private void cleanSearchResults(){
        newsRepository.deleteSearchResult();
    }


    // Getting articles for home
    public void fetchArticles(){

        if(isInternetAvailable()){

            deleteUnsavedArticles();
            callAPIForArticles();

        }else {

            Log.i("News","Network not available");
        }
    }
    private void callAPIForArticles(){
        homeNewsCall.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    // Handle the response here
                    List<Article> responseArticles = response.body().getArticles();

                    articlesFromAPI.addAll(responseArticles);

                    if( responseArticles.size()< INITIAL_ARTICLE_SIZE_FOR_ROOM){
                        INITIAL_ARTICLE_SIZE_FOR_ROOM = responseArticles.size();
                    }
                    for (int i = 0; i < INITIAL_ARTICLE_SIZE_FOR_ROOM; i++) {
                        newsRepository.addNewsArticle(responseArticles.get(i));
                    }

                    for (int i = 0; i < responseArticles.size(); i++) {

                        Log.i("Article","Headline: "+responseArticles.get(i).getUrlToImage());
                    }
                } else {
                    // Handle error response
                    try {
                        // Log or print the error response
                        Log.e("API Error", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                // Handle network failure


            }
        });

    }
    private void deleteUnsavedArticles(){
        newsRepository.deleteUnsavedArticles();
    }

    // All users scroll home recyclerview, more and more articles added to Room Library
    public void saveArticlesToRoom(int size){

        int saveSize= size;

        if(saveSize > articlesFromAPI.size()){

            saveSize = articlesFromAPI.size();
        }


        for (int i = 0; i < saveSize; i++) {
            newsRepository.addNewsArticle(articlesFromAPI.get(i));
            Log.i("Room","Loading data "+i);
        }

        for (int i = 0; i < saveSize; i++) {

            articlesFromAPI.remove(0);
        }

    }
    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                application.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(
                connectivityManager.getActiveNetwork());

        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
    }
}
