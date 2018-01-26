package com.example.peter.coffeekeeper;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by peter on 12/6/17.
 */

public class CoffeeWaterTextWatcher implements TextWatcher {

    EditText coffeeEdit, waterEdit;
    double waterCoffeeRatio;

    public CoffeeWaterTextWatcher(EditText coffeeEdit, EditText waterEdit, double waterCoffeeRatio) {
        this.coffeeEdit = coffeeEdit;
        this.waterEdit = waterEdit;
        this.waterCoffeeRatio = waterCoffeeRatio;
    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
