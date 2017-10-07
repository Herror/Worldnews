package com.example.android.worldnews;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by enach on 10/7/2017.
 */

public class QueryUtils {

    private QueryUtils() {

    }

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Get all the necessary information after parsing the JSON
     * This ties together the createURL, makeHttpRequest and extractNews methods
     */

    public static List<News> fetchNewsData(String requestUrl) {
        //create the URL object
        URL url = createUrl(requestUrl);
        //Perform HTTP request tp the URL and receive the JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        //Extract relevant fields from the JSON response and create an {@link Event} object
        List<News> news = extractNews(jsonResponse);
        return news;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }


    //Make an HTTP request to the given URL and return a String as the response.

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<News> extractNews(String newsJSON) {
        //If the JSon is empty - return early
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        //create an empty ArrayList on which we will start adding the news
        List<News> newsArray = new ArrayList<>();

        try {
            //extract the news root object
            JSONObject root = new JSONObject(newsJSON);

            //get the response object
            JSONObject response = root.getJSONObject("response");
            //get the result Array
            JSONArray resultArray = response.getJSONArray("results");
            //extract the results array
            for(int i = 0; i < resultArray.length(); i++){
                JSONObject firstNews = resultArray.getJSONObject(i);
                //get the category
                String category = firstNews.getString("sectionName");
                //get the title
                String title = firstNews.getString("webTitle");
                //get the date the article was published
                String date = firstNews.getString("webPublicationDate");
                //get the Web URL
                String webUrl = firstNews.getString("webUrl");

                //Create a new object with the category, title date and webUrl from the JSON response
                News newsObject = new News(category, title, date, webUrl);

                //add it to the list of news
                newsArray.add(newsObject);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the news JSON results", e);
        }
        return newsArray;
    }
}
