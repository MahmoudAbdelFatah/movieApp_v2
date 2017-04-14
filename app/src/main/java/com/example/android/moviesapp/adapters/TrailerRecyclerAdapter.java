package com.example.android.moviesapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.moviesapp.R;
import com.example.android.moviesapp.data.Trailer;

import java.util.ArrayList;

/**
 * Created by Mahmoud on 4/13/2017.
 */

public class TrailerRecyclerAdapter extends RecyclerView.Adapter<TrailerRecyclerAdapter.ViewHolder> {

    private ArrayList<Trailer> mTrailers;
    private Context mContext;

    public TrailerRecyclerAdapter(Context context, ArrayList<Trailer> trailers ) {
        this.mTrailers = trailers;
        mContext = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.list_item_trailer, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }


    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.trailer_title.setText(mTrailers.get(position).getName());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView trailer_title ;

        public ViewHolder(View itemView) {
            super(itemView);
            trailer_title = (TextView) itemView.findViewById(R.id.trailer_title);

        }
    }
}
