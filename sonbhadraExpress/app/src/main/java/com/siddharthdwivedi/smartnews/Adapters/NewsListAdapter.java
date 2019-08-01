package com.siddharthdwivedi.smartnews.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.siddharthdwivedi.smartnews.DetectConnection;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.siddharthdwivedi.smartnews.Fragments.NewsDetail;
import com.siddharthdwivedi.smartnews.MVP.NewsListResponse;
import com.siddharthdwivedi.smartnews.R;


/**
 * Created by Siddharth Dwivedi
 */
public class NewsListAdapter extends RecyclerView.Adapter<NewsListViewHolder> {
    Context context;
    List<NewsListResponse> newsListResponse;
    List<NewsListResponse> newsListResponse1;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public NewsListAdapter(Context context, List<NewsListResponse> newsListResponse) {
        this.context = context;
        this.newsListResponse = newsListResponse;
        this.newsListResponse1 = new ArrayList<NewsListResponse>();
        this.newsListResponse1.addAll(newsListResponse);
        sharedPreferences = context.getSharedPreferences("cacheExist", 0);
        editor = sharedPreferences.edit();
        if (DetectConnection.checkInternetConnection(context)) {
            editor.putBoolean("exist", true);
            editor.commit();
        }
    }

    @Override
    public NewsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_list_items, null);
        NewsListViewHolder NewsListViewHolder = new NewsListViewHolder(context, view, newsListResponse);
        return NewsListViewHolder;
    }

    @Override
    public void onBindViewHolder(NewsListViewHolder holder, final int position) {
        holder.catName.setText(newsListResponse.get(position).getCategory());
        holder.newsTitle.setText(newsListResponse.get(position).getTitle());
        holder.date.setText(newsListResponse.get(position).getDate());
        Picasso.with(context)
                .load(newsListResponse.get(position).getImage())
                .placeholder(R.drawable.defaultimage)
                .error(R.drawable.defaulterrorimage)
                .into(holder.image);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("position", position + "");
                NewsDetail.newsListResponsesData = newsListResponse;
                Intent intent = new Intent(context, NewsDetail.class);
                intent.putExtra("pos", position);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return newsListResponse.size();
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        newsListResponse.clear();
        if (charText.length() == 0) {
            newsListResponse.addAll(newsListResponse1);
        } else {
            for (NewsListResponse wp : newsListResponse1) {
                if (wp.getTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    newsListResponse.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
