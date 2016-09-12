package com.example.vtree.euvote_lh;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.vtree.euvote_lh.app.AppSingleton;
import com.example.vtree.euvote_lh.utils.Const;
import com.example.vtree.euvote_lh.utils.UserPicture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResultVoteActivity extends Activity {
    private static final int PICK_IMAGE = 100;
    TextView tvResultVoteRemain, tvResultVoteLeave, tvResultVote;
    private List<Integer> listFlagCountry = new ArrayList<>();
    private List<String> listNameOtherCountry = new ArrayList<>();
    private List<String> listNumIn = new ArrayList<>();
    private List<String> listNumOut = new ArrayList<>();
    ImageView imgViewPhoto;
    ProgressBar progressBar;
    Bitmap resultBitmapPhoto;
    Bitmap mask = null;
    Bitmap result;
    String nameCountry;
    String nameImageInSdcard = "";
    String resultVoteRemain = "";
    String resultVoteLeave = "";
    Boolean isLeave = false;
    Date d = new Date();
    Button btnShareFB, btnShareTwiter, btnShareInstagram, btnSeeOtherCountry,btnEditPhoto;
    int sumVote = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_vote);
        init();
        getResultVote(Const.JSON_OBJECT_REQUEST_URL);

        imgViewPhoto.setImageBitmap(resultBitmapPhoto);
        btnShareFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                saveImage();
