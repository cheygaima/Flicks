package com.example.cgaima.flicks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.cgaima.flicks.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieListActivity extends AppCompatActivity {

    //constants
    //base URL for the API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";

    //the parameter name for the API key
    public final static String API_KEY_PARAM = "api_key";

    //tag for logging from this activity
    public final static String TAG = "MovieListActivity";

    // instance fields
    AsyncHttpClient client;
    //the base url for loading images
    String imageBaseUrl;
    //the poster size to use when fetching images, part of yrl
    String posterSize;

    //the list of currently playing movies
    ArrayList<Movie> movies;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        //initialize the client
        client = new AsyncHttpClient();
        //initialize the list of movies
        movies  = new ArrayList<>();
        //get configuration on app creation
        getConfig();

    }

    //get the list of currently playing movies from the API
    private void getNowPlaying(){
        //create the url
        String url = API_BASE_URL + "/movie/now_playing";
        //set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); //api key req
        //execute a get request expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //load the results into movie list
                try {
                    JSONArray results = response.getJSONArray("results");
                    //iterate through result set and create Movie objects
                    for (int i = 0; i < results.length(); i++)
                    {
                        Movie movie = new Movie(results.getJSONObject(i));
                        movies.add(movie);
                    }

                    Log.i(TAG, String.format("Loaded %s movies", results.length()));
                } catch (JSONException e) {
                    logError("Failed to get data from now_playing endpoint", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get dara from now_playing endpoint", throwable, true);
            }
        });

    }

    //get the config from the API
    private void getConfig() {
        //create the url
        String url = API_BASE_URL + "/configuration";
        //set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); //api key req

        //execute a get request expecting a json object response
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    JSONObject images = response.getJSONObject("images");
                    //get the image base url
                    imageBaseUrl = images.getString("secure_base_url");
                    //get poster size
                    JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
                    //use the option at index 3 or w342 as a fallback
                    posterSize = posterSizeOptions.optString(3, "w342");
                    Log.i(TAG, String.format("Loaded configuration with imageBaseUrl %s", imageBaseUrl, posterSize));
                    //get the now playing movies
                    getNowPlaying();

                } catch (JSONException e) {
                    logError("Failed parsing configuration", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed getting configuration", throwable, true);
            }
        });

    }

    //handle errors, log and alert user
    private void logError(String message, Throwable error, boolean alertUser) {
        //log the error
        Log.e(TAG, message, error);
        //alert the user to avoid silent errors
        if (alertUser){
            //show a long toast (shows the user that something went wrong)
            Toast. makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}
