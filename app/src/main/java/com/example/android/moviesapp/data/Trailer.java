package com.example.android.moviesapp.data;

/**
 * Created by Mahmoud on 4/13/2017.
 */

public class Trailer {
    public Trailer(){

    }
    private String key;
    private String name;

    public String getKey(){
        return key;
    }
    public String getName(){
        return name;
    }

    public void setKey(String Key) {
        this.key = Key;
    }
    public void setName(String name) {
        this.name = name;
    }
}
