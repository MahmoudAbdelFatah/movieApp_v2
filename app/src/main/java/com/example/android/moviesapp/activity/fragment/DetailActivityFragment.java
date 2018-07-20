package com.example.android.moviesapp.activity.fragment;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moviesapp.R;
import com.example.android.moviesapp.adapters.ReviewAdapter;
import com.example.android.moviesapp.adapters.TrailerAdapter;
import com.example.android.moviesapp.data.DataItem;
import com.example.android.moviesapp.data.Review;
import com.example.android.moviesapp.data.Trailer;
import com.example.android.moviesapp.data.Uris;
import com.example.android.moviesapp.database.FavoriteMoviesContract;
import com.example.android.moviesapp.interfaces.IOnItemClickListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailActivityFragment extends Fragment  {
    private ImageView backdropImage, posterImage;
    private TextView movieTitle, releaseDate, movieRating, overView;
    private DataItem dataItem;
    private Button addButton;
    private boolean isFav=false;
    private RecyclerView rvTrailer , rvReview;
    private ReviewAdapter reviewAdapter;
    private TrailerAdapter trailerAdapter;
    private ArrayList<Trailer> lstTrailers;
    private ArrayList<Review> lstReview;
    private Trailer mTrailers;
    private Review mReview;
    private Toast mToast;

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init() {
        lstTrailers = new ArrayList<>();
        lstReview = new ArrayList<>();
        trailerAdapter = new TrailerAdapter(getContext(), getTrailers());
        reviewAdapter = new ReviewAdapter(getContext(), getReviews());
    }

    private void injectViews(View rootView) {
        addButton = (Button) rootView.findViewById(R.id.btn_favourite);
        checkFav();
        if(isFav){
            addButton.setBackgroundResource(R.drawable.add_to_db);
        }
        backdropImage = (ImageView) rootView.findViewById(R.id.backdrop_image);
        posterImage = (ImageView) rootView.findViewById(R.id.movie_poster);
        movieTitle = (TextView) rootView.findViewById(R.id.movie_title);
        releaseDate = (TextView) rootView.findViewById(R.id.movie_release_date);
        movieRating = (TextView) rootView.findViewById(R.id.movie_rating);
        overView = (TextView) rootView.findViewById(R.id.overView);
        //set title of the activity
        getActivity().setTitle(dataItem.getOriginal_title());

        Picasso.with(getContext())
                .load(dataItem.getBackdrop_path())
                .into(backdropImage);
        Picasso.with(getContext())
                .load(dataItem.getImageUrl())
                .into(posterImage);

        movieTitle.setText(dataItem.getOriginal_title());
        releaseDate.setText(dataItem.getRelease_date());
        movieRating.setText(dataItem.getVote_average());
        overView.setText(dataItem.getOverview());

        rvTrailer =(RecyclerView) rootView.findViewById(R.id.recyclerView_trailer);
        rvReview = (RecyclerView) rootView.findViewById(R.id.recyclerView_review);

        //set layout
        rvTrailer.setLayoutManager(new LinearLayoutManager(getContext()));
        rvReview.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setAdapters() {
        rvTrailer.setAdapter(trailerAdapter);
        rvReview.setAdapter(reviewAdapter);

        rvTrailer.setAdapter(new TrailerAdapter(lstTrailers, new IOnItemClickListener() {
            @Override public void onItemClick(int position) {
                Uri uri = Uri.parse("https://www.youtube.com/watch?v=" +
                        lstTrailers.get(position).getKey());
                Intent intent = new Intent(Intent.ACTION_VIEW , uri);
                startActivity(intent);
            }
        }));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(intent.EXTRA_TEXT)) {
            Integer position = Integer.parseInt(intent.getStringExtra(intent.EXTRA_TEXT));
            dataItem = MainActivityFragment.lstDataItems.get(position);
        }
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        setHasOptionsMenu(true);
        injectViews(rootView);
        init();
        setAdapters();


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFav) {
                    addMovieDb();
                } else {
                    removeMovieDb();
                }
            }
        });

        return rootView;
    }

    private void addMovieDb() {
        isFav = true;
        addButton.setBackgroundResource(R.drawable.add_to_db);
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_ID, dataItem.getId());
        contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_ORIGINAL_TITLE, dataItem.getOriginal_title());
        contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_OVERVIEW, dataItem.getOverview());
        contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_IMAGE_URL, dataItem.getImageUrl());
        contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_VOTE_AVERAGE, dataItem.getVote_average());
        contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_RELEASE_DATE, dataItem.getRelease_date());
        contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_BACKDROP_PATH, dataItem.getBackdrop_path());

        Uri insertUri = getContext().getContentResolver().insert(FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI,
                contentValues);
        long insertedId = ContentUris.parseId(insertUri);
        if (insertedId > 0) {
            if (mToast != null)
                mToast.cancel();
            mToast = Toast.makeText(getContext(), "Movie Added to Favorite Movies :)", Toast.LENGTH_LONG);
            mToast.show();
        }
    }

    private void removeMovieDb() {
        isFav = false;
        addButton.setBackgroundResource(R.drawable.remove_from_db);
        int rowDeleted = getContext().getContentResolver().delete(
                FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI,
                FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_ID + " = ?",
                new String[]{dataItem.getId().toString()}
        );
        if (rowDeleted > 0) {
            if (mToast != null)
                mToast.cancel();
            mToast = Toast.makeText(getContext(), "Movie Removed from Favorite Movies :(", Toast.LENGTH_LONG);
            mToast.show();
        }
    }

    private void checkFav() {
        Cursor cursor = getContext().getContentResolver().query(
                FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI,
                new String[]{FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_ID},
                FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_ID + " = ? ",
                new String[]{dataItem.getId().toString()},
                null
        );
        if (!cursor.moveToFirst())
        {
            cursor.close();
            return;
        }
        // if Database contains movie
        isFav = true;
        cursor.close();
    }

    private ArrayList<Trailer> getTrailers() {
        lstTrailers.clear();
        final Integer MOVIE_ID = dataItem.getId();
        String url = "";
        try {
            url = Uris.MOVIE_BASE_URL + MOVIE_ID + Uris.TRAILER + "?api_key=" + Uris.API_KEY;
            //Log.i("URI", url);
            Ion.with(getActivity())
                    .load(url)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        String tmp = "";

                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            if (e == null) {
                                JsonArray jsonArray = result.getAsJsonArray("results");
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    mTrailers = new Trailer();

                                    //key of trailer
                                    tmp = jsonArray.get(i).getAsJsonObject().get("key").toString();
                                    tmp = tmp.substring(1, tmp.length() - 1);
                                    mTrailers.setKey(tmp);

                                    //name of trailer
                                    tmp = jsonArray.get(i).getAsJsonObject().get("name").toString();
                                    tmp = tmp.substring(1, tmp.length() - 1);
                                    mTrailers.setName(tmp);
                                    lstTrailers.add(mTrailers);

                                }
                            }
                            Log.i("test", "" + lstTrailers.size());
                            trailerAdapter.notifyDataSetChanged();
                        }
                    });
        } catch (Exception e) {
            Log.v("connect", "Ion not work well!");
        }
        return lstTrailers;
    }

    private ArrayList<Review> getReviews() {
        lstReview.clear();
        final Integer MOVIE_ID = dataItem.getId();
        String url = "";
        try {
            url = Uris.MOVIE_BASE_URL + MOVIE_ID + Uris.REVIEWS + "?api_key=" + Uris.API_KEY;
            Log.i("URI", url);
            Ion.with(getActivity())
                    .load(url)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        String tmp = "";

                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            if (e == null) {
                                JsonArray jsonArray = result.getAsJsonArray("results");
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    mReview = new Review();

                                    //id of review
                                    tmp = jsonArray.get(i).getAsJsonObject().get("id").toString();
                                    tmp = tmp.substring(1, tmp.length() - 1);
                                    mReview.setId(tmp);

                                    //author of review
                                    tmp = jsonArray.get(i).getAsJsonObject().get("author").toString();
                                    tmp = tmp.substring(1, tmp.length() - 1);
                                    mReview.setAuthor(tmp);

                                    //content of review
                                    tmp = jsonArray.get(i).getAsJsonObject().get("content").toString();
                                    tmp = tmp.substring(1, tmp.length() - 1);
                                    mReview.setContent(tmp);

                                    lstReview.add(mReview);

                                }
                            }
                            Log.i("test", "" + lstReview.size());
                            reviewAdapter.notifyDataSetChanged();
                            //reviewAdapter.notify();
                        }
                    });
        } catch (Exception e) {
            Log.v("connect", "Ion not work well!");
        }
        return lstReview;
    }

}
