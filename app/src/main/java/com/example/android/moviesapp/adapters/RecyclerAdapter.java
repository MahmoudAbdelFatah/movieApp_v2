package com.example.android.moviesapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.moviesapp.DetailActivity;
import com.example.android.moviesapp.MainActivity;
import com.example.android.moviesapp.R;
import com.example.android.moviesapp.data.DataItem;
import com.example.android.moviesapp.interfaces.MovieChosen;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mahmoud on 3/22/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    private List<DataItem> lstDataItems;
    private Context mContext;
    private MovieChosen mMovieChosen;

    public RecyclerAdapter(Context context, List<DataItem> dataItems) {
        this.lstDataItems = dataItems;
        mContext = context;
    }

    public void setMovieChosen(MovieChosen mMovieChosen){
        this.mMovieChosen = mMovieChosen;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.recycler_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder viewHolder, final int position) {
        // Get the data model based on position
        final DataItem dataItem = lstDataItems.get(position);

        //Download image using picasso library
        Picasso.with(mContext).load(dataItem.getImageUrl())
                .into(viewHolder.imageView);
        viewHolder.txtOfTitle.setText(dataItem.getOriginal_title());
        viewHolder.txtOfReleaseDate.setText(dataItem.getRelease_date());

        //Toast.makeText(mContext, "Hello out :"+position , Toast.LENGTH_SHORT).show();

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, "Hello :" + position, Toast.LENGTH_SHORT).show();
                if(!MainActivity.mTwoPane) {
                    String pos = "" + position;
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra(intent.EXTRA_TEXT, pos);
                    mContext.startActivity(intent);
                }else{
                    mMovieChosen.paneHandleItemClick(dataItem);
                }
            }
        });
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return lstDataItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView txtOfTitle;
        public TextView txtOfReleaseDate;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView=(ImageView)itemView.findViewById(R.id.imageView);
            txtOfTitle = (TextView) itemView.findViewById(R.id.movie_title);
            txtOfReleaseDate = (TextView) itemView.findViewById(R.id.movie_release_date);
            cardView = (CardView)itemView.findViewById(R.id.cardView1);
        }
    }
}