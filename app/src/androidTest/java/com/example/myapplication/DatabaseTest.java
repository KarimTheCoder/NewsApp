package com.example.myapplication;

import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.myapplication.data.local.dao.NewsDao;
import com.example.myapplication.data.local.database.NewsDatabase;
import com.example.myapplication.data.local.entity.Article;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private NewsDao newsDao;
    private NewsDatabase db;
    List<Article> articles;
    @Before
    public void createDb() {

        FakeDataSource.NewsArticleSampleData data = new FakeDataSource.NewsArticleSampleData();
        articles = data.createSampleData();
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, NewsDatabase.class).build();
        newsDao = db.newsDao();
    }
    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertTest() {

       Article article = articles.get(0);
       String title = article.getTitle();
       newsDao.insertArticle(article);
       Article byName = newsDao.getArticleByTitle(title);
        assertNotNull("Article with title '" + title + "' not found in the database.", byName);
       assertEquals(byName.getTitle(),title);

    }

    @Test
    public void updateTest() {

        String updatedTitle = "Updated Title";
        Article article = articles.get(0);
        String title = article.getTitle();
        newsDao.insertArticle(article);

        Article articleToUpdate = newsDao.getArticleByTitle(title);
        // Check if the article is null and fail the test if it is
        assertNotNull("Article with title '" + title + "' not found in the database.", articleToUpdate);


        articleToUpdate.setTitle(updatedTitle);
        newsDao.updateArticle(articleToUpdate);
        Article updatedArticle = newsDao.getArticleByTitle(updatedTitle);
        assertNotNull("Article with title '" + updatedTitle + "' not found in the database.", updatedArticle);


        assertEquals(updatedArticle.getTitle(),updatedTitle);

    }

    @Test
    public void deleteTest(){

        Article article = articles.get(0);
        String title = article.getTitle();
        newsDao.insertArticle(article);

        Article articleToDelete = newsDao.getArticleByTitle(title);
        assertNotNull("Article with title '" + title + "' not found in the database.", articleToDelete);

        newsDao.deleteArticle(articleToDelete);

        Article deletedArticle = newsDao.getArticleByTitle(title);

        assertNull(deletedArticle);

    }
}


