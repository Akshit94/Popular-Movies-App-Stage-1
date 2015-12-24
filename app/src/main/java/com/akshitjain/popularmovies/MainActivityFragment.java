package com.akshitjain.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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
    MoviesAdapter mMoviesAdapter;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        GridView gridview = (GridView) rootView.findViewById(R.id.grid_view_movies);

        mMoviesAdapter = new MoviesAdapter(getActivity());
        gridview.setAdapter(mMoviesAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
                detailIntent.putExtra(Constants.MOVIE_OBJECT_PARCELABLE_EXTRA, (Parcelable) mMoviesAdapter.getItem(position));
                startActivity(detailIntent);
            }
        });
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_mainfragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onStart() {
        updateMovies(Constants.SORT_BY);
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_refresh:
                updateMovies(Constants.SORT_BY);
                break;
            case R.id.action_sort_popularity:
                item.setChecked(true);
                updateMovies(Constants.POPULARITY_SORT);
                break;
            case R.id.action_sort_rating:
                item.setChecked(true);
                updateMovies(Constants.VOTE_AVERAGE_SORT);
                break;
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateMovies(String sortOrder) {
        new FetchMoviesTask().execute(sortOrder);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, Movies[]> {
        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        private Movies[] getMoviesDataFromJson(String moviesJsonStr)
                throws JSONException {

            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray(Constants.TMDB_RESULTS);

            Movies movies[] = new Movies[moviesArray.length()];
            for (int i = 0; i < moviesArray.length(); ++i) {
                JSONObject movieObject = moviesArray.getJSONObject(i);
                JSONArray genreArray = movieObject.getJSONArray(Constants.TMDB_GENRES);
                int[] genre = new int[genreArray.length()];
                for (int j = 0; j < genreArray.length(); ++j) {
                    genre[j] = genreArray.getInt(j);
                }
                movies[i] = new Movies(movieObject.getString(Constants.TMDB_POSTER),
                        movieObject.getString(Constants.TMDB_TITLE),
                        movieObject.getString(Constants.TMDB_BACKDROP),
                        movieObject.getString(Constants.TMDB_OVERVIEW),
                        movieObject.getString(Constants.TMDB_VOTE_AVG),
                        movieObject.getString(Constants.TMDB_RELEASE_DATE),
                        genre);
            }
            return movies;
        }

        @Override
        protected Movies[] doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr;

            try {
                // Construct the URL for the TheMovieDb query
                // Possible parameters are available at TheMovieDb API page

                Uri builtUri = Uri.parse(Constants.MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(Constants.SORT_PARAM, params[0])
                        .appendQueryParameter(Constants.VOTE_COUNT_PARAM, Constants.VOTE_COUNT)
                        .appendQueryParameter(Constants.API_KEY_PARAM, Constants.API_KEY)
                        .build();
                URL url = new URL(builtUri.toString());

                // Create the request to TheMovieDb, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e("MainActivityFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
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
                        Log.e("MainActivityFragment", "Error closing stream", e);
                    }
                }
            }

            try {
                return getMoviesDataFromJson(moviesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Movies[] strings) {
            if (strings != null) {
                mMoviesAdapter.clear();
                for (Movies moviesStr : strings) {
                    mMoviesAdapter.add(moviesStr);
                }
            }
            mMoviesAdapter.notifyDataSetChanged();
            super.onPostExecute(strings);
        }
    }
}

