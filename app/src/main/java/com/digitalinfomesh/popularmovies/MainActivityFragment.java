package com.digitalinfomesh.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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


    public static String[] posterPaths = new String[20];
    public static String[] titles = new String[20];
    public static String[] plots = new String[20];
    public static String[] ratings = new String[20];
    public static String[] releases = new String[20];

    public MainActivityFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateMovies();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);



        return rootView;

    }


    @Override
    public void onStart() {
        super.onStart();

    }

    private void updateMovies() {
        FetchMoviesTask moviesTask = new FetchMoviesTask();
        moviesTask.execute();
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected String[] doInBackground(String... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;

            //http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=1e6681f9617a4e50af4a8d0588a4429d

           // SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
           // String searchType = getResources().getString(R.string.pref_search_key);
            try {
                final String MOVIES_BASE_URL = "http://api.themoviedb.org/3";
                final String TYPE_PATH = "discover";
                final String VIDEO_TYPE = "movie";
                final String SORT_BY = "popularity.desc";
                final String API_Key = "1e6681f9617a4e50af4a8d0588a4429d";


                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendPath(TYPE_PATH)
                        .appendPath(VIDEO_TYPE)
                        .appendQueryParameter("sort_by", SORT_BY)
                        .appendQueryParameter("api_key", API_Key)
                        .build();

                URL url = new URL(builtUri.toString());

                // Create the request to themoviedb, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                moviesJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the movie data, there's no point in attemping
                // to parse it.
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
                return getMoviesDataFromJson(moviesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        private String[] getMoviesDataFromJson(String moviesJsonStr)
                throws JSONException {


            // These are the names of the JSON objects that need to be extracted.
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

            for (int i = 0; i < resultArray.length(); i++) {


                // Get the JSON object representing the poster path
                JSONObject pathObject = resultArray.getJSONObject(i);
                JSONObject titleObject = resultArray.getJSONObject(i);
                JSONObject plotObject = resultArray.getJSONObject(i);
                JSONObject ratingObject = resultArray.getJSONObject(i);
                JSONObject releaseObject = resultArray.getJSONObject(i);

                // path is in a child array called "poster_path", which is 1 element long.
                //JSONObject posterObject = pathObject.getJSONArray(MOVIE_POSTER).getJSONObject(0);
                path = pathObject.getString(MOVIE_POSTER);
                title = pathObject.getString(MOVIE_TITLE);
                plot = pathObject.getString(MOVIE_PLOT);
                rating = pathObject.getString(MOVIE_RATING);
                release = pathObject.getString(MOVIE_RELEASE);

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


        // create a new ImageView for each item referenced by the Adapter
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(500, 750));
                imageView.setScaleType(ImageView.ScaleType.CENTER);
            } else {
                imageView = (ImageView) convertView;
            }

            Picasso.with(mContext).load("http://image.tmdb.org/t/p/w500/" + posterPaths[position]).into(imageView);


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
                    detailIntent.putExtra("moviePosition",position);
                    startActivity(detailIntent);

                }
            });
            return imageView;
        }

    }
}








