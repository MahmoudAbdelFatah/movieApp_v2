package com.example.android.moviesapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moviesapp.adapters.ReviewRecyclerAdapter;
import com.example.android.moviesapp.adapters.TrailerRecyclerAdapter;
import com.example.android.moviesapp.data.DataItem;
import com.example.android.moviesapp.data.Review;
import com.example.android.moviesapp.data.Trailer;
import com.example.android.moviesapp.data.Uris;
import com.example.android.moviesapp.database.FavoriteMoviesContract;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private ImageView backdropImage;
    private ImageView posterImage;
    private TextView movieTitle;
    private TextView releaseDate;
    private TextView movieRating;
    private TextView overView;
    private DataItem dataItem;
    private FloatingActionButton fabButton;
    private boolean isFav=false;

    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;
    private ReviewRecyclerAdapter reviewRecyclerAdapter;
    private TrailerRecyclerAdapter trailerRecyclerAdapter;
    public static ArrayList<Trailer> lstTrailers = new ArrayList<>();
    public static ArrayList<Review> lstReview = new ArrayList<>();
    private ListView lvTrailer, lvReview;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(MainActivity.mTwoPane)
            dataItem = getArguments().getParcelable("MOVIE");
        else
        {
            Intent intent = getActivity().getIntent();
            if(intent !=null && intent.hasExtra(intent.EXTRA_TEXT)) {
                Integer position = Integer.parseInt(intent.getStringExtra(intent.EXTRA_TEXT));
                dataItem = MainActivityFragment.lstDataItems.get(position);
            }
        }
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        checkFav();
        fabButton = (FloatingActionButton) rootView.findViewById(R.id.fab);
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

        recyclerView1 =(RecyclerView) rootView.findViewById(R.id.recyclerView_review);
        recyclerView2 = (RecyclerView) rootView.findViewById(R.id.recyclerView_trailer);

        //set layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());

        recyclerView1.setLayoutManager(linearLayoutManager);
        recyclerView2.setLayoutManager(linearLayoutManager2);

        getTrailers();
        trailerRecyclerAdapter = new TrailerRecyclerAdapter(getContext(), lstTrailers);

        getReviews();
        reviewRecyclerAdapter = new ReviewRecyclerAdapter(getContext(), lstReview);

        recyclerView1.setAdapter(reviewRecyclerAdapter);
        recyclerView2.setAdapter(trailerRecyclerAdapter);

        if(isFav){

        }
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFav){
                    isFav=true;
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
                    long insertedId= ContentUris.parseId(insertUri);
                        if(insertedId>0) {
                            if(mToast !=null)
                                mToast.cancel();
                            mToast = Toast.makeText(getContext(), "Movie Added to Favorite Movies :)", Toast.LENGTH_LONG);
                            mToast.show();
                        }
                }else {
                    isFav = false;
                    int rowDeleted = getContext().getContentResolver().delete(
                            FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI,
                            FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_ID+" = ?" ,
                            new String[] {dataItem.getId().toString()}
                    );
                    if(rowDeleted >0) {
                        if(mToast !=null)
                            mToast.cancel();
                        mToast = Toast.makeText(getContext(), "Movie Removed from Favorite Movies :(", Toast.LENGTH_LONG);
                        mToast.show();
                }
            }
            }
        });

        return rootView;
    }

    private void savaClicked(){
        ContentValues contentValues = new ContentValues();

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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(getContext()) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
            }

            @Override
            public Cursor loadInBackground() {
                return null;
            }

        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void getTrailers() {
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
                        }
                    });
        } catch (Exception e) {
            Log.v("connect", "Ion not work well!");
        }
    }

    public void getReviews() {
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
                        }
                    });
        } catch (Exception e) {
            Log.v("connect", "Ion not work well!");
        }
    }


}
