package com.example.myapplication.ui.save;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.local.database.NewsDatabase;
import com.example.myapplication.data.local.entity.Article;
import com.example.myapplication.databinding.FragmentDashboardBinding;
import com.example.myapplication.newsAPI.INewsDataSource;
import com.example.myapplication.newsAPI.NewsDataSource;
import com.example.myapplication.ui.viewmodel.SharedViewModel;
import com.example.myapplication.ui.adapter.NewsAdapter;
import com.example.myapplication.ui.callbacks.RecyclerviewCallback;
import com.example.myapplication.ui.viewmodel.SharedViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class SavedFragment extends Fragment implements RecyclerviewCallback {

    private FragmentDashboardBinding binding;
    private List<Article> articleList;
    private SharedViewModel sharedViewModel;
    private NewsAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        NewsDatabase database = NewsDatabase.getDatabase(requireActivity());
        INewsDataSource newsDataSource = new NewsDataSource(requireActivity().getApplication());
        ViewModelProvider.Factory factory = new SharedViewModelFactory(requireActivity().getApplication(),newsDataSource,database);
        sharedViewModel = new ViewModelProvider(requireActivity(),factory).get(SharedViewModel.class);
        articleList = new ArrayList<>();

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerViewSave;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        adapter = new NewsAdapter(this,2);
        recyclerView.setAdapter(adapter);


        sharedViewModel.getSavedArticles().observe(getViewLifecycleOwner(), lists->{

            articleList.clear();
            articleList.addAll(lists);

            adapter.submitList(lists);

        });


        Log.i("Test saved",sharedViewModel.getTestText());



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onRecyclerviewClick(int adapterPosition) {
        if (adapterPosition >= 0 && adapterPosition < articleList.size()) {
            Article article = articleList.get(adapterPosition);
            if (article != null) {
                sharedViewModel.setNewsArticle(article);

                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_navigation_dashboard_to_navigation_notifications);

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