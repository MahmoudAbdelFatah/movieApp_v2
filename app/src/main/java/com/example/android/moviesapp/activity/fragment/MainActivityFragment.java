package com.example.android.moviesapp.activity.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.moviesapp.R;
import com.example.android.moviesapp.adapters.MovieAdapter;
import com.example.android.moviesapp.data.DataItem;
import com.example.android.moviesapp.data.Uris;
import com.example.android.moviesapp.database.FavoriteMoviesContract;
import com.example.android.moviesapp.database.FavoriteMoviesDbHelper;
import com.example.android.moviesapp.interfaces.IMovieChosen;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private SharedPreferences sharedPref;
    public static ArrayList<DataItem>lstDataItems;
    private FavoriteMoviesDbHelper mFavoriteMoviesDbHelper;
    private Toast mToast;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        setHasOptionsMenu(true);
        lstDataItems = new ArrayList<>();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        movieAdapter = new MovieAdapter(getActivity(), lstDataItems);
        movieAdapter.setMovieChosen((IMovieChosen) getActivity());

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns()));
        recyclerView.setAdapter(movieAdapter);

        return rootView;
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerView.setAdapter(movieAdapter);
        updateOrderType();
    }

    private void updateOrderType(){
        lstDataItems.clear();
        String query="";
        URL url ;
        try{
            sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
            String orderType = sharedPref.getString("movie", "popular");
            switch (orderType){
                case "top_rated":
                    query= Uris.MOVIE_BASE_URL+Uris.TOP_RATED+"?api_key="+Uris.API_KEY;
                    if(isNetworkAvailable(getContext())) {
                        downloadFromInternet(query);
                    }
                    else{
                        displayToast("No internet connection");
                    }
                    break;
                case "popular":
                    query =Uris.MOVIE_BASE_URL+Uris.POPULAR+"?api_key="+Uris.API_KEY;
                    if(isNetworkAvailable(getContext())) {
                        downloadFromInternet(query);
                    }
                    else{
                        displayToast("No internet connection");
                    }
                    break;
                case "favorite_movie":
                    getFavoriteMoviesFromDb();
                    if(lstDataItems.size()>0){
                        movieAdapter.notifyDataSetChanged();
                    }else{
                        displayToast("There isn't Favorite Movies");
                        movieAdapter.notifyDataSetChanged();
                    }
                    break;
            }

        }catch (Exception e){
            Log.e("connect", "the internet not working");
        }
    }

    private void displayToast(String toastText) {
        if (mToast != null)
            mToast.cancel();
        mToast = Toast.makeText(getContext(), toastText, Toast.LENGTH_LONG);
        mToast.show();
    }

    private void downloadFromInternet(String url){
        Ion.with(getActivity())
                .load(url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    DataItem dataItem;
                    String imageUrl;
                    String tmp;

                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e == null) {
                            JsonArray jsonArray = result.getAsJsonArray("results");
                            for (int i = 0; i < jsonArray.size(); i++) {
                                dataItem = new DataItem();

                                //Image Poster
                                imageUrl = jsonArray.get(i).getAsJsonObject().get("poster_path").toString();
                                imageUrl = imageUrl.substring(2, imageUrl.length() - 1);
                                imageUrl = Uris.IMAGE_PATH + imageUrl;
                                dataItem.imageUrl = imageUrl;

                                //overView
                                tmp = jsonArray.get(i).getAsJsonObject().get("overview").toString();
                                tmp = tmp.substring(1, tmp.length() - 1);
                                dataItem.overview = tmp;

                                //original title for movie
                                tmp = jsonArray.get(i).getAsJsonObject().get("original_title").toString();
                                tmp = tmp.substring(1, tmp.length() - 1);
                                dataItem.original_title = tmp;

                                //vote average
                                tmp = jsonArray.get(i).getAsJsonObject().get("vote_average").toString();
                                tmp = tmp + "/10.0";
                                dataItem.vote_average = tmp;

                                //release Date
                                tmp = jsonArray.get(i).getAsJsonObject().get("release_date").toString();
                                tmp = tmp.substring(1, tmp.length() - 1);
                                dataItem.release_date = tmp;

                                imageUrl = jsonArray.get(i).getAsJsonObject().get("backdrop_path").toString();
                                imageUrl = imageUrl.substring(2, imageUrl.length() - 1);
                                imageUrl = Uris.IMAGE_PATH + imageUrl;
                                dataItem.backdrop_path = imageUrl;

                                dataItem.id = Integer.parseInt(jsonArray.get(i).getAsJsonObject().get("id").toString());

                                Log.i("test", "in ION");
                                //movieAdapter.add(dataItem);
                                lstDataItems.add(dataItem);
                            }
                        }
                        Log.i("test", "" + lstDataItems.size());
                        movieAdapter.notifyDataSetChanged();
                    }
                });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void getFavoriteMoviesFromDb(){
        Cursor cursor = getMoviesEntry();
        if (cursor.moveToFirst()) {
            moviesFromDb(cursor);
        }
        cursor.close();
    }

    private Cursor getMoviesEntry() {
        return getContext().getContentResolver().query(
                FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

    }

    private void moviesFromDb(Cursor cursor) {
        DataItem dataItem ;
        do {
            dataItem = new DataItem();
            //Read row by row
            dataItem.id = Integer.parseInt(cursor.getString(0));
            dataItem.original_title = cursor.getString(1);
            dataItem.overview = cursor.getString(2);
            dataItem.imageUrl = cursor.getString(3);
            dataItem.vote_average = cursor.getString(4);
            dataItem.release_date = cursor.getString(5);
            dataItem.backdrop_path = cursor.getString(6);
            lstDataItems.add(dataItem);
        } while (cursor.moveToNext());
        movieAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
