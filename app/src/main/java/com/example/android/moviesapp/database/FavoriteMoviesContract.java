package com.example.android.moviesapp.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoriteMoviesContract {
    /*
        1) Content authority,
        2) Base content URI,
        3) Path(s) to the tasks directory
        4) Content URI for data in the TaskEntry class
     */
    public static final String CONTENT_AUTHORITY = "com.example.android.moviesapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_FAVORITE = "favoritemovies";


    public static final class FavoriteMoviesEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE).build();

        public static final String TABLE_NAME ="favoritemovies";
        public static final String MOVIE_ID="id";
        public static final String MOVIE_ORIGINAL_TITLE = "original_title";
        public static final String MOVIE_OVERVIEW = "overview";
        public static final String MOVIE_IMAGE_URL = "imageUrl";
        public static final String MOVIE_VOTE_AVERAGE = "vote_average";
        public static final String MOVIE_RELEASE_DATE = "release_date";
        public static final String MOVIE_BACKDROP_PATH = "backdrop_path";
    }
}
