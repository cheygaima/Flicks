package com.example.cgaima.flicks;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cgaima.flicks.models.Config;
import com.example.cgaima.flicks.models.Movie;

import org.parceler.Parcels;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    //list of movies
    ArrayList<Movie> movies;

    //config needed for image urls
    Config config;

    //context for rendering
    Context context;

    //initialize with list


    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    //creates and inflate a new view
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //get context from the parent
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //create the view using the item_movie layout
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);
        //return a new ViewHolder
        return new ViewHolder(movieView);
    }

    //binds an inflated view to a new item
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //get the movie data at the specified position
        Movie movie = movies.get(position);
        //populate the view with the movie data
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());

        //determine the current orientation
        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        // build url for poster image
        String ImageUrl = null;

        //portrait mode, load the poster image
        if (isPortrait)
        {
            ImageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());
        }
        else
        {
            //load the backdrop img
            ImageUrl = config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath());
        }

        //get correct placeholder and imageview
        int placeHolder = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
        ImageView imageView = isPortrait ? holder.ivPosterImage : holder.ivBackImg;

        //load image using glide
        GlideApp.with(context)
                .load(ImageUrl)
                .transform(new RoundedCornersTransformation(15, 0))
                .placeholder(R.drawable.flicks_movie_placeholder)
                .error(placeHolder)
                .into(imageView);


    }


    //returns the total number of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    //create the viewholder as a static inner class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        //track view objects
        ImageView ivPosterImage;
        ImageView ivBackImg;
        TextView tvTitle;
        TextView tvOverview;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //lookup view objects by id
            ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            ivBackImg = (ImageView) itemView.findViewById(R.id.ivBackImg);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            //gets item position
            int position = getAdapterPosition();
            //make sure the position is valid/exists
            if (position != RecyclerView.NO_POSITION)
            {
                Movie movie = movies.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // show the activity
                context.startActivity(intent);
            }
        }
    }
}
