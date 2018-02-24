package com.example.peter.coffeekeeper.Controllers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.peter.coffeekeeper.R;

public class RegisterActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RegisterFragment.OnFragmentInteractionListener, LoginFragment.OnFragmentInteractionListener {

    //if they haven't purchased the pro version, show purchase
    //if they purchased but arent signed in show login with link to sign up


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState!=null) {return;}

        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.backgroundDefaultWhite));
        setSupportActionBar(toolbar);
        SharedPreferences prefs = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
        //TODO check if pro is purchased first, if not launch payment thing
            getSupportActionBar().setTitle("Login");
            LoginFragment loginFrag = new LoginFragment();
            loginFrag.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, loginFrag)
                    .commit();


    }


    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onRegister() {
        LoginFragment loginFragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, loginFragment).addToBackStack(null).commit();
    }

    @Override
    public void onLogin() {
        Intent data = new Intent();
        data.putExtra("success", true);
        this.setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void register() {
        getSupportActionBar().setTitle("Register");
        RegisterFragment registerFragment = new RegisterFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, registerFragment).addToBackStack(null).commit();
    }
}
