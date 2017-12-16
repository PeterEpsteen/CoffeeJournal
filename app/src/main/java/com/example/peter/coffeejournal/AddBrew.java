package com.example.peter.coffeejournal;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
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

public class AddBrew extends AppCompatActivity implements View.OnClickListener {

    BrewRecipe br;
    String name, brewMethod, grind, notes;
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
    EditText brewTimeEdit;
    EditText bloomTimeEdit;
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
         brewTimeEdit = findViewById(R.id.brew_time_edit);
         bloomTimeEdit = findViewById(R.id.bloom_time_edit);
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
    }

    @Override
    public void onClick(View v) {



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
            String timeString = brewTimeEdit.getText().toString();

            if(!timeString.equals("")) {
                String[] array1 = timeString.split(":");
                if (array1.length > 1 && !array1[0].equals("")) {
                    brewTime = Integer.parseInt(array1[0]) * 60 + Integer.parseInt(array1[1]);
                } else if (array1.length > 1 && array1[0].equals("")) {
                    bloomTime = Integer.parseInt(array1[1]);
                } else {
                    brewTime = Integer.parseInt(array1[0]);
                }
            }

            bloomTime = 0;
            String bloomTimeString = bloomTimeEdit.getText().toString();
            if (!bloomTimeString.equals("")) {
                String[] array2 = bloomTimeString.split(":");
                if (array2.length > 1 && !array2[0].equals("")) {
                    bloomTime = Integer.parseInt(array2[0]) * 60 + Integer.parseInt(array2[1]);
                } else if (array2.length > 1 && array2[0].equals("")) {
                    bloomTime = Integer.parseInt(array2[1]);
                } else {
                    bloomTime = Integer.parseInt(array2[0]);
                }
            }

            //Create brewRecipe, open db and insert
            br = new BrewRecipe(name, brewMethod, grind, notes, coffeeUnits, waterUnits, metric, brewTime, bloomTime);
            db = new DBOperator(this);
            long rows = db.insert(br);
            if (rows != -1)
                Log.i("db", "Sucessfully inserted rows: " + rows);
            Intent myIntent = new Intent(this, MainActivity.class);
            startActivity(myIntent);
            finish();

        }
    }
}
