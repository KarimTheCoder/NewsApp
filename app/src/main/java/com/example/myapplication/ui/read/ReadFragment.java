package com.example.myapplication.ui.read;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.data.local.database.NewsDatabase;
import com.example.myapplication.data.local.entity.Article;
import com.example.myapplication.databinding.FragmentReadBinding;
import com.example.myapplication.newsAPI.INewsDataSource;
import com.example.myapplication.newsAPI.NewsDataSource;
import com.example.myapplication.ui.viewmodel.SharedViewModel;
import com.example.myapplication.ui.callbacks.BottomBarCallbacks;
import com.example.myapplication.ui.viewmodel.SharedViewModelFactory;

import java.util.Objects;

public class ReadFragment extends Fragment {
    private static final int MENU_ITEM_1_ID = R.id.save_menu;
    private com.example.myapplication.databinding.FragmentReadBinding binding;
    private SharedViewModel sharedViewModel;
    private Article article;
    private TextView author,body,headline;
    private BottomBarCallbacks bottomBarCallbacks;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // Enable options menu

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        binding = FragmentReadBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        NewsDatabase database = NewsDatabase.getDatabase(requireActivity());
        INewsDataSource newsDataSource = new NewsDataSource(requireActivity().getApplication());
        ViewModelProvider.Factory factory = new SharedViewModelFactory(requireActivity().getApplication(),newsDataSource,database);



        sharedViewModel = new ViewModelProvider(requireActivity(),factory).get(SharedViewModel.class);
        article = sharedViewModel.getNewsArticle();
        bottomBarCallbacks = sharedViewModel.getBottomBarCallbacks();
        bottomBarCallbacks.hideBottomBar();

        String url = article.getUrl();


        WebView webView = binding.webView;

        // Enable JavaScript (if needed)
        webView.getSettings().setJavaScriptEnabled(true);

        // Load the URL from the API
       // String articleUrl = getIntent().getStringExtra("articleUrl");
        webView.loadUrl(url);

        // Set WebViewClient to handle page loading and navigation
        webView.setWebViewClient(new WebViewClient());

        // Set WebChromeClient for handling UI events like progress bar, title, etc.
        webView.setWebChromeClient(new WebChromeClient());



        // Initialize the Toolbar
        Toolbar toolbar = binding.toolbar;


        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (activity != null) {
            activity.setSupportActionBar(toolbar);
            Objects.requireNonNull(activity.getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // Show back button
        }



        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.read_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == MENU_ITEM_1_ID) {// Handle menu item 1 click

            boolean isSaved = article.isSaved();

            Log.i("save", "save clicked. is saved: "+isSaved);



            if(!isSaved){
                Log.i("save", "saving article");
                sharedViewModel.saveArticle(article);
                item.setIcon(R.drawable.baseline_saved);



            }else {
                Log.i("save", "unsaving article");

                sharedViewModel.unsaveArticle(article);
                item.setIcon(R.drawable.baseline_unsaved);

            }



            return true;


            // Add more cases for other menu items if needed
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        bottomBarCallbacks.showBottomBar();
    }


}