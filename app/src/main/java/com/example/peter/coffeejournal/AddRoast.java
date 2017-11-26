package com.example.peter.coffeejournal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddRoast extends AppCompatActivity implements View.OnClickListener {

    Button addButton, addStepButton, addBeansButton;
    Roast roast;
    String roastName, roastDate;
    EditText roastNameEdit, roastDateEdit;
    DBOperator db;
    LinearLayout stepsContainer, beansContainer;
    List<LinearLayout> beanRowLinearLayoutList;
    List<LinearLayout> stepsRowLinearLayoutList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_roast);
        beanRowLinearLayoutList = new ArrayList<LinearLayout>();
        stepsRowLinearLayoutList = new ArrayList<LinearLayout>();
        addButton = findViewById(R.id.add_roast_button);
        roastNameEdit = findViewById(R.id.roast_name_edit);
        addStepButton = findViewById(R.id.add_step_row_button);
        addBeansButton = findViewById(R.id.add_bean_row_button);
        stepsContainer = findViewById(R.id.steps_linear_layout);
        beansContainer = findViewById(R.id.beans_linear_layout);
        LinearLayout firstStepRow = stepsContainer.findViewById(R.id.first_steps_row);
        LinearLayout firstBeanRow = beansContainer.findViewById(R.id.first_bean_row);
        beanRowLinearLayoutList.add(firstBeanRow);
        stepsRowLinearLayoutList.add(firstStepRow);
        addBeansButton.setOnClickListener(this);
        addStepButton.setOnClickListener(this);
        addButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        LayoutInflater inflater = getLayoutInflater();
        switch (v.getId()) {
            case (R.id.add_roast_button):
                addRoast();
                break;
            case (R.id.add_bean_row_button):
                LinearLayout beanRow = (LinearLayout) inflater.inflate(R.layout.add_beans_row, null, false);
                beansContainer.addView(beanRow);
                beanRowLinearLayoutList.add(beanRow);
                Log.i("Row", "Adding bean row...");
                break;
            case (R.id.add_step_row_button):
                LinearLayout stepRow = (LinearLayout) inflater.inflate(R.layout.add_tempature_row, null, false);
                stepsContainer.addView(stepRow);
                stepsRowLinearLayoutList.add(stepRow);
                Log.i("Row", "Adding temp row...");
                break;

        }
    }

    public Roast addRoast() {
        //first, set date
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/YY HH:mm");
        Date date = new Date();
        roastDate = dateFormat.format(date);
        //get name and create a new roast
        roastName = roastNameEdit.getText().toString();
        roast = new Roast(roastName, roastDate, 1, 0);
        //add all bean rows to roast
        for (LinearLayout row : beanRowLinearLayoutList) {
            Log.i("BeanRow", "Bean Row: " + row.toString());
            EditText beanNameEditText = row.findViewById(R.id.bean_name_edit_text);
            String beanName = beanNameEditText.getText().toString();
            EditText beanWeightEditText = row.findViewById(R.id.bean_weight_edit_text);
            int beanWeight = Integer.parseInt(beanWeightEditText.getText().toString());
            if (!beanName.equals("")){
                Log.i("Bean", "Adding bean name: " + beanName);
                Bean newBean = new Bean(beanName, beanWeight);
                roast.addToBeanList(newBean);
            }
        }

        //add steps to roast
        for (LinearLayout row : stepsRowLinearLayoutList) {
            EditText stepTimeEditText = row.findViewById(R.id.time_edit_text);
            String stepTime = stepTimeEditText.getText().toString();
            EditText stepTempEditText = row.findViewById(R.id.temp_edit_text);
            int stepTemp = Integer.parseInt(stepTempEditText.getText().toString());
            EditText commentsEdit = row.findViewById(R.id.comments_edit_text);
            String comments = commentsEdit.getText().toString();
            if (!stepTime.equals("")){
                Log.i("Bean", "Adding step time: " + stepTime);
                RoastStep newStep = new RoastStep(stepTime, stepTemp, comments);
                roast.addToStepList(newStep);
            }
        }

        Log.i("Roast", roast.toString());

        EditText commentsEdit = findViewById(R.id.tasting_notes_edit);
        String tastingNotes = commentsEdit.getText().toString();
        roast.setNotes(tastingNotes);
        db = new DBOperator(this);
        long rows = db.insert(roast);
        if (rows != -1)
            Log.i("db", "Sucessfully inserted rows: " + rows);
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
        finish();

        return roast;
    }
}
