package com.example.android.worldnews;

import android.os.Bundle;
import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<News>>{

    private static final String NEWS_URL = "https://content.guardianapis.com/search?api-key=8fc27acf-6d80-4f05-80cf-16a02c68a725";
    //ID of loader
    private static final int NEWS_LOADER_ID = 1;
    //create an instance of the NewsAdapter so we can access and modify it
    private NewsAdapter mNewsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get a reference to the LoaderManager, in order to interact with loaders
        LoaderManager loaderManager = getLoaderManager();
        //initialize the loader
        loaderManager.initLoader(NEWS_LOADER_ID, null, this);

        //find the reference of the ListView in the layout

        ListView newsListView = (ListView) findViewById(R.id.list);
        //create a new adapter that takes an empty list of books as input
        mNewsAdapter = new NewsAdapter(this, new ArrayList<News>());

        newsListView.setAdapter(mNewsAdapter);
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

        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        //Loader reset, so we can clear out the existing data
        mNewsAdapter.clear();
    }
}
