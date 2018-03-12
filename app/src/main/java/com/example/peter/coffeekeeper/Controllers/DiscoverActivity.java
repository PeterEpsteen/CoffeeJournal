package com.example.peter.coffeekeeper.Controllers;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.peter.coffeekeeper.Adapters.DiscoverBrewAdapter;
import com.example.peter.coffeekeeper.Models.BrewRecipe;
import com.example.peter.coffeekeeper.R;
import com.example.peter.coffeekeeper.RestClients.UserRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class DiscoverActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ArrayList<BrewRecipe> brews = new ArrayList<>();
    private int userID;
    private String token;
    RecyclerView recyclerView;
    DiscoverBrewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadBrew();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        recyclerView = findViewById(R.id.discover_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new DiscoverBrewAdapter(brews, this);
        recyclerView.setAdapter(adapter);
        SharedPreferences prefs = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
        userID = prefs.getInt(MainActivity.PREFS_USER_ID, 0);
        token = prefs.getString(MainActivity.PREFS_API_TOKEN, "none");
        if(userID == 0 || token.equals("none")) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivityForResult(intent, 0);
        }
        else {
            fillBrewList();
        }
    }

    private void uploadBrew() {
        UploadBrewDialogFragment uploadFrag = new UploadBrewDialogFragment();
        uploadFrag.show(getFragmentManager(), "uploadBrew");
    }

    private void fillBrewList(){
        RequestParams params = new RequestParams();
        UserRestClient.get(this, "brews?pageNo=1", params, new JsonHttpResponseHandler() {
            //TODO get more info from brew list - function for serializing
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray jsonBrewsArray = response.getJSONArray("data");
                    ArrayList<BrewRecipe> brewList = new ArrayList<>();
                    for(int i = 0; i < jsonBrewsArray.length(); i++) {
                        BrewRecipe recipe = new BrewRecipe();
                        JSONObject brewJson = jsonBrewsArray.getJSONObject(i);
                         recipe = UserRestClient.toBrewRecipe(brewJson);
                         recipe.setUserName(brewJson.getString("username"));
                         brewList.add(recipe);
                    }
                    brews = new ArrayList<>(brewList);
                    updateBrewList();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                try {
                    Toast.makeText(getApplicationContext(), errorResponse.get("message").toString(), Toast.LENGTH_LONG);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void updateBrewList(){
        adapter.setBrews(brews);
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
//        getMenuInflater().inflate(R.menu.drawer_view_pro, menu);
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
        switch (item.getItemId()) {
            case R.id.nav_profile:
                Intent intent1 = new Intent(this, ProfileActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.nav_my_recipes:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_contact:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","coffeekeeperbrewingandroasting@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for Coffee Keeper");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                break;
            case R.id.nav_discover:
                break;
            case R.id.nav_feedback:
                launchMarket();
                break;
            case R.id.nav_help:
                showHelpPopup();
                break;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void launchDiscover() {

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
