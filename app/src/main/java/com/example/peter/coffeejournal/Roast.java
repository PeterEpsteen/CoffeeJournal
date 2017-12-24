package com.example.peter.coffeejournal;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by peter on 11/21/17.
 */

public class Roast implements Parcelable {

    String roastName, dateAdded, notes;
    int roastID, beanMetric, tempMetric;
    List<Bean> beanList;
    List<RoastStep> stepList;

    public Roast() {
        roastName = "Custom";
        stepList = new ArrayList<RoastStep>();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d yyyy h:mm a");
        dateAdded = simpleDateFormat.format(date);
        beanMetric = 1;
        tempMetric = 0;
        beanList = new ArrayList<Bean>();
        notes = "";
    }

    public Roast(String name, String date, int tempMetric, int beanMetric) {
        roastName = name;
        dateAdded = date;
        this.beanMetric = beanMetric;
        this.tempMetric = tempMetric;
        beanList = new ArrayList<Bean>();
        stepList = new ArrayList<RoastStep>();
        notes = "";
    }

    public static final Creator<Roast> CREATOR = new Creator<Roast>() {
        @Override
        public Roast createFromParcel(Parcel in) {
            return new Roast(in);
        }

        @Override
        public Roast[] newArray(int size) {
            return new Roast[size];
        }
    };

    public int getID() {
        return roastID;
    }

    public int getBeanMetric() {
        return beanMetric;
    }

    public int getTempMetric() {
        return tempMetric;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDate() {
        return dateAdded;
    }

    public String getName() {
        return roastName;
    }

    public void setBeanMetric(int beanMetric) {
        this.beanMetric = beanMetric;
    }

    public void setTempMetric(int tempMetric) {
        this.tempMetric = tempMetric;
    }

    public void addToBeanList(Bean newBean) {
        beanList.add(newBean);
    }

    public void addToStepList(RoastStep newStep) {
        stepList.add(newStep);
    }

    public List<Bean> getBeanList() {
        return beanList;
    }

    public List<RoastStep> getStepList() {
        return stepList;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setRoastID(int roastID) {
        this.roastID = roastID;
    }

    public void setRoastName(String roastName) {
        this.roastName = roastName;
    }

    @Override
    public String toString() {
        String printString = "Roast name: " + getName() + "Roast Date: " + getDate() + "\nBeans: " + getBeanList().toString() + "\nSteps: " + getStepList();
        return printString;
    }

//    public Roast(String name, String date, int tempMetric, int beanMetric) {
//        roastName = name;
//        dateAdded = date;
//        this.beanMetric = beanMetric;
//        this.tempMetric = tempMetric;
//        beanList = new ArrayList<Bean>();
//        stepList = new ArrayList<RoastStep>();
//        notes = "";
//    }
//
    public Roast(Parcel in){
        this.roastName = in.readString();
        this.dateAdded = in.readString();
        this.tempMetric = in.readInt();
        this.beanMetric = in.readInt();
        beanList = new ArrayList<Bean>();
        stepList = new ArrayList<RoastStep>();
        notes = "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(roastName);
        dest.writeString(dateAdded);
        dest.writeInt(tempMetric);
        dest.writeInt(beanMetric);
    }
}
