package com.example.peter.coffeekeeper;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RoastActivity extends AppCompatActivity {

    String brewName, brewDate;
    DBOperator db;
    Roast roast;
    LinearLayout roastContainer;
    List<RoastStep> steps;
    List<Bean> beans;
    CollapsingToolbarLayout collapsingToolbarLayout;
    PopupWindow popupWindow;
    ConstraintLayout layoutOfPopup;
    TextView datapointTv;
    private AdView adView;
    float lastTouchedX, lastTouchedY;
    final static int ROAST_PAGE_RESULT = 5;

    //TODO
    //Get steps to work with graph
    //Special steps have special characters
    //little info pop ups next to special steps
    // touching steps on graphs highlights bottom


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roast);
        brewName = getIntent().getExtras().getString("Name");
        brewDate = getIntent().getExtras().getString("Date");

        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        collapsingToolbarLayout = findViewById(R.id.main_collapsing);
        collapsingToolbarLayout.setTitle(brewName);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.TextAppearance_AppCompat_Headline);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.backgroundDefaultWhite));
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.TextAppearance_AppCompat_Widget_ActionBar_Title);
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.backgroundDefaultWhite));
        android.support.v7.widget.Toolbar mToolBar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        LayoutInflater layoutInflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutOfPopup = (ConstraintLayout) layoutInflater.inflate(R.layout.pop_up_generic, null, false);
        popupWindow = new PopupWindow(this);
        roastContainer = findViewById(R.id.roast_container);
        datapointTv = findViewById(R.id.datapoint_text_view);
        datapointTv.setVisibility(View.GONE);
        LayoutInflater inflater = getLayoutInflater();
        db = new DBOperator(this);
        roast = db.getRoast(brewName, brewDate);
        beans = roast.getBeanList();
        steps = roast.getStepList();
        Collections.sort(steps, new Comparator<RoastStep>() {
            @Override
            public int compare(RoastStep o1, RoastStep o2) {
                Integer time1 = 0;
                Integer time2 = 0;
                String[] arr1 = o1.getTime().split(":");
                time1 = Integer.parseInt(arr1[0]) * 60 + Integer.parseInt(arr1[1]);
                String[] arr2 = o2.getTime().split(":");
                time2 = Integer.parseInt(arr2[0]) * 60 + Integer.parseInt(arr2[1]);
                return time1.compareTo(time2);
            }
        });
        int count = 1;
        TextView beanTv = (TextView) inflater.inflate(R.layout.generic_text_view, roastContainer, false);
        beanTv.setText("Beans");
        roastContainer.addView(beanTv);
        CardView beansCv = (CardView) inflater.inflate(R.layout.generic_card_view, roastContainer, false);
        LinearLayout beansCvContainer = beansCv.findViewById(R.id.card_view_container);
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
        for (Bean bean : beans) {
            Log.i("BeanRow", "Bean row: " + bean.getBeanName());
            LinearLayout beanRow = (LinearLayout) inflater.inflate(R.layout.bean_row, roastContainer, false);
            TextView beanNameTv = beanRow.findViewById(R.id.bean_name_text_view);
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
        roastContainer.addView(stepTv);
        CardView stepsCv = (CardView) inflater.inflate(R.layout.generic_card_view, roastContainer, false);
        LinearLayout stepsCvContainer = stepsCv.findViewById(R.id.card_view_container);
        LinearLayout topStepRow = (LinearLayout) inflater.inflate(R.layout.step_row, roastContainer, false);
        TextView topStepTimeTv = topStepRow.findViewById(R.id.step_time_text_view);
        topStepTimeTv.setTextColor(getResources().getColor(R.color.colorTextLight));
        topStepTimeTv.setText("Time");
        TextView topStepTempTv = topStepRow.findViewById(R.id.temp_text_view);
        topStepTempTv.setTextColor(getResources().getColor(R.color.colorTextLight));
        topStepTempTv.setText("Roast");
        TextView topStepBeanTempTv = topStepRow.findViewById(R.id.bean_temp_text_view);
        topStepBeanTempTv.setTextColor(getResources().getColor(R.color.colorTextLight));
        topStepBeanTempTv.setText("Bean");
        TextView topStepCommentsTv = topStepRow.findViewById(R.id.comments_text_view);
        topStepCommentsTv.setTextColor(getResources().getColor(R.color.colorTextLight));
        topStepCommentsTv.setText("Comment");
        stepsCvContainer.addView(topStepRow);
        for (RoastStep step : steps) {
            LinearLayout stepRow = (LinearLayout) inflater.inflate(R.layout.step_row, roastContainer, false);
            TextView timeTv = stepRow.findViewById(R.id.step_time_text_view);
            timeTv.setText(step.getTime());
            TextView tempTv = stepRow.findViewById(R.id.temp_text_view);
            String tempString = String.valueOf(step.getTemp()) + "°";
            tempTv.setText(tempString);
            TextView tempBeanTv = stepRow.findViewById(R.id.bean_temp_text_view);
            String tempBeanString = String.valueOf(step.getBeanTemp()) + "°";
            tempBeanTv.setText(tempBeanString);
            TextView commentsTv = stepRow.findViewById(R.id.comments_text_view);
            commentsTv.setText(step.getComment());
            stepsCvContainer.addView(stepRow);
        }
        roastContainer.addView(stepsCv);
        TextView notesTitleTv = (TextView) inflater.inflate(R.layout.generic_text_view, roastContainer, false);
        notesTitleTv.setText("Tasting Notes");
        roastContainer.addView(notesTitleTv);
        CardView notesCv = (CardView) inflater.inflate(R.layout.generic_card_view, roastContainer, false);
        LinearLayout tastingLayout = (LinearLayout) inflater.inflate(R.layout.roast_tasting_notes_linear_layout, roastContainer, false);
        TextView notesTv = tastingLayout.findViewById(R.id.tasting_notes_text_view);
        notesTv.setText(roast.getNotes());
        notesCv.addView(tastingLayout);
        roastContainer.addView(notesCv);


        final GraphView graph = (GraphView) findViewById(R.id.graph);

        try {
//            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
//            String finalTime = steps.get(steps.size()-1).getTime();
//            int finalTimeMinutes = Integer.parseInt(finalTime.split(":")[0]) % 60 + 1;
//            String[] labels = new String[finalTimeMinutes+1];
//            for(int i = 0; i < labels.length; i++){
//                labels[i] = i + ""
//            }
//            staticLabelsFormatter.setHorizontalLabels(new String[] {0:00, });
            graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                @Override
                public String formatLabel(double value, boolean isValueX) {
                    if (isValueX) {
//                        String timeString = "";
//                        int minutes = (int) value/60;
//                        int seconds = (int) value%60;
//                        String minuteString = Integer.toString(minutes);
//                        if (minuteString.length() == 1) {
//                            minuteString = 0 + minuteString;
//                        }
//                        String secondsString = Integer.toString(seconds);
//                        if (secondsString.length() == 1) {
//                            secondsString = 0 + secondsString;
//                        }
//                        return minuteString + ":" + secondsString;
                        double minutes = value/60;
                        return super.formatLabel(minutes, isValueX);
                    }
                    else {
                        return super.formatLabel(value, isValueX);
                    }
                }

            });
