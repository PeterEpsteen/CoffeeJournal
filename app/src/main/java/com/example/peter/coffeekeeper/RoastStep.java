package com.example.peter.coffeekeeper;

/**
 * Created by peter on 11/24/17.
 */

public class RoastStep {

    private String time, comment;
    private int temp, specialStep, beanTemp;

    final static int ROAST_START = 1;
    final static int ROAST_END = 2;
    final static int FIRST_CRACK_START = 3;
    final static int FIRST_CRACK_END = 4;
    final static int SECOND_CRACK_START = 5;
    final static int SECOND_CRACK_END = 6;

    public RoastStep(){
        this.comment = "";
        this.temp = 0;
        this.time = "";
        this.beanTemp = 0;
    }

    public RoastStep(String time, int temp, String comment, int beanTemp) {
        this.comment = comment;
        this.temp = temp;
        this.time = time;
        this.beanTemp = beanTemp;
    }

    public int getTemp() {
        return temp;
    }

    public int getBeanTemp() {
        return beanTemp;
    }

    public void setBeanTemp(int beanTemp) {
        this.beanTemp = beanTemp;
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

    public void setSpecialStep(int specialStep) {
        this.specialStep = specialStep;
    }

    public int getSpecialStep() {
        return specialStep;
    }



    public void setTime(String time) {
        this.time = time;
    }
}
