package com.origamiboat.wang.origamiboat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.origamiboat.wang.origamiboat.utils.SysApplication;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    //定义3个Fragment的对象
    private Fragment1 fg1;
    private Fragment2 fg2;
    private Fragment3 fg3;
    //帧布局对象,就是用来存放Fragment的容器
    private FrameLayout flayout;
    //定义底部导航栏的三个布局
    private RelativeLayout course_layout;
    private RelativeLayout found_layout;
    private RelativeLayout settings_layout;
    //定义底部导航栏中的ImageView与TextView
    private ImageView course_image;
    private ImageView found_image;
    private ImageView settings_image;
    private TextView course_text;
    private TextView settings_text;
    private TextView found_text;
    //定义要用的颜色值
    private int whirt = 0xFFFFFFFF;
    private int gray = 0xFF7597B3;

    private int blue=Color.parseColor("#32a5e7");
    private int green= Color.parseColor("#16a230");
    //定义FragmentManager对象
    FragmentManager fManager;

    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SysApplication.getInstance().addActivity(this);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_main);
        fManager = getSupportFragmentManager();
        initViews();
        setChioceItem(0);
    }


    //完成组件的初始化
    public void initViews()
    {
        course_image = (ImageView) findViewById(R.id.course_image);
        found_image = (ImageView) findViewById(R.id.found_image);
        settings_image = (ImageView) findViewById(R.id.setting_image);
        course_text = (TextView) findViewById(R.id.course_text);
        found_text = (TextView) findViewById(R.id.found_text);
        settings_text = (TextView) findViewById(R.id.setting_text);
        course_layout = (RelativeLayout) findViewById(R.id.course_layout);
        found_layout = (RelativeLayout) findViewById(R.id.found_layout);
        settings_layout = (RelativeLayout) findViewById(R.id.setting_layout);
        course_layout.setOnClickListener(this);
        found_layout.setOnClickListener(this);
        settings_layout.setOnClickListener(this);
    }

    //重写onClick事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.course_layout:
                setChioceItem(0);
                break;

            case R.id.setting_layout:
                setChioceItem(2);
                break;
            default:
                break;
        }
        if(view.getId()==R.id.found_layout) {
            Intent intent = new Intent(MainActivity.this, NewActivity.class);
            startActivity(intent);

            // 第一个参数是目标Activity进入时的动画，第二个参数是当前Activity退出时的动画
            overridePendingTransition(R.anim.slide_in, R.anim.activity_stay);
        }
    }


    //定义一个选中一个item后的处理
    public void setChioceItem(int index)
    {
        //重置选项+隐藏所有Fragment
        FragmentTransaction transaction = fManager.beginTransaction();
        clearChioce();
        hideFragments(transaction);

        switch (index) {
            case 0:
                course_image.setImageResource(R.drawable.ic_tabbar_course_pressed);
                course_text.setTextColor(blue);
                //course_layout.setBackgroundResource(R.drawable.ic_tabbar_bg_click);
                if (fg1 == null) {
                    // 如果fg1为空，则创建一个并添加到界面上
                    fg1 = new Fragment1();
                    transaction.add(R.id.content, fg1);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    fg1 = new Fragment1();
                    transaction.replace(R.id.content, fg1);
                }
                break;

            case 1:
                /*found_image.setImageResource(R.drawable.ic_tabbar_found_pressed);
                found_text.setTextColor(green);
                //found_layout.setBackgroundResource(R.drawable.ic_tabbar_bg_click);
                if (fg2 == null) {
                    // 如果fg1为空，则创建一个并添加到界面上
                    fg2 = new Fragment2();
                    transaction.add(R.id.content, fg2);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(fg2);
                }
                */

                break;

            case 2:
                settings_image.setImageResource(R.drawable.ic_tabbar_settings_pressed);
                settings_text.setTextColor(blue);
               // settings_layout.setBackgroundResource(R.drawable.ic_tabbar_bg_click);
                if (fg3 == null) {
                    // 如果fg1为空，则创建一个并添加到界面上
                    fg3 = new Fragment3();
                    transaction.add(R.id.content, fg3);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    fg3 = new Fragment3();
                    transaction.replace(R.id.content, fg3);
                    //transaction.show(fg3);
                }
                break;
        }
        transaction.commit();
    }

    //隐藏所有的Fragment,避免fragment混乱
    private void hideFragments(FragmentTransaction transaction) {
        if (fg1 != null) {
            transaction.hide(fg1);
        }
        if (fg2 != null) {
            transaction.hide(fg2);
        }
        if (fg3 != null) {
            transaction.hide(fg3);
        }
    }


    //定义一个重置所有选项的方法
    public void clearChioce()
    {
        course_image.setImageResource(R.drawable.ic_tabbar_course_normal);
        //course_layout.setBackgroundColor(whirt);
        course_text.setTextColor(gray);
        found_image.setImageResource(R.drawable.ic_tabbar_new_normal);
        //found_layout.setBackgroundColor(whirt);
        found_text.setTextColor(gray);
        settings_image.setImageResource(R.drawable.ic_tabbar_settings_normal);
        //settings_layout.setBackgroundColor(whirt);
        settings_text.setTextColor(gray);
    }

}
