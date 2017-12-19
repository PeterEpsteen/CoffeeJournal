//TODO Rethink the layout of spinner and grind. Fix layouts in landscape
//Test
package com.example.peter.coffeejournal;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BrewFragment.OnFragmentInteractionListener, RoastFragment.OnFragmentInteractionListener {

    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    private FloatingActionButton addButton;

    DBOperator mDBOperator;
    String test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDBOperator = new DBOperator(this);

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        addButton = findViewById(R.id.fab);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int current = mViewPager.getCurrentItem();
                Log.v("CurrentPage", Integer.toString(current));
                if (current == 0) {
                    Intent intent = new Intent(v.getContext(), AddBrew.class);
                    startActivity(intent);
                }

                else if (current == 1) {
                    Intent intent = new Intent(v.getContext(), AddRoast.class);
                    startActivity(intent);
                }
            }
        });



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


    public void showMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.inflate(R.menu.brew_menu);
        popup.show();
        View v = (View) view.getParent();
        TextView tv = v.findViewById(R.id.brew_title_text_view);
        String brewName = tv.getText().toString();
        popup.setOnMenuItemClickListener(new myMenuClickListener(brewName));
    }


    public void deleteBrew(String brewName) {
        boolean returnB = mDBOperator.deleteBrew(brewName);
        if (returnB) {
            recreate();
        }

    }

    public void editBrew(String brewName) {
        Intent myIntent = new Intent(this, AddBrew.class);
        myIntent.putExtra("Brew Name", brewName);
        startActivityForResult(myIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            Intent refresh = new Intent(this, MainActivity.class);
            startActivity(refresh);
            this.finish();
    }




    class myMenuClickListener implements PopupMenu.OnMenuItemClickListener {
        String brewName;

        public myMenuClickListener(String brewName) {
            this.brewName = brewName;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            // Handle item selection

            switch (item.getItemId()) {
                case R.id.menu_delete:
                    deleteBrew(brewName);
                    return true;
                case R.id.menu_edit:
                    editBrew(brewName);
                    return true;
                default:
                    return false;
            }
        }
    }
}

/** Just did:
        Can add, delete brews
    TODO:
        Set up recipe page

    Bugs:
        Moka pot image doesnt load


**/