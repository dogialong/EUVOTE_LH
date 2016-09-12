package com.example.vtree.euvote_lh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.vtree.euvote_lh.adapter.CountryAdapter;
import com.example.vtree.euvote_lh.model.Country;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SelectCountryActivity extends Activity {
    private List<Country> listCountry = new ArrayList<>();
    private RecyclerView recyclerView;
    private CountryAdapter mAdapter;
    // List contain name countries
    private static List<String> listNameCountry = new ArrayList<>();
    // List contain int flag countries
    private List<Integer> listFlagCountry = new ArrayList<>();
    // List contain id countries
    private List<String> listIdCountry = new ArrayList<>();
    private ImageView ivBack;
    static SelectCountryActivity selectCountryActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_country);
        init();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new CountryAdapter(listCountry);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        // add item touch listenner
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                try{
                    Country country = listCountry.get(position);
                    Intent myIntent = new Intent(SelectCountryActivity.this,VoteActivity.class);
                    myIntent.putExtra("name_country",(Serializable)country.getName_country());
                    myIntent.putExtra("flag_country",(Serializable)country.getFlag_country());
                    myIntent.putExtra("id_country",(Serializable)country.getId());
                    startActivity(myIntent);
                    MainActivity.getInstance().finish();
                    SelectCountryActivity.this.finish();
                    VoteActivity.getInstance().finish();
                } catch (NullPointerException n){

                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectCountryActivity.this.finish();
            }
        });
        // set animation for activity
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private SelectCountryActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final SelectCountryActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }//
    }

    /**
     * get info of country on server
     */
    public void init(){
        ivBack = (ImageView)findViewById(R.id.ivBackToMainActivity);
        selectCountryActivity = this;
        Intent i = getIntent();
        listNameCountry = (List<String>) i.getSerializableExtra("listnamecountry");
        listIdCountry = (List<String>)i.getSerializableExtra("listidcountry");
        listFlagCountry.add(R.drawable.flag_of_austria);
        listFlagCountry.add(R.drawable.flag_of_belgium);
        listFlagCountry.add(R.drawable.flag_of_bulgaria);
        listFlagCountry.add(R.drawable.flag_of_croatia);
        listFlagCountry.add(R.drawable.flag_of_cyprus_national);
        listFlagCountry.add(R.drawable.flag_of_the_czech_republic);
        listFlagCountry.add(R.drawable.flag_of_denmark);
        listFlagCountry.add(R.drawable.flag_of_estonia);
        listFlagCountry.add(R.drawable.flag_of_finland);
        listFlagCountry.add(R.drawable.flag_of_france);
        listFlagCountry.add(R.drawable.flag_of_germany);
        listFlagCountry.add(R.drawable.flag_of_greece);
        listFlagCountry.add(R.drawable.flag_of_hungary);
        listFlagCountry.add(R.drawable.flag_of_ireland);
        listFlagCountry.add(R.drawable.flag_of_the_italy);
        listFlagCountry.add(R.drawable.flag_of_latvia);
        listFlagCountry.add(R.drawable.flag_of_lithuania);
        listFlagCountry.add(R.drawable.flag_of_luxembourg);
        listFlagCountry.add(R.drawable.flag_of_malta);
        listFlagCountry.add(R.drawable.flag_of_the_netherlands);
        listFlagCountry.add(R.drawable.flag_of_the_poland);
        listFlagCountry.add(R.drawable.flag_of_portugal);
        listFlagCountry.add(R.drawable.flag_of_romania);
        listFlagCountry.add(R.drawable.flag_of_slovakia);
        listFlagCountry.add(R.drawable.flag_of_slovenia);
        listFlagCountry.add(R.drawable.flag_of_spain);
        listFlagCountry.add(R.drawable.flag_of_sweden);
        listFlagCountry.add(R.drawable.flag_of_the_united_kingdom);
        for(int j = 0 ; j < 28 ; j++){
            listCountry.add(new Country(listIdCountry.get(j),listNameCountry.get(j),listFlagCountry.get(j)));
        }
    }

    public static SelectCountryActivity getInstance(){
        return selectCountryActivity;
    }
}
