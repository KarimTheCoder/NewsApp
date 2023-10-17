package com.example.myapplication.data.local.repository;

import android.app.Application;

import androidx.paging.DataSource;

import com.example.myapplication.data.local.dao.NewsDao;
import com.example.myapplication.data.local.database.NewsDatabase;
import com.example.myapplication.data.local.entity.Article;

import java.util.List;

public class NewsRepository {
    private final NewsDao newsDao;

    public NewsDao getNewsDao() {
        return newsDao;
    }

    public NewsRepository(Application application, NewsDatabase newsDatabase) {
        NewsDatabase db = newsDatabase;
        newsDao = db.newsDao();
    }

    public void updateArticle(Article article){

        newsDao.updateArticle(article);
    }

    // Database operations
    public void addNewsArticle(Article article){
        NewsDatabase.executor.execute(() -> newsDao.insertArticle(article));
    }
    public void addNewsArticles(List<Article> articles){
        NewsDatabase.executor.execute(() -> newsDao.insertArticles(articles));
    }
    public void deleteNewsArticle(Article article){
        newsDao.deleteArticle(article);
    }
    public DataSource.Factory<Integer, Article> getAllNews(){
        return newsDao.getAllArticles();
    }
    public DataSource.Factory<Integer, Article> getAllSearchResult(){
        return newsDao.getAllSearchResults();
    }
    public Article getArticleByTitle(String title){

        return newsDao.getArticleByTitle(title);
    }

    public DataSource.Factory<Integer, Article> getSavedArticles(){
        return newsDao.getSavedArticles();
    }
    public void saveArticle(Article article){
        article.setSaved(true);
        article.setSearchResult(false);
        NewsDatabase.executor.execute(() -> newsDao.updateArticle(article));
    }
    public void unsaveArticle(Article article){
        article.setSaved(false);
        article.setSearchResult(false);
        NewsDatabase.executor.execute(()-> newsDao.updateArticle(article));
    }
    public void deleteUnsavedArticles(){
        NewsDatabase.executor.execute(newsDao::deleteUnsavedArticles);
    }
    public void deleteSearchResult(){
        NewsDatabase.executor.execute(newsDao::deleteSearchResults);
    }


}
