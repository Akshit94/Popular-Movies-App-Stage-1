package com.akshitjain.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView backdropImageView = (ImageView) findViewById(R.id.detail_image_view);
        ImageView posterImageView = (ImageView) findViewById(R.id.detail_poster);
        TextView overviewTextView = (TextView) findViewById(R.id.overview_text_view);
        TextView releaseDateTextView = (TextView) findViewById(R.id.release_date);
        TextView userRatingTextView = (TextView) findViewById(R.id.user_rating);
        TextView genreTextView = (TextView) findViewById(R.id.genre);
        genreTextView.setText("");
        Genre genreObject = new Genre();
        String genreName;

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(Constants.MOVIE_OBJECT_PARCELABLE_EXTRA)) {
            Movies movies = intent.getParcelableExtra(Constants.MOVIE_OBJECT_PARCELABLE_EXTRA);
            int[] genre;
            genre = movies.genre;
            for (int i = 0; i < genre.length; ++i) {
                genreName = genreObject.getGenreName(genre[i]);
                genreTextView.append(genreName);
                if (i < genre.length - 1) {
                    genreTextView.append(", ");
                }
            }

            setTitle(movies.originalTitle);
            Picasso.with(getApplicationContext())
                    .load((Constants.IMAGE_BASE_URL + Constants.POSTER_SIZE_LARGE).trim() + movies.backdropPath)
                    .into(backdropImageView);
            Picasso.with(getApplicationContext())
                    .load((Constants.IMAGE_BASE_URL + Constants.POSTER_SIZE_SMALL).trim() + movies.posterPath)
                    .into(posterImageView);
            overviewTextView.setText(movies.overview);
            releaseDateTextView.setText(movies.releaseDate);
            userRatingTextView.setText(movies.userRating);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
