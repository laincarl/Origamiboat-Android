package com.origamiboat.wang.origamiboat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.origamiboat.wang.origamiboat.Model.ResponseJson;
import com.origamiboat.wang.origamiboat.common.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wang on 2016/8/19 0019.
 */
public class RegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final EditText reg_name = (EditText) findViewById(R.id.Reg_UserText);
        final EditText reg_pwd = (EditText) findViewById(R.id.Reg_PswdText);
        Button confirm_reg=(Button)findViewById(R.id.confirm_register);
        //login按钮监听
        confirm_reg.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {

                final String username = reg_name.getText().toString();
                final String password = reg_pwd.getText().toString();
                new Thread(new Runnable(){
                    public void run() {
                        try{
                            String requestUrl = com.origamiboat.wang.origamiboat.common.ServerWebRoot.getServerWebRoot()+"UserReg?username="+username+"&password="+password;
                            URL url = new URL(requestUrl);
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
                                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }else{
                                        Toast.makeText(RegisterActivity.this, jsonObj.msg, Toast.LENGTH_SHORT).show();
                                        Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

        });

    }
}
