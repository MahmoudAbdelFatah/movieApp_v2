package com.example.android.moviesapp.data;

import com.example.android.moviesapp.BuildConfig;

/**
 * Created by Mahmoud on 3/24/2017.
 */

public class Uris {
    //Uris of popular or top rated
    public static final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/";
    //TODO : add your API KEY HERE
    public static final String API_KEY = BuildConfig.API_KEY;
    public static final String TOP_RATED = "top_rated/";
    public static final String POPULAR="popular/";
    public static final String IMAGE_PATH = "http://image.tmdb.org/t/p/w185/";
}
