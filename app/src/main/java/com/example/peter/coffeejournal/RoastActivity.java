package com.example.peter.coffeejournal;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

public class RoastActivity extends AppCompatActivity {

    String brewName, brewDate;
    DBOperator db;
    Roast roast;
    LinearLayout roastContainer;
    List<RoastStep> steps;
    List<Bean> beans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roast);
        roastContainer = findViewById(R.id.roast_container);
        brewName = getIntent().getExtras().getString("Name");
        brewDate = getIntent().getExtras().getString("Date");
        ActionBar ab = getSupportActionBar();
        try {
            ab.setTitle(brewName);
        } catch (NullPointerException e){}
        LayoutInflater inflater = getLayoutInflater();
        db = new DBOperator(this);
        roast = db.getRoast(brewName, brewDate);
        beans = roast.getBeanList();
        steps = roast.getStepList();
        int count = 1;
        TextView beanTv = (TextView) inflater.inflate(R.layout.generic_text_view, roastContainer, false);
        beanTv.setText("Beans");
//        roastContainer.addView(beanTv);
        CardView beansCv = (CardView) inflater.inflate(R.layout.generic_card_view, roastContainer, false);
        LinearLayout beansCvContainer = beansCv.findViewById(R.id.card_view_container);
        beansCvContainer.addView(beanTv);
        LinearLayout topBeanRow = (LinearLayout) inflater.inflate(R.layout.bean_row, roastContainer, false);
        TextView topBeanNumbTv = topBeanRow.findViewById(R.id.bean_number_text_view);
        topBeanNumbTv.setText("");
        TextView topBeanNameTv = topBeanRow.findViewById(R.id.bean_name_text_view);
        topBeanNameTv.setTextColor(getResources().getColor(R.color.colorTextLight));
        topBeanNameTv.setText("Name");
        TextView topBeanWeightTv = topBeanRow.findViewById(R.id.bean_weight_text_view);
        topBeanWeightTv.setTextColor(getResources().getColor(R.color.colorTextLight));
        topBeanWeightTv.setText("Weight");
        beansCvContainer.addView(topBeanRow);
        for(Bean bean : beans) {
            Log.i("BeanRow", "Bean row: " + bean.getBeanName());
            LinearLayout beanRow = (LinearLayout) inflater.inflate(R.layout.bean_row, roastContainer, false);
            TextView beanNameTv = (TextView) beanRow.findViewById(R.id.bean_name_text_view);
            beanNameTv.setText(bean.getBeanName());
            TextView beanWeightTv = beanRow.findViewById(R.id.bean_weight_text_view);
            beanWeightTv.setText(Integer.toString(bean.getBeanWeight()));
            TextView countTv = beanRow.findViewById(R.id.bean_number_text_view);
            countTv.setText("");
            count++;
            beansCvContainer.addView(beanRow);

        }
        roastContainer.addView(beansCv);
        count = 1;
        TextView stepTv = (TextView) inflater.inflate(R.layout.generic_text_view, roastContainer, false);
        stepTv.setText("Steps");
//        roastContainer.addView(stepTv);
        CardView stepsCv = (CardView) inflater.inflate(R.layout.generic_card_view, roastContainer, false);
        LinearLayout stepsCvContainer = stepsCv.findViewById(R.id.card_view_container);
        stepsCvContainer.addView(stepTv);
        LinearLayout topStepRow = (LinearLayout) inflater.inflate(R.layout.step_row, roastContainer, false);
        TextView topStepNumbTv = topStepRow.findViewById(R.id.step_number_text_view);
        topStepNumbTv.setText("");
        TextView topStepTimeTv = topStepRow.findViewById(R.id.step_time_text_view);
        topStepTimeTv.setTextColor(getResources().getColor(R.color.colorTextLight));
        topStepTimeTv.setText("Time");
        TextView topStepTempTv = topStepRow.findViewById(R.id.temp_text_view);
        topStepTempTv.setTextColor(getResources().getColor(R.color.colorTextLight));
        topStepTempTv.setText("Temp");
        TextView topStepCommentsTv = topStepRow.findViewById(R.id.comments_text_view);
        topStepCommentsTv.setTextColor(getResources().getColor(R.color.colorTextLight));
        topStepCommentsTv.setText("Comment");
        stepsCvContainer.addView(topStepRow);
        for(RoastStep step : steps) {
            LinearLayout stepRow = (LinearLayout) inflater.inflate(R.layout.step_row, roastContainer, false);
            TextView timeTv = stepRow.findViewById(R.id.step_time_text_view);
            timeTv.setText(step.getTime());
            TextView tempTv = stepRow.findViewById(R.id.temp_text_view);
            tempTv.setText(String.valueOf(step.getTemp()));
            TextView commentsTv = stepRow.findViewById(R.id.comments_text_view);
            commentsTv.setText(step.getComment());
            TextView countTv = stepRow.findViewById(R.id.step_number_text_view);
            countTv.setText("");
            count++;
            stepsCvContainer.addView(stepRow);
        }
        roastContainer.addView(stepsCv);
        TextView notesTitleTv = (TextView) inflater.inflate(R.layout.generic_text_view, roastContainer, false);
        notesTitleTv.setText("Tasting Notes");
        CardView notesCv = (CardView) inflater.inflate(R.layout.generic_card_view, roastContainer, false);
        notesCv.addView(notesTitleTv);
        LinearLayout tastingLayout = (LinearLayout) inflater.inflate(R.layout.roast_tasting_notes_linear_layout, roastContainer, false);
        TextView notesTv = tastingLayout.findViewById(R.id.tasting_notes_text_view);
        notesTv.setText(roast.getNotes());
        notesCv.addView(tastingLayout);
        roastContainer.addView(notesCv);

    }
}
