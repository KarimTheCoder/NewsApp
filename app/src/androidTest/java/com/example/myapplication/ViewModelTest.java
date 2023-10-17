package com.example.myapplication;

import static org.junit.Assert.assertEquals;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.myapplication.data.local.database.NewsDatabase;
import com.example.myapplication.newsAPI.INewsDataSource;
import com.example.myapplication.newsAPI.NewsDataSource;
import com.example.myapplication.ui.viewmodel.SharedViewModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ViewModelTest {

    Application application;

    NewsDatabase database;

    @Before
    public void createDb() {

        FakeDataSource.NewsArticleSampleData data = new FakeDataSource.NewsArticleSampleData();
        application = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(application, NewsDatabase.class).build();

    }

    @After
    public void closeDb() {
        database.close();
    }

    @Test
    public void testViewModelFunctionality() {
        // Arrange
        INewsDataSource newsDataSource = new NewsDataSource(application);
        SharedViewModel viewModel = new SharedViewModel(application,newsDataSource,database);

        // Create an observer for the LiveData
        Observer<String> dataObserver = new Observer<String>() {
            @Override
            public void onChanged(String newData) {
                // Assert the data here
                assertEquals("Hello, World!", newData);
            }
        };


        // Act
        viewModel.setTestTextLiveData(new MutableLiveData<String>("Hello, World!"));

        // Assert
        MutableLiveData<String> data = viewModel.getTestTextLiveData();

        data.observeForever(dataObserver);
       // assertEquals("Hello, World!", data.getValue());
    }

}
