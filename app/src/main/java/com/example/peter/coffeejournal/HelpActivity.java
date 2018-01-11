package com.example.peter.coffeejournal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    private  TextView welcomeText, brewText, roastText, otherText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        otherText = findViewById(R.id.other_text);
        welcomeText = findViewById(R.id.welcome_text);
        brewText = findViewById(R.id.brew_text);
        roastText = findViewById(R.id.roast_text);
        welcomeText.setText("Coffee Keeper is an app to keep track of custom brew recipes and roast profiles.");
        brewText.setText("The brew page allow users to create custom brew recipes, or view pre-loaded recipes.\n\n Tap the plus button to get started on a custom brew. \n\nAfter filling out the details, select the brew to start. Here you can customize the specific settings, and the ratio will stay consistent to the recipe. \n\n Pressing start will trigger a timer that will guide through a brew's bloom and total brew time.");
        roastText.setText("On the roast tab, tap the plus button to create a new entry. \n\nPressing the play icon will start a timer, and any steps added to the entry will auto-fill with the current time. Enter the beans used. Then, enter vital steps of your roast, along with temperatures. These steps and temperatures will be used to create a roast graph. \n\nAfter filling out the roast notes, you can view the roast which will show all the details, plus a visualized roast graph tracking bean tempature and roaster tempature.");
        otherText.setText("Please contact me with any questions, desires, or bugs and I promise I will address your concerns as quickly as possible. Thanks for using Coffee Keeper!");
    }


}
