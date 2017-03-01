

package com.origamiboat.wang.origamiboat;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

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
