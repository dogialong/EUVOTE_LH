package com.example.vtree.euvote_lh;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.vtree.euvote_lh.app.AppSingleton;
import com.example.vtree.euvote_lh.model.Country;
import com.example.vtree.euvote_lh.utils.Const;
import com.example.vtree.euvote_lh.utils.UserPicture;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class VoteActivity extends Activity {

    private static final int PICK_IMAGE = 100;
    Button btnEditPhoto,btnVoteRemain,btnVoteLeave,btnFindout;
    Country country = new Country();
    Button btnSelectedCountry;
    ImageView imgViewPhoto;
    TextView tvCountrySelectedToVote;
    int flagCountry ;
    String nameCountry;
    String idCountry;
    Bitmap result;
    Bitmap defaultImage; // if user no choose any image. App use default image to action share FB
    Bitmap mask = null;
    Boolean chooseEditPhoto = false;
    Boolean isLeave = false;
    boolean doubleBackToExitPressedOnce = false;
    static VoteActivity voteActivity;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);
        country = (Country)getIntent().getSerializableExtra("country_selected");
        init();
        // add event listenner for btnEditPhoto
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

        // handler action vote remain
        btnVoteRemain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              voteRemain();
                isLeave = true;
                Intent myIntent = new Intent(VoteActivity.this,ResultVoteActivity.class);
                myIntent.putExtra("name_country",(Serializable) nameCountry);
                myIntent.putExtra("isLeave",(Serializable)isLeave);
                drawPhotoWithFlagRemain();
                if(chooseEditPhoto){
                    myIntent.putExtra("result_bitmap",result);
                } else {
                    myIntent.putExtra("result_bitmap",result);
                }

                startActivity(myIntent);
            }
        });

        // handler action vote leave
        btnVoteLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voteLeave();
                isLeave = false;
                Intent myIntent = new Intent(VoteActivity.this,ResultVoteActivity.class);
                myIntent.putExtra("name_country",(Serializable) nameCountry);
                myIntent.putExtra("isLeave",(Serializable)isLeave);
                drawPhotoWithFlagLeave();
                if(chooseEditPhoto){
                    myIntent.putExtra("result_bitmap",result);
                } else {
                    myIntent.putExtra("result_bitmap",defaultImage);
                }
                startActivity(myIntent);
            }
        });

        btnSelectedCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getInstance().recreate();
                MainActivity.getInstance().volleyJsonObjectRequest(Const.JSON_OBJECT_REQUEST_URL);
                MainActivity.getInstance().btnSelectCountry.performClick();
                SelectCountryActivity.getInstance().recreate();
               // VoteActivity.this.finish();
            }
        });
        btnFindout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(VoteActivity.this,FindOutActivity.class);
                startActivity(myIntent);
            }
        });
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    /**
     * Init component
     */
    private void init() {
        voteActivity = this;
        btnEditPhoto = (Button)findViewById(R.id.btnEditPhoto);
        btnVoteLeave = (Button)findViewById(R.id.btnLeaveVote);
        btnVoteRemain = (Button)findViewById(R.id.btnRemainVote);
        btnFindout = (Button)findViewById(R.id.btnFindoutVoteActivity);
        btnSelectedCountry = (Button)findViewById(R.id.btnSelectedCountry);
        tvCountrySelectedToVote = (TextView)findViewById(R.id.tvNameCountrySelectedToVote);
        imgViewPhoto = (ImageView)findViewById(R.id.imageViewPhoto);
        flagCountry = (int) getIntent().getSerializableExtra("flag_country");
        nameCountry = (String)getIntent().getSerializableExtra("name_country");
        idCountry = (String)getIntent().getSerializableExtra("id_country");
        btnSelectedCountry.setCompoundDrawablesWithIntrinsicBounds( flagCountry, 0, R.drawable.ic_arrow, 0);
        btnSelectedCountry.setText(nameCountry);
        defaultImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.avata),150,150,false);
        // init textview bitmap
        tvCountrySelectedToVote.setText(nameCountry);
        tvCountrySelectedToVote.setTextColor(Color.WHITE);
        tvCountrySelectedToVote.setDrawingCacheEnabled(true);

    }

    /**
     *
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Bitmap image be choose

        // uri file data


        try {
            Uri selectedImageUri = data.getData();
             mask = new UserPicture(selectedImageUri, getContentResolver()).getBitmap();

            // image eu leave avatar frame
            Bitmap original = BitmapFactory.decodeResource(getResources(),R.drawable.eu_stay_avatar_frame);
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
            chooseEditPhoto = true;
            imgViewPhoto.setImageBitmap(result);
        } catch (IOException e) {
            Log.e(VoteActivity.class.getSimpleName(), "Failed to load image", e);
        } catch (NullPointerException e){
            Log.e(VoteActivity.class.getSimpleName(), "Failed to load image", e);
        }
    }

    /**
     * Request to server
     */
    public void voteRemain(){
        String  REQUEST_TAG = "request_post_data";
        StringRequest postRequest = new StringRequest(Request.Method.POST, Const.JSON_OBJECT_POST_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                     //   Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("country_id", idCountry);
                params.put("type", "0");        // type = 0 --> vote remain
                params.put("status", "0");
                return params;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(postRequest, REQUEST_TAG);
    }

    /**
     * Vote leave
     */
    public void voteLeave(){
        String  REQUEST_TAG = "request_post_data";
        StringRequest postRequest = new StringRequest(Request.Method.POST, Const.JSON_OBJECT_POST_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("country_id", idCountry);
                params.put("type", "1");        // type = 1 --> vote leave
                params.put("status", "0");
                return params;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(postRequest, REQUEST_TAG);
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
    /**
     * Drawing photo with flag leave
     */
    private void drawPhotoWithFlagLeave(){
        if(chooseEditPhoto){
            Bitmap original = BitmapFactory.decodeResource(getResources(),R.drawable.eu_leave_avatar_frame);
            //resize image original
            Bitmap newOriginal = Bitmap.createScaledBitmap(original, 150, 150, false);
            //resize image mark
            Bitmap newMask = Bitmap.createScaledBitmap(mask,150,150,false);
            result = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);
            //using canvas to merge images
            Canvas mCanvas = new Canvas(result);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

            mCanvas.drawBitmap(newMask, 0, 0, null);
            mCanvas.drawBitmap(newOriginal, 0, 0, null);
            //   mCanvas.drawBitmap(bitmapTv,0,110,null);
            paint.setXfermode(null);
        }
    }
    private void drawPhotoWithFlagRemain(){
        if(chooseEditPhoto){
            Bitmap original = BitmapFactory.decodeResource(getResources(),R.drawable.eu_stay_avatar_frame);
            //resize image original
            Bitmap newOriginal = Bitmap.createScaledBitmap(original, 150, 150, false);
            //resize image mark
            Bitmap newMask = Bitmap.createScaledBitmap(mask,150,150,false);
            result = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);
            //using canvas to merge images
            Canvas mCanvas = new Canvas(result);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

            mCanvas.drawBitmap(newMask, 0, 0, null);
            mCanvas.drawBitmap(newOriginal, 0, 0, null);
            //   mCanvas.drawBitmap(bitmapTv,0,110,null);
            paint.setXfermode(null);
        } else {
            Bitmap original = BitmapFactory.decodeResource(getResources(),R.drawable.eu_stay_avatar_frame);
            //resize image original
            Bitmap newOriginal = Bitmap.createScaledBitmap(original, 150, 150, false);
            result = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);
            Canvas mCanvas = new Canvas(result);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

            mCanvas.drawBitmap(defaultImage, 0, 0, null);
            mCanvas.drawBitmap(newOriginal, 0, 0, null);
            //   mCanvas.drawBitmap(bitmapTv,0,110,null);
            paint.setXfermode(null);
        }

    }
    public static VoteActivity getInstance(){
        return  voteActivity;
    }
}
