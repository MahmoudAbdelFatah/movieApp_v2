package com.example.android.moviesapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(recyclerAdapter);

        return rootView;
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

                                /*dataItem = new DataItem();

                                //Image Poster
                                String imageUrl = jsonArray.get(i).getAsJsonObject().get("poster_path").toString();
                                imageUrl = imageUrl.substring(2, imageUrl.length() - 1);
                                imageUrl = Uris.IMAGE_PATH + imageUrl;
                                //dataItem.imageUrl = imageUrl;

                                //overView
                                String overView = jsonArray.get(i).getAsJsonObject().get("overview").toString();
                                overView = overView.substring(1, overView.length() - 1);
                                //dataItem.overview = tmp;

                                //original title for movie
                                String original_title = jsonArray.get(i).getAsJsonObject().get("original_title").toString();
                                original_title = original_title.substring(1, original_title.length() - 1);
                                //dataItem.original_title = tmp;

                                //vote average
                                String vote_average = jsonArray.get(i).getAsJsonObject().get("vote_average").toString();
                                vote_average = vote_average + "/10.0";
                                //dataItem.vote_average = tmp;

                                //release Date
                                String release_date = jsonArray.get(i).getAsJsonObject().get("release_date").toString();
                                release_date = release_date.substring(1, release_date.length() - 1);
                                //dataItem.release_date = tmp;

                                String backdrop_path = jsonArray.get(i).getAsJsonObject().get("backdrop_path").toString();
                                backdrop_path = backdrop_path.substring(2, backdrop_path.length() - 1);
                                backdrop_path = Uris.IMAGE_PATH + backdrop_path;
                                //dataItem.backdrop_path = imageUrl;

                                Integer id = Integer.parseInt(jsonArray.get(i).getAsJsonObject().get("id").toString());
                               // new DataItem(original_title , overView, release_date, vote_average,
                                 //       release_date, backdrop_path , id);
                                Log.i("test", "in ION");
                                //recyclerAdapter.add(dataItem);
                                lstDataItems.add(dataItem);*/
                            }
                        }
                        Log.i("test", "" + lstDataItems.size());
                        recyclerAdapter.notifyDataSetChanged();
                    }
                });
    }
}
