package com.example.peter.coffeejournal;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

public class AddBrew extends AppCompatActivity implements View.OnClickListener {

    BrewRecipe br;
    String name, brewMethod, grind, notes;
    int ratio, brewTime, bloomTime, metric, icon;
    DBOperator db;
    double waterUnits, coffeeUnits;

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

        Button btn = findViewById(R.id.add_brew_button);
        btn.setOnClickListener(this);

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
        //Initialize all settings for BrewRecipe obj
        //Get all edit texts
        EditText nameEdit = findViewById(R.id.recipe_name_edit);
        EditText grindEdit = findViewById(R.id.grind_edit);
        EditText notesEdit = findViewById(R.id.notes_edit);
        SwitchCompat metricEdit = findViewById(R.id.metric_edit);
        EditText coffeeEdit = findViewById(R.id.coffee_amount_edit);
        EditText waterEdit = findViewById(R.id.water_amount_edit);
        EditText brewTimeEdit = findViewById(R.id.brew_time_edit);
        EditText bloomTimeEdit = findViewById(R.id.bloom_time_edit);

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;

        name = nameEdit.getText().toString();

        if(name.equals("")) {
            CharSequence text = "Please enter a valid brew name.";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
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
                CharSequence text = "Please enter amount of coffee.";
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                return;
            }
            if(waterUnitsString.equals("")) {
                CharSequence text = "Please enter amount of water.";
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
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
