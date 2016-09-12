package com.example.vtree.euvote_lh;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.vtree.euvote_lh.adapter.OtherCountryAdapter;
import com.example.vtree.euvote_lh.model.Country;

import java.util.ArrayList;
import java.util.List;

public class SeeOtherCountriesActivity extends Activity {

    private List<Country> listCountry = new ArrayList<>();
    private List<Integer> listFlagCountry = new ArrayList<>();
    private List<String> listNameCountryOther = new ArrayList<>();
    private List<String> listNumint = new ArrayList<>();
    private List<String> listNumOut = new ArrayList<>();
    private RecyclerView recyclerView;
    private OtherCountryAdapter mAdapter;
    private ImageView ivBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_other_countries);
        Bundle bundle = getIntent().getExtras();
        init();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_viewOther);

        mAdapter = new OtherCountryAdapter(listCountry);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        // back to privious screen
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               SeeOtherCountriesActivity.this.finish();
            }
        });
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    /**
     * Init components
     */
    private void init(){
        ivBack = (ImageView)findViewById(R.id.ivBackToResultVoteScreen);
        listNameCountryOther = (List<String>) getIntent().getSerializableExtra("listnamecountryother");
        listFlagCountry = (List<Integer>) getIntent().getSerializableExtra("listflag");
        listNumint = (List<String>) getIntent().getSerializableExtra("listnumin");
        listNumOut = (List<String>) getIntent().getSerializableExtra("listnumout");
        for(int i = 0 ; i < listNameCountryOther.size(); i ++){
            listCountry.add(new Country(listNameCountryOther.get(i),listFlagCountry.get(i),listNumint.get(i),listNumOut.get(i)));
        }
    }

}
