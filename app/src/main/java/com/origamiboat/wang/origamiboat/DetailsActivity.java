package com.origamiboat.wang.origamiboat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.origamiboat.wang.origamiboat.ImageActivity;
import com.origamiboat.wang.origamiboat.Model.ResponseJson;
import com.origamiboat.wang.origamiboat.R;
import com.origamiboat.wang.origamiboat.common.ServerWebRoot;
import com.origamiboat.wang.origamiboat.data_storage.LoginService;
import com.origamiboat.wang.origamiboat.utils.GoodView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by wang on 2016/9/13 0013.
 */
public class DetailsActivity extends Activity {
    private WebView mWebView;
    private WebSettings webSettings;
    private JavaScriptInterface javascriptInterface;
    //private static String URL = "file:///android_asset/xx.html";
    private static String URL ="";
    TextView show_title;
    //public static final String BASE_PATH = "http://42.202.144.112:8080/";
    GoodView mGoodView;
    private static String user_artical;
    private LoginService service_new;
    Map<String, ?> map_new = null;
    String author="";
    String filename="";
    int collectflag=0;
    int supportflag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoodView = new GoodView(this);
        Bundle extras = getIntent().getExtras();
        URL =extras.getString("Link");
        //Toast.makeText(DetailsActivity.this, URL, Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_details);
        service_new = new LoginService(this);
        //提取用户数据
        map_new = service_new.getSharePreference("login");//提取用户数据
        if (map_new != null && !map_new.isEmpty()) {
            user_artical = map_new.get("username").toString();

        }
        int startindex=0;
        int endindex=0;
        startindex=URL.lastIndexOf("/")+1;

        filename=URL.substring(startindex);
        endindex=filename.indexOf("_");
        author=filename.substring(0,endindex);
        mWebView = (WebView) findViewById(R.id.webView);
        show_title=(TextView)findViewById(R.id.show_title);
        show_title.setText(extras.getString("Title"));
        new Thread(new Runnable(){
            public void run() {
                try{
                    String requestUrl = com.origamiboat.wang.origamiboat.common.ServerWebRoot.getServerWebRoot()+"SupportCheck?username="+user_artical+"&filename="+filename+"&author="+author;
                    java.net.URL url = new URL(requestUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    InputStream in = conn.getInputStream();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] buf = new byte[1024];
                    while(true){
                        int len = in .read(buf);
                        if(len==-1) break;
                        out.write(buf,0,len);
                    }

                    final String json  = new String (out.toByteArray(),"UTF-8");

                    runOnUiThread(new Runnable() {
                        public void run(){
                            Gson g =new Gson();
                            ResponseJson jsonObj = g.fromJson(json, ResponseJson.class);
                            if(jsonObj.status==200){
                                //Toast.makeText(DetailsActivity.this, "点赞成功", Toast.LENGTH_SHORT).show();
                                ((ImageView) findViewById(R.id.good)).setImageResource(R.drawable.good_checked);
                                supportflag=1;
                            }else{
                                //Toast.makeText(DetailsActivity.this, jsonObj.msg, Toast.LENGTH_SHORT).show();
                                //Toast.makeText(DetailsActivity.this, "点赞失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable(){
            public void run() {
                try{
                    String requestUrl = com.origamiboat.wang.origamiboat.common.ServerWebRoot.getServerWebRoot()+"CollectCheck?username="+user_artical+"&filename="+filename;
                    java.net.URL url = new URL(requestUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    InputStream in = conn.getInputStream();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] buf = new byte[1024];
                    while(true){
                        int len = in .read(buf);
                        if(len==-1) break;
                        out.write(buf,0,len);
                    }

                    final String json  = new String (out.toByteArray(),"UTF-8");

                    runOnUiThread(new Runnable() {
                        public void run(){
                            Gson g =new Gson();
                            ResponseJson jsonObj = g.fromJson(json, ResponseJson.class);
                            if(jsonObj.status==200){
                                //Toast.makeText(DetailsActivity.this, "点赞成功", Toast.LENGTH_SHORT).show();
                                ((ImageView) findViewById(R.id.collection)).setImageResource(R.drawable.collection_checked);
                                collectflag=1;
                            }else{
                                //Toast.makeText(DetailsActivity.this, jsonObj.msg, Toast.LENGTH_SHORT).show();
                                //Toast.makeText(DetailsActivity.this, "点赞失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        initData();
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
        //mWebView.loadUrl(Environment.getExternalStorageDirectory() + "/"+"share.html");
        //mWebView.loadUrl(ServerWebRoot.getServerWebRoot()+"artical/admin.html");

        //mWebView.loadDataWithBaseURL("file:///storage/emulated/0/", "<img src=\"0.jpg\"/>", "text/html", "utf-8", null);
        mWebView.loadUrl(URL);

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

    public void good(View view) {
        if(supportflag==1){
            return;
        }
        ((ImageView) view).setImageResource(R.drawable.good_checked);

        mGoodView.setText("+1");
        //Toast.makeText(DetailsActivity.this,author+"|"+user_artical+"|"+filename,Toast.LENGTH_LONG).show();
        new Thread(new Runnable(){
            public void run() {
                try{
                    String requestUrl = com.origamiboat.wang.origamiboat.common.ServerWebRoot.getServerWebRoot()+"Support?username="+user_artical+"&filename="+filename+"&author="+author;
                    java.net.URL url = new URL(requestUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    InputStream in = conn.getInputStream();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] buf = new byte[1024];
                    while(true){
                        int len = in .read(buf);
                        if(len==-1) break;
                        out.write(buf,0,len);
                    }

                    final String json  = new String (out.toByteArray(),"UTF-8");
                    runOnUiThread(new Runnable() {
                        public void run(){
                            Gson g =new Gson();
                            ResponseJson jsonObj = g.fromJson(json, ResponseJson.class);
                            if(jsonObj.status==200){
                                //Toast.makeText(DetailsActivity.this, "点赞成功", Toast.LENGTH_SHORT).show();

                            }else{
                                //Toast.makeText(DetailsActivity.this, jsonObj.msg, Toast.LENGTH_SHORT).show();
                                Toast.makeText(DetailsActivity.this, "点赞失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        mGoodView.show(view);
    }


    public void collection(View view) {
        if(collectflag==1){
            return;
        }
        ((ImageView) view).setImageResource(R.drawable.collection_checked);
        mGoodView.setTextInfo("收藏成功", Color.parseColor("#f66467"), 12);
        new Thread(new Runnable(){
            public void run() {
                try{
                    String requestUrl = com.origamiboat.wang.origamiboat.common.ServerWebRoot.getServerWebRoot()+"Collect?username="+user_artical+"&filename="+filename;
                    java.net.URL url = new URL(requestUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    InputStream in = conn.getInputStream();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] buf = new byte[1024];
                    while(true){
                        int len = in .read(buf);
                        if(len==-1) break;
                        out.write(buf,0,len);
                    }

                    final String json  = new String (out.toByteArray(),"UTF-8");
                    runOnUiThread(new Runnable() {
                        public void run(){
                            Gson g =new Gson();
                            ResponseJson jsonObj = g.fromJson(json, ResponseJson.class);
                            if(jsonObj.status==200){
                                //Toast.makeText(DetailsActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();

                            }else{
                                //Toast.makeText(DetailsActivity.this, jsonObj.msg, Toast.LENGTH_SHORT).show();
                                Toast.makeText(DetailsActivity.this, "收藏失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        mGoodView.show(view);
    }



}


