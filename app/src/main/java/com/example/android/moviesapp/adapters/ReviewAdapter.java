package com.example.android.moviesapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.moviesapp.R;
import com.example.android.moviesapp.data.Review;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private ArrayList<Review> mReviews;
    private Context mContext;

    public ReviewAdapter(Context context, ArrayList<Review> reviews) {
        this.mReviews = reviews;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View view = inflater.inflate(R.layout.list_item_review, viewGroup, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        viewHolder.reviewContent.setText(mReviews.get(position).getContent());
        viewHolder.reviewAuthor.setText(mReviews.get(position).getAuthor());
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView reviewAuthor;
        TextView reviewContent;
        public ViewHolder(View itemView) {
            super(itemView);
            reviewAuthor = (TextView) itemView.findViewById(R.id.author);
            reviewContent = (TextView) itemView.findViewById(R.id.content);
        }
    }
}
