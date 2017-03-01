package com.origamiboat.wang.origamiboat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.origamiboat.wang.origamiboat.ImageActivity;
import com.origamiboat.wang.origamiboat.R;

/**
 * Created by wang on 2016/9/13 0013.
 */
public class DetailsActivity extends Activity {
    private WebView mWebView;
    private WebSettings webSettings;
    private JavaScriptInterface javascriptInterface;
    // private static final String URL = "http://42.202.144.112:8080/dagunzi/api/data_getNewsDetail.action?newsId=16";
    //public static final String BASE_PATH = "http://42.202.144.112:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        setContentView(R.layout.activity_details);

        mWebView = (WebView) findViewById(R.id.webView);
    }

    @SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
    private void initData() {
        javascriptInterface = new JavaScriptInterface(this);
        webSettings = mWebView.getSettings();
        //是否支持js
        webSettings.setJavaScriptEnabled(true);
        // 设置支持缩放
        webSettings.setSupportZoom(true);
        //显示图片时自适应屏幕大小,但是4.4以前好用,4,4以后不好用
        //mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //weView中有链接,在当前 browser 中相应
        mWebView.setWebViewClient(new MyWebViewClient());
        //设置进度条,处理提示框
        mWebView.setWebChromeClient(new MyWebChromeClient());
        //添加监听
        mWebView.addJavascriptInterface(javascriptInterface, "imagelistner");
        mWebView.loadUrl("file:///android_asset/xx.html");
       // mWebView.loadUrl("http://192.168.43.244:8080/OrigamiBoatServer/upload/xx.html");

    }



    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            // html加载完成之后，添加监听图片的点击js函数
            addImageClickListner();
            imgReset();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


    /**
     * 循环遍历标签中的图片
     * js 语法
     */
    private void imgReset() {
        mWebView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('img'); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "var img = objs[i];   " +
                "    img.style.maxWidth = '100%';   " +
                "}" +
                "})()");
    }

    // js通信接口
    public static class JavaScriptInterface {

        private Context context;

        public JavaScriptInterface(Context context) {
            this.context = context;
        }

        //点击图片回调方法
        //必须添加注解,否则无法响应
        @JavascriptInterface
        public void openImage(String img) {
            System.out.println(img);
            Log.i("TAG", "响应点击事件!");
            Intent intent = new Intent();
            intent.putExtra("image", img);
            intent.setClass(context, ImageActivity.class);
            context.startActivity(intent);
            System.out.println(img);
        }
    }

    /**
     * 设置进度条和提示框
     */
    private class MyWebChromeClient extends WebChromeClient {
        //该方法中可以设置进度条
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        //发方法中可以处理提示框
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }
    }

    // 注入js函数监听
    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        mWebView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imagelistner.openImage(this.src);  " +
                "    }  " +
                "}" +
                "})()");
    }

    //如果不作处理,点击 back 时整个 browser 会被 finish 掉
    //这样处理浏览器会回退,而不是退出浏览器
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}


