package com.example.vtree.euvote_lh;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.vtree.euvote_lh.app.AppSingleton;
import com.example.vtree.euvote_lh.utils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements Serializable{

    Button btnSelectCountry,btnRemain,btnLeave,btnFindout;
    Snackbar snackbar;
    private static final String TAG = "MainActivity";
    // list contains name countries
    private static  List<String> listNameCountry;
    // list contains id countries
    private static List<String> listIdCountry;
    boolean doubleBackToExitPressedOnce = false;
    static MainActivity mainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        volleyJsonObjectRequest(Const.JSON_OBJECT_REQUEST_URL);
            // Set event listenner for button select country
            btnSelectCountry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // push list contains name country to selectcountry activity
                    if(listNameCountry.size()>0){

                        Intent myIntent = new Intent(MainActivity.this,SelectCountryActivity.class);
                        myIntent.putExtra("listnamecountry",(Serializable) listNameCountry);
                        myIntent.putExtra("listidcountry",(Serializable)listIdCountry);
                        startActivity(myIntent);
                    } else {
                        showDialog();
                    }
                }
            });
        // handle event when button vote being click
        btnRemain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notVote(btnRemain);
            }
        });
        btnLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notVote(btnLeave);
            }
        });

        // handle event when button findout being click -> go to activity find out
        btnFindout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this,FindOutActivity.class);
                startActivity(myIntent);
            }
        });
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    /**
     * init components
     */
    public void init(){
        mainActivity = this;
        listNameCountry = new ArrayList<>();
        listIdCountry = new ArrayList<>();
        btnSelectCountry = (Button)findViewById(R.id.btnSelectCountry);
        btnRemain = (Button)findViewById(R.id.btnRemain);
        btnLeave = (Button)findViewById(R.id.btnLeave);
        btnFindout = (Button)findViewById(R.id.btnFindout);
    }


    /**
     * Get info of Country object on server
     */
    public  List<String> volleyJsonObjectRequest(String url){

        String  REQUEST_TAG = "com.example.vtree.euvote_lh";
        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        //get data
                        JSONObject obj = response;
                            JSONArray array = null;
                            try {
                                array = obj.getJSONArray("data");
                                for(int i = 0 ; i < array.length() ; i++){
                                    listNameCountry.add(array.getJSONObject(i).getString("name"));
                                    listIdCountry.add(array.getJSONObject(i).getString("id"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        });

        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq,REQUEST_TAG);
        return listNameCountry;
    }
    /**
     * Show dialog
     */
    private void showDialog(){
         snackbar = Snackbar
                .make(btnSelectCountry, "No internet connection!",Snackbar.LENGTH_LONG)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                        startActivity(getIntent());
                    }
                }).setDuration(Snackbar.LENGTH_INDEFINITE);

// Changing message text color
        snackbar.setActionTextColor(Color.RED);

// Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    /**
     * Not allow vote before select country
     */
    private void notVote(Button b){
        snackbar = Snackbar
                .make(b, "Please select your country before vote action!",Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        finish();
//                        startActivity(getIntent());
                    }
                }).setDuration(Snackbar.LENGTH_INDEFINITE);

// Changing message text color
        snackbar.setActionTextColor(Color.RED);

// Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    public static MainActivity getInstance(){
        return   mainActivity;
    }
}
