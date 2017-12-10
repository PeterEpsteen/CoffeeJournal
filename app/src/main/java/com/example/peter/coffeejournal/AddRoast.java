package com.example.peter.coffeejournal;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


//TODO Refine validation and make sure there are no bugs. Seems stable for now.

public class AddRoast extends AppCompatActivity implements View.OnClickListener {

    Button addButton, addStepButton, addBeansButton, resetButton;
    FloatingActionButton timerButton;
    Roast roast;
    String roastName, roastDate;
    EditText roastNameEdit, roastDateEdit;
    DBOperator db;
    LinearLayout stepsContainer, beansContainer;
    List<LinearLayout> beanRowLinearLayoutList;
    List<LinearLayout> stepsRowLinearLayoutList;
    CollapsingToolbarLayout collapsingToolbarLayout;
    String timerText;
    int seconds, minutes;
    long startTime = 0;
    boolean isTimerOn = false;
    private Toolbar mToolBar;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            seconds = (int) (millis / 1000);
            minutes = seconds / 60;
            seconds = seconds % 60;

            collapsingToolbarLayout.setTitle(String.format("%02dm %02ds", minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };


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
        TextView stepTv = stepsContainer.findViewById(R.id.time_edit_text);
        stepTv.setText("00:00");
        beansContainer = findViewById(R.id.beans_linear_layout);
        resetButton = findViewById(R.id.reset_roast_button);
        LinearLayout firstStepRow = stepsContainer.findViewById(R.id.first_steps_row);
        LinearLayout firstBeanRow = beansContainer.findViewById(R.id.first_bean_row);
        beanRowLinearLayoutList.add(firstBeanRow);
        stepsRowLinearLayoutList.add(firstStepRow);
        addBeansButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
        addStepButton.setOnClickListener(this);
        addButton.setOnClickListener(this);
        collapsingToolbarLayout = findViewById(R.id.main_collapsing);
        collapsingToolbarLayout.setTitle("00m 00s");
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.TextAppearance_AppCompat_Display3);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.backgroundDefaultWhite));
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.TextAppearance_AppCompat_Display1);
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.backgroundDefaultWhite));

        mToolBar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        timerButton = findViewById(R.id.timer_floating_button);
        timerButton.setScaleType(ImageView.ScaleType.FIT_CENTER);

        timerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FloatingActionButton b = (FloatingActionButton) v;
                if (isTimerOn) {
                    timerHandler.removeCallbacks(timerRunnable);
                    isTimerOn = false;
                    b.setImageResource(R.drawable.ic_play_arrow);
                }
                else {
                    startTime = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnable, 0);
                    isTimerOn = true;
                    b.setImageResource(R.drawable.ic_stop);
                }
//                Button b = (Button) v;
//                if (b.getText().equals("stop")) {
//                    b.setText("start");
//                } else {

//                    tool.setText("stop");
                }
        });

    }

    public boolean validateInput() {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        if(TextUtils.isEmpty(roastNameEdit.getText())){
            CharSequence text = "Please enter a roast name.";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        LayoutInflater inflater = getLayoutInflater();
        switch (v.getId()) {
            case (R.id.add_roast_button):
                if(validateInput())
                addRoast();
                break;
            case (R.id.add_bean_row_button):
                CardView beanRow = (CardView) inflater.inflate(R.layout.add_beans_row, null, false);
                beansContainer.addView(beanRow);
                LinearLayout beanRowLinear = beanRow.findViewById(R.id.bean_row_linear_layout);
                beanRowLinearLayoutList.add(beanRowLinear);
                Log.i("Row", "Adding bean row...");
                break;
            case (R.id.add_step_row_button):
                CardView stepRow = (CardView) inflater.inflate(R.layout.add_tempature_row, null, false);
                stepsContainer.addView(stepRow);
                TextView timeStepTv = stepRow.findViewById(R.id.time_edit_text);
                timeStepTv.setText(String.format("%02d:%02d", minutes, seconds));
                LinearLayout stepRowLinear = stepRow.findViewById(R.id.add_step_linear);
                stepsRowLinearLayoutList.add(stepRowLinear);
                Log.i("Row", "Adding temp row...");
                break;
            case (R.id.reset_roast_button) :
                Intent intent = getIntent();
                finish();
                startActivity(intent);
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
            EditText beanWeightEditText = row.findViewById(R.id.bean_weight_edit_text);
            String beanName = beanNameEditText.getText().toString();
            String beanWeightString = beanWeightEditText.getText().toString();

            if (!beanName.equals("") && !beanWeightString.equals("")){
                Log.i("Bean", "Adding bean name: " + beanName);
                int beanWeight = Integer.parseInt(beanWeightString);
                Bean newBean = new Bean(beanName, beanWeight);
                roast.addToBeanList(newBean);
            }

            else if (beanWeightString.equals("")) {
                Log.i("Bean", "Adding bean name: " + beanName);
                int beanWeight = -1;
                Bean newBean = new Bean(beanName, beanWeight);
                roast.addToBeanList(newBean);
            }

            else if (roast.getBeanList().size() < 1) {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_LONG;
                    CharSequence text = "Please enter at least one bean row.";
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    return roast;
            }
        }

        //add steps to roast
        for (LinearLayout row : stepsRowLinearLayoutList) {
            EditText stepTimeEditText = row.findViewById(R.id.time_edit_text);
            String stepTime = stepTimeEditText.getText().toString();
            EditText stepTempEditText = row.findViewById(R.id.temp_edit_text);
            EditText commentsEdit = row.findViewById(R.id.comments_edit_text);
            String comments = commentsEdit.getText().toString();
            String tempString = stepTempEditText.getText().toString();
            if (!stepTime.equals("") && !tempString.equals("")){
                Log.i("Bean", "Adding step time: " + stepTime);
                int stepTemp = Integer.parseInt(tempString);
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

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
