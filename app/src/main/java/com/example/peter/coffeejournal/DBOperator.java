package com.example.peter.coffeejournal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.LayoutInflater;

import java.util.ArrayList;

/**
 * Created by peter on 11/4/17.
 */


public class DBOperator extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "COFFEE_JOURNAL_DB";
    private static final String BREW_TABLE_NAME = "BREW_TABLE";
    private static final String ROAST_TABLE_NAME = "ROAST_TABLE";
    private static final String BEANS_TABLE_NAME = "BEANS_TABLE";
    private static final String ROAST_STEPS_TABLE_NAME = "ROAST_STEPS_TABLE";

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String BREW_METHOD = "brew_method";
    private static final String RATIO = "ratio";
    private static final String NOTES = "notes";
    private static final String GRIND = "grind";
    private static final String BLOOM_TIME = "bloom";
    private static final String BREW_TIME = "brew_time";
    private static final String TEMP = "temp";
    private static final String DATE = "date";
    private static final String AMOUNT = "amount";
    private static final String MEASUREMENT = "measurement";
    private static final String STEP_NUMBER = "step_number";
    private static final String ROAST_ID = "roast_id";




    private static final String CREATE_BREW_TABLE = "create table " + BREW_TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT, " + BREW_METHOD + " TEXT, " +
            RATIO + " REAL, " + NOTES + " TEXT, " + GRIND + " TEXT, " + BLOOM_TIME + " INTEGER, " + BREW_TIME + " INTEGER, " + TEMP + " INTEGER)";
    private static final String CREATE_ROAST_TABLE = "create table " + ROAST_TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT, " + DATE + " TEXT)";
    private static final String CREATE_BEANS_TABLE = "create table " + BEANS_TABLE_NAME + " ( " + NAME + " TEXT PRIMARY KEY, " + ID + " INTEGER, " + AMOUNT + " REAL, " + MEASUREMENT + " TEXT)";
    private static final String CREATE_ROAST_STEPS_TABLE = "create table " + ROAST_STEPS_TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + STEP_NUMBER + " " +
            "INTEGER, " + ROAST_ID + " INTEGER, " + TEMP + " INTEGER, " + NOTES + " TEXT)";


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
        brewMethodValues.put(RATIO, 17);
        brewMethodValues.put(NOTES, "TO DO - ADD DEFAULT STEPS AND WHATNOT");
        brewMethodValues.put(GRIND, "Medium");
        brewMethodValues.put(BLOOM_TIME, 30);
        brewMethodValues.put(BREW_TIME, 180);
        brewMethodValues.put(TEMP, 200);  //NOT SURE
        db.insert(BREW_TABLE_NAME, null, brewMethodValues);

        brewMethodValues = new ContentValues();
        brewMethodValues.put(NAME, "French Press");
        brewMethodValues.put(BREW_METHOD, "French Press");
        brewMethodValues.put(RATIO, 17);
        brewMethodValues.put(NOTES, "TO DO - ADD DEFAULT STEPS AND WHATNOT");
        brewMethodValues.put(GRIND, "Course");
        brewMethodValues.put(BLOOM_TIME, 30);
        brewMethodValues.put(BREW_TIME, 240);
        brewMethodValues.put(TEMP, 201);  //NOT SURE
        db.insert(BREW_TABLE_NAME, null, brewMethodValues);

        brewMethodValues = new ContentValues();
        brewMethodValues.put(NAME, "Chemex");
        brewMethodValues.put(BREW_METHOD, "Chemex");
        brewMethodValues.put(RATIO, 17);
        brewMethodValues.put(NOTES, "TO DO - ADD DEFAULT STEPS AND WHATNOT");
        brewMethodValues.put(GRIND, "Medium");
        brewMethodValues.put(BLOOM_TIME, 30);
        brewMethodValues.put(BREW_TIME, 180);
        brewMethodValues.put(TEMP, 200);  //NOT SURE
        db.insert(BREW_TABLE_NAME, null, brewMethodValues);

        brewMethodValues = new ContentValues();
        brewMethodValues.put(NAME, "Espresso");
        brewMethodValues.put(BREW_METHOD, "Espresso");
        brewMethodValues.put(RATIO, 17);
        brewMethodValues.put(NOTES, "TO DO - ADD DEFAULT STEPS AND WHATNOT");
        brewMethodValues.put(GRIND, "Medium");
        brewMethodValues.put(BLOOM_TIME, 30);
        brewMethodValues.put(BREW_TIME, 180);
        brewMethodValues.put(TEMP, 200);  //NOT SURE
        db.insert(BREW_TABLE_NAME, null, brewMethodValues);

        brewMethodValues = new ContentValues();
        brewMethodValues.put(NAME, "Clover");
        brewMethodValues.put(BREW_METHOD, "Pour Over");
        brewMethodValues.put(RATIO, 17);
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

    public boolean addData(String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        return false;
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
                int ratio = data.getInt(data.getColumnIndex(RATIO));
                int brewTime = data.getInt(data.getColumnIndex(BREW_TIME));
                int bloomTime = data.getInt(data.getColumnIndex(BLOOM_TIME));

                BrewRecipe br = new BrewRecipe(name, brewMethod, grind, notes, ratio,
                        brewTime, bloomTime);

                recipes.add(br);
            }

        }
        return recipes;
    }


}
