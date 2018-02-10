package com.example.peter.coffeekeeper;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by peter on 11/5/17.
 */
public class BrewRecipe implements Parcelable {

    String name, brewMethod, grind, notes, dateAdded;
    int coffeeMetric, waterMetric, brewTime, bloomTime, icon;
    double coffeeUnits, waterUnits;

    public BrewRecipe(){
    }

    public BrewRecipe(String name, String brewMethod, String grind, String notes, double coffeeUnits, double waterUnits, int coffeeMetric, int waterMetric, int brewTime, int bloomTime) {
        this.name = name;
        this.brewMethod = brewMethod;
        this.grind = grind;
        this.notes = notes;
        this.coffeeMetric = coffeeMetric;
        this.waterMetric = waterMetric;
        this.coffeeUnits = coffeeUnits;
        this.waterUnits = waterUnits;
        this.brewTime = brewTime;
        this.bloomTime = bloomTime;
        this.icon = getIcon();
        Date date = new Date();
        dateAdded = date.toString();
    }


    public BrewRecipe(Parcel in){
        // the order needs to be the same as in writeToParcel() method
        this.name = in.readString();
        this.brewMethod = in.readString();
        this.grind = in.readString();
        this.notes = in.readString();
        this.coffeeUnits = in.readDouble();
        this.waterUnits = in.readDouble();
        this.coffeeMetric = in.readInt();
        this.waterMetric = in.readInt();
        this.brewTime = in.readInt();
        this.bloomTime = in.readInt();
        this.dateAdded = in.readString();
        this.icon = getIcon();
    }

    public static final Creator<BrewRecipe> CREATOR = new Creator<BrewRecipe>() {
        @Override
        public BrewRecipe createFromParcel(Parcel in) {
            return new BrewRecipe(in);
        }

        @Override
        public BrewRecipe[] newArray(int size) {
            return new BrewRecipe[size];
        }
    };

    //Parcable overrides
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(brewMethod);
        dest.writeString(grind);
        dest.writeString(notes);
        dest.writeInt(coffeeMetric);
        dest.writeInt(waterMetric);
        dest.writeInt(brewTime);
        dest.writeInt(bloomTime);
        dest.writeInt(icon);
        dest.writeDouble(coffeeUnits);
        dest.writeDouble(waterUnits);
        dest.writeString(dateAdded);
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }


    //GET AND SET METHODS

    public String getName() {
        return name;
    }

    public String getBrewMethod() {
        return brewMethod;
    }

    public int getCoffeeMetric() {
        return coffeeMetric;
    }

    public int getWaterMetric() {
        return waterMetric;
    }

    public String getGrind(){
        return grind;
    }

    public String getNotes() {
        return notes;
    }

    public boolean isCoffeeMetric() {return coffeeMetric == 1;}

    public boolean isWaterMetric() {return waterMetric == 1;}

    public double getCoffeeUnits() {return coffeeUnits;}

    public double getWaterUnits() {return waterUnits;
    }

    public void setCoffeeMetric(int coffeeMetric) {
        this.coffeeMetric = coffeeMetric;
    }


    public void setWaterMetric(int waterMetric) {
        this.waterMetric = waterMetric;
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

    public void setWaterUnits(double waterUnits) {
        this.waterUnits = waterUnits;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    @Override
    public boolean equals(Object obj) {
        // Basic checks.
        if (obj == this) return true;
        if (!(obj instanceof BrewRecipe)) return false;

        // Property checks.
        BrewRecipe other = (BrewRecipe) obj;
        return name.equalsIgnoreCase(other.getName());
    }
}
