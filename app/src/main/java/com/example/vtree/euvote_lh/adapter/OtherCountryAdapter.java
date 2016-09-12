package com.example.vtree.euvote_lh.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.vtree.euvote_lh.R;
import com.example.vtree.euvote_lh.model.Country;

import java.util.List;

/**
 * Created by longdg123 on 8/23/2016.
 */
public class OtherCountryAdapter extends RecyclerView.Adapter<OtherCountryAdapter.MyViewHolder> {

    private List<Country> countryList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNameCountryOther,tvVoteLeaveOther,tvVoteRemainOther;
        public ImageView imgFlagOther;
        public ProgressBar progressBarOther;
        public MyViewHolder(View view) {
            super(view);
            tvNameCountryOther = (TextView) view.findViewById(R.id.tvNameCountryOther);
            imgFlagOther = (ImageView) view.findViewById(R.id.ivIconCountryOther);
            tvVoteLeaveOther = (TextView)view.findViewById(R.id.tvResultVoteLeaveOther);
            tvVoteRemainOther = (TextView)view.findViewById(R.id.tvResultVoteRemainOther);
            progressBarOther = (ProgressBar)view.findViewById(R.id.progressBarResultVoteOther);
        }
    }


    public OtherCountryAdapter(List<Country> countrylist) {
        this.countryList = countrylist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.result_vote_other_country_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Country country = countryList.get(position);
        holder.tvNameCountryOther.setText(country.getName_country());
        holder.imgFlagOther.setImageResource(country.getFlag_country());
        holder.tvVoteRemainOther.setText(country.getNum_in());
        holder.tvVoteLeaveOther.setText(country.getNum_out());
        holder.progressBarOther.setMax(Integer.parseInt(country.getNum_in())+ Integer.parseInt(country.getNum_out()));
        holder.progressBarOther.setProgress(Integer.parseInt(country.getNum_out()));
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }
}

