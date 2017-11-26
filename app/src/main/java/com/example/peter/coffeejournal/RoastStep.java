package com.example.peter.coffeejournal;

/**
 * Created by peter on 11/24/17.
 */

public class RoastStep {

    String time, comment;
    int temp;

    public RoastStep(){return;}

    public RoastStep(String time, int temp, String comment) {
        this.comment = comment;
        this.temp = temp;
        this.time = time;
    }

    public int getTemp() {
        return temp;
    }

    public String getComment() {
        return comment;
    }

    public String getTime() {
        return time;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