//        graph.setBackgroundColor(getResources().getColor(R.color.backgroundDefaultWhite));
            graph.getGridLabelRenderer().setGridColor(R.color.colorTextDark);

//        graph.getGridLabelRenderer().setHorizontalLabelsColor(R.color.colorTextDark);
//        graph.getGridLabelRenderer().setVerticalLabelsColor(R.color.colorTextDark);
//        graph.getGridLabelRenderer().setVerticalAxisTitleColor(R.color.colorTextDark);
            graph.getGridLabelRenderer().setLabelsSpace(10);
            graph.getViewport().setScalable(false);
            graph.getViewport().setBackgroundColor(Color.WHITE);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(0);
            graph.getViewport().setMaxX(Integer.parseInt(steps.get(steps.size()-1).getTime().split(":")[0])*60+60);
            graph.getViewport().setMinY(0);
            graph.getViewport().setMaxY(steps.get(steps.size()-1).getTemp() + 100);
            final DataPoint[] datapoint = new DataPoint[steps.size()];
            DataPoint[] beanDatapoint = new DataPoint[steps.size()];

            for (int i = 0; i < steps.size(); i++) {
                // add new DataPoint object to the array for each of your list entries
                RoastStep step = steps.get(i);
                String timeString = step.getTime();
                double timeDouble = 0;
                String[] arr = timeString.split(":");
                timeDouble = Double.parseDouble(arr[0]) * 60 + Double.parseDouble(arr[1]);
                datapoint[i] = new DataPoint(timeDouble, step.getTemp()); // not sure but I think the second argument should be of type double
                beanDatapoint[i] = new DataPoint(timeDouble, step.getBeanTemp());
            }

            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(datapoint);
            series.setDrawDataPoints(true);
            graph.addSeries(series);
            LineGraphSeries<DataPoint> seriesBean = new LineGraphSeries<>(beanDatapoint);
            seriesBean.setColor(Color.RED);
            seriesBean.setDrawDataPoints(true);
            graph.addSeries(seriesBean);
            series.setTitle("Environment");
            series.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    RoastStep step = roast.findStep((int)dataPoint.getX());
                    if (!step.getComment().equals("")) {
                        double timeSeconds = dataPoint.getX();
                        String timeSecondsString = "";
                        if (timeSeconds % 60 < 10) {
                            timeSecondsString = "0" + (int)timeSeconds%60;
                        }
                        else {
                            timeSecondsString = "" + (int)timeSeconds%60;
                        }
                        String timeString = (int)timeSeconds/60 + ":" + timeSecondsString;
                        datapointTv.setText("Comment at " + timeString+ ": \n" + step.getComment());
                        datapointTv.setVisibility(View.VISIBLE);

                    }
                }

            });
            seriesBean.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    RoastStep step = roast.findStep((int)dataPoint.getX());
                    if (!step.getComment().equals("")) {
                        double timeSeconds = dataPoint.getX();
                        String timeSecondsString = "";
                        if (timeSeconds % 60 < 10) {
                            timeSecondsString = "0" + (int)timeSeconds%60;
                        }
                        else {
                            timeSecondsString = "" + (int)timeSeconds%60;
                        }
                        String timeString = (int)timeSeconds/60 + ":" + timeSecondsString;
                        datapointTv.setText("Comment at " + timeString+ ": \n" + step.getComment());
                        datapointTv.setVisibility(View.VISIBLE);

                    }
                }
            });
            seriesBean.setTitle("Bean");
            graph.getLegendRenderer().setVisible(true);
            graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);




        } catch (Exception e) {
            //Error making graph
            Log.e("Error", e.toString());
            graph.setVisibility(View.GONE);
            AppBarLayout mAppBarLayout = findViewById(R.id.main_appbar);
            mAppBarLayout.setExpanded(false, false);
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)mAppBarLayout.getLayoutParams();
            lp.height = (int) getResources().getDimension(R.dimen.toolbar_height);
            collapsingToolbarLayout.setTitleEnabled(false);
            android.support.v7.widget.Toolbar mainToolbar = findViewById(R.id.main_toolbar);
            mainToolbar.setTitle(brewName);
            TextView tv = findViewById(R.id.roast_profile_title_tv);
            tv.setVisibility(View.GONE);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(ROAST_PAGE_RESULT);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

}
