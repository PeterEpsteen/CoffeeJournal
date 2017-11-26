package com.example.peter.coffeejournal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddBrew extends AppCompatActivity implements View.OnClickListener {

    BrewRecipe br;
    String name, brewMethod, grind, notes;
    int ratio, brewTime, bloomTime, metric,icon;
    DBOperator db;
    double waterUnits, coffeeUnits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //create brewRecipe
        //Add to DB

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_brew);

        Button btn = findViewById(R.id.add_brew_button);
        btn.setOnClickListener(this);

        Spinner spinner = (Spinner) findViewById(R.id.brew_method_spinner);
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
        EditText nameEdit = (EditText) findViewById(R.id.recipe_name_edit);
        name = nameEdit.getText().toString();
        //Spinner selection is handled in onItemSelected above
        EditText grindEdit = (EditText) findViewById(R.id.grind_edit);
        grind = grindEdit.getText().toString();
        EditText notesEdit = (EditText) findViewById(R.id.notes_edit);
        notes = notesEdit.getText().toString();
        EditText metricEdit = (EditText) findViewById(R.id.metric_edit);
        metric =Integer.parseInt(metricEdit.getText().toString());
        EditText coffeeEdit = (EditText) findViewById(R.id.coffee_amount_edit);
        coffeeUnits =Double.parseDouble(coffeeEdit.getText().toString());
        EditText waterEdit = (EditText) findViewById(R.id.water_amount_edit);
        waterUnits =Double.parseDouble(waterEdit.getText().toString());
        EditText brewTimeEdit = (EditText) findViewById(R.id.brew_time_edit);
        brewTime =Integer.parseInt(brewTimeEdit.getText().toString());
        EditText bloomTimeEdit = (EditText) findViewById(R.id.bloom_time_edit);
        bloomTime = Integer.parseInt(bloomTimeEdit.getText().toString());
        //Create brewRecipe, open db and insert
        br = new BrewRecipe(name, brewMethod, grind, notes, coffeeUnits, waterUnits, metric, brewTime, bloomTime);
        db = new DBOperator(this);
        long rows = db.insert(br);
        if(rows != -1)
            Log.i("db", "Sucessfully inserted rows: " + rows);
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
        finish();


        //Update main activity and send back to it






    }
}
