package com.example.peter.coffeejournal;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.DecimalFormat;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BrewRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrewRecipeFragment extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private BrewRecipe br;
    DBOperator db;
    TextView textTimer, stepTv, waterUnitsTv, coffeeUnitsTv;
    EditText waterWeightEdit, coffeeWeightEdit;
    CountDownTimer countDownTimer;
    ProgressBar barTimer;
    Button startButton, plusButton, minusButton, resetButton;
    ToggleButton lightButton, regButton, strongButton;
    int bloomMinutes, bloomSeconds, brewSeconds, seekBarMin, seekBarMax;
    boolean brewFinished;
    double originalRatio, ratio, seekBarStep;
    double waterUnits, coffeeUnits;
    SwitchCompat coffeeSwitch, waterSwitch;
    SeekBar scaleSlider;
    LinearLayout dummyFocus;
    boolean metric, isWaterMetric, isCoffeeMetric;
    SendBrew mCallback;
    private boolean plusMinusButtonClicked;


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
        waterWeightEdit = getActivity().findViewById(R.id.water_weight_text_view);
        dummyFocus = rootView.findViewById(R.id.dummy_focus);
        coffeeWeightEdit = getActivity().findViewById(R.id.coffee_weight_text_view);


        waterWeightEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                double newWaterUnits = parseDoubleSafely(s.toString());
                if(newWaterUnits != waterUnits && waterWeightEdit.isFocused()) {
                    setWaterUnits(newWaterUnits);
                    updateSeekBar();
                    updateCoffeeMeasurementView();
                }

            }
        });
        coffeeWeightEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                double newCoffeeUnits = parseDoubleSafely(s.toString());
                if (newCoffeeUnits != coffeeUnits && coffeeWeightEdit.isFocused()) {
                    setCoffeeUnits(newCoffeeUnits);
                    updateSeekBar();
                    updateWaterMeasurementView();
                }
            }
        });

//        Spinner spinner = rootView.findViewById(R.id.strength_spinner);
//// Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
//                R.array.brew_strength_array, android.R.layout.simple_spinner_item);
//// Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(R.layout.my_simple_spinner_dropdown_item);
//// Apply the adapter to the spinner
//        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(this);

        DecimalFormat df = new DecimalFormat("#.##");
        stepTv = getActivity().findViewById(R.id.step_tv);

        waterUnits = br.getWaterUnits();
        coffeeUnits = br.getCoffeeUnits();
        ratio = waterUnits/coffeeUnits;
        originalRatio = ratio;

        resetButton = rootView.findViewById(R.id.reset_button);
        resetButton.setOnClickListener(this);


        minusButton = rootView.findViewById(R.id.minus_volume_button);
        minusButton.setOnClickListener(this);
        plusButton = rootView.findViewById(R.id.plus_volume_button);
        plusButton.setOnClickListener(this);
        plusMinusButtonClicked = false;

        waterSwitch = rootView.findViewById(R.id.water_units_switch);
        coffeeSwitch = rootView.findViewById(R.id.coffee_units_switch);
        waterSwitch.setOnCheckedChangeListener(this);
        coffeeSwitch.setOnCheckedChangeListener(this);
