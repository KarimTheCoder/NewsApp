package com.example.myapplication.data.local.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapplication.data.local.dao.NewsDao;
import com.example.myapplication.data.local.entity.Article;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Article.class}, version = 1, exportSchema = false)
public abstract class NewsDatabase extends RoomDatabase {

    public abstract NewsDao newsDao();

    public static volatile NewsDatabase INSTANCE;
    public static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static NewsDatabase getDatabase(final Context context){

        if(INSTANCE == null){
            synchronized (NewsDatabase.class) {
                if(INSTANCE == null){

                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), NewsDatabase.class, "news_database").build();

                }
            }
        }
        return INSTANCE;
    }
}
