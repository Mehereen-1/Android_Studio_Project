package com.example.taletrove;

import java.util.Objects;

public class Book {
    private String id;
    private String url;
    private String title;
    private String author;
    private double rating;
    private int ratings;
    private String smallImageURL;
    private int publicationYear;
    private String status;

    public Book() {
    }

    // Constructor
    public Book(String id, String url, String title, String author, double rating, int ratings, String smallImageURL, int publicationYear) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.author = author;
        this.rating = rating;
        this.ratings = ratings;
        this.smallImageURL = smallImageURL;
        this.publicationYear = publicationYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(title, book.title) && Objects.equals(author, book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author);
    }

    // Getters
    public String getId() { return id; }
    public String getUrl() { return url; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public double getRating() { return rating; }

    public int getRatings() { return ratings; }
    public String getSmallImageURL() { return smallImageURL; }
    public int getPublicationYear() { return publicationYear; }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setRatings(int ratings) {
        this.ratings = ratings;
    }

    public void setSmallImageURL(String smallImageURL) {
        this.smallImageURL = smallImageURL;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }
}
