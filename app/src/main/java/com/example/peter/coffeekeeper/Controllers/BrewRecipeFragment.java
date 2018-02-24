package com.example.peter.coffeekeeper.Controllers;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.peter.coffeekeeper.Database.DBOperator;
import com.example.peter.coffeekeeper.Models.BrewRecipe;
import com.example.peter.coffeekeeper.R;

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
    double waterUnits, coffeeUnits, visibleCoffeeUnits, visibleWaterUnits, multiplier;
    SwitchCompat coffeeSwitch, waterSwitch;
    SeekBar scaleSlider;
    LinearLayout dummyFocus;
    boolean isWaterMetric, isCoffeeMetric;
    SendBrew mCallback;
    private boolean plusMinusButtonClicked;


    public BrewRecipeFragment() {
        // Required empty public constructor
    }

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

        //declare views
        waterWeightEdit = getActivity().findViewById(R.id.water_weight_text_view);
        dummyFocus = rootView.findViewById(R.id.dummy_focus);
        coffeeWeightEdit = getActivity().findViewById(R.id.coffee_weight_text_view);
        stepTv = getActivity().findViewById(R.id.step_tv);
        resetButton = getActivity().findViewById(R.id.reset_button);
        minusButton = rootView.findViewById(R.id.minus_volume_button);
        plusButton = rootView.findViewById(R.id.plus_volume_button);
        plusMinusButtonClicked = false;
        waterSwitch = rootView.findViewById(R.id.water_units_switch);
        coffeeSwitch = rootView.findViewById(R.id.coffee_units_switch);
        waterSwitch.setOnCheckedChangeListener(this);
        coffeeSwitch.setOnCheckedChangeListener(this);
        textTimer = getActivity().findViewById(R.id.tvTimeCount);
        lightButton = rootView.findViewById(R.id.light_toggle_button);
        regButton = rootView.findViewById(R.id.regular_toggle_button);
        strongButton = rootView.findViewById(R.id.strong_toggle_button);
        regButton.setChecked(true);
        waterUnitsTv = getActivity().findViewById(R.id.water_units_text_view);
        coffeeUnitsTv = getActivity().findViewById(R.id.coffee_units_text_view);
        scaleSlider = rootView.findViewById(R.id.scaleSeekBar);
        scaleSlider.setMax(200);
        scaleSlider.setProgress(100);
        multiplier = 1.0;
        barTimer = getActivity().findViewById(R.id.progressbarRL);
        startButton = getActivity().findViewById(R.id.start_button);

        //Set up brew info
        String brewName = getActivity().getIntent().getExtras().getString("Brew Name");
        db = new DBOperator(getContext());
        br = db.getBrewRecipe(brewName);
        waterUnits = br.getWaterUnits();
        coffeeUnits = br.getCoffeeUnits();
        visibleCoffeeUnits = coffeeUnits;
        visibleWaterUnits = waterUnits;
        //standardize for easier ratio handling
        isCoffeeMetric = br.isCoffeeMetric();
        isWaterMetric = br.isWaterMetric();
        waterUnits = (isWaterMetric) ? waterUnits : convertOzToGrams(waterUnits);
        coffeeUnits = (isCoffeeMetric) ? coffeeUnits : convertOzToGrams(coffeeUnits);
        ratio = waterUnits/coffeeUnits;
        originalRatio = ratio;
        bloomSeconds = br.getBloomTime();

        if(isWaterMetric)
            setWaterMetric();
        else
            setWaterImperial();
        if(isCoffeeMetric)
            setCoffeeMetric();
        else
            setCoffeeImperial();


        plusButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
        scaleSlider.setOnSeekBarChangeListener(this);
        lightButton.setOnCheckedChangeListener(this);
        regButton.setOnCheckedChangeListener(this);
        strongButton.setOnCheckedChangeListener(this);
        startButton.setOnClickListener(this);
        minusButton.setOnClickListener(this);

        brewSeconds = br.getBrewTime();
        if (bloomSeconds == 0) {
            stepTv.setText("Brew");
        }

        //send recipe for to parent
        try {
            mCallback = (SendBrew) getActivity();
            mCallback.sendBrewRecipe(br);
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement TextClicked");
        }
        updateMeasurementViews();
        textTimer.setText(String.format("%02d", bloomSeconds / 60) + ":" + String.format("%02d", bloomSeconds % 60));

        //text watcher for water units changing
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
                newWaterUnits = (isWaterMetric) ? newWaterUnits : convertOzToGrams(newWaterUnits);
                if(newWaterUnits != waterUnits && waterWeightEdit.isFocused()) {
                    setWaterUnits(newWaterUnits);
                }

            }
        });
        //same for coffee
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
                newCoffeeUnits = isCoffeeMetric ? newCoffeeUnits : convertOzToGrams(newCoffeeUnits);
                if (newCoffeeUnits != coffeeUnits && coffeeWeightEdit.isFocused()) {
                    setCoffeeUnits(newCoffeeUnits);
                }
            }
        });

        return rootView;
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
                }
                @Override
                public void onFinish() {
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
            plusMinusButtonClicked = false;
        }
        else if (v == minusButton) {
            plusMinusButtonClicked = true;
            scaleSlider.incrementProgressBy(-1);
            plusMinusButtonClicked = false;
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
            multiplier = progress/100.0;
            updateMeasurementViews();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    private void setWaterUnits(double newUnits) {
        waterUnits = newUnits;
        coffeeUnits = waterUnits / ratio;
        scaleSlider.setProgress(100);
        multiplier = 1.0;
        updateMeasurementViews();
    }

    private void setCoffeeUnits(double newUnits) {
        coffeeUnits = newUnits;
        waterUnits = coffeeUnits * ratio;
        scaleSlider.setProgress(100);
        multiplier = 1.0;
        updateMeasurementViews();
    }


    private void updateMeasurementViews() {
        if (!waterWeightEdit.isFocused()) {
            if (isWaterMetric) {
                waterWeightEdit.setText(String.format("%.0f", (multiplier * waterUnits)));
            } else {
                waterWeightEdit.setText(String.format("%.1f", convertGramsToOz((multiplier * waterUnits))));
            }
        }
        if (!coffeeWeightEdit.isFocused()) {
            if (isCoffeeMetric) {
                coffeeWeightEdit.setText(String.format("%.1f", (coffeeUnits * multiplier)));
            } else {
                coffeeWeightEdit.setText(String.format("%.2f", convertGramsToOz(coffeeUnits * multiplier)));
            }
        }

    }

    private void setWaterMetric() {
        if (waterSwitch.isChecked()) {
            waterSwitch.setChecked(false);
        }
        waterUnitsTv.setText("Grams");
        if (!isWaterMetric) {
            isWaterMetric = true;
            updateMeasurementViews();
        }
    }
    private void setWaterImperial() {
        if (!waterSwitch.isChecked()) {
            waterSwitch.setChecked(true);
        }
        if(isWaterMetric) {
            isWaterMetric = false;
            updateMeasurementViews();
        }
        waterUnitsTv.setText("Ounces");
    }
    private void setCoffeeMetric() {
        if(coffeeSwitch.isChecked()) {
            coffeeSwitch.setChecked(false);
        }
        coffeeUnitsTv.setText("Grams");
        if(!isCoffeeMetric) {
            isCoffeeMetric = true;
            updateMeasurementViews();
        }
    }
    private void setCoffeeImperial() {
        if(!coffeeSwitch.isChecked())
        {
            coffeeSwitch.setChecked(true);
        }
        coffeeUnitsTv.setText("Ounces");
        if(isCoffeeMetric) {
            isCoffeeMetric = false;
            updateMeasurementViews();
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

       dummyFocus.requestFocus();
       hideKeyboard();

       if (buttonView == waterSwitch) {
            if (isChecked) {
                setWaterImperial();
            } else {
                setWaterMetric();
            }
        } else if (buttonView == coffeeSwitch) {
            if (isChecked) {
                setCoffeeImperial();

            } else {
                setCoffeeMetric();
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
                setCoffeeUnits(waterUnits/ratio);
            } else if (buttonView == lightButton) {
                ratio = 1.1 * originalRatio;
                setCoffeeUnits(waterUnits/ratio);
            } else if (buttonView == strongButton) {
                ratio = .9 * originalRatio;
                setCoffeeUnits(waterUnits/ratio);
            }
            updateMeasurementViews();
        }
    }


    public interface SendBrew {
        void sendBrewRecipe(BrewRecipe brew);
    }
}
