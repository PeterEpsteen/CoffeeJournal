//TODO Rethink the layout of spinner and grind. Fix layouts in landscape
//Test
package com.example.peter.coffeejournal;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BrewFragment.OnFragmentInteractionListener, RoastFragment.OnFragmentInteractionListener {

    //TODO handle correct tab on back pressed

    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    private FloatingActionButton addButton;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private AdView adView;

    DBOperator mDBOperator;
    String test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
        }
        catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

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
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

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
                }

                else if (current == 1) {
                    Intent intent = new Intent(v.getContext(), AddRoast.class);
                    startActivityForResult(intent, 1);

                }
            }
        });



    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open,  R.string.drawer_close);
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

    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new BrewFragment(), "Brew");
        adapter.addFragment(new RoastFragment(), "Roast" );
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
        if (resultCode == AddRoast.GO_TO_ROASTS || resultCode == AddRoast.ROAST_DB_CHANGED) {
            mViewPager.setCurrentItem(1);
        }
        Fragment fragment = getSupportFragmentManager().getFragments().get(0);
        fragment.onActivityResult(requestCode, resultCode, data);
        Fragment fragment2 = getSupportFragmentManager().getFragments().get(1);
        fragment2.onActivityResult(requestCode, resultCode, data);
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
}

/** Just did:
        Can add, delete brews
    TODO:
        Set up recipe page

    Bugs:
        Moka pot image doesnt load


**/