package com.example.peter.coffeekeeper.RestClients;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.peter.coffeekeeper.Models.BrewRecipe;
import com.example.peter.coffeekeeper.Controllers.MainActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by peter on 2/20/18.
 */

public class UserRestClient {
    private static final String BASE_URL = "https://coffeekeeper.host/api/";
    private static final String BREW_ENDPOINT = "brews";


    private static AsyncHttpClient client = new AsyncHttpClient();


    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }
    public static void get(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
        Log.wtf("token", sharedPrefs.getString("token", ""));
        if(sharedPrefs.contains("token") && sharedPrefs.getString("token", "").length() > 4){
            client.addHeader("x-access-token", sharedPrefs.getString("token", ""));
        }
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler){
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }
    public static void put(String url, RequestParams params, AsyncHttpResponseHandler responseHandler){
        client.put(getAbsoluteUrl(url), params, responseHandler);
    }
    public static void delete(String url, RequestParams params, AsyncHttpResponseHandler responseHandler){
        client.delete(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void postBrew(BrewRecipe brew, Context context, AsyncHttpResponseHandler responseHandler){
        SharedPreferences prefs = context.getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
        String token = prefs.getString(MainActivity.PREFS_API_TOKEN, "");
        int userID = prefs.getInt(MainActivity.PREFS_USER_ID, 0);

        RequestParams params = new RequestParams();
        //add token
        params.add("token", token);
        //add all key value pairs
        params.add("user_id", String.valueOf(userID));
        params.add("brew_name", brew.getName());
        params.add("brew_date", brew.getDateAdded());
        params.add("brew_method", brew.getBrewMethod());
        params.add("water_units", String.valueOf(brew.getWaterUnits()));
        params.add("coffee_units", String.valueOf(brew.getCoffeeUnits()));
        params.add("water_metric", (brew.getWaterMetric() == 1) ? "true" : "false" );
        params.add("coffee_metric", (brew.getCoffeeMetric() == 1) ? "true" : "false" );
        params.add("notes", brew.getNotes());
        params.add("grind", brew.getGrind());
        params.add("bloom_time", String.valueOf(brew.getBloomTime()));
        params.add("brew_time", String.valueOf(brew.getBrewTime()));
        params.add("temperature", "0");
        client.post(getAbsoluteUrl(BREW_ENDPOINT), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relUrl) {
        return BASE_URL + relUrl;
    }

    public static BrewRecipe toBrewRecipe(JSONObject brewJson) throws JSONException {

        BrewRecipe recipe = new BrewRecipe();
        recipe.setName(brewJson.getString("brew_name"));
        recipe.setDateAdded(brewJson.get("brew_date").toString().substring(0, 10));
        recipe.setPoints(brewJson.getInt("points"));
        recipe.setBrewMethod(brewJson.getString("brew_method"));
        recipe.setWaterUnits(Double.parseDouble(brewJson.get("water_units").toString()));
        recipe.setCoffeeUnits(Double.parseDouble(brewJson.get("coffee_units").toString()));
        recipe.setCoffeeMetric(brewJson.getBoolean("coffee_metric") ? 1 : 0);
        recipe.setWaterMetric(brewJson.getBoolean("water_metric") ? 1 : 0);
        recipe.setNotes(brewJson.getString("notes"));
        recipe.setGrind(brewJson.getString("grind"));
        recipe.setBrewTime(brewJson.getInt("brew_time"));
        recipe.setBloomTime(brewJson.getInt("bloom_time"));
        return recipe;
    }
}
