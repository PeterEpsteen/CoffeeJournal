package com.example.peter.coffeekeeper.Controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.peter.coffeekeeper.R;

/*TODO
    -make fragments for each brew and roast. Clicking them launches activity with details, comments, save
        local
    -setup nav menu
            */
public class DiscoverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //check if logged in, if not launch register activity

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);
    }
}
