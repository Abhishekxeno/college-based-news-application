package com.siddharthdwivedi.smartnews.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.siddharthdwivedi.smartnews.Adapters.NewsListAdapter;
import com.siddharthdwivedi.smartnews.DetectConnection;
import com.siddharthdwivedi.smartnews.MVP.NewsListResponse;
import com.siddharthdwivedi.smartnews.R;
import com.siddharthdwivedi.smartnews.Retrofit.Api;
import com.siddharthdwivedi.smartnews.SplashScreen;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LatestNews extends Fragment {

    View view;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    public static NewsListAdapter newsListAdapter;
    public static SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_latest_news, container, false);
        ButterKnife.bind(this, view);
        setData();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.simpleSwipeRefreshLayout);
        // implement setOnRefreshListener event on SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (DetectConnection.checkInternetConnection(getActivity())) {
                    getNewsList();
                } else {
                    Toast.makeText(getActivity(), "Internet Not Available", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        return view;
    }

    public void getNewsList() {
        Api.getClient().getNewsList(new Callback<List<NewsListResponse>>() {
            @Override
            public void success(List<NewsListResponse> newsListResponses, Response response) {
                SplashScreen.newsListResponsesData.clear();
                SplashScreen.newsListResponsesData.addAll(newsListResponses);
                newsListAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.toString());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(linearLayoutManager);
        newsListAdapter = new NewsListAdapter(getActivity(), SplashScreen.newsListResponsesData);
        recyclerview.setAdapter(newsListAdapter);
    }
}
