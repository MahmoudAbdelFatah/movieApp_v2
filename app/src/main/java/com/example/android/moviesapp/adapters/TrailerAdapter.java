package com.example.android.moviesapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.moviesapp.R;
import com.example.android.moviesapp.data.Trailer;
import com.example.android.moviesapp.interfaces.IOnItemClickListener;

import java.util.ArrayList;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {
    private ArrayList<Trailer> mTrailers;
    private Context mContext;
    private IOnItemClickListener listener;

    public TrailerAdapter(ArrayList<Trailer> trailers, IOnItemClickListener listener) {
        this.mTrailers = trailers;
        this.listener = listener;
    }

    public TrailerAdapter(Context context, ArrayList<Trailer> trailers) {
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
        viewHolder.bind(mTrailers.get(position), listener , position);
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

        public void bind(final Trailer trailer, final IOnItemClickListener listener, final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });
        }
    }
}
