package com.example.peter.coffeejournal;

/**
 * Created by peter on 11/5/17.
 */

public class BrewRecipe {

    String name, brewMethod, grind, notes;
    int metric, brewTime, bloomTime, icon;
    double coffeeUnits, waterUnits;

    public BrewRecipe(){
        return;
    }

    public BrewRecipe(String name, String brewMethod, String grind, String notes, double coffeeUnits, double waterUnits, int metric, int brewTime, int bloomTime) {
        this.name = name;
        this.brewMethod = brewMethod;
        this.grind = grind;
        this.notes = notes;
        this.metric = metric;
        this.coffeeUnits = coffeeUnits;
        this.waterUnits = waterUnits;
        this.brewTime = brewTime;
        this.bloomTime = bloomTime;
        this.icon = getIcon();
    }




    //GET AND SET METHODS

    public String getName() {
        return name;
    }

    public String getBrewMethod() {
        return brewMethod;
    }

    public String getGrind(){
        return grind;
    }

    public String getNotes() {
        return notes;
    }

    public boolean isMetric() {return metric == 1;}

    public double getCoffeeUnits() {return coffeeUnits;}

    public double getWaterUnits() {return waterUnits;
    }

    public int getMetric() {
        return metric;
    }

    public int getBrewTime() {
        return brewTime;
    }

    public int getBloomTime() {
        return bloomTime;
    }

    public int getIcon() {
        switch (brewMethod) {
            case "Pour Over":
                icon = R.drawable.pour_over_circle;
                break;
            case "French Press":
                icon = R.drawable.french_press_circle;
                break;
            case "Chemex":
                icon = R.drawable.chemex_circle;
                break;
            case "Aeropress":
                icon = R.drawable.aeropress_circle;
                break;
            case "Espresso":
                icon = R.drawable.espresso_circle;
                break;
            case "Moka Pot":
                icon = R.drawable.moka_circle;
                break;
            default:
                icon = R.drawable.pour_over_circle;
                break;

        }
        return icon;
    }



    public void setBloomTime(int bloomTime) {
        this.bloomTime = bloomTime;
    }

    public void setBrewMethod(String brewMethod) {
        this.brewMethod = brewMethod;
    }

    public void setBrewTime(int brewTime) {
        this.brewTime = brewTime;
    }

    public void setGrind(String grind) {
        this.grind = grind;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setCoffeeUnits(double coffeeUnits) {
        this.coffeeUnits = coffeeUnits;
    }

    public void setMetric(int metric) {
        this.metric = metric;
    }

    public void setWaterUnits(double waterUnits) {
        this.waterUnits = waterUnits;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
