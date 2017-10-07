package com.example.android.worldnews;


import java.util.Date;

/**
 * Created by enach on 10/7/2017.
 */

public class News {
    //category of the article
    private String mCategory;
    //title of the article
    private String mTitle;
    //author of the article - if there is one
    private String mAuthor;
    //date the article was published
    private String mDate;
    //url to re-direct the user to the page of the news
    private String mWebUrl;

    public News(String category, String title, String author, String date, String webUrl){
        mCategory = category;
        mTitle = title;
        mAuthor = author;
        mDate = date;
        mWebUrl = webUrl;
    }

    public News(String category, String title, String date, String webUrl){
        mCategory = category;
        mTitle = title;
        mDate = date;
        mWebUrl = webUrl;
    }

    public String getCategory(){
        return mCategory;
    }

    public String getTitle(){
        return mTitle;
    }

    public String getAuthor(){
        return mAuthor;
    }

    public String getDate(){
        return mDate;
    }

    public String getWebUrl(){
        return mWebUrl;
    }
}
