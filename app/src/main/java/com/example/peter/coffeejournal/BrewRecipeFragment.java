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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BrewRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrewRecipeFragment extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private BrewRecipe br;
    DBOperator db;
    TextView waterWeightTv, coffeeWeightTv, grindTv, strengthTv, textTimer, stepTv;
    CountDownTimer countDownTimer;
    ProgressBar barTimer;
    Button startButton, waterUnitsButton, coffeeUnitsButton;
    int bloomMinutes, bloomSeconds, brewSeconds;
    boolean brewFinished;
    double waterUnits, coffeeUnits, ogWaterUnits, ogCoffeeUnits;
    SeekBar scaleSlider;
    boolean metric;
    SendBrew mCallback;



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

        String brewName = getActivity().getIntent().getExtras().getString("Brew Name");
        db = new DBOperator(getContext());
        br = db.getBrewRecipe(brewName);
        Log.i("BR", "Brew recipe is: " + br.getName());
        waterWeightTv = (TextView) getActivity().findViewById(R.id.water_weight_text_view);
        coffeeWeightTv = (TextView) getActivity().findViewById(R.id.coffee_weight_text_view);
        Spinner spinner = (Spinner) rootView.findViewById(R.id.strength_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.brew_strength_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.my_simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        DecimalFormat df = new DecimalFormat("#.##");
        stepTv = getActivity().findViewById(R.id.step_tv);

        waterUnits = br.getWaterUnits();
        coffeeUnits = br.getCoffeeUnits();
        ogCoffeeUnits = coffeeUnits;
        ogWaterUnits = waterUnits;
        coffeeWeightTv.setText(String.valueOf(coffeeUnits));
        waterWeightTv.setText(String.valueOf(waterUnits));
        scaleSlider = (SeekBar) rootView.findViewById(R.id.scaleSeekBar);
        scaleSlider.setOnSeekBarChangeListener(this);
        grindTv = (TextView) rootView.findViewById(R.id.grind_recipe_textview);
        grindTv.setText(br.getGrind());
        textTimer = (TextView) getActivity().findViewById(R.id.tvTimeCount);
        bloomSeconds = br.getBloomTime();
        coffeeUnitsButton = (Button) rootView.findViewById(R.id.coffee_units_button);
        waterUnitsButton = (Button) rootView.findViewById(R.id.water_units_button);
        metric = br.isMetric();
        coffeeUnitsButton.setOnClickListener(this);
        waterUnitsButton.setOnClickListener(this);
        brewSeconds = br.getBrewTime();
        if(bloomSeconds == 0) {
            stepTv.setText("Brew");
        }

        if(br.isMetric()) {
            coffeeUnitsButton.setText("Grams");
            waterUnitsButton.setText("Grams");
        }

        else {
            coffeeUnitsButton.setText("Ounces");
            waterUnitsButton.setText("Ounces");
        }


        textTimer.setText(String.format("%02d", bloomSeconds/60) + ":" + String.format("%02d", bloomSeconds%60));
        barTimer = getActivity().findViewById(R.id.progressbarRL);
        startButton = rootView.findViewById(R.id.start_button);
        startButton.setOnClickListener(this);

        try {
            mCallback = (SendBrew) getActivity();
            mCallback.sendBrewRecipe(br);
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement TextClicked");
        }

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
        if (v == startButton) {
            switch (btn.getText().toString()) {
                case "Start!":
                    startBloomTimer(bloomSeconds);
                    brewFinished = false;
                    btn.setText("Reset");
                    break;
                case "Reset":
                    ((BrewRecipeActivity) getActivity()).update();
                    break;
            }
        }

        else if (v == waterUnitsButton) {
            if (waterUnitsButton.getText().toString().equalsIgnoreCase("Grams")) {
                double oz = convertGramsToOz(Double.parseDouble(waterWeightTv.getText().toString()));
                waterUnits = convertGramsToOz(waterUnits);
                ogWaterUnits = convertGramsToOz(ogWaterUnits);
                waterWeightTv.setText(String.format("%.1f", oz));
                waterUnitsButton.setText("Ounces");
            }
            else {
                double grams = convertOzToGrams(Double.parseDouble(waterWeightTv.getText().toString()));
                waterWeightTv.setText(String.format("%.0f", grams));
                waterUnits = convertOzToGrams(waterUnits);
                ogWaterUnits = convertOzToGrams(ogWaterUnits);
                waterUnitsButton.setText("Grams");
            }
        }

        else if (v == coffeeUnitsButton) {
            if (coffeeUnitsButton.getText().toString().equalsIgnoreCase("Grams")) {
                double oz = convertGramsToOz(Double.parseDouble(coffeeWeightTv.getText().toString()));
                coffeeWeightTv.setText(String.format("%.2f", oz));
                coffeeUnitsButton.setText("Ounces");
                coffeeUnits = convertGramsToOz(coffeeUnits);
                ogCoffeeUnits = convertGramsToOz(ogCoffeeUnits);
            }
            else {
                double grams = convertOzToGrams(Double.parseDouble(coffeeWeightTv.getText().toString()));
                coffeeWeightTv.setText(String.format("%.0f", grams));
                coffeeUnitsButton.setText("Grams");
                coffeeUnits = convertOzToGrams(coffeeUnits);
                ogCoffeeUnits = convertOzToGrams(ogCoffeeUnits);
            }
        }
    }

    public double convertGramsToOz(double grams) {
        return 0.035274*grams;
    }

    public double convertOzToGrams(double oz) {
        return oz * 28.35;
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        double multiplier = seekBar.getProgress()/100.0;
        double waterWeight = 0;
        double coffeeWeight = 0;
        waterWeight = waterUnits * multiplier;
        coffeeWeight = coffeeUnits * multiplier;
        waterWeightTv.setText(String.format("%.2f", waterWeight));
        coffeeWeightTv.setText(String.format("%.2f", coffeeWeight));

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String brewStrength = parent.getItemAtPosition(position).toString();
        switch (brewStrength){
            case "Regular":
                coffeeUnits = ogCoffeeUnits;
                break;
            case "Strong":
                coffeeUnits = ogCoffeeUnits * 1.1;
                break;
            case "Light":
                coffeeUnits = ogCoffeeUnits * .9;
                break;
        }

        updateMeasurementViews();
    }

    private void updateMeasurementViews() {
        scaleSlider.incrementProgressBy(1);
        scaleSlider.incrementProgressBy(-1);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface SendBrew{
        public void sendBrewRecipe(BrewRecipe brew);
    }
}
