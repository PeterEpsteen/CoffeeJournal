package com.example.peter.coffeejournal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peter on 11/4/17.
 */


public class DBOperator extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 9;
    private static final String DATABASE_NAME = "COFFEE_JOURNAL_DB";
    private static final String BREW_TABLE_NAME = "BREW_TABLE";
    private static final String ROAST_TABLE_NAME = "ROAST_TABLE";
    private static final String BEANS_TABLE_NAME = "BEANS_TABLE";
    private static final String ROAST_STEPS_TABLE_NAME = "ROAST_STEPS_TABLE";

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String BREW_METHOD = "brew_method";
    private static final String WATER_UNITS = "water_units";
    private static final String COFFEE_UNITS = "coffee_units";
    private static final String METRIC = "metric";
    private static final String NOTES = "notes";
    private static final String GRIND = "grind";
    private static final String BLOOM_TIME = "bloom";
    private static final String BREW_TIME = "brew_time";
    private static final String TEMP = "temp";
    private static final String DATE = "date";
    private static final String WEIGHT = "weight";
    private static final String MEASUREMENT = "measurement";
    private static final String STEP_TIME = "step_time";
    private static final String ROAST_ID = "roast_id";




    private static final String CREATE_BREW_TABLE = "create table if not exists " + BREW_TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT, " + BREW_METHOD + " TEXT, " +
            WATER_UNITS + " REAL, "+ COFFEE_UNITS +" REAL, "+ METRIC + " INTEGER, " + NOTES + " TEXT, " + GRIND + " TEXT, " + BLOOM_TIME + " INTEGER, " + BREW_TIME + " INTEGER, " + TEMP + " INTEGER)";
    private static final String CREATE_ROAST_TABLE = "create table if not exists " + ROAST_TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT, " + NOTES + " TEXT, " + DATE + " TEXT)";
    private static final String CREATE_BEANS_TABLE = "create table if not exists " + BEANS_TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT, " + WEIGHT + " REAL, " + METRIC + " INTEGER, " + ROAST_ID + " INTEGER)";
    private static final String CREATE_ROAST_STEPS_TABLE = "create table if not exists " + ROAST_STEPS_TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + STEP_TIME + " " +
            "TEXT, " + ROAST_ID + " INTEGER, " + TEMP + " INTEGER, " + NOTES + " TEXT, " + METRIC + " INTEGER)";


    public DBOperator(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DBOperator(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BREW_TABLE);
        db.execSQL(CREATE_ROAST_TABLE);
        db.execSQL(CREATE_BEANS_TABLE);
        db.execSQL(CREATE_ROAST_STEPS_TABLE);

        //START ADDING BREW METHODS

        ContentValues brewMethodValues = new ContentValues();
        brewMethodValues.put(NAME, "V60");
        brewMethodValues.put(BREW_METHOD, "Pour Over");
        brewMethodValues.put(WATER_UNITS, 300);
        brewMethodValues.put(COFFEE_UNITS, 20);
        brewMethodValues.put(METRIC, 1);
        brewMethodValues.put(NOTES, "TO DO - ADD DEFAULT STEPS AND WHATNOT");
        brewMethodValues.put(GRIND, "Medium-Fine");
        brewMethodValues.put(BLOOM_TIME, 30);
        brewMethodValues.put(BREW_TIME, 180);
        brewMethodValues.put(TEMP, 200);  //NOT SURE
        db.insert(BREW_TABLE_NAME, null, brewMethodValues);

        brewMethodValues = new ContentValues();
        brewMethodValues.put(NAME, "French Press");
        brewMethodValues.put(BREW_METHOD, "French Press");
        brewMethodValues.put(WATER_UNITS, 400);
        brewMethodValues.put(COFFEE_UNITS, 27);
        brewMethodValues.put(METRIC, 1);
        brewMethodValues.put(NOTES, "TO DO - ADD DEFAULT STEPS AND WHATNOT");
        brewMethodValues.put(GRIND, "Course");
        brewMethodValues.put(BLOOM_TIME, 30);
        brewMethodValues.put(BREW_TIME, 240);
        brewMethodValues.put(TEMP, 201);  //NOT SURE
        db.insert(BREW_TABLE_NAME, null, brewMethodValues);

        brewMethodValues = new ContentValues();
        brewMethodValues.put(NAME, "Chemex");
        brewMethodValues.put(BREW_METHOD, "Chemex");
        brewMethodValues.put(WATER_UNITS, 600);
        brewMethodValues.put(COFFEE_UNITS, 40);
        brewMethodValues.put(METRIC, 1);
        brewMethodValues.put(NOTES, "TO DO - ADD DEFAULT STEPS AND WHATNOT");
        brewMethodValues.put(GRIND, "Medium");
        brewMethodValues.put(BLOOM_TIME, 30);
        brewMethodValues.put(BREW_TIME, 180);
        brewMethodValues.put(TEMP, 200);  //NOT SURE
        db.insert(BREW_TABLE_NAME, null, brewMethodValues);

        brewMethodValues = new ContentValues();
        brewMethodValues.put(NAME, "Espresso");
        brewMethodValues.put(BREW_METHOD, "Espresso");
        brewMethodValues.put(WATER_UNITS, 60);
        brewMethodValues.put(COFFEE_UNITS, 18);
        brewMethodValues.put(METRIC, 1);
        brewMethodValues.put(NOTES, "TO DO - ADD DEFAULT STEPS AND WHATNOT");
        brewMethodValues.put(GRIND, "Extra-Fine");
        brewMethodValues.put(BLOOM_TIME, 30);
        brewMethodValues.put(BREW_TIME, 180);
        brewMethodValues.put(TEMP, 200);  //NOT SURE
        db.insert(BREW_TABLE_NAME, null, brewMethodValues);

        brewMethodValues = new ContentValues();
        brewMethodValues.put(NAME, "Kalita Wave");
        brewMethodValues.put(BREW_METHOD, "Pour Over");
        brewMethodValues.put(WATER_UNITS, 300);
        brewMethodValues.put(COFFEE_UNITS, 21);
        brewMethodValues.put(METRIC, 1);
        brewMethodValues.put(NOTES, "TO DO - ADD DEFAULT STEPS AND WHATNOT");
        brewMethodValues.put(GRIND, "Medium");
        brewMethodValues.put(BLOOM_TIME, 30);
        brewMethodValues.put(BREW_TIME, 180);
        brewMethodValues.put(TEMP, 200);  //NOT SURE
        db.insert(BREW_TABLE_NAME, null, brewMethodValues);

        brewMethodValues = new ContentValues();
        brewMethodValues.put(NAME, "Aeropress");
        brewMethodValues.put(BREW_METHOD, "Aeropress");
        brewMethodValues.put(WATER_UNITS, 300);
        brewMethodValues.put(COFFEE_UNITS, 21);
        brewMethodValues.put(METRIC, 1);
        brewMethodValues.put(NOTES, "TO DO - ADD DEFAULT STEPS AND WHATNOT");
        brewMethodValues.put(GRIND, "Medium");
        brewMethodValues.put(BLOOM_TIME, 30);
        brewMethodValues.put(BREW_TIME, 180);
        brewMethodValues.put(TEMP, 200);  //NOT SURE
        db.insert(BREW_TABLE_NAME, null, brewMethodValues);

        //db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //THIS WILL BE EXECUTED WHEN YOU UPDATED VERSION OF DATABASE_VERSION
        //YOUR DROP AND CREATE QUERIES
        db.execSQL("DROP TABLE IF EXISTS " + BREW_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BEANS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ROAST_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ROAST_STEPS_TABLE_NAME);
        onCreate(db);
    }

    /*TODO : make adding data functional*/

    public long insert(BrewRecipe br) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NAME, br.getName());
        cv.put(BREW_METHOD, br.getBrewMethod());
        cv.put(COFFEE_UNITS, br.getCoffeeUnits());
        cv.put(WATER_UNITS, br.getWaterUnits());
        cv.put(METRIC, br.getMetric());
        cv.put(NOTES, br.getNotes());
        cv.put(GRIND, br.getGrind());
        cv.put(BLOOM_TIME, br.getBloomTime());
        cv.put(BREW_TIME, br.getBrewTime());
        cv.put(TEMP, "200");  //NOT SURE
        return db.insert(BREW_TABLE_NAME, null, cv);


    }

    public long insert(Roast roast) {
        long returnVal = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NAME, roast.getName());
        cv.put(DATE, roast.getDate());
        cv.put(NOTES, roast.getNotes());
        db.insert(ROAST_TABLE_NAME, null, cv);
        int roastID = -1;
        String roastIdQuery = "select "+ ID + " from " + ROAST_TABLE_NAME + " where " + NAME + " = '" + roast.getName() + "'";
        Cursor data = db.rawQuery(roastIdQuery, null);
        if (data.moveToFirst()){
            roastID = data.getInt(data.getColumnIndex(ID));
        }
        if(data != null)
            data.close();
        //insert bean rows
        List<Bean> beanArrayList = roast.getBeanList();
        for(Bean bean : beanArrayList) {
            cv = new ContentValues();
            cv.put(NAME, bean.getBeanName());
            cv.put(WEIGHT, bean.getBeanWeight());
            cv.put(ROAST_ID, roastID);
            returnVal = db.insert(BEANS_TABLE_NAME, null, cv);
            Log.i("Insert", "Inserted " + bean.getBeanName() +"with roast id: " + roastID + " Return value is: " + returnVal);
        }
        //insert step rows
        List<RoastStep> stepList = roast.getStepList();
        for(RoastStep step : stepList) {
            cv = new ContentValues();
            cv.put(STEP_TIME, step.getTime());
            cv.put(TEMP, step.getTemp());
            cv.put(NOTES, step.getComment());
            cv.put(ROAST_ID, roastID);
            db.insert(ROAST_STEPS_TABLE_NAME, null, cv);
            Log.i("Insert", "Inserted " + step.getTime());
        }

        db.close();
        return  returnVal;
    }

    //Might want to handle duplicate names in the future

    public boolean deleteBrew(String primaryKey) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = db.delete(BREW_TABLE_NAME, NAME + " = '" + primaryKey + "'", null) > 0;
        db.close();
        return success;
    }

    public ArrayList<Roast> getRoasts() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Roast> roasts = new ArrayList<Roast>();
        String query = "select * from " + ROAST_TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        if(data != null) {
            while (data.moveToNext()) {
                String roastName = data.getString(data.getColumnIndex(NAME));
                String date = data.getString(data.getColumnIndex(DATE));
                Roast newRoast = new Roast();
                newRoast.setRoastName(roastName);
                newRoast.setDateAdded(date);
                roasts.add(newRoast);
            }
        }
        if(data != null)
            data.close();
        db.close();
        return roasts;
    }

    public Roast getRoast(String name, String date) {
        Roast roast = new Roast();
        int roastID = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "select * from " + ROAST_TABLE_NAME + " where " + NAME + " = '" + name + "' AND " + DATE + " = '" + date + "'";
        Cursor data = db.rawQuery(queryString, null);
        if (data.moveToFirst()){
            roast.setNotes(data.getString(data.getColumnIndex(NOTES)));
            roastID = data.getInt(data.getColumnIndex(ID));
        }
        data.close();
        //Get all beans associated with roast
        queryString = "select * from " + BEANS_TABLE_NAME + " where " + ROAST_ID + " = " + roastID;
        data = db.rawQuery(queryString, null);
        if (data != null){
            while (data.moveToNext()) {
                String beanName = data.getString(data.getColumnIndex(NAME));
                int weight = (int) data.getDouble(data.getColumnIndex(WEIGHT));
                Log.i("Bean", "Adding bean to Object: " + beanName);
                Bean newBean = new Bean(beanName, weight);
                roast.addToBeanList(newBean);
            }
        }
        data.close();
        //Get all steps
        queryString = "select * from " + ROAST_STEPS_TABLE_NAME + " where " + ROAST_ID + " = " + roastID;
        data = db.rawQuery(queryString, null);
        if (data != null){
            while (data.moveToNext()) {
                String stepTime = data.getString(data.getColumnIndex(STEP_TIME));
                int temp = data.getInt(data.getColumnIndex(TEMP));
                String comments = data.getString(data.getColumnIndex(NOTES));
                RoastStep step = new RoastStep(stepTime, temp, comments);
                roast.addToStepList(step);
            }
        }
        data.close();
        db.close();
        return roast;
    }

    public ArrayList<BrewRecipe> getBrewRecipes() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<BrewRecipe> recipes = new ArrayList<BrewRecipe>();
        String query = "select * from " + BREW_TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        if (data != null) {
            while (data.moveToNext()) {
                String name = data.getString(data.getColumnIndex(NAME));
                String brewMethod = data.getString(data.getColumnIndex(BREW_METHOD));
                String grind = data.getString(data.getColumnIndex(GRIND));
                String notes = data.getString(data.getColumnIndex(NOTES));
                int metric = data.getInt(data.getColumnIndex(METRIC));
                double coffeeUnits = data.getDouble(data.getColumnIndex(COFFEE_UNITS));
                double waterUnits = data.getDouble(data.getColumnIndex(WATER_UNITS));
                int brewTime = data.getInt(data.getColumnIndex(BREW_TIME));
                int bloomTime = data.getInt(data.getColumnIndex(BLOOM_TIME));

                BrewRecipe br = new BrewRecipe(name, brewMethod, grind, notes, coffeeUnits, waterUnits, metric,
                        brewTime, bloomTime);

                recipes.add(br);
            }

        }
        return recipes;
    }

    public BrewRecipe getBrewRecipe(String name) {
        BrewRecipe br = new BrewRecipe();
        ArrayList<BrewRecipe> brArray = getBrewRecipes();
        for (BrewRecipe recipe: brArray) {
            if (recipe.getName().equalsIgnoreCase(name))
                br = recipe;
        }
        return br;
    }


    public boolean deleteRoast(String roastName) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = db.delete(ROAST_TABLE_NAME, NAME + " = '" + roastName + "'", null) > 0;
        db.close();
        return success;
    }
}
