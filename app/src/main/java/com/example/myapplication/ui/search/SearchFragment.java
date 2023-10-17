package com.example.myapplication.ui.search;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.data.local.database.NewsDatabase;
import com.example.myapplication.data.local.entity.Article;
import com.example.myapplication.databinding.FragmentSearchBinding;
import com.example.myapplication.newsAPI.INewsDataSource;
import com.example.myapplication.newsAPI.NewsDataSource;
import com.example.myapplication.ui.viewmodel.SharedViewModel;
import com.example.myapplication.ui.adapter.NewsAdapter;
import com.example.myapplication.ui.callbacks.RecyclerviewCallback;
import com.example.myapplication.ui.viewmodel.SharedViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public class SearchFragment extends Fragment implements RecyclerviewCallback {
    private List<Article> articleList;
    private int RECYCLER_VIEW_TYPE = 1;
    private SearchView searchView;
    private FragmentSearchBinding binding;
    private Toolbar toolbar;
    private SharedViewModel sharedViewModel;
    private RecyclerView recyclerView;
    NewsAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        searchView = binding.searchView;

        NewsDatabase database = NewsDatabase.getDatabase(requireActivity());
        INewsDataSource newsDataSource = new NewsDataSource(requireActivity().getApplication());
        ViewModelProvider.Factory factory = new SharedViewModelFactory(requireActivity().getApplication(),newsDataSource,database);

        articleList = new ArrayList<>();
        sharedViewModel = new ViewModelProvider(requireActivity(),factory).get(SharedViewModel.class);
        sharedViewModel.getBottomBarCallbacks().hideBottomBar();

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        adapter = new NewsAdapter(this, RECYCLER_VIEW_TYPE);
        recyclerView.setAdapter(adapter);

        sharedViewModel.getPagedSearchResult().observe(requireActivity(),list->{

            articleList.clear();
            adapter.submitList(list);

            articleList.addAll(list);
        });

        activateSearchView();

        toolbar = binding.toolbar;

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // This method is called when the RecyclerView is scrolled
                // You can get the current position here and show it in a Toast

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int currentPosition = layoutManager.findLastVisibleItemPosition();

                // Show a Toast with the current position
                if (currentPosition != RecyclerView.NO_POSITION) {
                    String toastMessage = "Current Position: " + currentPosition;
                    Log.i("Recyclerview",toastMessage);
                }
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle the search query here
                sharedViewModel.queryArticles(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle changes in the search query here
                return false;
            }
        });

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (activity != null) {
            activity.setSupportActionBar(toolbar);
            Objects.requireNonNull(activity.getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // Show back button
        }

        return root;
    }

    private void activateSearchView() {
        // Set focus to the SearchView
        searchView.setFocusable(true);
        searchView.setIconified(false); // Expand the SearchView
        searchView.requestFocus();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
        sharedViewModel.getBottomBarCallbacks().showBottomBar();
    }

    @Override
    public void onRecyclerviewClick(int adapterPosition) {

        if (adapterPosition >= 0 && adapterPosition < articleList.size()) {
            Article article = articleList.get(adapterPosition);
            if (article != null) {
                sharedViewModel.setNewsArticle(article);

                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_searchFragment_to_navigation_read);

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
}