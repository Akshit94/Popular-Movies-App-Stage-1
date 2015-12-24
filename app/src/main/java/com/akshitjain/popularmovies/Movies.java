package com.akshitjain.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;


//Parcelable because we can't send an object through Intent and Parcelable helps us do that.
public class Movies implements Parcelable {
    String posterPath;
    String originalTitle;
    String backdropPath;
    String overview;
    String userRating;
    String releaseDate;
    int[] genre;

    public Movies(String pP, String oT, String bP, String o, String uR, String rD, int[] g) {
        posterPath = pP;
        originalTitle = oT;
        backdropPath = bP;
        overview = o;
        userRating = uR;
        releaseDate = rD;
        genre = g;
    }

    protected Movies(Parcel in) {
        posterPath = in.readString();
        originalTitle = in.readString();
        backdropPath = in.readString();
        overview = in.readString();
        userRating = in.readString();
        releaseDate = in.readString();
        genre = in.createIntArray();
    }

    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeString(originalTitle);
        dest.writeString(backdropPath);
        dest.writeString(overview);
        dest.writeString(userRating);
        dest.writeString(releaseDate);
        dest.writeIntArray(genre);
    }
}
