package com.example.android.moviesapp.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mahmoud on 3/23/2017.
 */
public class DataItem implements Parcelable {
    public String original_title;
    public String overview;
    public String imageUrl;
    public String vote_average;
    public String release_date;
    public String backdrop_path;
    public Integer id;


    public DataItem(){

    }

    private DataItem(Parcel in) {
        id = in.readInt();
        original_title = in.readString();
        overview = in.readString();
        imageUrl = in.readString();
        vote_average = in.readString();
        release_date = in.readString();
        backdrop_path = in.readString();
    }

    public static final Creator<DataItem> CREATOR = new Creator<DataItem>() {
        @Override
        public DataItem createFromParcel(Parcel in) {
            return new DataItem(in);
        }

        @Override
        public DataItem[] newArray(int size) {
            return new DataItem[size];
        }
    };

    public String getImageUrl(){
        return imageUrl;
    }
    public String getRelease_date(){
        return release_date;
    }
    public String getOverview(){
        return overview;
    }
    public String getOriginal_title(){
        return original_title;
    }
    public String getBackdrop_path(){
        return  backdrop_path;
    }
    public String getVote_average(){
        return vote_average;
    }
    public Integer getId(){
        return id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(original_title);
        dest.writeString(overview);
        dest.writeString(imageUrl);
        dest.writeString(vote_average);
        dest.writeString(release_date);
        dest.writeString(backdrop_path);
        dest.writeInt(id);
    }
}



/*
public class DataItem implements Parcelable {
    private String original_title;
    private String overview;
    private String imageUrl;
    private String vote_average;
    private String release_date;
    private String backdrop_path;
    private Integer id;

    public DataItem(){}
    public DataItem(String original_title , String overview, String imageUrl, String vote_average, String release_date ,
                    String backdrop_path, Integer id){
        this.original_title= original_title;
        this.overview = overview;
        this.imageUrl = imageUrl;
        this.vote_average = vote_average;
        this.release_date = release_date;
        this.backdrop_path = backdrop_path;
        this.id = id;
    }

    public String getImageUrl(){
        return imageUrl;
    }
    public String getRelease_date(){
        return release_date;
    }
    public String getOverview(){
        return overview;
    }
    public String getOriginal_title(){
        return original_title;
    }
    public String getBackdrop_path(){
        return  backdrop_path;
    }
    public String getVote_average(){
        return vote_average;
    }
    public Integer getId(){
        return id;
    }

    protected DataItem(Parcel in) {
        original_title = in.readString();
        overview = in.readString();
        imageUrl = in.readString();
        vote_average = in.readString();
        release_date = in.readString();
        backdrop_path = in.readString();
    }

    public static final Creator<DataItem> CREATOR = new Creator<DataItem>() {
        @Override
        public DataItem createFromParcel(Parcel in) {
            return new DataItem(in);
        }

        @Override
        public DataItem[] newArray(int size) {
            return new DataItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(original_title);
        dest.writeString(overview);
        dest.writeString(imageUrl);
        dest.writeString(vote_average);
        dest.writeString(release_date);
        dest.writeString(backdrop_path);
        dest.writeInt(id);
    }
}
*/