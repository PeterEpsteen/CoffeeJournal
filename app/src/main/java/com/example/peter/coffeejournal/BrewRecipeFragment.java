package com.example.peter.coffeejournal;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BrewRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrewRecipeFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private BrewRecipe br;
    DBOperator db;
    private int waterUnits = 16;
    TextView waterWeightTv, coffeeWeightTv, grindTv, strengthTv, textTimer, stepTv;
    CountDownTimer countDownTimer;
    ProgressBar barTimer;
    Button startButton;
    int bloomMinutes, bloomSeconds, brewSeconds;
    boolean brewFinished;



    public BrewRecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BrewRecipeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BrewRecipeFragment newInstance(String param1, String param2) {
        BrewRecipeFragment fragment = new BrewRecipeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_brew_recipe, container, false);
        BrewRecipeActivity act = (BrewRecipeActivity) getActivity();
        String brewName = getActivity().getIntent().getExtras().getString("Brew Name");
        db = new DBOperator(getContext());
        br = db.getBrewRecipe(brewName);
        Log.i("BR", "Brew recipe is: " + br.getName());
        waterWeightTv = (TextView) rootView.findViewById(R.id.water_weight_text_view);
        coffeeWeightTv = (TextView) rootView.findViewById(R.id.coffee_weight_text_view);
        grindTv = (TextView) rootView.findViewById(R.id.grind_recipe_textview);
        strengthTv = (TextView) rootView.findViewById(R.id.strength_recipe_textview);
        waterWeightTv.setText("16");
        DecimalFormat df = new DecimalFormat("#.##");
        stepTv = rootView.findViewById(R.id.step_tv);
        coffeeWeightTv.setText(df.format(16.0/br.getRatio()));
        grindTv.setText(br.getGrind());
        textTimer = (TextView) rootView.findViewById(R.id.tvTimeCount);
        bloomSeconds = br.getBloomTime();
        brewSeconds = br.getBrewTime();
        if(bloomSeconds == 0) {
            TextView tv = rootView.findViewById(R.id.step_tv);
            tv.setText("Brew");
        }


        textTimer.setText(String.format("%02d", bloomSeconds/60) + ":" + String.format("%02d", bloomSeconds%60));
        barTimer = rootView.findViewById(R.id.progressbarRL);
        startButton = rootView.findViewById(R.id.start_button);
        startButton.setOnClickListener(this);

        return rootView;
    }

    private void startBrewTimer(final int seconds){
        stepTv.setText("Brew");
        barTimer.setMax(seconds);
        countDownTimer = new CountDownTimer(seconds * 1000, 500) {
            // 500 means, onTick function will be called at every 500 milliseconds

            @Override
            public void onTick(long leftTimeInMilliseconds) {
                long seconds = leftTimeInMilliseconds / 1000;
                textTimer.setText(String.format("%02d", seconds / 60) + ":" + String.format("%02d", seconds % 60));
                barTimer.setProgress((int) seconds);
                // format the textview to show the easily readable format

            }

            @Override
            public void onFinish() {
                if (textTimer.getText().equals("00:00")) {
                    textTimer.setText("STOP");
                } else {
                    textTimer.setText("2:00");
                }
            }
        }.start();
    }


    private void startBloomTimer(final int seconds) {
        if(seconds == 0) {
            startBrewTimer(brewSeconds);
        }
        else {
            barTimer.setMax(seconds);
            countDownTimer = new CountDownTimer(seconds * 1000, 500) {
                // 500 means, onTick function will be called at every 500 milliseconds

                @Override
                public void onTick(long leftTimeInMilliseconds) {
                    long seconds = leftTimeInMilliseconds / 1000;
                    textTimer.setText(String.format("%02d", seconds / 60) + ":" + String.format("%02d", seconds % 60));
                    barTimer.setProgress((int) seconds);
                    // format the textview to show the easily readable format

                }

                @Override
                public void onFinish() {
//                    if (textTimer.getText().equals("00:00")) {
//                        textTimer.setText("STOP");
//                    } else {
//                        textTimer.setText("2:00");
//                    }

                    startBrewTimer(brewSeconds);
                }
            }.start();
        }

    }

    @Override
    public void onClick(View v) {
        Button btn = (Button) v;
        switch (btn.getText().toString()) {
            case "Start!":
                startBloomTimer(bloomSeconds);
                brewFinished = false;
                btn.setText("Reset");
                break;
            case "Reset":
                ((BrewRecipeActivity)getActivity()).update();
                break;
        }
    }


}
