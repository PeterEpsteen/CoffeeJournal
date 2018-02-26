package com.example.peter.coffeekeeper.Controllers;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peter.coffeekeeper.Adapters.UploadBrewAdapter;
import com.example.peter.coffeekeeper.Adapters.UserBrewListAdapter;
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

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DialogInterface.OnDismissListener, View.OnClickListener, UploadBrewAdapter.UploadBrewInterface, UserBrewListAdapter.UserBrewListListener {

    //check if logged in, if so show profile, if not show login/signup activity
    private ActionBarDrawerToggle drawerToggle;
    private Button uploadBrewButton;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private TextView usernameTextView, pointsTv;
    private int userID;
    private RecyclerView brewRecyclerView;
    private ArrayList<BrewRecipe> userBrews;
    private UserBrewListAdapter brewListAdapter;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userBrews = new ArrayList<>();
        brewRecyclerView = findViewById(R.id.user_brews_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        brewRecyclerView.setLayoutManager(linearLayoutManager);
        brewListAdapter = new UserBrewListAdapter(userBrews, this, this);
        brewRecyclerView.setNestedScrollingEnabled(false);
        brewRecyclerView.setAdapter(brewListAdapter);
        SharedPreferences prefs = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
        userID = prefs.getInt(MainActivity.PREFS_USER_ID, 0);
        token = prefs.getString(MainActivity.PREFS_API_TOKEN, "none");
        if(userID == 0 || token.equals("none")) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivityForResult(intent, 0);
        }
        else {
            initializeProfile();
        }


    }

    public void updateUserID() {
        SharedPreferences prefs = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
        userID = prefs.getInt(MainActivity.PREFS_USER_ID, 0);
        token = prefs.getString(MainActivity.PREFS_API_TOKEN, "none");
    }

    private void initializeProfile(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView nv = findViewById(R.id.nvView);
        nv.setNavigationItemSelectedListener(this);
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        drawerToggle.syncState();
        usernameTextView = findViewById(R.id.username_text_view);
        pointsTv = findViewById(R.id.points_text_view);
        uploadBrewButton = findViewById(R.id.upload_brew_button);
        uploadBrewButton.setOnClickListener(this);
        setUsernameTextView();
        populateBrews();
        populateRoasts();
    }
    private void populateBrews(){
        updateUserID();
        RequestParams params = new RequestParams();
        final ArrayList<BrewRecipe> userBrewsBackend = new ArrayList<>();
        UserRestClient.get(getApplication(), "brews/user/"+userID, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                    try {
                        for (int i = 0; i < response.length(); i++) {
                            userBrewsBackend.add(UserRestClient.toBrewRecipe(response.getJSONObject(i)));
                            Log.d("JSON", response.getJSONObject(i).toString());
                        }
                        userBrews = new ArrayList<>(userBrewsBackend);
                        updateBrewList();
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray array = response.getJSONArray("data");
                    for(int i = 0; i < array.length(); i++) {
                        userBrewsBackend.add(UserRestClient.toBrewRecipe(array.getJSONObject(i)));
                    }
                    userBrews = new ArrayList<>(userBrewsBackend);
                    updateBrewList();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return true;
    }



    private void updateBrewList(){
        brewListAdapter.setBrews(userBrews);
    }
    private void populateRoasts(){

    }
    private boolean closeDrawer(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(Gravity.LEFT);
            return true;
        }
        return false;
    }

    private void signOut(){
        //clear shared prefs then relaunch home
        SharedPreferences preferences = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(MainActivity.PREFS_USERNAME);
        editor.remove(MainActivity.PREFS_USER_ID);
        editor.remove(MainActivity.PREFS_USER_POINTS);
        editor.remove(MainActivity.PREFS_API_TOKEN);
        editor.apply();
        goHome();
    }

    private void goHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setUsernameTextView(){
        SharedPreferences prefs = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
        userID = prefs.getInt(MainActivity.PREFS_USER_ID, 0);
        int points = prefs.getInt(MainActivity.PREFS_USER_POINTS, 0);
        token = prefs.getString(MainActivity.PREFS_API_TOKEN, "none");
        String username = prefs.getString(MainActivity.PREFS_USERNAME, "none");
        if(username.equals("none") || !prefs.contains(MainActivity.PREFS_USER_POINTS)){
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivityForResult(intent, 0);
        }
        else {
            usernameTextView.setText(username);
            pointsTv.setText(String.valueOf(points));
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_CANCELED) {
            goHome();
        }
        else {
            initializeProfile();
        }
    }

    @Override
    public void onBackPressed() {
        if (!closeDrawer()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_signout:
                signOut();
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle()            does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string              .drawer_open,  R.string.drawer_close);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        drawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_profile:
                closeDrawer();
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

    private void uploadBrew() {
        UploadBrewDialogFragment uploadFrag = new UploadBrewDialogFragment();
        uploadFrag.show(getFragmentManager(), "uploadBrew");
    }

    private void launchDiscover() {
        Intent intent = new Intent(this, DiscoverActivity.class);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.upload_brew_button:
                uploadBrew();
                break;
        }
    }

    @Override
    public void upload(BrewRecipe brew) {
        Toast.makeText(this, "Uploading brew: "+brew.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    public void deleteBrew(BrewRecipe brew) {
        RequestParams params = new RequestParams();
        UserRestClient.delete(getApplicationContext(),"brews/"+userID+"/"+brew.getName(), params, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                try {
                    Toast.makeText(getApplicationContext(), errorResponse.get("message").toString(), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Toast.makeText(getApplicationContext(), response.get("message").toString(), Toast.LENGTH_LONG).show();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        populateBrews();
    }
}
