package com.example.android.moviesapp.database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.android.moviesapp.database.FavoriteMoviesContract.CONTENT_AUTHORITY;
import static com.example.android.moviesapp.database.FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME;
import static com.example.android.moviesapp.database.FavoriteMoviesContract.PATH_FAVORITE;

public class FavoriteMoviesProvider extends ContentProvider {
    private FavoriteMoviesDbHelper mFavoriteMoviesDbHelper;
    public static final int FAVORITE_MOVIES = 100;
    public static final UriMatcher sUriMatcher= buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(CONTENT_AUTHORITY , PATH_FAVORITE , FAVORITE_MOVIES);
        return uriMatcher;
    }

    //initialize the provider
    @Override
    public boolean onCreate() {
        Context context = getContext();
        mFavoriteMoviesDbHelper = new FavoriteMoviesDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor retCursor;
        SQLiteDatabase db = mFavoriteMoviesDbHelper.getReadableDatabase();
        switch (sUriMatcher.match(uri)) {
            case FAVORITE_MOVIES: {
                retCursor = db.query(
                        TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match) {

            case FAVORITE_MOVIES:
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +PATH_FAVORITE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = mFavoriteMoviesDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case FAVORITE_MOVIES:
                long id = db.insert(TABLE_NAME , null , contentValues);
                if(id > 0) {
                    returnUri = ContentUris.withAppendedId(uri, id);
                }else {
                    throw new android.database.SQLException("Field to insert into "+id);
                }
                break;
            default:
                throw new UnsupportedOperationException("unknown URI" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db = mFavoriteMoviesDbHelper.getWritableDatabase();
        int numRowsDeleted;
        int match = sUriMatcher.match(uri);
        if(s==null) {
            s = "1";
        }
        switch (match) {
            case FAVORITE_MOVIES:
                numRowsDeleted = db.delete(TABLE_NAME , s, strings);
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI " + uri);
        }
        if(numRowsDeleted !=0)
            getContext().getContentResolver().notifyChange(uri,null);
        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db = mFavoriteMoviesDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case FAVORITE_MOVIES:
                rowsUpdated = db.update(TABLE_NAME,
                        contentValues,
                        s,
                        strings);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        SQLiteDatabase db = mFavoriteMoviesDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsInserted=0;
        switch (match) {
            case FAVORITE_MOVIES:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                     db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return rowsInserted;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
