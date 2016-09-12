package com.example.vtree.euvote_lh.model;

import android.os.Parcel;

import java.io.Serializable;

/**
 * Created by longdg123 on 8/18/2016.
 */
public class Country implements Serializable {
    private String name_country;
    private int flag_country;
    private String id;
    private String num_in;
    private String num_out;

    public Country( String name_country, int flag_country, String num_in, String num_out) {

        this.name_country = name_country;
        this.flag_country = flag_country;
        this.num_in = num_in;
        this.num_out = num_out;
    }
    public Country(String id, String name_country, String num_in, String num_out) {
        this.id = id;
        this.name_country = name_country;
        this.num_in = num_in;
        this.num_out = num_out;
    }

    public Country() {
    }

    public Country(String id,String name_country, int flag_country) {
        this.name_country = name_country;
        this.id = id;
        this.flag_country = flag_country;
    }

    protected Country(Parcel in) {
        name_country = in.readString();
        flag_country = in.readInt();
        id = in.readString();
        num_in = in.readString();
        num_out = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName_country() {
        return name_country;
    }

    public String getNum_in() {
        return num_in;
    }

    public String getNum_out() {
        return num_out;
    }

    public int getFlag_country() {
        return flag_country;
    }


}
