package com.digitalinfomesh.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        final MovieParcelable movie = detailIntent.getParcelableExtra("movieDetails");

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


        //Set Rating TextView
        TextView rating = (TextView) detailView.findViewById(R.id.rating);
        rating.setText("Rating: " + movie.getRating() + "/10");

        //Set Release Date TextView
        TextView release = (TextView) detailView.findViewById(R.id.release);
        release.setText("Released: " + movie.getRelease());

        //Set Favorites button OnClickListener
        Button favButton = (Button) detailView.findViewById(R.id.favoritesBtn);
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = getActivity();
                CharSequence text = "Added to Favorites";
                int duration = Toast.LENGTH_SHORT;

                MovieRepo MovieRepo = new MovieRepo(context);
                MovieRepo.addFavorite(movie.getID(), movie.getPosterPath(), movie.getTitle(), movie.getPlot(), movie.getRating(), movie.getRelease());

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                MovieRepo.getFavorites();


            }

        });



        return detailView;
    }
}


