package com.example.myapplication.data.local.dao;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.data.local.entity.Article;

import java.util.List;

@Dao
public interface NewsDao {

    @Query("DELETE FROM news_article")
    void deleteAllArticles();
    @Insert
    void insertArticles(List<Article> articles);
    @Update
    void updateArticles(List<Article> articles);

    @Query("SELECT * FROM news_article WHERE title = :articleTitle")
    Article getArticleByTitle(String articleTitle);

    @Insert
    void insertArticle(Article article);
    @Update
    void updateArticle(Article article);
    @Delete
    void deleteArticle(Article article);

    @Query("DELETE FROM news_article WHERE isSearchResult = 1")
    void deleteSearchResults();
    @Query("DELETE FROM news_article WHERE isSaved = 0")
    void deleteUnsavedArticles();

    @Query("SELECT * FROM news_article WHERE isSaved = 1")
    DataSource.Factory<Integer, Article> getSavedArticles();

    @Query("SELECT * FROM news_article WHERE isSaved= 0 AND isSearchResult=0")
    DataSource.Factory<Integer, Article> getAllArticles();

    @Query("SELECT * FROM news_article WHERE isSearchResult= 1")
    DataSource.Factory<Integer, Article> getAllSearchResults();


}
