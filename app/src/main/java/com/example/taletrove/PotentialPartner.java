package com.example.taletrove;

import java.util.List;

public class PotentialPartner {

    private String userId; // ID of the potential partner
    private List<Book> booksToGive; // Books that the current user can give to this partner
    private List<Book> booksToReceive; // Books that the current user can receive from this partner

    public PotentialPartner(String userId, List<Book> booksToGive, List<Book> booksToReceive) {
        this.userId = userId;
        this.booksToGive = booksToGive;
        this.booksToReceive = booksToReceive;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Book> getBooksToGive() {
        return booksToGive;
    }

    public void setBooksToGive(List<Book> booksToGive) {
        this.booksToGive = booksToGive;
    }

    public List<Book> getBooksToReceive() {
        return booksToReceive;
    }

    public void setBooksToReceive(List<Book> booksToReceive) {
        this.booksToReceive = booksToReceive;
    }

    @Override
    public String toString() {
        return "PotentialPartner{" +
                "userId='" + userId + '\'' +
                ", booksToGive=" + booksToGive +
                ", booksToReceive=" + booksToReceive +
                '}';
    }
}

