package com.example.android.moviesapp.data;

public class Review {
    private String id;
    private String author;
    private String content;

    public String getContent(){
        return content;
    }
    public String getAuthor(){
        return author;
    }
    public String getId(){
        return id;
    }

    public void setContent(String content){
        this.content=content;
    }
    public void setAuthor(String author){
        this.author=author;
    }
    public void setId(String id){
        this.id=id;
    }
}
