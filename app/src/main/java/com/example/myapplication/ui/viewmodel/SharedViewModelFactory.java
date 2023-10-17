package com.example.myapplication.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.data.local.database.NewsDatabase;
import com.example.myapplication.newsAPI.INewsDataSource;

public class SharedViewModelFactory implements ViewModelProvider.Factory {

    private Application application;
    private INewsDataSource newsDataSource;
    private NewsDatabase database;

    public SharedViewModelFactory(Application application, INewsDataSource newsDataSource, NewsDatabase database) {
        this.application = application;
        this.newsDataSource = newsDataSource;
        this.database = database;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SharedViewModel.class)) {
            return (T) new SharedViewModel(application, newsDataSource,database);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
