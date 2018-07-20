package com.example.android.moviesapp.data;

public enum MovieSort {
    POPULAR("popular"),
    TOP_RATED("top_rated"),
    FAVORITE("favorite");

    private final String value;

    MovieSort(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
