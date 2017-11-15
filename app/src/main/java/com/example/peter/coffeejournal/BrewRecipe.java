package com.example.peter.coffeejournal;

/**
 * Created by peter on 11/5/17.
 */

public class BrewRecipe {

    String name, brewMethod, grind, notes;
    int ratio, brewTime, bloomTime, icon;

    public BrewRecipe(){
        return;
    }

    public BrewRecipe(String name, String brewMethod, String grind, String notes, int ratio, int brewTime, int bloomTime) {
        this.name = name;
        this.brewMethod = brewMethod;
        this.grind = grind;
        this.notes = notes;
        this.ratio = ratio;
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

    public int getRatio() {
        return ratio;
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
                icon = R.drawable.pour_over;
                break;
            case "French Press":
                icon = R.drawable.french_press;
                break;
            case "Chemex":
                icon = R.drawable.chemex;
                break;
            case "Espresso":
                icon = R.drawable.espresso_icon;
                break;
            case "Moka Pot":
                icon = R.drawable.moka;
                break;
            default:
                icon = R.drawable.pour_over;
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

    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
