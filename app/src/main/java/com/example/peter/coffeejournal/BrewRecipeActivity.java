package com.example.peter.coffeejournal;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import java.lang.reflect.Method;
import java.text.DecimalFormat;

public class BrewRecipeActivity extends AppCompatActivity implements BrewRecipeFragment.SendBrew {

    private BrewRecipe br;
    DBOperator db;
    private int waterUnits = 16;
    TextView waterWeightTv, coffeeWeightTv, grindTv, strengthTv, textTimer, stepTv;
    CountDownTimer countDownTimer;
    ProgressBar barTimer;
    Button startButton;
    int bloomMinutes, bloomSeconds, brewSeconds;
    boolean brewFinished;
    android.support.v7.widget.Toolbar myBar;
    String name;

    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;



    //Testing
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = getIntent().getExtras().getString("Brew Name");
//        ActionBar ab = getSupportActionBar();
//        ab.setTitle(name);
//        ab.setElevation(0);
//        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        setContentView(R.layout.activity_brew_recipe);
        myBar = findViewById(R.id.my_bar);
        setSupportActionBar(myBar);

        ActionBar bar = getSupportActionBar();
        bar.setTitle(name);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowTitleEnabled(true);

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container2);
        setupViewPager(mViewPager);

        TabLayout tabLayout = findViewById(R.id.tabs2);
        tabLayout.setupWithViewPager(mViewPager);


    }

    @Override
    public void onConfigurationChanged(final Configuration config) {
        super.onConfigurationChanged(config);
//        TextView title = findViewById(R.id.landscape_title);
//        title.setText(name);

    }

    private void forceTabs() {
        try {
            final android.app.ActionBar actionBar = getActionBar();
            final Method setHasEmbeddedTabsMethod = actionBar.getClass().getDeclaredMethod("setHasEmbeddedTabs", boolean.class);
            setHasEmbeddedTabsMethod.setAccessible(true);
            setHasEmbeddedTabsMethod.invoke(actionBar, true);
        }
        catch (Exception e){}
    }


    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new BrewRecipeFragment(), "Recipe");
        adapter.addFragment(new BrewNotesFragment(), "Notes" );
        viewPager.setAdapter(adapter);
    }

    public void update(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public void sendBrewRecipe(BrewRecipe brew) {
        String notes = brew.getNotes();
        BrewNotesFragment bn = (BrewNotesFragment) getSupportFragmentManager().getFragments().get(1);
        bn.setNotes(notes);
    }
}
