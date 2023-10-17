package com.example.myapplication.ui.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.data.local.entity.Article;
import com.example.myapplication.ui.callbacks.RecyclerviewCallback;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class NewsAdapter extends PagedListAdapter<Article, RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_NEWS_ARTICLE_HOME = 1;
    private static final int VIEW_TYPE_NEWS_ARTICLE_SAVED= 2;

    private int viewTYpe;

    RecyclerviewCallback recyclerviewCallback;
    public NewsAdapter(RecyclerviewCallback recyclerviewCallback,int viewTYpe) {
        super(DIFF_CALLBACK);

        this.viewTYpe = viewTYpe;

        this.recyclerviewCallback = recyclerviewCallback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if(viewType== VIEW_TYPE_NEWS_ARTICLE_HOME){

            View itemView = inflater.inflate(R.layout.item_layout_news_article_home,parent,false);
            return new NewsArticleHolderHome(itemView,recyclerviewCallback);

        }else {
            View itemView = inflater.inflate(R.layout.item_layout_news_article_saved,parent,false);
                    return new NewsArticleHolderSaved(itemView,recyclerviewCallback);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);

        if (viewType == VIEW_TYPE_NEWS_ARTICLE_HOME) {
            if (holder instanceof NewsArticleHolderHome) {
                NewsArticleHolderHome newsArticleHolderHome = (NewsArticleHolderHome) holder;
                Article article = getItem(position);
                if (article != null) {
                    newsArticleHolderHome.bindTo(article);
                } else {
                    newsArticleHolderHome.clear();
                }
            }
        } else if (viewType == VIEW_TYPE_NEWS_ARTICLE_SAVED) {
            if (holder instanceof NewsArticleHolderSaved) {
                NewsArticleHolderSaved newsArticleHolderSaved = (NewsArticleHolderSaved) holder;
                Article article = getItem(position);
                if (article != null) {
                    newsArticleHolderSaved.bindTo(article);
                } else {
                    newsArticleHolderSaved.clear();
                }
            }
        }


    }

    @Override
    public int getItemViewType(int position) {
        // Determine the view type for the item at the given position
        // You can use your own logic here to decide the view type based on the data or position.
        // For example, if you have two types of items, you can return VIEW_TYPE_NEWS_ARTICLE_HOME
        // for some positions and VIEW_TYPE_NEWS_ARTICLE_SAVED for others.

        // Example:
        Article article = getItem(position);
        if (article != null) {
            if (article.isSaved()) {
                return VIEW_TYPE_NEWS_ARTICLE_SAVED;
            } else {
                return VIEW_TYPE_NEWS_ARTICLE_HOME;
            }
        }

        // Return a default view type if needed
        return super.getItemViewType(position);
    }


    private static final DiffUtil.ItemCallback<Article> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Article>() {
                @Override
                public boolean areItemsTheSame(Article oldArticle, Article newArticle) {
                    return oldArticle.getId() == newArticle.getId();
                }

                @Override
                public boolean areContentsTheSame(Article oldArticle, Article newArticle) {
                    return oldArticle.isSaved() == newArticle.isSaved();
                }
            };
}


class NewsArticleHolderHome extends RecyclerView.ViewHolder {

    final ImageView imageNews;
    ImageButton saveButton;
    TextView textViewHeadline;
    TextView textViewAuthor;
    View view;

    public NewsArticleHolderHome(@NonNull View itemView, RecyclerviewCallback recyclerviewCallback) {
        super(itemView);


        view = itemView.findViewById(R.id.recycler_view_click);
        imageNews = itemView.findViewById(R.id.imageview);
        textViewHeadline = itemView.findViewById(R.id.textview_news_title_home);
        textViewAuthor = itemView.findViewById(R.id.textview_author);
        saveButton = itemView.findViewById(R.id.imageButton_save);



        saveButton.setOnClickListener(view -> recyclerviewCallback.onRecyclerviewSaveClick(getAdapterPosition()));
        view.setOnClickListener(view -> recyclerviewCallback.onRecyclerviewClick(getAdapterPosition()));


    }

    public void bindTo(Article article) {

        String imageUrl = article.getUrlToImage();

        // Load image with Picasso
        Picasso.get()
                .load(imageUrl)
                .resize(300, 200)
                .centerInside()
                .into(imageNews, new Callback() {
                    @Override
                    public void onSuccess() {
                        // Image loaded successfully, no action needed
                    }

                    @Override
                    public void onError(Exception e) {
                        // Handle error, e.g., set a placeholder image or show an error message
                        imageNews.setImageResource(R.drawable.ic_launcher_background); // Replace with your placeholder image resource
                    }
                });

        textViewAuthor.setText(article.getAuthor());
        textViewHeadline.setText(article.getTitle());

        boolean isSaved = article.isSaved();

        if(isSaved){
            saveButton.setImageResource(R.drawable.baseline_saved);
        }else {
            saveButton.setImageResource(R.drawable.baseline_unsaved);
        }


    }

    public void clear() {
        textViewAuthor.setText("");
        textViewHeadline.setText("");
    }
}

class NewsArticleHolderSaved extends RecyclerView.ViewHolder {

    final ImageView imageNews;
    ImageButton saveButton;
    TextView textViewHeadline;
    TextView textViewAuthor;
    View view;

    public NewsArticleHolderSaved(@NonNull View itemView, RecyclerviewCallback recyclerviewCallback) {
        super(itemView);


        view = itemView.findViewById(R.id.recycler_view_click);
        imageNews = itemView.findViewById(R.id.imageview);
        textViewHeadline = itemView.findViewById(R.id.textview_news_title_save);
        textViewAuthor = itemView.findViewById(R.id.textview_author);
        saveButton = itemView.findViewById(R.id.imageButton_save);



        saveButton.setOnClickListener(view -> recyclerviewCallback.onRecyclerviewSaveClick(getAdapterPosition()));
        view.setOnClickListener(view -> recyclerviewCallback.onRecyclerviewClick(getAdapterPosition()));


    }

    public void bindTo(Article article) {

        String imageUrl = article.getUrlToImage();

        // Load image with Picasso
        Picasso.get()
                .load(imageUrl)
                .into(imageNews, new Callback() {
                    @Override
                    public void onSuccess() {
                        // Image loaded successfully, no action needed
                    }

                    @Override
                    public void onError(Exception e) {
                        // Handle error, e.g., set a placeholder image or show an error message
                        imageNews.setImageResource(R.drawable.ic_launcher_background); // Replace with your placeholder image resource
                    }
                });

        textViewAuthor.setText(article.getAuthor());
        textViewHeadline.setText(article.getTitle());

        boolean isSaved = article.isSaved();

        if(isSaved){
            saveButton.setImageResource(R.drawable.baseline_saved);
        }else {
            saveButton.setImageResource(R.drawable.baseline_unsaved);
        }


    }

    public void clear() {
        textViewAuthor.setText("");
        textViewHeadline.setText("");
    }
}
