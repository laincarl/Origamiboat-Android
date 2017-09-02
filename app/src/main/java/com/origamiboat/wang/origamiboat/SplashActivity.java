package com.origamiboat.wang.origamiboat;


import android.app.Activity;

import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;

import android.widget.Toast;

import com.google.gson.Gson;
import com.origamiboat.wang.origamiboat.Model.ResponseJson;
import com.origamiboat.wang.origamiboat.common.ServerWebRoot;
import com.origamiboat.wang.origamiboat.data_storage.LoginService;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by wang on 2016/5/29 0029.
 */
public class SplashActivity extends Activity {

    private LoginService service;
    Map<String, ?> map = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash);
        service = new LoginService(this);
        map = service.getSharePreference("login");//提取数据，第一次进来的时候要读文件
        //Toast.makeText(SplashActivity.this, map.get("username").toString(), Toast.LENGTH_SHORT).show();
        //Toast.makeText(SplashActivity.this, map.get("password").toString(), Toast.LENGTH_SHORT).show();
        if (map != null && !map.isEmpty()) {

            if(!map.get("username").toString().trim().equals("")&&!map.get("password").toString().trim().equals(""))
                new Thread(new Runnable(){
                    public void run() {
                        try{
                            String requestUrl = ServerWebRoot.getServerWebRoot()+"UserLogin?username="+map.get("username").toString()+"&password="+map.get("password").toString();
                            URL url = new URL(requestUrl);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setConnectTimeout(3000);//设置连接超时时间3s
                            conn.setReadTimeout(3000);
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

                                        new Handler().postDelayed(new Runnable(){

                                            @Override
                                            public void run() {
                                                // TODO Auto-generated method stub
                                                Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
                                                SplashActivity.this.startActivity(mainIntent);//跳转到MainActivity
                                                SplashActivity.this.finish();//结束SplashActivity
                                            }
                                        }, 2000);//给postDelayed()方法传递延迟参数

                                    }else{
                                        Toast.makeText(SplashActivity.this, jsonObj.msg, Toast.LENGTH_SHORT).show();
                                        Toast.makeText(SplashActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                                        new Handler().postDelayed(new Runnable(){

                                            @Override
                                            public void run() {
                                                // TODO Auto-generated method stub
                                                Intent mainIntent = new Intent(SplashActivity.this,LoginActivity.class);
                                                SplashActivity.this.startActivity(mainIntent);//跳转到MainActivity
                                                SplashActivity.this.finish();//结束SplashActivity
                                            }
                                        }, 2000);//给postDelayed()方法传递延迟参数

                                    }
                                }
                            });

                        }catch(Exception e){
                            e.printStackTrace();

                                    Intent mainIntent = new Intent(SplashActivity.this,LoginActivity.class);
                                    SplashActivity.this.startActivity(mainIntent);
                                    SplashActivity.this.finish();//结束SplashActivity

                        }
                    }
                }).start();
else{
                new Handler().postDelayed(new Runnable(){

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        Intent mainIntent = new Intent(SplashActivity.this,LoginActivity.class);
                        SplashActivity.this.startActivity(mainIntent);//跳转到MainActivity
                        SplashActivity.this.finish();//结束SplashActivity
                    }
                }, 500);//给postDelayed()方法传递延迟参数
            }

        }
else{
            new Handler().postDelayed(new Runnable(){

                @Override
                public void run() {
                    // TODO Auto-generated method stub

                    Intent mainIntent = new Intent(SplashActivity.this,LoginActivity.class);
                    SplashActivity.this.startActivity(mainIntent);//跳转到MainActivity
                    SplashActivity.this.finish();//结束SplashActivity
                }
            }, 2000);//给postDelayed()方法传递延迟参数
        }

    }
}
