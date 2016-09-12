package com.example.vtree.euvote_lh.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vtree.euvote_lh.R;
import com.example.vtree.euvote_lh.model.Country;

import java.util.List;

/**
 * Created by longdg123 on 8/18/2016.
 */

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.MyViewHolder> {

    private List<Country> countryList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNameCountry;
        public ImageView imgFlag;

        public MyViewHolder(View view) {
            super(view);
            tvNameCountry = (TextView) view.findViewById(R.id.tvNameCountry);
            imgFlag = (ImageView) view.findViewById(R.id.ivIconCountry);

        }
    }


    public CountryAdapter(List<Country> countrylist) {
        this.countryList = countrylist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.country_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Country country = countryList.get(position);
        holder.tvNameCountry.setText(country.getName_country());
        holder.imgFlag.setImageResource(country.getFlag_country());
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }
}
