package com.example.peter.coffeekeeper.Controllers;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.peter.coffeekeeper.R;

public class BrewDetailsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private TextView brewTitle, usernameTv, brewMethodTv, coffeeUnitsTv, waterUnitsTv, likesCountTv,
            bloomTimeTv, brewTimeTv, brewNotesTv;
    private Button likeButton, downloadButton, commentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brew_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //Text Views
        brewTitle = findViewById(R.id.brew_title_text_view);
        usernameTv = findViewById(R.id.username_text_view);
        brewMethodTv = findViewById(R.id.brew_method_text_view);
        coffeeUnitsTv = findViewById(R.id.coffee_units_text_view);
        waterUnitsTv = findViewById(R.id.water_units_text_view);
        brewTimeTv = findViewById(R.id.brew_time_text_view);
        bloomTimeTv = findViewById(R.id.bloom_time_text_view);
        likesCountTv = findViewById(R.id.likes_text_view);
        brewNotesTv = findViewById(R.id.notes_text_view);

        //Buttons and listeners
        commentButton = findViewById(R.id.comment_button);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentOnBrew();
            }
        });

        likeButton = findViewById(R.id.like_button);
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeBrew();
            }
        });

        downloadButton = findViewById(R.id.download_button);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadBrew();
            }
        });
    }

    private void downloadBrew() {
    }

    private void likeBrew() {

    }
    private void commentOnBrew() {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {

    }
}
