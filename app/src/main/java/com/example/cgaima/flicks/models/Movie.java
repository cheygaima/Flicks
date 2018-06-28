package com.example.cgaima.flicks.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Movie {

    // instance fields

    //values from API
    private String title;
    private String overview;
    private String posterPath; //only the path

    //initialize from JSON data
    public Movie(JSONObject object) throws JSONException { //constructor
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");

    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }
}
