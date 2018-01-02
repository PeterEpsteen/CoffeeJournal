package com.example.peter.coffeejournal;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.lang.reflect.Method;
import java.text.DecimalFormat;

public class BrewRecipeActivity extends AppCompatActivity implements BrewRecipeFragment.SendBrew {

    private BrewRecipe br;
    DBOperator db;
    private int waterUnits = 16;
    TextView waterWeightTv, coffeeWeightTv, grindTv, strengthTv, textTimer, stepTv, notesTv, titleTv;
    CountDownTimer countDownTimer;
    ProgressBar barTimer;
    Button showNotesButton;
    int bloomMinutes, bloomSeconds, brewSeconds;
    boolean brewFinished, isNotesShowing;
    android.support.v7.widget.Toolbar myBar;
    String name;
    LinearLayout bottomSheet;
    BottomSheetBehavior bottomSheetBehavior;
    ImageView imageView;
    AdView adView;

    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;



    //Testing
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brew_recipe);
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        name = getIntent().getExtras().getString("Brew Name");
        DBOperator dbOperator = new DBOperator(this);
        br = dbOperator.getBrewRecipe(name);
//        ActionBar ab = getSupportActionBar();
//        ab.setTitle(name);
//        ab.setElevation(0);
//        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        if(savedInstanceState == null) {
            BrewRecipeFragment brewRecipeFragment = new BrewRecipeFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frameLayout, brewRecipeFragment, "recipeFragment")
                    .disallowAddToBackStack()
                    .commit();
            isNotesShowing = false;
        }
        imageView = findViewById(R.id.notes_drop_icon);
        bottomSheet = findViewById(R.id.bottom_sheet);
        // init the bottom sheet behavior
         bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);



// set callback for changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED || newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    float deg = (imageView.getRotation() == 0F) ? 180F : 0F;
                    imageView.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        myBar = findViewById(R.id.my_bar);
        setSupportActionBar(myBar);
        ActionBar bar = getSupportActionBar();
        bar.setTitle(name);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowTitleEnabled(true);
        showNotesButton = findViewById(R.id.show_notes_button);
        final BrewNotesFragment notesFragment = new BrewNotesFragment();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                float deg = (v.getRotation() == 0F) ? 180F : 0F;
//                v.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
        showNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                float deg = (imageView.getRotation() == 0F) ? 180F : 0F;
//                imageView.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });



        notesTv = findViewById(R.id.notes_text_view);
        grindTv = findViewById(R.id.brew_grind_notes_text_view);
        titleTv = findViewById(R.id.brew_title_notes_text_view);
        notesTv.setText(br.getNotes());
        titleTv.setText(name);
        grindTv.setText(br.getGrind());
//
    }

    @Override
    public void onBackPressed() {
        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        else {
            super.onBackPressed();
        }
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
        String grind = brew.getGrind();
//        BrewNotesFragment bn = (BrewNotesFragment) getSupportFragmentManager().findFragmentByTag("notesFragment");
//        bn.setTitle(brew.getName());
//        bn.setNotes(notes);
//        bn.setGrind(grind);
    }
}
