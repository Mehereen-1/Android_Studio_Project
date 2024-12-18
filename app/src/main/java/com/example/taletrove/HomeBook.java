package com.example.taletrove;

public class HomeBook {
    private String userName;
    private String title;
    private String author;
    private String imgUrl;

    public HomeBook() {
    }

    public  HomeBook(String userName, String title, String author, String imgUrl) {
        this.userName = userName;
        this.title = title;
        this.author = author;
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    // Getters
    public String getUserName() { return userName; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
}
