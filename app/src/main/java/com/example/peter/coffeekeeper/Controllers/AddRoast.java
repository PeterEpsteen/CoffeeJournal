package com.example.peter.coffeekeeper.Controllers;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.peter.coffeekeeper.Database.DBOperator;
import com.example.peter.coffeekeeper.Models.Bean;
import com.example.peter.coffeekeeper.Models.Roast;
import com.example.peter.coffeekeeper.Models.RoastStep;
import com.example.peter.coffeekeeper.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;


//TODO Refine validation and make sure there are no bugs. Seems stable for now.

public class AddRoast extends AppCompatActivity implements View.OnClickListener, QuickStepDialogFragment.NoticeDialogListener, RoastNameDialogFragment.NoticeDialogListener, AddBeansDialogFragment.NoticeDialogListener, QuickStepTempaturesDialogFragment.NoticeDialogListener {

    Button addButton, addStepButton, addBeansButton, resetButton, quickStepButton;
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
    private AdView adView;
    long startTime = 0;
    boolean isTimerOn = false;
    private Toolbar mToolBar;
    private String editRoastName, editRoastDate;
    private static final Pattern timePattern
            = Pattern.compile("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$");
    public static final int ROAST_DB_CHANGED = 2;
    public static final int GO_TO_ROASTS = 3;

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
        Intent intent = getIntent();
        editRoastName = intent.getStringExtra("Name");
        editRoastDate = intent.getStringExtra("Date");
        db = new DBOperator(this);

//
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        beanRowLinearLayoutList = new ArrayList<LinearLayout>();
        stepsRowLinearLayoutList = new ArrayList<LinearLayout>();
        addButton = findViewById(R.id.add_roast_button);
        roastNameEdit = findViewById(R.id.roast_name_edit);
        addStepButton = findViewById(R.id.add_step_row_button);
        addBeansButton = findViewById(R.id.add_bean_row_button);
        stepsContainer = findViewById(R.id.steps_linear_layout);
        quickStepButton = findViewById(R.id.quick_step_row_button);

        beansContainer = findViewById(R.id.beans_linear_layout);
        resetButton = findViewById(R.id.reset_roast_button);
        if (editRoastName == null) {
            RoastStep step = new RoastStep("00:00", 0, "Start", 0);
            addStepRow(step);
            DialogFragment dialog = new RoastNameDialogFragment();
            dialog.show(getFragmentManager(), "test");

        }
        addBeansButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
        addStepButton.setOnClickListener(this);
        addButton.setOnClickListener(this);
        quickStepButton.setOnClickListener(this);
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

