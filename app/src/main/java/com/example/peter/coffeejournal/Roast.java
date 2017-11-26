package com.example.peter.coffeejournal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by peter on 11/21/17.
 */

public class Roast {

    String roastName, dateAdded, notes;
    int roastID, beanMetric, tempMetric;
    List<Bean> beanList;
    List<RoastStep> stepList;

    public Roast() {
        roastName = "Custom";
        stepList = new ArrayList<RoastStep>();
        Date date = new Date();
        dateAdded = date.toString();
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
}
