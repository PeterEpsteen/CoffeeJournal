package com.example.peter.coffeekeeper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by peter on 11/4/17.
 */


public class DBOperator extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 20;
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
    private static final String COFFEE_METRIC = "coffee_metric";
    private static final String WATER_METRIC = "water_metric";
    private static final String NOTES = "notes";
    private static final String GRIND = "grind";
    private static final String BLOOM_TIME = "bloom";
    private static final String BREW_TIME = "brew_time";
    private static final String TEMP = "temp";
    private static final String BEAN_TEMP = "bean_temp";

    private static final String DATE = "date";
    private static final String WEIGHT = "weight";
    private static final String MEASUREMENT = "measurement";
    private static final String STEP_TIME = "step_time";
    private static final String ROAST_ID = "roast_id";


    private static final String CREATE_BREW_TABLE = "create table if not exists " + BREW_TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT, " + BREW_METHOD + " TEXT, " +
            WATER_UNITS + " REAL, " + COFFEE_UNITS + " REAL, " + COFFEE_METRIC + " INTEGER, "+ WATER_METRIC + " INTEGER, " + NOTES + " TEXT, " + DATE + " TEXT, " + GRIND + " TEXT, " + BLOOM_TIME + " INTEGER, " + BREW_TIME + " INTEGER, " + TEMP + " INTEGER)";
    private static final String CREATE_ROAST_TABLE = "create table if not exists " + ROAST_TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT, " + NOTES + " TEXT, " + DATE + " TEXT)";
    private static final String CREATE_BEANS_TABLE = "create table if not exists " + BEANS_TABLE_NAME + " ( " + NAME + " TEXT, " + WEIGHT + " REAL, " + METRIC + " INTEGER, " + ROAST_ID + " INTEGER, PRIMARY KEY(" + ROAST_ID + ", " + NAME + "))";
    private static final String CREATE_ROAST_STEPS_TABLE = "create table if not exists " + ROAST_STEPS_TABLE_NAME + " ( " + STEP_TIME + " " +
            "TEXT, " + ROAST_ID + " INTEGER, " + TEMP + " INTEGER, " + BEAN_TEMP + " INTEGER, " + NOTES + " TEXT, " + METRIC + " INTEGER, PRIMARY KEY(" + ROAST_ID + ", " + STEP_TIME + "))";


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
        Date date = new Date();
        String dateAdded = date.toString();

        ContentValues brewMethodValues = new ContentValues();
        brewMethodValues.put(NAME, "V60");
        brewMethodValues.put(BREW_METHOD, "Pour Over");
        brewMethodValues.put(WATER_UNITS, 440);
        brewMethodValues.put(COFFEE_UNITS, 29);
        brewMethodValues.put(COFFEE_METRIC, 1);
        brewMethodValues.put(WATER_METRIC, 1);
        brewMethodValues.put(NOTES, "    Dosage: 29 Grams Coffee for 440g Water\n" +
                "    Grind: Medium (or finer) (15-20/40)\n" +
                "    Temp: Off the boil (203°-205°)\n" +
                "    Pre-Infusion: Pre-wet and let sit for 45 seconds\n" +
                "    Total Time: 2:15\n" +
                "\n" +
                "Method\n" +
                "\n" +
                "    Rinse Filter\n" +
                "    Grind and pour coffee into V60 shaking to settle grounds.\n" +
                "    Pre-infuse with water allowing it to sit 45 seconds\n" +
                "    Begin pouring from the center out clockwise in a circular motion until volume is reached.\n");
        brewMethodValues.put(GRIND, "Medium-Fine");
        brewMethodValues.put(BLOOM_TIME, 45);
        brewMethodValues.put(BREW_TIME, 90);
        brewMethodValues.put(TEMP, 200);  //NOT SURE
        brewMethodValues.put(DATE, dateAdded);
        db.insert(BREW_TABLE_NAME, null, brewMethodValues);

        brewMethodValues = new ContentValues();
        brewMethodValues.put(NAME, "French Press");
        brewMethodValues.put(BREW_METHOD, "French Press");
        brewMethodValues.put(WATER_UNITS, 400);
        brewMethodValues.put(COFFEE_UNITS, 27);
        brewMethodValues.put(COFFEE_METRIC, 1);
        brewMethodValues.put(WATER_METRIC, 1);
        brewMethodValues.put(NOTES, "27g (5 Tbsp) coffee  •  400g (1.75 cups) water  •  4 minutes \n Recipe Instructions\n" +
                "This recipe is for the 4-cup (17-oz) French Press coffee maker (also known as a \"press pot\"), which makes 2 small mugs of coffee. Double everything and follow the same process for the 8-cup (34-oz) version.\n" +
                "Step 1:\n" +
                "Preheat your press with hot water. Measure 5 tablespoons or 27 grams of coffee and grind. It should have the consistency of kosher salt.\nStep 2:\n" +
                " Add coffee into the press.\nStep 3:\n" +
                "Starting the timer, add water in a circular motion, wetting all the grounds, until the press is half full. Bloom for 30 seconds.\nStep 4:\n" +
                "30 seconds in, stir the grounds with a spoon to fully infuse.\nStep 5\n" +
                "Evenly pour remaining water to the top of the press and add the lid. (If you're using a kitchen scale, it should come to around 400g of water altogether.)\nStep 6:\n" +
                "Wait until your timer reads 4:00, then slowly push the plunger all the way down.\nStep 7:\n" +
                "Immediately pour the coffee to prevent over-extraction.");
        brewMethodValues.put(GRIND, "Course");
        brewMethodValues.put(BLOOM_TIME, 30);
        brewMethodValues.put(BREW_TIME, 210);
        brewMethodValues.put(TEMP, 201);  //NOT SURE
        brewMethodValues.put(DATE, dateAdded);
        db.insert(BREW_TABLE_NAME, null, brewMethodValues);

        brewMethodValues = new ContentValues();
        brewMethodValues.put(NAME, "Chemex");
        brewMethodValues.put(BREW_METHOD, "Chemex");
        brewMethodValues.put(WATER_UNITS, 600);
        brewMethodValues.put(COFFEE_UNITS, 40);
        brewMethodValues.put(COFFEE_METRIC, 1);
        brewMethodValues.put(WATER_METRIC, 1);        brewMethodValues.put(NOTES, "One: Fill pouring kettle with hot filtered water and rinse filter until the glass is heated. Discard water from both.\n" +
                "\n" +
                "Two: Weigh coffee to desired strength (50-57 grams) and grind.\n" +
                "\n" +
                "Three: Place the chemex on a scale, add coffee, shake to level the coffee bed, zero the scale.\n" +
                "\n" +
                "Four: Fill your pouring kettle with hot filtered water.\n" +
                "\n" +
                "Five: Start timer and pour 260g of water in 30 seconds. Start in the center and work your way out in concentric circles.\n" +
                "\n" +
                "Six: at 1:00, pour to 520g of water in 30 seconds.\n" +
                "\n" +
                "Seven: At 2:30, pour to 780g of water in 30 seconds.\n" +
                "\n" +
                "Eight: Coffee should drain at around 5 minutes. Remove filter. Serve.");
        brewMethodValues.put(GRIND, "Medium");
        brewMethodValues.put(BLOOM_TIME, 0);
        brewMethodValues.put(BREW_TIME, 300);
        brewMethodValues.put(TEMP, 200);  //NOT SURE
        brewMethodValues.put(DATE, dateAdded);
        db.insert(BREW_TABLE_NAME, null, brewMethodValues);

        brewMethodValues = new ContentValues();
        brewMethodValues.put(NAME, "Espresso");
        brewMethodValues.put(BREW_METHOD, "Espresso");
        brewMethodValues.put(WATER_UNITS, 60);
        brewMethodValues.put(COFFEE_UNITS, 18);
        brewMethodValues.put(COFFEE_METRIC, 1);
        brewMethodValues.put(WATER_METRIC, 1);        brewMethodValues.put(NOTES, "\n" +
                "Measure ~18g per 2oz (60g) shot of espresso. \n Grind very fine, but the exact grind will take trial and error. \nPut the grounds in the portafilter basket, and tamp with firm pressure. \nLoad the portafilter into the machine and pull the shot, aiming for 2oz of espresso in about 30 seconds. \nEspresso is an art and takes a lot of practice. If the pull is too quick, try using an extra gram of coffee, or a finer ground. The reverse is also true.");
        brewMethodValues.put(GRIND, "Extra-Fine");
        brewMethodValues.put(BLOOM_TIME, 5);
        brewMethodValues.put(BREW_TIME, 30);
        brewMethodValues.put(TEMP, 200);  //NOT SURE
        brewMethodValues.put(DATE, dateAdded);
        db.insert(BREW_TABLE_NAME, null, brewMethodValues);


        brewMethodValues = new ContentValues();
        brewMethodValues.put(NAME, "Aeropress");
        brewMethodValues.put(BREW_METHOD, "Aeropress");
        brewMethodValues.put(WATER_UNITS, 370);
        brewMethodValues.put(COFFEE_UNITS, 35);
        brewMethodValues.put(COFFEE_METRIC, 1);
        brewMethodValues.put(WATER_METRIC, 1);        brewMethodValues.put(NOTES, "Winner: 2017 World AeroPress Championship\n" +
                "\n" +
                "Coffee: 35g\n" +
                "Water: 370g @ 84°C\n" +
                "Brewer: Inverted\n" +
                "Filter: Paper\n" +
                "\n" +
                "    Put 35g of coffee into your AeroPress\n" +
                "\n" +
                "    From 0:00 to 0:15, add 150g of water\n" +
                "\n" +
                "    From 0:15 to 0:35, stir and keep stirring\n" +
                "\n" +
                "    At 0:35, put filter cap (with pre-wet filter) in place\n" +
                "\n" +
                "    At 1:05, flip the AeroPress and start pressing\n" +
                "\n" +
                "    At 1:35, stop pressing. You should have now 90ml of the concentrated brew (4.5%TDS)\n" +
                "\n" +
                "    Add 160g - 200g of hot water and enjoy!\n" +
                "\n" +
                "Total brew time - 1:35");
        brewMethodValues.put(GRIND, "Medium-Fine");
        brewMethodValues.put(BLOOM_TIME, 0);
        brewMethodValues.put(BREW_TIME, 95);
        brewMethodValues.put(TEMP, 200);  //NOT SURE
        brewMethodValues.put(DATE, dateAdded);
        db.insert(BREW_TABLE_NAME, null, brewMethodValues);

        //db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //THIS WILL BE EXECUTED WHEN YOU UPDATED VERSION OF DATABASE_VERSION
        //YOUR DROP AND CREATE QUERIES
        onCreate(db);
        String renameBrewTable = "ALTER TABLE " + BREW_TABLE_NAME + " RENAME TO og_brew_table";
        db.execSQL(renameBrewTable);
        String correctBrewTableColumns = "create table " + BREW_TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT, " + BREW_METHOD + " TEXT, " + WATER_UNITS + " REAL, " + COFFEE_UNITS + " REAL, " + COFFEE_METRIC + " INTEGER, " + WATER_METRIC + " INTEGER, " + NOTES + " TEXT, " + DATE + " TEXT, " + GRIND + " TEXT, " + BLOOM_TIME + " INTEGER, " + BREW_TIME + " INTEGER, " + TEMP + " INTEGER)";
        db.execSQL(correctBrewTableColumns);
        String insertOg = "INSERT INTO " + BREW_TABLE_NAME + " (" + ID + ", " + NAME + ", " + BREW_METHOD + ", "+ WATER_UNITS + ", "+ COFFEE_UNITS + ", "+ COFFEE_METRIC + ", "+ NOTES + ", "+ DATE + ", " + GRIND + ", "+ BLOOM_TIME + ", "+ BREW_TIME + ", "+ TEMP + ") SELECT " + ID + ", " + NAME + ", " + BREW_METHOD + ", "+ WATER_UNITS + ", "+ COFFEE_UNITS + ", "+ METRIC + ", "+ NOTES + ", "+ DATE + ", " + GRIND + ", "+ BLOOM_TIME + ", "+ BREW_TIME + ", "+ TEMP + " FROM og_brew_table";
        db.execSQL(insertOg);
        db.execSQL("DROP TABLE og_brew_table");
        String updateBrew = "UPDATE " + BREW_TABLE_NAME + " SET " + WATER_METRIC+" = " + COFFEE_METRIC + " WHERE " + WATER_METRIC + " IS NULL";
        db.execSQL(updateBrew);
    }


    public long insert(BrewRecipe br) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NAME, br.getName());
        cv.put(BREW_METHOD, br.getBrewMethod());
        cv.put(COFFEE_UNITS, br.getCoffeeUnits());
        cv.put(WATER_UNITS, br.getWaterUnits());
        cv.put(COFFEE_METRIC, br.getCoffeeMetric());
        cv.put(WATER_METRIC, br.getWaterMetric());
        cv.put(NOTES, br.getNotes());
        cv.put(GRIND, br.getGrind());
        cv.put(BLOOM_TIME, br.getBloomTime());
        cv.put(BREW_TIME, br.getBrewTime());
        cv.put(TEMP, "200");  //NOT SURE
        cv.put(DATE, br.getDateAdded());
        long returnLong = db.insert(BREW_TABLE_NAME, null, cv);
        if (returnLong > 0) {
            Log.i("DB", "Succesfully inserted brew name: " + br.getName());
        }
        db.close();
        return returnLong;

    }

    public long update(BrewRecipe br, String editBrewName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = NAME + " = '" + editBrewName + "'";
        if (db.delete(BREW_TABLE_NAME, whereClause, null) > 0) {
            Log.i("DB", "Deleted old brew name: " + editBrewName);
        } else {
            Log.e("DB", "Error deleting old brew");
            return -1;
        }
        db.close();
        return insert(br);
    }

    public ArrayList<String> getBrewNames() {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT " + NAME + " FROM " + BREW_TABLE_NAME, null);
        ArrayList<String> brewNamesList = new ArrayList<String>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                brewNamesList.add(cursor.getString(cursor.getColumnIndex(NAME)));
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return brewNamesList;

    }

    public long update(Roast r) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NAME, r.getName());
        cv.put(NOTES, r.getNotes());
        cv.put(DATE, r.getDate());
        String whereClause = DATE + " = '" + r.getDate() + "'";
        long returnLong = db.update(ROAST_TABLE_NAME, cv, whereClause, null);
        int roastID = -1;
        String roastIdQuery = "select " + ID + " from " + ROAST_TABLE_NAME + " where " + DATE + " = '" + r.getDate() + "'";
        Cursor data = db.rawQuery(roastIdQuery, null);
        if (data.moveToFirst()) {
            roastID = data.getInt(data.getColumnIndex(ID));
        }
        if (data != null)
            data.close();
        Log.i("DB", "Roast ID of updated roast: " + roastID);
        updateBeans(r, roastID);
        //insert step rows
        updateSteps(r, roastID);

        db.close();

        return returnLong;
    }

    private long updateBeans(Roast r, int roastID) {
        SQLiteDatabase db = this.getWritableDatabase();
        long returnLong = -1;
        String whereClause = ROAST_ID + " = " + roastID;
        if (db.delete(BEANS_TABLE_NAME, whereClause, null) > 0) {
            Log.i("DB", "Deleted old beans");
        }
        ContentValues cv = new ContentValues();
        List<Bean> beanArrayList = r.getBeanList();
        for (Bean bean : beanArrayList) {
            cv = new ContentValues();
            cv.put(NAME, bean.getBeanName());
            cv.put(WEIGHT, bean.getBeanWeight());
            cv.put(ROAST_ID, roastID);
            returnLong = db.insert(BEANS_TABLE_NAME, null, cv);
            Log.i("Insert", "Inserted " + bean.getBeanName() + "with roast id: " + roastID + " Return value is: " + returnLong);
        }
        db.close();
        return returnLong;
    }

    private long updateSteps(Roast r, int roastID) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = ROAST_ID + " = " + roastID;
        if (db.delete(ROAST_STEPS_TABLE_NAME, whereClause, null) > 0) {
            Log.i("DB", "Deleted old steps");
        }
        long returnLong = -1;
        List<RoastStep> stepList = r.getStepList();
        for (RoastStep step : stepList) {
            cv = new ContentValues();
            cv.put(STEP_TIME, step.getTime());
            cv.put(TEMP, step.getTemp());
            cv.put(NOTES, step.getComment());
            cv.put(ROAST_ID, roastID);
            cv.put(BEAN_TEMP, step.getBeanTemp());
            returnLong = db.insert(ROAST_STEPS_TABLE_NAME, null, cv);
            Log.i("Insert", "Inserted " + step.getTime());
        }
        db.close();
        return returnLong;
    }

    private long insertBeans(Roast r, int roastID) {
        //insert bean rows
        SQLiteDatabase db = this.getWritableDatabase();
        long returnLong = -1;
        ContentValues cv = new ContentValues();
        List<Bean> beanArrayList = r.getBeanList();
        for (Bean bean : beanArrayList) {
            cv = new ContentValues();
            cv.put(NAME, bean.getBeanName());
            cv.put(WEIGHT, bean.getBeanWeight());
            cv.put(ROAST_ID, roastID);
            returnLong = db.insert(BEANS_TABLE_NAME, null, cv);
            Log.i("Insert", "Inserted " + bean.getBeanName() + "with roast id: " + roastID + " Return value is: " + returnLong);
        }
        db.close();
        return returnLong;

    }


    private long insertSteps(Roast r, int roastID) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        long returnLong = -1;
        List<RoastStep> stepList = r.getStepList();
        for (RoastStep step : stepList) {
            cv = new ContentValues();
            cv.put(STEP_TIME, step.getTime());
            cv.put(TEMP, step.getTemp());
            cv.put(NOTES, step.getComment());
            cv.put(BEAN_TEMP, step.getBeanTemp());
            cv.put(ROAST_ID, roastID);
            returnLong = db.insert(ROAST_STEPS_TABLE_NAME, null, cv);
            Log.i("Insert", "Inserted " + step.getTime());
        }
        db.close();
        return returnLong;

    }

    public long insert(Roast roast) {
        long returnVal = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NAME, roast.getName());
        cv.put(DATE, roast.getDate());
        cv.put(NOTES, roast.getNotes());
        returnVal = db.insert(ROAST_TABLE_NAME, null, cv);
        int roastID = -1;
        String roastIdQuery = "select " + ID + " from " + ROAST_TABLE_NAME + " where " + NAME + " = '" + roast.getName() + "'";
        Cursor data = db.rawQuery(roastIdQuery, null);
        if (data.moveToFirst()) {
            roastID = data.getInt(data.getColumnIndex(ID));
        }
        if (data != null)
            data.close();
        db.close();
        Log.i("DB", "Roast id of new roast: " + roastID);

        insertBeans(roast, roastID);
        insertSteps(roast, roastID);
        return returnVal;
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
        if (data != null) {
            while (data.moveToNext()) {
                String roastName = data.getString(data.getColumnIndex(NAME));
                String date = data.getString(data.getColumnIndex(DATE));
                Roast newRoast = new Roast();
                newRoast.setRoastName(roastName);
                newRoast.setDateAdded(date);
                roasts.add(newRoast);
            }
        }
        if (data != null)
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
        if (data.moveToFirst()) {
            roast.setNotes(data.getString(data.getColumnIndex(NOTES)));
            roastID = data.getInt(data.getColumnIndex(ID));
            roast.setRoastName(name);
        }
        data.close();
        //Get all beans associated with roast
        queryString = "select * from " + BEANS_TABLE_NAME + " where " + ROAST_ID + " = " + roastID;
        data = db.rawQuery(queryString, null);
        if (data != null) {
            while (data.moveToNext()) {
                String beanName = data.getString(data.getColumnIndex(NAME));
                int weight = (int) data.getDouble(data.getColumnIndex(WEIGHT));
                Log.i("Bean", "Adding bean to Object: " + beanName);
                Bean newBean = new Bean(beanName, weight);
                roast.addToBeanList(newBean);
            }
        }
        if (data != null) {
            data.close();
        }
        //Get all steps
        queryString = "select * from " + ROAST_STEPS_TABLE_NAME + " where " + ROAST_ID + " = " + roastID;
        data = db.rawQuery(queryString, null);
        if (data != null) {
            while (data.moveToNext()) {
                String stepTime = data.getString(data.getColumnIndex(STEP_TIME));
                int temp = data.getInt(data.getColumnIndex(TEMP));
                int beanTemp = data.getInt(data.getColumnIndex(BEAN_TEMP));
                String comments = data.getString(data.getColumnIndex(NOTES));
                RoastStep step = new RoastStep(stepTime, temp, comments, beanTemp);
                roast.addToStepList(step);
            }
        }
        if (data != null) {
            data.close();
        }
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
                int coffeeMetric = data.getInt(data.getColumnIndex(COFFEE_METRIC));
                int waterMetric = data.getInt(data.getColumnIndex(WATER_METRIC));

                double coffeeUnits = data.getDouble(data.getColumnIndex(COFFEE_UNITS));
                double waterUnits = data.getDouble(data.getColumnIndex(WATER_UNITS));
                int brewTime = data.getInt(data.getColumnIndex(BREW_TIME));
                int bloomTime = data.getInt(data.getColumnIndex(BLOOM_TIME));
                String dateAdded = data.getString(data.getColumnIndex(DATE));

                BrewRecipe br = new BrewRecipe(name, brewMethod, grind, notes, coffeeUnits, waterUnits, coffeeMetric, waterMetric,
                        brewTime, bloomTime);
                br.setDateAdded(dateAdded);

                recipes.add(br);
            }

        }
        if (data != null) {
            data.close();
        }
        db.close();
        return recipes;
    }
    //TODO Query the database instead
    public BrewRecipe getBrewRecipe(String name) {
        BrewRecipe br = new BrewRecipe();
        ArrayList<BrewRecipe> brArray = getBrewRecipes();
        for (BrewRecipe recipe : brArray) {
            if (recipe.getName().equalsIgnoreCase(name))
                br = recipe;
        }
        return br;
    }


    public boolean deleteRoast(String roastName, String dateString) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = db.delete(ROAST_TABLE_NAME, NAME + " = '" + roastName + "' AND " + DATE + " = '" + dateString + "'", null) > 0;
        db.close();
        return success;
    }
}