//                shareFB(v, nameImageInSdcard);
                Toast.makeText(getApplicationContext(),"kkk",Toast.LENGTH_LONG).show();
            }
        });
        btnShareTwiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage();
                shareTwiter(v,nameImageInSdcard);
            }
        });
        // See Other Country result vote Activity
        btnSeeOtherCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ResultVoteActivity.this, SeeOtherCountriesActivity.class);
                myIntent.putExtra("listnamecountryother", (Serializable) listNameOtherCountry);
                myIntent.putExtra("listnumin", (Serializable) listNumIn);
                myIntent.putExtra("listnumout", (Serializable) listNumOut);
                myIntent.putExtra("listflag", (Serializable) listFlagCountry);
                startActivity(myIntent);
            }
        });
        // edit photo
        btnEditPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                getIntent.setType("image/*");
                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);
            }
        });
    }

    /**
     * Empty constuctor
     */
    public ResultVoteActivity() {
    }

    /**
     * Init components
     */
    private void init() {
        resultBitmapPhoto = (Bitmap) getIntent().getParcelableExtra("result_bitmap");
        nameCountry = (String) getIntent().getSerializableExtra("name_country");
        isLeave = (Boolean) getIntent().getSerializableExtra("isLeave");
        tvResultVoteLeave = (TextView) findViewById(R.id.tvResultVoteLeave);
        tvResultVoteRemain = (TextView) findViewById(R.id.tvResultVoteRemain);
        tvResultVote = (TextView) findViewById(R.id.tvResultVoteCountry);
        progressBar = (ProgressBar) findViewById(R.id.progressBarResultVote);
        btnShareFB = (Button) findViewById(R.id.btnShareFb);
        btnShareTwiter = (Button) findViewById(R.id.btnShareTwiter);
        btnShareInstagram = (Button) findViewById(R.id.btnShareInstagram);
        btnSeeOtherCountry = (Button) findViewById(R.id.btn_seeother);
        imgViewPhoto = (ImageView)findViewById(R.id.imageViewPhotoResult);
        btnEditPhoto = (Button)findViewById(R.id.btnEditPhoto);
        // add flag to list flag countries
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

        if (isLeave) {
            String vote = nameCountry.toUpperCase() + " REMAIN";
            if (vote.length() > 15) {
                tvResultVote.setText(vote);
                tvResultVote.setTextSize(getResources().getDimension(R.dimen._6sdp));
                tvResultVote.setTextColor(Color.BLACK);
                tvResultVote.setBackgroundResource(R.color.colorYellow);
                tvResultVote.setDrawingCacheEnabled(true);
            } else {
                tvResultVote.setText(vote);
                tvResultVote.setTextColor(Color.BLACK);
                tvResultVote.setBackgroundResource(R.color.colorYellow);
                tvResultVote.setDrawingCacheEnabled(true);
            }

        } else {
            String vote = nameCountry.toUpperCase() + " LEAVE";
            if (vote.length() > 15) {
                tvResultVote.setText(vote);
                tvResultVote.setTextSize(getResources().getDimension(R.dimen._6sdp));
                tvResultVote.setTextColor(Color.WHITE);
                tvResultVote.setBackgroundResource(R.color.colorBlue);
                tvResultVote.setDrawingCacheEnabled(true);
            } else {
                tvResultVote.setText(vote);
                tvResultVote.setTextColor(Color.WHITE);
                tvResultVote.setBackgroundResource(R.color.colorBlue);
                tvResultVote.setDrawingCacheEnabled(true);
            }
        }
    }

    /**
     * Get info of Country object on server
     */
    public void getResultVote(String url) {

        String REQUEST_TAG = "com.example.vtree.euvote_lh";
        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONObject obj = response;
                        JSONArray array = null;
                        try {
                            array = obj.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                if (array.getJSONObject(i).getString("name").equals(nameCountry)) {
                                    resultVoteRemain = array.getJSONObject(i).getString("num_in");
                                    resultVoteLeave = array.getJSONObject(i).getString("num_out");
                                    tvResultVoteRemain.setText(resultVoteRemain + " votes");
                                    tvResultVoteLeave.setText(resultVoteLeave + " votes");
                                    sumVote = Integer.parseInt(resultVoteLeave) + Integer.parseInt(resultVoteRemain);
                                    progressBar.setMax(sumVote);
                                    progressBar.setProgress(Integer.parseInt(resultVoteLeave));
                                    // Toast.makeText(getApplicationContext(),resultVoteRemain +" " + resultVoteLeave, Toast.LENGTH_LONG).show();
                                } else {
                                    JSONObject jsonOb = array.getJSONObject(i);
                                    listNameOtherCountry.add(jsonOb.getString("name"));
                                    listNumIn.add(jsonOb.getString("num_in"));
                                    listNumOut.add(jsonOb.getString("num_out"));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq, REQUEST_TAG);
    }


    /**
     * Save image from imageview
     */
    public void saveImage() {
        boolean success = false;
        nameImageInSdcard = DateFormat.format("MM-dd-yy hh-mm-ss", d.getTime()).toString() + ".png";
        // tao image bitmap
        BitmapDrawable drawable = (BitmapDrawable) imgViewPhoto.getDrawable();
        // tao bitmap tv
        Bitmap bitmapTv = Bitmap.createScaledBitmap(tvResultVote.getDrawingCache(), 150, 40, false);
        Bitmap bitmap = drawable.getBitmap();
        Bitmap result = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);
        //using canvas to merge images
        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        mCanvas.drawBitmap(bitmap, 0, 0, null);
        mCanvas.drawBitmap(bitmapTv, 0, 110, null);
        paint.setXfermode(null);
        // save to sdcard
        File sdCardDirectory = Environment.getExternalStorageDirectory();
        File image = new File(sdCardDirectory, nameImageInSdcard);

        // Encode the file as a PNG image.
        FileOutputStream outStream;
        try {

            outStream = new FileOutputStream(image);
            result.compress(Bitmap.CompressFormat.PNG, 100, outStream);
        /* 100 to keep full quality of the image */

            outStream.flush();
            outStream.close();
            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (success) {

        } else {
            Toast.makeText(getApplicationContext(),
                    "Error during image saving", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Activity result handler choose edit photo
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Bitmap image be choose

        // uri file data


        try {
            Uri selectedImageUri = data.getData();
            mask = new UserPicture(selectedImageUri, getContentResolver()).getBitmap();
            Bitmap original = null;
            // image eu leave avatar frame
            if(isLeave){
                 original = BitmapFactory.decodeResource(getResources(),R.drawable.eu_stay_avatar_frame);
            } else {
                 original = BitmapFactory.decodeResource(getResources(),R.drawable.eu_leave_avatar_frame);
            }

            //resize image original
            Bitmap newOriginal = Bitmap.createScaledBitmap(original, 150, 150, false);
            //resize image mark
            Bitmap newMask = Bitmap.createScaledBitmap(mask,150,150,false);
            //decrale bitmap result (merge two image bitmap above)
            result = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);
            //using canvas to merge images
            Canvas mCanvas = new Canvas(result);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

            mCanvas.drawBitmap(newMask, 0, 0, null);
            mCanvas.drawBitmap(newOriginal, 0, 0, null);
            //   mCanvas.drawBitmap(bitmapTv,0,110,null);
            paint.setXfermode(null);
            // set image to imageview
            imgViewPhoto.setImageBitmap(result);
        } catch (IOException e) {
            Log.e(VoteActivity.class.getSimpleName(), "Failed to load image", e);
        } catch (NullPointerException e){
            Log.e(VoteActivity.class.getSimpleName(), "Failed to load image", e);
        }


    }

    /**
     * share facebook
     */
    //share fb
    public void shareFB(View v, String nameImage) {

        // Select image from sdcard
        String url = "/sdcard/" + nameImage;

        Uri uri = Uri.fromFile(new File(url));
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("image/png");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

        PackageManager pm = v.getContext().getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
        for (final ResolveInfo app : activityList) {
            if ((app.activityInfo.name).startsWith("com.facebook.composer")) {
                final ActivityInfo activity = app.activityInfo;
                final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                shareIntent.setComponent(name);
                v.getContext().startActivity(shareIntent);
                break;
            }
        }
    }
    /**
     * Share Twiter
     */
    public void shareTwiter(View v,String nameImage){
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String url = "/sdcard/" + nameImage;

        Uri uri = Uri.fromFile(new File(url));
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

        PackageManager pm = v.getContext().getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
        for (final ResolveInfo app : activityList)
        {
            if ("com.twitter.android.composer.ComposerActivity".equals(app.activityInfo.name))
            {
                final ActivityInfo activity = app.activityInfo;
                final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                shareIntent.setComponent(name);
                v.getContext().startActivity(shareIntent);
                break;
            }
            //Toast.makeText(getApplicationContext(),app.activityInfo.name +"  ",+Toast.LENGTH_LONG).show();
        }
    }

}
