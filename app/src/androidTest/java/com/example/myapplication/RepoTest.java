package com.example.myapplication;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import android.app.Application;
import androidx.paging.DataSource;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.myapplication.data.local.dao.NewsDao;
import com.example.myapplication.data.local.database.NewsDatabase;
import com.example.myapplication.data.local.entity.Article;
import com.example.myapplication.data.local.repository.NewsRepository;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

@RunWith(JUnit4.class)
public class RepoTest {

    private NewsRepository repo;

    private List<Article> articles;
    private NewsDao newsDao;
    private NewsDatabase db;
    @Before
    public void createDB(){
        FakeDataSource.NewsArticleSampleData data = new FakeDataSource.NewsArticleSampleData();
        articles = data.createSampleData();

        Application application = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(application, NewsDatabase.class).allowMainThreadQueries().build();

        repo = new NewsRepository(application,db);
        newsDao = repo.getNewsDao();
    }
    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertTest() throws InterruptedException {

        Article article = articles.get(0);
        String title = article.getTitle();
        repo.addNewsArticle(article);
        // Wait for the background operation to complete (you may need to adapt this to your specific situation).
        Thread.sleep(1000); // Sleep for 1 second (adjust as needed).
        Article gottenArticle = newsDao.getArticleByTitle(title);
        Assert.assertEquals(title,gottenArticle.getTitle());
    }

    @Test
    public void updateTest() throws InterruptedException {

        String newTitle = "new title";
        Article article = articles.get(0);
        String title = article.getTitle();
        repo.addNewsArticle(article);
        Thread.sleep(1000); // Sleep for 1 second (adjust as needed).
        Article gottenArticle = newsDao.getArticleByTitle(title);

        assertNotNull("Article with title '" + title + "' not found in the database.", gottenArticle);
        gottenArticle.setTitle(newTitle);

        repo.updateArticle(gottenArticle);
        Thread.sleep(1000); // Sleep for 1 second (adjust as needed).
        Article updatedArticle = repo.getArticleByTitle(newTitle);
        assertNotNull("Article with title '" + newTitle + "' not found in the database.", updatedArticle);
        assertEquals(newTitle,updatedArticle.getTitle());

    }


    @Test
    public void testInitializationDataSource() throws InterruptedException {

        Article article = articles.get(0);
        String title = article.getTitle();
        repo.addNewsArticle(article);
        Thread.sleep(1000); // Sleep for 1 second (adjust as needed).
        DataSource<Integer, Article> dataSource = repo.getAllNews().create();
        assertNotNull(dataSource);
    }



}
