package com.example.myapplication.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.SettingsActivity;
import com.example.myapplication.data.local.database.NewsDatabase;
import com.example.myapplication.data.local.entity.Article;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.newsAPI.INewsDataSource;
import com.example.myapplication.newsAPI.NewsDataSource;
import com.example.myapplication.ui.viewmodel.SharedViewModel;
import com.example.myapplication.ui.adapter.NewsAdapter;
import com.example.myapplication.ui.callbacks.RecyclerviewCallback;
import com.example.myapplication.ui.viewmodel.SharedViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements RecyclerviewCallback, View.OnClickListener {

    private FragmentHomeBinding binding;
    private ImageButton settingsButton;
    private List<Article> articleList;
    private SharedViewModel sharedViewModel;
    private RecyclerView recyclerView;
    public static final int HOME_VIEW_TYPE = 1;
    private ImageButton searchButton;
    private NewsAdapter adapter;
    private int lastRow = -1;

    private boolean isScrolling= false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // Inflate the fragment layout using data binding
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize UI elements
        searchButton = binding.searchHome;
        searchButton.setOnClickListener(this);
        settingsButton = binding.settingsButton;
        recyclerView = binding.recyclerViewHome;

        // Set up RecyclerView with LinearLayoutManager and an adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        adapter = new NewsAdapter(this, HOME_VIEW_TYPE);
        recyclerView.setAdapter(adapter);

        NewsDatabase database = NewsDatabase.getDatabase(requireActivity());
        INewsDataSource newsDataSource = new NewsDataSource(requireActivity().getApplication());
        ViewModelProvider.Factory factory = new SharedViewModelFactory(requireActivity().getApplication(),newsDataSource,database);


        // Set up ViewModel for shared data
        sharedViewModel = new ViewModelProvider(requireActivity(),factory).get(SharedViewModel.class);

        // Initialize a list to hold news articles
        articleList = new ArrayList<>();

        // Set an OnClickListener for the settingsButton
        settingsButton.setOnClickListener(this);



        sharedViewModel.getAllNews().observe(requireActivity(), list -> {
            Log.i("Article", "Loading articles to recyclerview");
            articleList.clear();
            articleList.addAll(list);
            adapter.submitList(list);


            
            if(!isScrolling){
                isScrolling = true;
                recyclerView.getLayoutManager().onRestoreInstanceState(sharedViewModel.getRecyclerviewState());
            }

        });

        recyclerView.addOnScrollListener(new MyScrollListener(lastRow,sharedViewModel));

        return root;
    }

    //Lifecycle methods

    @Override
    public void onResume() {
        super.onResume();

        recyclerView.getLayoutManager().onRestoreInstanceState(sharedViewModel.getRecyclerviewState());
    }

    @Override
    public void onPause() {
        super.onPause();
        sharedViewModel.setRecyclerviewState(recyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



    // Click and Scrolling
    @Override
    public void onRecyclerviewClick(int adapterPosition) {

        if (adapterPosition >= 0 && adapterPosition < articleList.size()) {
            Article article = articleList.get(adapterPosition);
            if (article != null) {
                sharedViewModel.setNewsArticle(article);

                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_navigation_home_to_navigation_notifications);

            } else {
                // Log an error message indicating that the article at the specified position is null.
                Log.e("ArticleError", "News article at position " + adapterPosition + " is null.");
            }
        } else {
            // Log an error message indicating that the adapter position is out of bounds.
            Log.e("PositionError", "Adapter position " + adapterPosition + " is out of bounds.");
        }
    }
    @Override
    public void onRecyclerviewSaveClick(int adapterPosition) {

        if (adapterPosition >= 0 && adapterPosition < articleList.size()) {
            Article article = articleList.get(adapterPosition);
            if (article != null) {

                boolean isSaved = article.isSaved();
                if(isSaved){
                    sharedViewModel.unsaveArticle(article);
                    Toast.makeText(requireActivity(),"Article unsaved",Toast.LENGTH_SHORT).show();
                }else {
                    sharedViewModel.saveArticle(article);
                    Toast.makeText(requireActivity(),"Article saved",Toast.LENGTH_SHORT).show();
                }
                adapter.notifyItemChanged(adapterPosition);


            } else {
                // Log an error message indicating that the article at the specified position is null.
                Log.e("ArticleError", "News article at position " + adapterPosition + " is null.");
            }
        } else {
            // Log an error message indicating that the adapter position is out of bounds.
            Log.e("PositionError", "Adapter position " + adapterPosition + " is out of bounds.");
        }

    }
    @Override
    public void onClick(View view) {
        if(view == settingsButton){
            startActivity(new Intent(requireActivity(), SettingsActivity.class));
        }
        if( view == searchButton){
            NavHostFragment.findNavController(getParentFragment())
                    .navigate(R.id.action_navigation_home_to_searchFragment);
        }
    }
}

// Scroll listener class
class MyScrollListener extends RecyclerView.OnScrollListener {

    private int lastRow;
    private final SharedViewModel sharedViewModel;

    MyScrollListener(int lastRow, SharedViewModel sharedViewModel){

        this.lastRow = lastRow;
        this.sharedViewModel = sharedViewModel;

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        // This method is called when the RecyclerView is scrolled
        // You can get the current position here and show it in a Toast

        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        assert layoutManager != null;
        int currentPosition = layoutManager.findLastVisibleItemPosition();
        int totalItems = layoutManager.getItemCount();

        if (lastRow != currentPosition) {
            lastRow = currentPosition;

            // Show a Toast with the current position
            if (currentPosition != RecyclerView.NO_POSITION) {
                String toastMessage = "Current Position: " + currentPosition + " total items " + totalItems;
                Log.i("Recyclerview", toastMessage);
            }

            if (currentPosition == totalItems - 1) {
                String toastMessage = "Bottom of the Recyclerview";
                Log.i("Recyclerview", toastMessage);

                // Call a method in sharedViewModel to save more articles to Room
                sharedViewModel.saveArticlesToRoom(2);
            }
        }
    }
}