        if(editRoastDate != null && editRoastName != null) {
            Log.i("Edit", "Found roast to edit, inserting info from roast: " + editRoastName);
            Roast editRoast = db.getRoast(editRoastName, editRoastDate);
            if(editRoast.getName() != null) {
                Log.i("Edit", "Roast found in db that is being edited name: " + editRoast.getName());
                roastNameEdit.setText(editRoast.getName());
                List<Bean> beanArrayList = editRoast.getBeanList();
                List<RoastStep> stepArrayList = editRoast.getStepList();
                for (Bean bean : beanArrayList) {
                    addBeanRow(bean);
                }
                for (RoastStep step : stepArrayList) {
                    addStepRow(step);
                }
            }
        }

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
        for (LinearLayout linearLayout : stepsRowLinearLayoutList) {
            TextInputLayout textInputLayout = linearLayout.findViewById(R.id.time_textinputlayout);
            if(textInputLayout.isErrorEnabled()){
                CharSequence text = "Please enter a valid time.";
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.add_roast_button):
                if(validateInput())
                addRoast();
                break;
            case (R.id.add_bean_row_button):
                addBeanRow();
                break;
            case (R.id.quick_step_row_button):
                showQuickStep();
                break;
            case (R.id.add_step_row_button):
                addStepRow();
                break;
            case (R.id.reset_roast_button) :
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                break;
        }
    }

    private void showQuickStep() {
        DialogFragment quickStepDialog = new QuickStepDialogFragment();
        quickStepDialog.show(getFragmentManager(), "test");

    }

    private void addStepRow() {
        LayoutInflater inflater = getLayoutInflater();
        final CardView stepRow = (CardView) inflater.inflate(R.layout.add_tempature_row, null, false);
        stepsContainer.addView(stepRow);
        TextInputLayout timeTextInput = stepRow.findViewById(R.id.time_textinputlayout);
        EditText timeStepTv = stepRow.findViewById(R.id.time_edit_text);
        timeStepTv.setText(String.format("%02d:%02d", minutes, seconds));
        timeStepTv.addTextChangedListener(new mTextWatcher(timeTextInput));
        final LinearLayout stepRowLinear = stepRow.findViewById(R.id.add_step_linear);

        stepsRowLinearLayoutList.add(stepRowLinear);
        Button deleteButton = stepRow.findViewById(R.id.step_row_delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepsRowLinearLayoutList.remove(stepRowLinear);
                stepRowLinear.setVisibility(View.GONE);
                stepsContainer.removeView(stepRow);
            }
        });
        Log.i("Row", "Adding temp row...");
    }

    public void deleteRow(View v) {
        LinearLayout ll = (LinearLayout) v.getParent();
        CardView cv = (CardView) ll.getParent();
        stepsRowLinearLayoutList.remove(ll);
        stepsContainer.removeView(cv);
        cv.setVisibility(View.GONE);
    }

    public void deleteBeanRow(View v) {
        LinearLayout ll = (LinearLayout) v.getParent();
        CardView cv = (CardView) ll.getParent();
        ll = (LinearLayout) ll.findViewById(R.id.bean_row_linear_layout);
        beanRowLinearLayoutList.remove(ll);
        beansContainer.removeView(cv);
        cv.setVisibility(View.GONE);
    }


    private void addStepRow(RoastStep step) {
        String stepTime = step.getTime();
        String stepComment = step.getComment();
        String stepTemp = String.valueOf(step.getTemp());
        String beanstepTemp = String.valueOf(step.getBeanTemp());
        LayoutInflater inflater = getLayoutInflater();
        CardView stepRow = (CardView) inflater.inflate(R.layout.add_tempature_row, null, false);
        EditText stepTimeEdit = stepRow.findViewById(R.id.time_edit_text);
        stepTimeEdit.setText(stepTime);
        EditText stepTempEdit = stepRow.findViewById(R.id.temp_edit_text);
        stepTempEdit.setText(stepTemp);
        EditText stepBeanTempEdit = stepRow.findViewById(R.id.bean_temp_edit_text);
        stepBeanTempEdit.setText(beanstepTemp);
        EditText stepCommentEdit = stepRow.findViewById(R.id.comments_edit_text);
        stepCommentEdit.setText(stepComment);
        stepsContainer.addView(stepRow);
        TextInputLayout timeTextInput = stepRow.findViewById(R.id.time_textinputlayout);
        stepTimeEdit.addTextChangedListener(new mTextWatcher(timeTextInput));
        LinearLayout stepRowLinear = stepRow.findViewById(R.id.add_step_linear);
        stepsRowLinearLayoutList.add(stepRowLinear);
        Log.i("Row", "Adding temp row...");
    }

    @Override
    public void onConfirmRoastName(DialogFragment dialog, View root) {
        dialog.dismiss();
        EditText roastNameDialogEditText = root.findViewById(R.id.roast_name_dialog_edit_text);
        roastNameEdit.setText(roastNameDialogEditText.getText().toString());
        DialogFragment beanDialog = new AddBeansDialogFragment();
        beanDialog.show(getFragmentManager(), "test");
    }


    @Override
    public void onConfirmBeans(DialogFragment dialogFragment, List<LinearLayout> beanLinearList) {
        List<Bean> beans = new ArrayList<Bean>();

        for(LinearLayout beanRow : beanLinearList) {
            Bean newBean = new Bean();
            EditText beanNameEdit = beanRow.findViewById(R.id.bean_name_edit_text);
            EditText beanWeightEdit = beanRow.findViewById(R.id.bean_weight_edit_text);
            int beanWeight = (beanWeightEdit.getText().toString().isEmpty()) ? 0 : Integer.parseInt(beanWeightEdit.getText().toString());
            String beanName = (beanNameEdit.getText().toString().isEmpty()) ? "Unnamed" : beanNameEdit.getText().toString();
            newBean.setBeanName(beanName);
            newBean.setBeanWeight(beanWeight);
            beans.add(newBean);
        }
        if (beans.size() != 0) {
            for (Bean bean : beans) {
                addBeanRow(bean);
            }
        }

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

// 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.roast_instructions).setTitle("Instructions").setPositiveButton("Got it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });


// 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onConfirmStep(String comment) {
        showQuickStepTempatures(comment);
    }

    public void showQuickStepTempatures(String comment) {
        Bundle bundle = new Bundle();
        bundle.putString("comment", comment);
        DialogFragment quickStepTempatureDialog = new QuickStepTempaturesDialogFragment();
        quickStepTempatureDialog.setArguments(bundle);
        quickStepTempatureDialog.show(getFragmentManager(), "test");
    }

    @Override
    public void onConfirmTempatures(RoastStep step) {
        step.setTime(String.format("%02d:%02d", minutes, seconds));
        addStepRow(step);
    }


    private class mTextWatcher implements TextWatcher {
        private TextInputLayout timeTextInput;

        public mTextWatcher(TextInputLayout textInputLayout) {
            this.timeTextInput = textInputLayout;
        }
        private boolean isValid(CharSequence s) {
            return timePattern.matcher(s).matches();
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String working = s.toString();
            boolean isValid = true;
            if (!isValid(s)) {
                timeTextInput.setError("Please enter valid time (00:00)");
                timeTextInput.setErrorEnabled(true);
            }
            else {
                timeTextInput.setErrorEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public void addBeanRow() {
        LayoutInflater inflater = getLayoutInflater();
        CardView beanRow = (CardView) inflater.inflate(R.layout.add_beans_row, null, false);
        beansContainer.addView(beanRow);
        LinearLayout beanRowLinear = beanRow.findViewById(R.id.bean_row_linear_layout);
        beanRowLinearLayoutList.add(beanRowLinear);
        Log.i("Row", "Adding bean row...");
    }

    public void addBeanRow(Bean bean){
        String beanName = bean.getBeanName();
        int beanWeight = bean.getBeanWeight();
        LayoutInflater inflater = getLayoutInflater();
        CardView beanRow = (CardView) inflater.inflate(R.layout.add_beans_row, null, false);
        EditText nameEdit = beanRow.findViewById(R.id.bean_name_edit_text);
        nameEdit.setText(beanName);
        EditText weightEdit = beanRow.findViewById(R.id.bean_weight_edit_text);
        weightEdit.setText(String.valueOf(beanWeight));
        beansContainer.addView(beanRow);
        LinearLayout beanRowLinear = beanRow.findViewById(R.id.bean_row_linear_layout);
        beanRowLinearLayoutList.add(beanRowLinear);
        Log.i("Row", "Adding bean row...");
    }

    public Roast addRoast() {


        //first, set date
        DateFormat dateFormat = new SimpleDateFormat("MMM d yyyy h:mm a");
        Date date = new Date();
        roastName = roastNameEdit.getText().toString();

            roastDate = dateFormat.format(date);
            //get name and create a new roast
            roast = new Roast(roastName, roastDate, 1, 0);
            //add all bean rows to roast

            for (LinearLayout row : beanRowLinearLayoutList) {
                Log.i("BeanRow", "Bean Row: " + row.toString());
                EditText beanNameEditText = row.findViewById(R.id.bean_name_edit_text);
                EditText beanWeightEditText = row.findViewById(R.id.bean_weight_edit_text);
                String beanName = beanNameEditText.getText().toString();
                String beanWeightString = beanWeightEditText.getText().toString();

                if (!beanName.equals("") && !beanWeightString.equals("")) {
                    Log.i("Bean", "Adding bean name: " + beanName);
                    int beanWeight = Integer.parseInt(beanWeightString);
                    Bean newBean = new Bean(beanName, beanWeight);
                    roast.addToBeanList(newBean);
                }

                else if (beanWeightString.equals("") && !beanName.equals("")) {
                    Log.i("Bean", "Adding bean name: " + beanName);
                    int beanWeight = 0;
                    Bean newBean = new Bean(beanName, beanWeight);
                    roast.addToBeanList(newBean);
                }
//                } else if (roast.getBeanList().size() < 1) {
//                    Context context = getApplicationContext();
//                    int duration = Toast.LENGTH_LONG;
//                    CharSequence text = "Please enter at least one bean for this roast.";
//                    Toast toast = Toast.makeText(context, text, duration);
//                    toast.show();
//                    return roast;
//                }
            }

            //add steps to roast
            for (LinearLayout row : stepsRowLinearLayoutList) {
                EditText stepTimeEditText = row.findViewById(R.id.time_edit_text);
                String stepTime = stepTimeEditText.getText().toString();
                EditText stepTempEditText = row.findViewById(R.id.temp_edit_text);
                EditText stepBeanTempEditText = row.findViewById(R.id.bean_temp_edit_text);
                EditText commentsEdit = row.findViewById(R.id.comments_edit_text);
                String comments = commentsEdit.getText().toString();
                String tempString = stepTempEditText.getText().toString();
                String beanTempString = stepBeanTempEditText.getText().toString();
                int stepTemp = 0;
                int stepBeanTemp = 0;
                if (!tempString.equals("")) {
                    Log.i("Bean", "Adding step time: " + stepTime);
                    stepTemp = Integer.parseInt(tempString);
                }
                if(!beanTempString.equals("")){
                    stepBeanTemp = Integer.parseInt(beanTempString);
                }
                RoastStep newStep = new RoastStep(stepTime, stepTemp, comments, stepBeanTemp);
                roast.addToStepList(newStep);
            }

            Log.i("Roast", roast.toString());

            EditText commentsEdit = findViewById(R.id.tasting_notes_edit);
            String tastingNotes = commentsEdit.getText().toString();
            roast.setNotes(tastingNotes);
            db = new DBOperator(this);
            if (editRoastName == null || !editRoastName.equals(roastName)) {
                long rows = db.insert(roast);
                if (rows != -1) {
                    Log.i("db", "Sucessfully inserted rows: " + rows);
                    setResult(ROAST_DB_CHANGED);
                    finish();
                }
                else {
                    Toast myToast = Toast.makeText(getApplicationContext(), "Error saving roast. Please check data and try again", Toast.LENGTH_LONG);
                    myToast.show();
//                    Intent i = new Intent(this, MainActivity.class);
//                    startActivity(i);
                }
            }
            else {
                roast.setDateAdded(editRoastDate);
                long rows = db.update(roast);
                if (rows > 0) {
                    Log.i("db", "Successfully updated roast");
                   /* Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);*/
                    setResult(ROAST_DB_CHANGED);
                    finish();                }
                else {
                    Toast myToast = Toast.makeText(getApplicationContext(), "Error saving roast. Please check data and try again", Toast.LENGTH_LONG);
                    myToast.show();
                }
        }

        return roast;
    }

    @Override
    public void onBackPressed() {
        setResult(GO_TO_ROASTS);
        finish();
        super.onBackPressed();
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
            this.onBackPressed();
            return true;
        }
        return false;
    }

}
