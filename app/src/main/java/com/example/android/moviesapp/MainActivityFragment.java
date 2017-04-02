package com.example.android.moviesapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.moviesapp.adapters.RecyclerAdapter;
import com.example.android.moviesapp.data.DataItem;
import com.example.android.moviesapp.data.Uris;
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
    private RecyclerAdapter recyclerAdapter;
    SharedPreferences sharedPref;
    public static ArrayList<DataItem>lstDataItems;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        lstDataItems = new ArrayList<>();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        recyclerAdapter = new RecyclerAdapter(getActivity() , lstDataItems);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns()));
        recyclerView.setAdapter(recyclerAdapter);

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
        updateOrderType();
    }

    public void updateOrderType(){
        lstDataItems.clear();
        String query="";
        URL url ;
        try{
            sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
            String orderType = sharedPref.getString("movie", "popular");
            switch (orderType){
                case "top_rated":
                    query= Uris.MOVIE_BASE_URL+Uris.TOP_RATED+"?api_key="+Uris.API_KEY;
                    downloadFromInternet(query);
                    break;
                case "popular":
                    query =Uris.MOVIE_BASE_URL+Uris.POPULAR+"?api_key="+Uris.API_KEY;
                    downloadFromInternet(query);
                    break;
            }

        }catch (Exception e){
            Log.e("connect", "the internet not working");
        }

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
                                //recyclerAdapter.add(dataItem);
                                lstDataItems.add(dataItem);
                            }
                        }
                        Log.i("test", "" + lstDataItems.size());
                        recyclerAdapter.notifyDataSetChanged();
                    }
                });
    }
}
