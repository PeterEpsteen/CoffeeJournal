package com.example.peter.coffeejournal;

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

    private static final int DATABASE_VERSION = 17;
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
    private static final String BEAN_TEMP = "bean_temp";

    private static final String DATE = "date";
    private static final String WEIGHT = "weight";
    private static final String MEASUREMENT = "measurement";
    private static final String STEP_TIME = "step_time";
    private static final String ROAST_ID = "roast_id";




    private static final String CREATE_BREW_TABLE = "create table if not exists " + BREW_TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT, " + BREW_METHOD + " TEXT, " +
            WATER_UNITS + " REAL, "+ COFFEE_UNITS +" REAL, "+ METRIC + " INTEGER, " + NOTES + " TEXT, " + DATE + " TEXT, " + GRIND + " TEXT, " + BLOOM_TIME + " INTEGER, " + BREW_TIME + " INTEGER, " + TEMP + " INTEGER)";
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
        brewMethodValues.put(WATER_UNITS, 300);
        brewMethodValues.put(COFFEE_UNITS, 20);
        brewMethodValues.put(METRIC, 1);
        brewMethodValues.put(NOTES, "    Dosage: 29 Grams for 440 mL (66g/L)\n" +
                "    Grind: Medium (or finer) (15-20/40)\n" +
                "    Temp: Off the boil (I brewed at 203°-205°)\n" +
                "    Pre-Infusion: Pre-wet and let sit for 45 seconds\n" +
                "    Total Time: N/A\n" +
                "\n" +
                "Method\n" +
                "\n" +
                "    Rinse Filter\n" +
                "    Grind and pour coffee into V60 shaking to settle grounds.\n" +
                "    Pre-infuse with water allowing it to sit 45 seconds\n" +
                "    Begin pouring from the center out clockwise in a circular motion until volume is reached.\n" +
                "\n" +
                "Tasting Notes\n" +
                "\n" +
                "    Light acidity for the duration of the sip\n" +
                "    Light orange peel sweetness\n" +
                "    Finishes clean with ripe berry (very similar to a naturally processed coffee)\n");
        brewMethodValues.put(GRIND, "Medium-Fine");
        brewMethodValues.put(BLOOM_TIME, 30);
        brewMethodValues.put(BREW_TIME, 180);
        brewMethodValues.put(TEMP, 200);  //NOT SURE
        brewMethodValues.put(DATE, dateAdded);
        db.insert(BREW_TABLE_NAME, null, brewMethodValues);

        brewMethodValues = new ContentValues();
        brewMethodValues.put(NAME, "French Press");
        brewMethodValues.put(BREW_METHOD, "French Press");
        brewMethodValues.put(WATER_UNITS, 400);
        brewMethodValues.put(COFFEE_UNITS, 27);
        brewMethodValues.put(METRIC, 1);
        brewMethodValues.put(NOTES, " 1. Start with delicious water...\n" +
                "\n" +
                "Coffee is 98 percent H2O, Moore says, so make sure your water tastes pretty darn good. Still, delicious doesn’t have to mean fancy. “If you want to use tap water, the real rule of thumb is to drink it first. If it tastes OK to you, go ahead and use it,” says Shawn Steiman, author of the forthcoming The Little Coffee Know-It-All. Tap leaving a bad taste in your mouth? Opt for filtered or bottle water instead.\n" +
                "2. ...and really, really fresh coffee beans.\n" +
                "\n" +
                "It probably goes without saying, but freshly roasted beans are key to a flavorful cuppa. “You don’t want beans that have been exposed to air for a long time. The beans should look a little oily and smell fresh and aromatic,” Moore says. Three ways to get your hands on them:\n" +
                "\n" +
                "    Flavor-sealed containers or bags. Unopened ones are airtight, so the beans inside will stay good until their expiration date (usually about 32 weeks after packaging). Once opened, though, the beans will start to lose flavor after about a week.\n" +
                "    Unsealed containers or bags. If you’re buying beans in a package that isn’t air-sealed (like a paper bag), look for the roast date. Anything roasted more than a month ago probably won’t be very fresh, Moore says.\n" +
                "    Bulk bins. Bulk coffee beans can be fresh—but the roast date isn’t always listed, and the beans are exposed to more air since people are constantly opening and closing the bins. If you’re not sure whether the bulk coffee you want to buy is fresh, speak up! “If you ask a purveyor when the coffee was roasted and they can answer with specific detail, there’s a good chance the coffee is decent,” Steiman says.\n" +
                "\n" +
                "3. Grind it up.\n" +
                "\n" +
                "You’ll get the best-flavored java by grinding your beans right before you brew them. “If you cut into an apple, it starts to oxidize and turn brown. Coffee beans do the same thing, you just can’t see it,” Moore explains. But watch that grind: The grounds used for drip coffee are way too small and risk clogging a French press filter. Instead, think big. “You want pieces the size of small breadcrumbs or a little smaller than Kosher salt,” Steiman says.\n" +
                "4. Break out the measuring cups.\n" +
                "Ground Coffee\n" +
                "\n" +
                "When it comes to the ideal coffee-to-water ratio, it depends how strong you want your brew. Between 18 and 20 grams of coffee (about a heaping tablespoon) per eight ounces of water is a good place to start, says coffee industry consultant Andrew Hetzel, who leads training courses for the Coffee Quality Institute. Like it bold? Moore recommends using two tablespoons of coffee per six ounces of water. “If it’s a little too strong, you can always add more hot water to your cup,” he adds.\n" +
                "5. Bring the water to the right temp.\n" +
                "\n" +
                "Too-hot agua can make for bitter joe, while water that isn’t hot enough means you miss out on flavor. Aim for a temperature of around 200 degrees Fahrenheit—but don’t bother breaking out a thermometer. “Bring the water to a boil and let it sit for 30 seconds,” Steiman says. (Told you this was easy.)\n" +
                "6. Fill ‘er up.\n" +
                "\n" +
                "Place the coffee grounds in the bottom of your French press, and pour about a third of the water over top. Let everything sit for about 30 seconds, then give it a gentle stir, Hetzel says. This makes sure all of the grounds are fully saturated with water so you get a flavor-packed brew. Add the rest of the water and place the lid on your carafe with the plunger pulled up all the way.\n" +
                "7. Start your timer.\n" +
                "\n" +
                "Again, it’s a balancing act. Brew too short, and your coffee will taste thin and sour. Too long, and it’ll end up bitter or astringent. For the best flavor, Hetzel and Moore recommend letting your coffee brew for four minutes. Though if you like your coffee stronger, you can let it go for as long as six, Steiman says.\n" +
                "8. Extract with care.\n" +
                "\n" +
                "When your timer buzzes, gently but firmly press the plunger all the way down. \n" +
                "9. Pour—and drink up.\n" +
                "\n" +
                "If you’re drip person, you’re probably used to brewing a big pot of coffee, pouring some into your mug, and letting the rest sit around all morning. But anything that’s left in a French press will keep brewing since the grounds and the water are still mingling in the carafe, and after a few minutes, it’ll turn bitter. 'If you have any leftovers, pour it into another mug or thermos. Or brew less next time,' Steiman says.");
        brewMethodValues.put(GRIND, "Course");
        brewMethodValues.put(BLOOM_TIME, 30);
        brewMethodValues.put(BREW_TIME, 240);
        brewMethodValues.put(TEMP, 201);  //NOT SURE
        brewMethodValues.put(DATE, dateAdded);
        db.insert(BREW_TABLE_NAME, null, brewMethodValues);

        brewMethodValues = new ContentValues();
        brewMethodValues.put(NAME, "Chemex");
        brewMethodValues.put(BREW_METHOD, "Chemex");
        brewMethodValues.put(WATER_UNITS, 600);
        brewMethodValues.put(COFFEE_UNITS, 40);
        brewMethodValues.put(METRIC, 1);
        brewMethodValues.put(NOTES, "One: Fill pouring kettle with hot filtered water and rinse filter until the glass is heated. Discard water from both.\n" +
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
        brewMethodValues.put(BLOOM_TIME, 30);
        brewMethodValues.put(BREW_TIME, 180);
        brewMethodValues.put(TEMP, 200);  //NOT SURE
        brewMethodValues.put(DATE, dateAdded);
        db.insert(BREW_TABLE_NAME, null, brewMethodValues);

        brewMethodValues = new ContentValues();
        brewMethodValues.put(NAME, "Espresso");
        brewMethodValues.put(BREW_METHOD, "Espresso");
        brewMethodValues.put(WATER_UNITS, 60);
        brewMethodValues.put(COFFEE_UNITS, 18);
        brewMethodValues.put(METRIC, 1);
        brewMethodValues.put(NOTES, "\n" +
                "\n" +
                "WATER: Your espresso will taste only as good as the water you start with. Sediment, scale, and unwelcome minerals will doom your drink and your equipment if they're not dealt with up front, so, before you get too far, learn about the quality of your water. Most hardware stores have inexpensive water test kits available for purchase but you can also contact your local water source for details about what they pump to your pipes. With that information fresh in hand, check out the Specialty Coffee Association of America's water standards . If your H2O is off-the-charts funky, give us a call and we'll walk you through some water treatment solutions. And no matter your situation, you can keep out a lot of nasty stuff with a simple carbon filter, like that in a Brita pitcher.\n" +
                "Espresso coffee grind\n" +
                "\n" +
                "GRIND: Before brewing, coffee beans need to be cut into smaller pieces. Making espresso requires a finer grind than most methods, with particles around the size of table salt. You know you're in the right neighborhood once the ground coffee begins to clump together. Later, you'll learn how to manipulate the grind to achieve different results.\n" +
                "\n" +
                "DOSE: For a \"double shot\" – the standard serving size – we prefer to use between 18 and 21 grams of ground coffee. As you add more coffee, your shot will increase in both body and intensity. Feel free to adjust your dose according to taste and make use of the troubleshooting tips below.\n" +


                "TAMP: Compacting ground coffee with a tamper restricts the flow of water, forcing coffee and water to interact. Start with a 30-pound press (your bathroom scale can tell you what this feels like), applied evenly. A firm, level tamp is essential to even extraction.\n" +
                "\n" +
                "TEMP: Water heated to 195-205ºF is ideal for preparing coffee, and some espresso machines allow you to control this temperature. (For most systems, this is made possible by a \"PID controller\".) If yours does, play within this range to find what you like. You'll notice that lower temperatures draw out more brightness, while cranking up the heat produces roasty flavors. If you're not able to choose the temperature for yourself, you can assume for now that the machine is doing its job.\n" +
                "Espresso shot yield\n" +
                "\n" +
                "YIELD: With brewed coffee, we measure coffee input and water input, but when making espresso it's coffee input and beverage output. Depending on your dose and basket size, shoot for about 2 ounces of espresso out, enough to fill a large shot glass. If you're weighing your shots, a 30-gram yield is a safe place to start.\n" +
                "\n" +
                "Note: The density of espresso can be a tricky thing, as the gasses trapped in the crema can make for fluffy, heady shots that only weigh 30 grams, or thin and silky shots that weigh 60. With many espresso blends, you should have a decent cap of crema - say, a 1/2 inch or so - and a total mass of about 30-40 grams for a 2-ounce shot. But some coffees will skew one way or the other, leading to less dense crema-bombs or denser, juicier single origin marvels. Whichever way you like your espresso, our reference points of 2 ounces or 30 grams are merely places to start, so feel free to make adjustments until you're doing a happy little dance after every shot!\n" +
                "\n" +
                "TIME: With our recommended dose and yield, about 25-30 seconds should pass between the beginning of extraction and the moment your glass is full. Half a minute for a happy tongue? Not bad.\n");
        brewMethodValues.put(GRIND, "Extra-Fine");
        brewMethodValues.put(BLOOM_TIME, 30);
        brewMethodValues.put(BREW_TIME, 180);
        brewMethodValues.put(TEMP, 200);  //NOT SURE
        brewMethodValues.put(DATE, dateAdded);
        db.insert(BREW_TABLE_NAME, null, brewMethodValues);

        brewMethodValues = new ContentValues();
        brewMethodValues.put(NAME, "Kalita Wave");
        brewMethodValues.put(BREW_METHOD, "Pour Over");
        brewMethodValues.put(WATER_UNITS, 300);
        brewMethodValues.put(COFFEE_UNITS, 21);
        brewMethodValues.put(METRIC, 1);
        brewMethodValues.put(NOTES, "Rinse filter (with boiling water) by pouring directly into middle of filter. Pouring on the sides can compromise the shape of the “wave” filter. Rinsing the filter removes any paper taste of the filter and the hot water will preheat the Kalita. \n" +

                "\n" +
                "Weigh out about 27 grams of coffee to be ground. Fill kettle and heat water. Pro tip: weigh out slightly more coffee than will be used, often a gram or two is lost in the grinder. \n" +

                "STEP 2\n" +
                "\n" +
                "GRIND AND WEIGH\n" +
                "\n" +
                "Grind coffee. The particle size should be just finer than sea salt. Weigh 25 grams of ground coffee into Kalita and tare scale. Give it a gentle tap to flatten coffee bed. This will help to even the water distribution when pouring. \n" +

                "STEP 3\n" +
                "\n" +
                "BREW \n" +
                "\n" +
                "Just after boiling, remove kettle and let water settle. Start timer and pour enough water to just saturate coffee bed. Let bloom, or expand for 30 seconds. Allowing the coffee to bloom ensures even water dispersion and a delicious cup. Pro tip: try to use only 25-30 grams of water to cover all coffee. This will leave you with the correct amount of water to complete the brewing on time.\n" +


                "Pouring in slow concentric circles, add enough water to raise slurry to about halfway up the side of filter. Continue adding water slowly in stages (also called pulse brewing), submerging the crust as you go and letting the slurry drop a little before adding water to bring it back to the same level. \n" +

                "\n" +
                "Try to add all water by 2:45-3:00. Once you’ve added 400 grams of water, give it a little stir if needed and let drain. You've done it right if the coffee bed is flat after draining. Your total brew time should be 3:30-3:50, depending on the coffee.\n" +

                "Step 4\n" +
                "\n" +
                "SERVE AND ENJOY!\n" +
                "\n" +
                "Remove filter and serve. Most importantly, enjoy!\n" +

                "\n" +
                "With any pour-over, the water level will greatly affect how your extraction progresses. Keeping your level low will slow down the flow rate as well as allow you to maintain a more constant temperature by adding water a little at a time. A high level will drain faster and dissipate heat more quickly. A good barista will be able to manipulate their water level to achieve the desired flow rate and overall contact time. In other words, if you’re making a smaller batch, take care not to add too much water too quickly as you’ll finish before your target time and end up with under extracted coffee. If you’re making a larger batch and you find things are going too slowly, you can add water faster to speed things up.");
        brewMethodValues.put(GRIND, "Medium");
        brewMethodValues.put(BLOOM_TIME, 30);
        brewMethodValues.put(BREW_TIME, 180);
        brewMethodValues.put(TEMP, 200);  //NOT SURE
        brewMethodValues.put(DATE, dateAdded);
        db.insert(BREW_TABLE_NAME, null, brewMethodValues);

        brewMethodValues = new ContentValues();
        brewMethodValues.put(NAME, "Aeropress");
        brewMethodValues.put(BREW_METHOD, "Aeropress");
        brewMethodValues.put(WATER_UNITS, 300);
        brewMethodValues.put(COFFEE_UNITS, 21);
        brewMethodValues.put(METRIC, 1);
        brewMethodValues.put(NOTES, "Winner: 2017 World AeroPress Championship\n" +
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
        brewMethodValues.put(GRIND, "Medium");
        brewMethodValues.put(BLOOM_TIME, 30);
        brewMethodValues.put(BREW_TIME, 180);
        brewMethodValues.put(TEMP, 200);  //NOT SURE
        brewMethodValues.put(DATE, dateAdded);
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
        cv.put(DATE, br.getDateAdded());
        long returnLong = db.insert(BREW_TABLE_NAME, null, cv);
        if (returnLong > 0) {
            Log.i("DB", "Succesfully inserted brew name: " + br.getName());
        }
        db.close();
        return returnLong;

    }

    public long update(BrewRecipe br, String editBrewName, String editBrewDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        long returnLong = -1;
        String whereClause = NAME + " = '" + editBrewName + "' AND " + DATE + " = '" + editBrewDate + "'";
        if(db.delete(BREW_TABLE_NAME, whereClause, null) > 0) {
            Log.i("DB", "Deleted old brew name: " + editBrewName);
        }
        else {
            Log.e("DB", "Error deleting old brew");
            return -1;
        }
        db.close();
        return insert(br);
    }

    public long update(Roast r) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NAME, r.getName());
        cv.put(NOTES, r.getNotes());
        cv.put(DATE, r.getDate());
        String whereClause =  DATE + " = '" + r.getDate() + "'";
        long returnLong = db.update(ROAST_TABLE_NAME, cv, whereClause, null);
        int roastID = -1;
        String roastIdQuery = "select "+ ID + " from " + ROAST_TABLE_NAME + " where " + DATE + " = '" + r.getDate() + "'";
        Cursor data = db.rawQuery(roastIdQuery, null);
        if (data.moveToFirst()){
            roastID = data.getInt(data.getColumnIndex(ID));
        }
        if(data != null)
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
        if(db.delete(BEANS_TABLE_NAME, whereClause, null) > 0) {
            Log.i("DB", "Deleted old beans");
        }
        ContentValues cv = new ContentValues();
        List<Bean> beanArrayList = r.getBeanList();
        for(Bean bean : beanArrayList) {
            cv = new ContentValues();
            cv.put(NAME, bean.getBeanName());
            cv.put(WEIGHT, bean.getBeanWeight());
            cv.put(ROAST_ID, roastID);
            returnLong = db.insert(BEANS_TABLE_NAME, null, cv);
            Log.i("Insert", "Inserted " + bean.getBeanName() +"with roast id: " + roastID + " Return value is: " + returnLong);
        }
        db.close();
        return returnLong;
    }

    private long updateSteps(Roast r, int roastID) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = ROAST_ID + " = " + roastID;
        if(db.delete(ROAST_STEPS_TABLE_NAME, whereClause, null) > 0) {
            Log.i("DB", "Deleted old steps");
        }
        long returnLong = -1;
        List<RoastStep> stepList = r.getStepList();
        for(RoastStep step : stepList) {
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
        for(Bean bean : beanArrayList) {
            cv = new ContentValues();
            cv.put(NAME, bean.getBeanName());
            cv.put(WEIGHT, bean.getBeanWeight());
            cv.put(ROAST_ID, roastID);
            returnLong = db.insert(BEANS_TABLE_NAME, null, cv);
            Log.i("Insert", "Inserted " + bean.getBeanName() +"with roast id: " + roastID + " Return value is: " + returnLong);
        }
        db.close();
        return returnLong;

    }



    private long insertSteps(Roast r, int roastID) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        long returnLong = -1;
        List<RoastStep> stepList = r.getStepList();
        for(RoastStep step : stepList) {
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
        db.insert(ROAST_TABLE_NAME, null, cv);
        int roastID = -1;
        String roastIdQuery = "select "+ ID + " from " + ROAST_TABLE_NAME + " where " + NAME + " = '" + roast.getName() + "'";
        Cursor data = db.rawQuery(roastIdQuery, null);
        if (data.moveToFirst()){
            roastID = data.getInt(data.getColumnIndex(ID));
        }
        if(data != null)
            data.close();
        db.close();
        Log.i("DB", "Roast id of new roast: "+ roastID);

        insertBeans(roast, roastID);
        insertSteps(roast, roastID);
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
            roast.setRoastName(name);
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
                int beanTemp = data.getInt(data.getColumnIndex(BEAN_TEMP));
                String comments = data.getString(data.getColumnIndex(NOTES));
                RoastStep step = new RoastStep(stepTime, temp, comments, beanTemp);
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
                String dateAdded = data.getString(data.getColumnIndex(DATE));

                BrewRecipe br = new BrewRecipe(name, brewMethod, grind, notes, coffeeUnits, waterUnits, metric,
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

    public BrewRecipe getBrewRecipe(String name) {
        BrewRecipe br = new BrewRecipe();
        ArrayList<BrewRecipe> brArray = getBrewRecipes();
        for (BrewRecipe recipe: brArray) {
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
