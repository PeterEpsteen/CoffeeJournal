package com.example.peter.coffeejournal;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.w3c.dom.Text;

import java.util.regex.Pattern;

public class AddBrew extends AppCompatActivity implements View.OnClickListener {

    BrewRecipe br;
    String name, brewMethod, grind, notes, editBrewName;
    int ratio, brewTime, bloomTime, metric, icon;
    DBOperator db;
    double waterUnits, coffeeUnits;
    SwitchCompat metricEdit;
    TextView dynamicWaterUnitsTv, dynamicCoffeeUnitsTv;
    NestedScrollView nsv;
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

        nsv = findViewById(R.id.nested_scrollview);
        Button btn = findViewById(R.id.add_brew_button);
        btn.setOnClickListener(this);

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
        metricEdit = findViewById(R.id.metric_edit);
        metricEdit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dynamicCoffeeUnitsTv.setText("Oz");
                    dynamicWaterUnitsTv.setText("Oz");
                }

                else {
                    dynamicWaterUnitsTv.setText("G");
                    dynamicCoffeeUnitsTv.setText("G");
                }
            }
        });

        Spinner spinner = findViewById(R.id.brew_method_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.brew_methods_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.my_simple_spinner_dropdown_item);
// Apply the adapter to the spinner
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

        if (editBrewName != null) {
            actionBar.setTitle("Edit Brew");
            btn.setText("Save Edit");
            DBOperator dbOperator = new DBOperator(this);
            BrewRecipe editBrew = dbOperator.getBrewRecipe(editBrewName);
            nameEdit.setText(editBrew.getName());
            grindEdit.setText(editBrew.getGrind());
            notesEdit.setText(editBrew.getNotes());
            coffeeEdit.setText(String.valueOf(editBrew.getCoffeeUnits()));
            waterEdit.setText(String.valueOf(editBrew.getWaterUnits()));
            if(!editBrew.isMetric()) {
                metricEdit.setChecked(true);
            }
            int brewTimeInt = editBrew.getBrewTime();
            int mins = brewTimeInt/60;
            int secs = brewTimeInt % 60;
            brewTimeEditSeconds.setText(Integer.toString(secs));
            brewTimeEditMinutes.setText(Integer.toString(mins));
            int bloomTimeInt = editBrew.getBloomTime();
            int minsBloom = bloomTimeInt/60;
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
    }

    @Override
    public void onClick(View v) {insertBrew();}

    public void insertBrew() {

        Log.i("DB", "Attempting to insert Brew");

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;

        name = nameEdit.getText().toString();

        if(name.equals("")) {
            //Request focus to text view and pop keyboard up
            nsv.scrollTo(0,0);
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
        else {
            //Spinner selection is handled in onItemSelected above
            grind = grindEdit.getText().toString();
            notes = notesEdit.getText().toString();
            if (metricEdit.isChecked())
                metric = 0;
            else
                metric = 1;
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
            br = new BrewRecipe(name, brewMethod, grind, notes, coffeeUnits, waterUnits, metric, brewTime, bloomTime);
            db = new DBOperator(this);

            Toast myToast = Toast.makeText(getApplicationContext(), "Error inserting or updating", duration);


            if (editBrewName == null) {
            long rows = db.insert(br);
            if (rows != -1)
                Log.i("db", "Sucessfully inserted rows: " + rows);
            finish();
            }

            if (editBrewName != null && !editBrewName.equals(name)) {
                long rows = db.insert(br);
                if (rows != -1)
                    Log.i("db", "Sucessfully inserted rows: " + rows);
                else
                    myToast.show();

                finish();
            }

            else {
                long rows = db.update(br);
                if (rows != -1) {
                    Log.i("db", "Sucessfully inserted rows: " + rows);
                    finish();
                }
                else {
                    myToast.show();
                }
            }
            db.close();

        }

    }

    private static final String TIME24HOURS_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]";


}
