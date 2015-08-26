package com.digitalinfomesh.popularmovies;

import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private ArrayAdapter<String> mPlotAdapter;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View detailView = inflater.inflate(R.layout.fragment_detail, container, false);

        //get movie data from parcelable
        Intent detailIntent = getActivity().getIntent();
        MovieParcelable movie = detailIntent.getParcelableExtra("movieDetails");

        //Set title TextView
        TextView title = (TextView) detailView.findViewById(R.id.title);
        title.setText(movie.getTitle());

        //Set poster image using Picasso ImageView
        ImageView poster = (ImageView) detailView.findViewById(R.id.poster);
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w500/" + movie.getPosterPath()).into(poster);

        //Set Plot TextView
        TextView plot = (TextView) detailView.findViewById(R.id.plot);
        if (movie.getPlot().equals("null")){
            plot.setText("Plot unavailable");
        }else{
            plot.setText(movie.getPlot());
        }

        plot.setMovementMethod(new ScrollingMovementMethod());

        //Set Rating TextView
        TextView rating = (TextView) detailView.findViewById(R.id.rating);
        rating.setText("Rating: " + movie.getRating());

        //Set Release Date TextView
        TextView release = (TextView) detailView.findViewById(R.id.release);
        release.setText("Released: " + movie.getRelease());


        return detailView;
    }
}

