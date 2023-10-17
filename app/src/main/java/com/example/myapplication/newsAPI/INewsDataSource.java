package com.example.myapplication.newsAPI;

public interface INewsDataSource {
    void queryArticles(String query);
    void fetchArticles();
    void saveArticlesToRoom(int size);

}
