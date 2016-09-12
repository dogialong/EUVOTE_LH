package com.example.vtree.euvote_lh;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

public class FindOutActivity extends Activity {

    private WebView webView;
    private ImageView btnBack;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_find_out);
        btnBack = (ImageView)findViewById(R.id.ivBackToMainActivityFromFindout);
        webView = (WebView) findViewById(R.id.webView1);

        final String mimeType = "text/html";
        final String encoding = "UTF-8";

        final String html = " <frameset><frame src='https://en.wikipedia.org/wiki/Brexit'></frameset>";
        webView.loadDataWithBaseURL(null, html, mimeType, encoding, "");
        // handle event button back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindOutActivity.this.finish();
            }
        });
        // set animation for activity
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}
