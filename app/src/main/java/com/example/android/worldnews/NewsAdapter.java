package com.example.android.worldnews;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by enach on 10/7/2017.
 */

public class NewsAdapter extends ArrayAdapter<News> {
    //public constructor
    public NewsAdapter(Activity context, ArrayList<News> news){
        super(context, 0 , news);
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        return dateFormat.format(dateObject);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View newsItemView = convertView;
        //Check to see if there is an existing listItem view that we can re-use
        //otherwise inflate a new one
        if (newsItemView == null) {
            newsItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_items, parent, false);
        }

        //get the position of each item in the ListView
        News newsPosition = getItem(position);

        //find the TextView with the ID @category
        TextView categoryTextView = (TextView) newsItemView.findViewById(R.id.category);
        //set the text for the string
        categoryTextView.setText(newsPosition.getCategory());

        //find the TextView with the ID @title
        TextView titleTextView = (TextView) newsItemView.findViewById(R.id.title);
        //set the text for the string
        titleTextView.setText(newsPosition.getTitle());

        Date dateObject = new Date(newsPosition.getDate());

        //find the TextView with the ID @author
        TextView authorTextView = (TextView) newsItemView.findViewById(R.id.author);
        //set the text for the string
        authorTextView.setText(newsPosition.getAuthor());


        //find the TextView with the ID @time
        TextView dateTextView = (TextView) newsItemView.findViewById(R.id.date_view);

        String formattedDate = formatDate(dateObject);
        dateTextView.setText(formattedDate);
//        //set the text for the string
//        dateTextView.setText(newsPosition.getDate());



        return newsItemView;
    }
}
