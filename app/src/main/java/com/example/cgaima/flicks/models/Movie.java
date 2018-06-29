package com.example.cgaima.flicks.models;



import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel // class is Parcelable!!!

public class Movie {

    // instance fields

    //values from API
    String title;
    String overview;
    String posterPath; //only the path
    String backdropPath;
    Double voteAverage; //track the votes from the API




    //empty constructor needed for @parcel
    public Movie(){}

    //initialize from JSON data
    public Movie(JSONObject object) throws JSONException { //constructor
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        backdropPath = object.getString("backdrop_path");
        voteAverage = object.getDouble("vote_average");

    }

    public Double getVoteAverage()
    {
        return voteAverage;
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

    public String getBackdropPath() {
        return backdropPath;
    }
}
