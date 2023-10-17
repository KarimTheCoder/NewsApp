package com.example.myapplication.ui.viewmodel;


import android.app.Application;
import android.os.Parcelable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import com.example.myapplication.data.local.database.NewsDatabase;
import com.example.myapplication.data.local.entity.Article;
import com.example.myapplication.data.local.repository.NewsRepository;
import com.example.myapplication.newsAPI.INewsDataSource;
import com.example.myapplication.ui.callbacks.BottomBarCallbacks;

public class SharedViewModel extends AndroidViewModel {


    private DataSource.Factory<Integer, Article> articlesForPaging;
    private Article article;
    private NewsRepository newsRepo;
    private String testText = "default";
    private MutableLiveData<String> testTextLiveData = new MutableLiveData<>();

    private BottomBarCallbacks bottomBarCallbacks;
    Parcelable recyclerviewState;
    private INewsDataSource newsDataSource;

    public SharedViewModel(Application application, INewsDataSource newsDataSource, NewsDatabase database){
        super(application);

        newsRepo = new NewsRepository(application,database);
        article = new Article();
        articlesForPaging = newsRepo.getAllNews();

        //Fetching articles from news API
        this.newsDataSource = newsDataSource;
        newsDataSource.fetchArticles();



    }



    // Search
    public void queryArticles(String query){

        newsDataSource.queryArticles(query);
    }
    public void saveArticlesToRoom(int size){

        newsDataSource.saveArticlesToRoom(size);
    }
    public LiveData<PagedList<Article>> getPagedSearchResult(){
        return new LivePagedListBuilder<>(
                newsRepo.getAllSearchResult(), /* page size */ 10).build();
    }



    // Callbacks and test text
    public String getTestText() {
        return testText;
    }
    public void setTestText(String testText) {
        this.testText = testText;
    }
    public BottomBarCallbacks getBottomBarCallbacks() {
        return bottomBarCallbacks;
    }
    public void setBottomBarCallbacks(BottomBarCallbacks bottomBarCallbacks) {
        this.bottomBarCallbacks = bottomBarCallbacks;
    }



    public LiveData<PagedList<Article>> getAllNews(){
        return new LivePagedListBuilder<>(
                articlesForPaging, /* page size */ 10).build();
    }
    public LiveData<PagedList<Article>> getSavedArticles(){
        return new LivePagedListBuilder<>(
                newsRepo.getSavedArticles(), /* page size */ 10).build();
    }
    public void saveArticle(Article article){
        newsRepo.saveArticle(article);
    }
    public void unsaveArticle(Article article){
        newsRepo.unsaveArticle(article);
    }

    // Setting and getting news articles to be read
    public Article getNewsArticle() {
        return article;
    }
    public void setNewsArticle(Article article) {
        this.article = article;
    }




    // Recyclerview state
    public Parcelable getRecyclerviewState() {
        return recyclerviewState;
    }
    public void setRecyclerviewState(Parcelable recyclerviewState) {
        this.recyclerviewState = recyclerviewState;
    }

    public MutableLiveData<String> getTestTextLiveData() {
        return testTextLiveData;
    }

    public void setTestTextLiveData(MutableLiveData testTextLiveData) {
        this.testTextLiveData = testTextLiveData;
    }
}
