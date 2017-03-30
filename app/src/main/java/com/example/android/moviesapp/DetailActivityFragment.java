package com.example.android.moviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.moviesapp.data.DataItem;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private ImageView backdropImage;
    private ImageView posterImage;
    private TextView movieTitle;
    private TextView releaseDate;
    private TextView movieRating;
    private TextView overView;
    private DataItem dataItem;

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
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent intent = getActivity().getIntent();
        if(intent !=null && intent.hasExtra(intent.EXTRA_TEXT)) {
            Integer position = Integer.parseInt(intent.getStringExtra(intent.EXTRA_TEXT));
            dataItem = MainActivityFragment.lstDataItems.get(position);
        }

        backdropImage = (ImageView) rootView.findViewById(R.id.backdrop_image);
        posterImage = (ImageView) rootView.findViewById(R.id.movie_poster);
        movieTitle = (TextView) rootView.findViewById(R.id.movie_title);
        releaseDate = (TextView) rootView.findViewById(R.id.movie_release_date);
        movieRating = (TextView) rootView.findViewById(R.id.movie_rating);
        overView = (TextView) rootView.findViewById(R.id.overView);
        getActivity().setTitle(dataItem.getOriginal_title());
        Picasso.with(getContext()).load(dataItem.getBackdrop_path())
                .into(backdropImage);
        Picasso.with(getContext()).load(dataItem.getImageUrl())
                .into(posterImage);

        movieTitle.setText(dataItem.getOriginal_title());
        releaseDate.setText(dataItem.getRelease_date());
        movieRating.setText(dataItem.getVote_average());
        overView.setText(dataItem.getOverview());


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       // inflater.inflate(R.menu.fragment_details, menu);

        // Retrieve the share menu item
       // MenuItem menuItem = menu.findItem(R.id.action_share);
    }
}
