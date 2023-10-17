package com.example.myapplication;


import static org.junit.Assert.assertEquals;

import android.app.Application;
import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.myapplication.data.local.database.NewsDatabase;
import com.example.myapplication.newsAPI.INewsDataSource;
import com.example.myapplication.ui.viewmodel.SharedViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

@RunWith(JUnit4.class)
public class ViewModelTest {


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private SharedViewModel viewModel;
    private MutableLiveData<String> testTextLiveData;

    @Before
    public void setUp() {
        // Mock your dependencies
        INewsDataSource newsDataSource = Mockito.mock(INewsDataSource.class);
        NewsDatabase database = Mockito.mock(NewsDatabase.class);

        // Initialize your ViewModel

        Application context = (Application) InstrumentationRegistry.getInstrumentation().getTargetContext();
        viewModel = new SharedViewModel(context, newsDataSource, database);
        testTextLiveData = new MutableLiveData<>();
    }

    @Test
    public void testViewModelFunctionality() {
        // Arrange
        String expectedText = "Hello, World!";

        // Create an observer for the LiveData
        Observer<String> dataObserver = newData -> {
            // Assert the data here
            assertEquals(expectedText, newData);
        };

        // Act
        //viewModel.setTestTextLiveData(testTextLiveData);

        // Assert
//        LiveData<String> data = viewModel.getTestTextLiveData();
//        data.observeForever(dataObserver);

        // Simulate data change
        testTextLiveData.setValue(expectedText);

        // Remove the observer to avoid memory leaks
        //data.removeObserver(dataObserver);
    }
}
