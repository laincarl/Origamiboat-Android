

package com.origamiboat.wang.origamiboat;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.google.gson.Gson;
import com.origamiboat.wang.origamiboat.Model.ResponseJson;
import com.origamiboat.wang.origamiboat.common.ServerWebRoot;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by xinxin on 16/4/18.
 */
public class ImageActivity extends Activity {
    private ImageView show_img;
    //public static final String BASE_PATH = "http://42.202.144.112:8080/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_image);
        show_img = (ImageView) findViewById(R.id.show_img);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //Log.i("TAG", BASE_PATH + getIntent().getStringExtra("image"));
        Glide.with(ImageActivity.this).load(getIntent().getStringExtra("image"))
                .into(show_img);



    }

}
