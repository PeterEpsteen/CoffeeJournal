package com.example.peter.coffeekeeper.Models;

/**
 * Created by root on 2/26/18.
 */

public class BrewComment {
    private String comment, date, username;
    private int userID, brewID;

    public BrewComment() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getBrewID() {
        return brewID;
    }

    public int getUserID() {
        return userID;
    }

    public String getComment() {
        return comment;
    }

    public String getDate() {
        return date;
    }

    public void setBrewID(int brewID) {
        this.brewID = brewID;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