//      -Handled in updateMeasurementViews()
//        coffeeWeightEdit.setText(String.valueOf(coffeeUnits));
//        waterWeightEdit.setText(String.valueOf(waterUnits));

        textTimer = getActivity().findViewById(R.id.tvTimeCount);
        bloomSeconds = br.getBloomTime();
        lightButton = rootView.findViewById(R.id.light_toggle_button);
        regButton = rootView.findViewById(R.id.regular_toggle_button);
        strongButton = rootView.findViewById(R.id.strong_toggle_button);
        regButton.setChecked(true);
        lightButton.setOnCheckedChangeListener(this);
        regButton.setOnCheckedChangeListener(this);
        strongButton.setOnCheckedChangeListener(this);
        waterUnitsTv = getActivity().findViewById(R.id.water_units_text_view);
        coffeeUnitsTv = getActivity().findViewById(R.id.coffee_units_text_view);
        metric = br.isMetric();
        brewSeconds = br.getBrewTime();
        if (bloomSeconds == 0) {
            stepTv.setText("Brew");
        }

        if (br.isMetric()) {
            coffeeUnitsTv.setText("Grams");
            isCoffeeMetric = true;
            waterUnitsTv.setText("Grams");
            isWaterMetric = true;
            seekBarStep = 1.0;
        } else {
            coffeeSwitch.setChecked(true);
            waterSwitch.setChecked(true);
            coffeeUnitsTv.setText("Ounces");
            waterUnitsTv.setText("Ounces");
            isWaterMetric = false;
            isCoffeeMetric = false;
            seekBarStep = 0.1;
        }

        scaleSlider = rootView.findViewById(R.id.scaleSeekBar);
        updateSeekBar();
        scaleSlider.setOnSeekBarChangeListener(this);



        textTimer.setText(String.format("%02d", bloomSeconds / 60) + ":" + String.format("%02d", bloomSeconds % 60));
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

        updateMeasurementViews();

        return rootView;
    }

    private void updateSeekBar() {
        if (isWaterMetric) {
            seekBarStep = 1;
            seekBarMax = (int) (waterUnits * 2);
            scaleSlider.setMax(seekBarMax);
            scaleSlider.setProgress((int) waterUnits);
        }
        else {
            seekBarStep = 0.1;
            seekBarMax = (int) (waterUnits * 2) * 10;
            scaleSlider.setMax(seekBarMax);
            scaleSlider.setProgress((int) (waterUnits * 10));
        }
        Log.i("Slider Progress", "Slider max set to: " + scaleSlider.getMax());
        Log.i("Slider Progress", "Slider progress set to: " + scaleSlider.getProgress());

    }

    private void startBrewTimer(final int seconds) {
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

    public static double parseDoubleSafely(String str) {
        double result = 0;
        try {
            result = Double.parseDouble(str);
        } catch (NullPointerException npe) {
        } catch (NumberFormatException nfe) {
        }
        return result;
    }


    private void startBloomTimer(final int seconds) {
        if (seconds == 0) {
            startBrewTimer(brewSeconds);
        } else {
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

    private void hideKeyboard() {
        InputMethodManager mImMan = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        mImMan.hideSoftInputFromWindow(dummyFocus.getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        //convert to switch statement
       dummyFocus.requestFocus();
       hideKeyboard();

        Button btn = (Button) v;

        if (v == startButton) {
            startBloomTimer(bloomSeconds);
            brewFinished = false;
            btn.setEnabled(false);
        }
        else if (v == resetButton) {
            ((BrewRecipeActivity) getActivity()).update();
        }

        else if (v == plusButton) {
            plusMinusButtonClicked = true;
            scaleSlider.incrementProgressBy(1);
        }
        else if (v == minusButton) {
            plusMinusButtonClicked = true;
            scaleSlider.incrementProgressBy(-1);
        }
    }

    public double convertGramsToOz(double grams) {
        return 0.035274 * grams;
    }

    public double convertOzToGrams(double oz) {
        return oz * 28.35;
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser || plusMinusButtonClicked) {
            dummyFocus.requestFocus();
            setWaterUnits(progress * seekBarStep);
            updateMeasurementViews();
            plusMinusButtonClicked = false;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void setWaterUnits(double newUnits) {
        waterUnits = newUnits;
        if(isCoffeeMetric != isWaterMetric) {
            if(isWaterMetric) {
                coffeeUnits = convertGramsToOz(newUnits/ratio);
            }
            else {
                coffeeUnits = convertOzToGrams(newUnits/ratio);
            }
        }
        else {
            coffeeUnits = newUnits/ratio;
        }
    }

    private void setCoffeeUnits(double newUnits) {
        coffeeUnits = newUnits;
        if(isCoffeeMetric != isWaterMetric) {
            if(isCoffeeMetric) {
                waterUnits = convertGramsToOz(newUnits * ratio);
            }
            else {
                waterUnits = convertOzToGrams(newUnits * ratio);
            }
        }
        else {
            waterUnits = newUnits * ratio;
        }
    }


    private void updateMeasurementViews() {
            if (waterUnitsTv.getText().toString().equalsIgnoreCase("grams")) {
                waterWeightEdit.setText(String.format("%.0f", waterUnits));
            } else {
                waterWeightEdit.setText(String.format("%.2f", waterUnits));
            }
            if (coffeeUnitsTv.getText().toString().equalsIgnoreCase("grams")) {
                coffeeWeightEdit.setText(String.format("%.1f", coffeeUnits));
            } else {
                coffeeWeightEdit.setText(String.format("%.2f", coffeeUnits));
            }

    }

    private void updateCoffeeMeasurementView() {
        if (coffeeUnitsTv.getText().toString().equalsIgnoreCase("grams")) {
            coffeeWeightEdit.setText(String.format("%.1f", coffeeUnits));
        } else {
            coffeeWeightEdit.setText(String.format("%.2f", coffeeUnits));
        }

    }

    private void updateWaterMeasurementView() {
        if (waterUnitsTv.getText().toString().equalsIgnoreCase("grams")) {
            waterWeightEdit.setText(String.format("%.0f", waterUnits));
        } else {
            waterWeightEdit.setText(String.format("%.2f", waterUnits));
        }

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

       dummyFocus.requestFocus();
       hideKeyboard();

       if (buttonView == waterSwitch) {
            if (isChecked) {
                isWaterMetric = false;
                waterUnitsTv.setText("Ounces");
                setWaterUnits(convertGramsToOz(waterUnits));
                seekBarStep = 0.1;
                updateSeekBar();

            } else {
                waterUnitsTv.setText("Grams");
                isWaterMetric = true;
                setWaterUnits(convertOzToGrams(waterUnits));
                seekBarStep = 1;
                updateSeekBar();
            }
        } else if (buttonView == coffeeSwitch) {
            if (isChecked) {
                coffeeUnitsTv.setText("Ounces");
                isCoffeeMetric = false;
                setCoffeeUnits(convertGramsToOz(coffeeUnits));
                updateSeekBar();

            } else {
                coffeeUnitsTv.setText("Grams");
                isCoffeeMetric = true;
                setCoffeeUnits(convertOzToGrams(coffeeUnits));
                updateSeekBar();
            }
        }
        else if (isChecked) {
            if (buttonView != lightButton) {
                lightButton.setChecked(false);

            }
            if (buttonView != regButton) {
                regButton.setChecked(false);
            }
            if (buttonView != strongButton) {
                strongButton.setChecked(false);
            }

            if (buttonView == regButton) {
                ratio = originalRatio;
                if (isCoffeeMetric != isWaterMetric) {
                    if (isWaterMetric) {
                        setCoffeeUnits(convertGramsToOz(waterUnits/ratio));
                    }
                    else {
                        setCoffeeUnits(convertOzToGrams(waterUnits/ratio));
                    }
                }
                else
                    setCoffeeUnits(waterUnits/ratio);
            } else if (buttonView == lightButton) {
                ratio = 1.1 * originalRatio;
                if (isCoffeeMetric != isWaterMetric) {
                    if (isWaterMetric) {
                        setCoffeeUnits(convertGramsToOz(waterUnits/ratio));
                    }
                    else {
                        setCoffeeUnits(convertOzToGrams(waterUnits/ratio));
                    }
                }
                else
                    setCoffeeUnits(waterUnits/ratio);
            } else if (buttonView == strongButton) {
                ratio = .9 * originalRatio;
                if (isCoffeeMetric != isWaterMetric) {
                    if (isWaterMetric) {
                        setCoffeeUnits(convertGramsToOz(waterUnits/ratio));
                    }
                    else {
                        setCoffeeUnits(convertOzToGrams(waterUnits/ratio));
                    }
                }
                else
                    setCoffeeUnits(waterUnits/ratio);
            }

        }
        updateMeasurementViews();

    }


    public interface SendBrew {
        void sendBrewRecipe(BrewRecipe brew);
    }
}
