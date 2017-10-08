package com.example.android.worldnews;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<News>>{

    private static final String NEWS_URL = "https://content.guardianapis.com/search?api-key=8fc27acf-6d80-4f05-80cf-16a02c68a725";
    //ID of loader
    private static final int NEWS_LOADER_ID = 1;
    //create an instance of the NewsAdapter so we can access and modify it
    private NewsAdapter mNewsAdapter;
    private ProgressBar spinningWheel;
    private TextView mEmptyStateView;

    //create a method to check if there is internet connection
    private boolean isNewtworkAvailable(){
        ConnectivityManager connectivityManager
                =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create an object for the ProgressBar
        spinningWheel = (ProgressBar) findViewById(R.id.spinning_wheel);
        //create an object for the empty TextView
        mEmptyStateView = (TextView) findViewById(R.id.empty_text_view);

        if(isNewtworkAvailable() == true){
            //get a reference to the LoaderManager, in order to interact with loaders
            LoaderManager loaderManager = getLoaderManager();
            //initialize the loader
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        }else{
            spinningWheel.setVisibility(View.GONE);
            mEmptyStateView.setText("No internet connection");
        }

        //find the reference of the ListView in the layout

        ListView newsListView = (ListView) findViewById(R.id.list);
        //create a new adapter that takes an empty list of books as input
        mNewsAdapter = new NewsAdapter(this, new ArrayList<News>());

        newsListView.setAdapter(mNewsAdapter);

        /**
         * Create a Intent so that when the user click on any of the displayed news
         * it will redirect him to the page where he can read more about the news
         */

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //find the current items the user click on
                News currentNews = mNewsAdapter.getItem(position);
                //convert the String url from the News class into a URI object
                Uri newsUri = Uri.parse(currentNews.getWebUrl());
                //create a new intent for the news Uri
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                //send the intent to launch the new activity
                startActivity(websiteIntent);
            }
        });
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        //create a new loader for the given URL
        return new NewsLoader(this, NEWS_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        //clear the adapter from previous news data
        mNewsAdapter.clear();

        if(data != null && !data.isEmpty()){
            mNewsAdapter.addAll(data);
        }else {
            mEmptyStateView.setText("No news for now");
        }
        spinningWheel.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        //Loader reset, so we can clear out the existing data
        mNewsAdapter.clear();
    }
}
