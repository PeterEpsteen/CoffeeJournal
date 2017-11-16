package com.example.peter.coffeejournal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddBrew extends AppCompatActivity implements View.OnClickListener {

    BrewRecipe br;
    String name, brewMethod, grind, notes;
    int ratio, brewTime, bloomTime, icon;
    DBOperator db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //create brewRecipe
        //Add to DB

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_brew);

        Button btn = findViewById(R.id.add_brew_button);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        EditText nameEdit = (EditText) findViewById(R.id.recipe_name_edit);
        name = nameEdit.getText().toString();
        Spinner brewMethodEdit = (Spinner) findViewById(R.id.brew_method_spinner);
        //Get spinner Value
        EditText grindEdit = (EditText) findViewById(R.id.grind_edit);
        grind = grindEdit.getText().toString();
        EditText notesEdit = (EditText) findViewById(R.id.notes_edit);
        notes = notesEdit.getText().toString();
        EditText ratioEdit = (EditText) findViewById(R.id.ratio_edit);
        ratio =Integer.parseInt(ratioEdit.getText().toString());
        EditText brewTimeEdit = (EditText) findViewById(R.id.brew_time_edit);
        brewTime =Integer.parseInt(brewTimeEdit.getText().toString());
        EditText bloomTimeEdit = (EditText) findViewById(R.id.bloom_time_edit);
        bloomTime = Integer.parseInt(bloomTimeEdit.getText().toString());

        br = new BrewRecipe(name, brewMethod, grind, notes, ratio, brewTime, bloomTime);
        db = new DBOperator(this);
        db.insert(br);




        //TODO
        // Keep getting info from settings, create a brew recipe then add to db
    }
}
