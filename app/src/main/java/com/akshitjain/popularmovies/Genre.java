package com.akshitjain.popularmovies;

public class Genre {
    String genreName;

    public String getGenreName(int id) {
        switch (id) {
            case 28:
                genreName = "Action";
                break;
            case 12:
                genreName = "Adventure";
                break;
            case 16:
                genreName = "Animation";
                break;
            case 35:
                genreName = "Comedy";
                break;
            case 80:
                genreName = "Crime";
                break;
            case 99:
                genreName = "Documentary";
                break;
            case 18:
                genreName = "Drama";
                break;
            case 10751:
                genreName = "Family";
                break;
            case 14:
                genreName = "Fantasy";
                break;
            case 10769:
                genreName = "Foreign";
                break;
            case 36:
                genreName = "History";
                break;
            case 27:
                genreName = "Horror";
                break;
            case 10402:
                genreName = "Music";
                break;
            case 9648:
                genreName = "Mystery";
                break;
            case 10749:
                genreName = "Romance";
                break;
            case 878:
                genreName = "Science Fiction";
                break;
            case 10770:
                genreName = "Tv Movie";
                break;
            case 53:
                genreName = "Thriller";
                break;
            case 10752:
                genreName = "War";
                break;
            case 37:
                genreName = "Western";
                break;
            default:
                genreName = null;
        }
        return genreName;
    }
}
