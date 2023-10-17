package com.example.myapplication;

import android.app.Application;

import com.example.myapplication.data.local.entity.Article;
import com.example.myapplication.data.local.repository.NewsRepository;
import com.example.myapplication.newsAPI.NewsDataSource;

import java.util.ArrayList;
import java.util.List;

public class FakeDataSource extends NewsDataSource {
    private List<Article> fakeArticles;
    private NewsRepository repository;
    public FakeDataSource(Application application) {
        super(application);
        fakeArticles = new NewsArticleSampleData().createSampleData();


       // repository = new NewsRepository(application);


    }


    @Override
    public void fetchArticles() {

        for (int i = 0; i < fakeArticles.size(); i++) {
           // repository.addNewsArticle(fakeArticles.get(i));
        }
    }


    @Override
    public void queryArticles(String query) {


    }



    public static class NewsArticleSampleData {
        public List<Article> createSampleData() {
            List<Article> sampleData = new ArrayList<>();

            for (int i = 1; i <= 50; i++) {
                Article article = new Article();
                article.setName("Sample News " + i);
                article.setAuthor("Author " + i);
                article.setTitle("Sample Title " + i);
                article.setDescription("Description for Sample News " + i);
                article.setUrl("https://example.com/news" + i);
                article.setUrlToImage("https://example.com/news" + i + "-image.jpg");
                article.setPublishedAt("2023-10-03T10:00:00Z");
                article.setContent("Content for Sample News " + i);
                article.setSaved(i % 2 == 0); // Alternate articles are saved
                article.setSearchResult(i % 3 == 0); // Every third article is a search result

                sampleData.add(article);
            }

            return sampleData;
        }

    }
}
