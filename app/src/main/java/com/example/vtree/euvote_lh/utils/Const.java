package com.example.vtree.euvote_lh.utils;

import com.example.vtree.euvote_lh.model.Country;

/**
 * Created by longdg123 on 8/18/2016.
 */
public class Const {
    public static final String JSON_OBJECT_REQUEST_URL = "http://128.199.86.151:8081/api/countries";
    public static final String JSON_OBJECT_POST_URL = "http://128.199.86.151:8081/api/country_votes";
    public static Integer MAXPROGRESS = Integer.parseInt(new Country().getNum_in()) + Integer.parseInt(new Country().getNum_out());
}
