package com.akshitjain.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MoviesAdapter extends BaseAdapter {


    private Context mContext;
    List<Movies> list = new ArrayList<>();

    public MoviesAdapter(Context c) {
        this.mContext = c;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item_movies, parent, false);
        }

        ImageView moviePoster = (ImageView) convertView.findViewById(R.id.grid_item_movie_image);
        Movies movies = list.get(position);
        String posterPath = movies.posterPath;
        final String POSTER_FINAL_URL = Constants.IMAGE_BASE_URL + Constants.POSTER_SIZE_LARGE + posterPath;

        Picasso.with(mContext).load(POSTER_FINAL_URL.trim()).into(moviePoster);

        return convertView;
    }

    public void add(Movies movies) {
        list.add(movies);
    }

    public void clear() {
        list.clear();
    }

}
