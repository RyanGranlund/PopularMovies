package com.digitalinfomesh.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Movie;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {


    //Variables that store movie data
    private String[] posterPaths = new String[20];
    private String[] titles = new String[20];
    private String[] plots = new String[20];
    private String[] ratings = new String[20];
    private String[] releases = new String[20];
    private String currentSortState = new String();


    public MainActivityFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            updateMovies();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String searchType = prefs.getString("search","popular");

        //if current movie search type does not match shared pref search type update movies
        if (currentSortState.equals(searchType)) {

        }else{
            updateMovies();
        }


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();

    }


    public void updateMovies() {
        FetchMoviesTask moviesTask = new FetchMoviesTask();
        moviesTask.execute();
    }

    //Get movie data using AsyncTask
    public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected String[] doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String moviesJsonStr = null;

            //Gets movie search type from shared preferences
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String searchType = prefs.getString("search","popular");
            currentSortState = searchType;



            try {
                //variables used to build URL
                String SORT_BY;
                final String MOVIES_BASE_URL = "http://api.themoviedb.org/3";
                final String TYPE_PATH = "discover";
                final String VIDEO_TYPE = "movie";
                final String COUNTRY = "US";
                final String API_Key = "1e6681f9617a4e50af4a8d0588a4429d";


                //Set sort by variable depending on shared preferences
                if (searchType.toString().equals("rating")) {
                    SORT_BY = "vote_average.desc";

                } else {
                    SORT_BY ="popularity.desc";

                }




                //build URL
                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendPath(TYPE_PATH)
                        .appendPath(VIDEO_TYPE)
                        .appendQueryParameter("certification_country",COUNTRY)
                        .appendQueryParameter("sort_by", SORT_BY)
                        .appendQueryParameter("api_key", API_Key)
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                moviesJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                //parse movie data from json
                return getMoviesDataFromJson(moviesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        private String[] getMoviesDataFromJson(String moviesJsonStr)
                throws JSONException {


            final String RESULT_LIST = "results";
            final String MOVIE_POSTER = "poster_path";
            final String MOVIE_TITLE = "original_title";
            final String MOVIE_PLOT = "overview";
            final String MOVIE_RATING = "vote_average";
            final String MOVIE_RELEASE = "release_date";

            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray resultArray = moviesJson.getJSONArray(RESULT_LIST);


            String[] resultStrs = new String[20];

            String path;
            String title;
            String plot;
            String rating;
            String release;

            //loop through json and set movie data
            for (int i = 0; i < resultArray.length(); i++) {


                JSONObject jsonObject = resultArray.getJSONObject(i);

                path = jsonObject.getString(MOVIE_POSTER);
                title = jsonObject.getString(MOVIE_TITLE);
                plot = jsonObject.getString(MOVIE_PLOT);
                rating = jsonObject.getString(MOVIE_RATING);
                release = jsonObject.getString(MOVIE_RELEASE);

                //set public static variables with movie data
                posterPaths[i] = path;
                titles[i] = title;
                plots[i] = plot;
                ratings[i] = rating;
                releases[i] = release;

                resultStrs[i] = path;
            }
            return resultStrs;

        }

        @Override
        protected void onPostExecute(String[] result){
            //Set gridview adapter
            GridView gridview = (GridView) getActivity().findViewById(R.id.gridView);
            gridview.setAdapter(new ImageAdapter(getActivity()));

        }

    }


    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return posterPaths.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }


        //Create imageviews for grid positions
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(500, 750));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                //imageView.setId(position +1 );
            } else {
                imageView = (ImageView) convertView;
            }

                //set image to image view
                Picasso.with(mContext).load("http://image.tmdb.org/t/p/w500/" + posterPaths[position]).into(imageView);



            //when clicking open details view
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
                    MovieParcelable movie = new MovieParcelable(posterPaths[position],titles[position],releases[position],ratings[position],plots[position]);
                    //detailIntent.putExtra("moviePosition",position);
                    detailIntent.putExtra("movieDetails", movie);
                    startActivity(detailIntent);

                }
            });
            return imageView;
        }

    }

}






