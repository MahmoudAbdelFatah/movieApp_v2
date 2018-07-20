package com.example.android.moviesapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoriteMoviesDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME ="favoritemovies.db";
    private static final int DATABASE_VERSION= 1;
    public FavoriteMoviesDbHelper(Context context) {
        super(context , DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVORITE_TABLE =
                "CREATE TABLE " + FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME + " (" +
                        FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_ID + " TEXT PRIMARY KEY," +
                        FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_ORIGINAL_TITLE + " TEXT NOT NULL," +
                        FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_OVERVIEW + " TEXT, " +
                        FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_IMAGE_URL + " TEXT," +
                        FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_VOTE_AVERAGE + " TEXT," +
                        FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_RELEASE_DATE + " TEXT," +
                        FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_BACKDROP_PATH + " TEXT);";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
