//TODO Rethink the layout of spinner and grind. Fix layouts in landscape
//Test
package com.example.peter.coffeekeeper.Controllers;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.peter.coffeekeeper.Database.DBOperator;
import com.example.peter.coffeekeeper.R;
import com.example.peter.coffeekeeper.SectionsPageAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity implements BrewFragment.OnFragmentInteractionListener, RoastFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener {

    public static final String PREFS_NAME = "prefs";
    public static final String PREFS_PRO_PURCHASED = "isProPurchased";
    public static final String PREFS_USER_ID = "userID";
    public static final String PREFS_API_TOKEN = "token";
    public static final String PREFS_USER_POINTS = "userPoints";
    public static final String PREFS_USERNAME = "username";

    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    private FloatingActionButton addButton;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private AdView adView;
    private PopupWindow popupWindow;

    DBOperator mDBOperator;
    String test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, "ca-app-pub-4742169084911884~8092065487");


        mDBOperator = new DBOperator(this);
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("My Recipes");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        NavigationView nv = findViewById(R.id.nvView);
        nv.setNavigationItemSelectedListener(this);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        drawerToggle.syncState();


        addButton = findViewById(R.id.fab);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int current = mViewPager.getCurrentItem();
                Log.v("CurrentPage", Integer.toString(current));
                if (current == 0) {
                    Intent intent = new Intent(v.getContext(), AddBrew.class);
                    startActivityForResult(intent, 1);
                } else if (current == 1) {
                    Intent intent = new Intent(v.getContext(), AddRoast.class);
                    startActivityForResult(intent, 1);

                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (!closeDrawer())
            finish();
    }

    private boolean closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
            return true;
        }
        return false;
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new BrewFragment(), "Brew");
        adapter.addFragment(new RoastFragment(), "Roast");
        viewPager.setAdapter(adapter);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction2(Uri uri) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().getFragments().get(0);
        fragment.onActivityResult(requestCode, resultCode, data);
        Fragment fragment2 = getSupportFragmentManager().getFragments().get(1);
        fragment2.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AddRoast.GO_TO_ROASTS || resultCode == AddRoast.ROAST_DB_CHANGED) {
            mViewPager.setCurrentItem(1);
            Log.i("pager", "setting to page " + 1);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                drawerLayout.openDrawer(GravityCompat.START);
//                return true;
//        }

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_contact:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "coffeekeeperbrewingandroasting@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for Coffee Keeper");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                break;
            case R.id.nav_profile:
                launchProfile();
                break;
            case R.id.nav_discover:
                launchDiscover();
                break;
            case R.id.nav_feedback:
                launchMarket();
                break;
            case R.id.nav_help:
                showHelpPopup();
                break;

        }
        return true;
    }

    private void launchDiscover() {
        Intent intent = new Intent(this, DiscoverActivity.class);
        startActivity(intent);
        finish();
    }

    private void launchProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    private void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }

    private void showHelpPopup() {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }
}