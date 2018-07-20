package com.example.android.moviesapp.data;


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
