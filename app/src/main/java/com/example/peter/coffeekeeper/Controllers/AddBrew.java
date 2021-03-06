package com.example.peter.coffeekeeper.Controllers;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peter.coffeekeeper.Database.DBOperator;
import com.example.peter.coffeekeeper.Models.BrewRecipe;
import com.example.peter.coffeekeeper.R;

public class AddBrew extends AppCompatActivity {

    BrewRecipe br;
    String name, brewMethod, grind, notes, editBrewName, editBrewDate;
    int ratio, brewTime, bloomTime, icon;
    DBOperator db;
    double waterUnits, coffeeUnits;
    SwitchCompat coffeeMetricSwitch, waterMetricSwitch;
    TextView dynamicWaterUnitsTv, dynamicCoffeeUnitsTv;
    ScrollView sv;
    EditText nameEdit;
    EditText grindEdit;
    EditText notesEdit;
    EditText coffeeEdit;
    EditText waterEdit;
    EditText brewTimeEditMinutes, brewTimeEditSeconds;
    EditText bloomTimeEditMinutes, bloomTimeEditSeconds;
    TextInputLayout nameTI;
    TextInputLayout coffeeWeightTI;
    TextInputLayout waterWeightTI;
    TextView nameTextView;
    Button addButton;

    public static final int CHANGED_BREW_DB_DATA = 1;
    public static final int INSERTED_BREW = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //create brewRecipe
        //Add to DB

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_brew);
        Intent myIntent = getIntent();
        editBrewName = myIntent.getStringExtra("Brew Name");

        android.support.v7.widget.Toolbar mtoolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create a Brew");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sv = findViewById(R.id.main_scroll_view);


        //Initialize all settings for BrewRecipe obj
        //Get all edit texts
         nameEdit = findViewById(R.id.recipe_name_edit);
         grindEdit = findViewById(R.id.grind_edit);
         notesEdit = findViewById(R.id.notes_edit);
         coffeeEdit = findViewById(R.id.coffee_amount_edit);
         waterEdit = findViewById(R.id.water_amount_edit);
         brewTimeEditMinutes = findViewById(R.id.brew_time_edit_minutes);
        brewTimeEditSeconds = findViewById(R.id.brew_time_edit_seconds);
        bloomTimeEditMinutes = findViewById(R.id.bloom_time_edit_minutes);
        bloomTimeEditSeconds = findViewById(R.id.bloom_time_edit_seconds);

        nameEdit.requestFocus();

        bloomTimeEditMinutes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (bloomTimeEditMinutes.getText().toString().length() == 2) {
                    bloomTimeEditSeconds.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        brewTimeEditMinutes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(brewTimeEditMinutes.getText().toString().length() == 2) {
                    brewTimeEditSeconds.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        nameTI = findViewById(R.id.name_text_input_layout);
         coffeeWeightTI = findViewById(R.id.coffee_weight_textinput);
         waterWeightTI = findViewById(R.id.water_weight_textinput);
        nameTextView = findViewById(R.id.recipe_name_tv);

         dynamicWaterUnitsTv = findViewById(R.id.dynamic_water_units_text_view);
        dynamicCoffeeUnitsTv = findViewById(R.id.dynamic_coffee_units_text_view);
        coffeeMetricSwitch = findViewById(R.id.coffee_metric_switch);
        waterMetricSwitch = findViewById(R.id.water_metric_switch);
        coffeeMetricSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dynamicCoffeeUnitsTv.setText("Oz");
                }

                else {
                    dynamicCoffeeUnitsTv.setText("G");
                }
            }
        });
        waterMetricSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    dynamicWaterUnitsTv.setText("Oz");
                else
                    dynamicWaterUnitsTv.setText("G");
            }
        });

        Spinner spinner = findViewById(R.id.brew_method_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.brew_methods_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.my_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                brewMethod = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        addButton = findViewById(R.id.add_brew_button);

        if (editBrewName != null) {
            actionBar.setTitle("Edit Brew");
            addButton.setText("Save Edit");
            DBOperator dbOperator = new DBOperator(this);
            BrewRecipe editBrew = dbOperator.getBrewRecipe(editBrewName);
            if (editBrew.getBrewMethod() != null) {
                nameEdit.setText(editBrew.getName());
                grindEdit.setText(editBrew.getGrind());
                notesEdit.setText(editBrew.getNotes());
                coffeeEdit.setText(String.valueOf(editBrew.getCoffeeUnits()));
                waterEdit.setText(String.valueOf(editBrew.getWaterUnits()));
                if (!editBrew.isCoffeeMetric()) {
                    coffeeMetricSwitch.setChecked(true);
                }
                if (!editBrew.isWaterMetric())
                    waterMetricSwitch.setChecked(true);
                int brewTimeInt = editBrew.getBrewTime();
                int mins = brewTimeInt / 60;
                int secs = brewTimeInt % 60;
                brewTimeEditSeconds.setText(Integer.toString(secs));
                brewTimeEditMinutes.setText(Integer.toString(mins));
                int bloomTimeInt = editBrew.getBloomTime();
                int minsBloom = bloomTimeInt / 60;
                int secsBloom = bloomTimeInt % 60;
                bloomTimeEditMinutes.setText(Integer.toString(minsBloom));
                bloomTimeEditSeconds.setText(Integer.toString(secsBloom));
                String method = editBrew.getBrewMethod();
                switch (method) {
                    case "Pour Over":
                        spinner.setSelection(0);
                        break;
                    case "Aeropress":
                        spinner.setSelection(1);
                        break;
                    case "French Press":
                        spinner.setSelection(2);
                        break;
                    case "Chemex":
                        spinner.setSelection(3);
                        break;
                    case "Espresso":
                        spinner.setSelection(4);
                        break;
                    case "Moka Pot":
                        spinner.setSelection(5);
                        break;
                    case "Other":
                        spinner.setSelection(6);
                        break;
                }
            }
            else {
                Toast myToast = Toast.makeText(getApplicationContext(), "No Roast Found", Toast.LENGTH_LONG);
                myToast.show();
            }



            if (editBrewName != null) {
                addButton.setText("Save");
            }

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    public void insertBrew(View v) {

        Log.i("DB", "Attempting to insert Brew");

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;

        db = new DBOperator(this);


        name = nameEdit.getText().toString();


        if(name.equals("") || name.isEmpty()) {
            //Request focus to text view and pop keyboard up
            sv.scrollTo(nameEdit.getLeft(),nameEdit.getTop());
            nameEdit.requestFocus();
            nameTI.setError("Brew name required!");
            nameEdit.postDelayed(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager keyboard = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    keyboard.showSoftInput(nameEdit, 0);
                }
            },200);
        }

        else if(db.getBrewNames().contains(name) && editBrewName == null) {
            Toast myToast = Toast.makeText(this, "Brew name already exists. Please enter a unique name.", Toast.LENGTH_LONG);
            myToast.show();
        }

        else {
            //Spinner selection is handled in onItemSelected above
            grind = grindEdit.getText().toString();
            notes = notesEdit.getText().toString();
            int coffeeMetric = (coffeeMetricSwitch.isChecked()) ? 0 : 1;
            int waterMetric = (waterMetricSwitch.isChecked()) ? 0 : 1;

            String coffeeUnitsString = coffeeEdit.getText().toString();
            String waterUnitsString = waterEdit.getText().toString();
            if(coffeeUnitsString.equals("")) {
                coffeeWeightTI.setError("Coffee weight required!");
                coffeeEdit.requestFocus();
                coffeeEdit.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager keyboard = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        keyboard.showSoftInput(coffeeEdit, 0);
                    }
                },200);
                return;
            }
            if(waterUnitsString.equals("")) {
                waterEdit.requestFocus();
                waterWeightTI.setError("Water weight required!");
                waterEdit.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager keyboard = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        keyboard.showSoftInput(waterEdit, 0);
                    }
                },200);
                return;
            }

            coffeeUnits = Double.parseDouble(coffeeEdit.getText().toString());
            waterUnits = Double.parseDouble(waterEdit.getText().toString());

            brewTime = 0;
            String brewTimeStringSecs = brewTimeEditSeconds.getText().toString();
            String brewTimeStringMins = brewTimeEditMinutes.getText().toString();
            int brewmins = 0;
            int brewsecs = 0;

            if(!brewTimeStringMins.equals("")) {
                brewmins = Integer.parseInt(brewTimeStringMins);
            }

            if(!brewTimeStringSecs.equals("")) {
                brewsecs = Integer.parseInt(brewTimeStringSecs);
            }

            brewTime = (brewmins * 60) + brewsecs;

            bloomTime = 0;
            String bloomTimeStringSecs = bloomTimeEditSeconds.getText().toString();
            String bloomTimeStringMins = bloomTimeEditMinutes.getText().toString();
            int bloommins = 0;
            int bloomsecs = 0;

            if(!bloomTimeStringMins.equals("")) {
                bloommins = Integer.parseInt(bloomTimeStringMins);
            }

            if(!bloomTimeStringSecs.equals("")) {
                bloomsecs = Integer.parseInt(bloomTimeStringSecs);
            }

            bloomTime = (bloommins * 60) + bloomsecs;





            //Create brewRecipe, open db and insert
            br = new BrewRecipe(name, brewMethod, grind, notes, coffeeUnits, waterUnits, coffeeMetric, waterMetric, brewTime, bloomTime);

            Toast myToast = Toast.makeText(getApplicationContext(), "Error inserting or updating", duration);


            if (editBrewName == null) {
            long rows = db.insert(br);
            if (rows != -1)
                Log.i("db", "Sucessfully inserted rows: " + rows);
            setResult(CHANGED_BREW_DB_DATA);
            finish();
            }

            else {
                long rows = db.update(br, editBrewName);
                if (rows != -1) {
                    Log.i("db", "Sucessfully inserted rows: " + rows);
                    setResult(CHANGED_BREW_DB_DATA);
                    finish();
                }
                else {
                    myToast.show();
                }
            }
            db.close();

        }

    }

}
