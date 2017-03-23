package com.origamiboat.wang.origamiboat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.origamiboat.wang.origamiboat.Model.ResponseJson;
import com.origamiboat.wang.origamiboat.common.ServerWebRoot;
import com.origamiboat.wang.origamiboat.data_storage.LoginService;
import com.origamiboat.wang.origamiboat.utils.CheckNet;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wang on 2016/8/19 0019.
 */
public class LoginActivity extends Activity {

    private LoginService service;
    Map<String, ?> map = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        service = new LoginService(this);
        final EditText name_view = (EditText) findViewById(R.id.UserText);
        final EditText pwd_view = (EditText) findViewById(R.id.PswdText);
        Button login = (Button) findViewById(R.id.button_login);
        TextView see = (TextView) findViewById(R.id.button_see);
        TextView register=(TextView)findViewById(R.id.button_register);
        map = service.getSharePreference("login");//提取数据，第一次进来的时候要读文件

       //login按钮监听
        login.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                if(!CheckNet.isNetaAvailable(LoginActivity.this))
                {
                    Toast.makeText(LoginActivity.this,"请检查网络状态", Toast.LENGTH_LONG).show();
                    return;
                }
                final String username = name_view.getText().toString();
                final String password = pwd_view.getText().toString();
                new Thread(new Runnable(){
                    public void run() {
                        try{
                            String requestUrl = ServerWebRoot.getServerWebRoot()+"UserLogin?username="+username+"&password="+password;
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
                                        /*SharedPreferences sp_user = getSharedPreferences("sp_user", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor sp_editor = sp_user.edit();
                                        sp_editor.putInt("UserID", jsonObj.userid);
                                        sp_editor.putString("UserName", username);


                                        sp_editor.commit();*/
                                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                                        //保存username和密码

                                            Map<String, Object> map = new HashMap<String, Object>();
                                            map.put("username", name_view.getText().toString().trim());
                                            map.put("password", pwd_view.getText().toString().trim());
                                            service.saveSharedPreferences("login", map);

                                        //
                                        Intent in = new Intent(LoginActivity.this,MainActivity.class);
                                        startActivity(in);

                                        finish();
                                    }else{
                                        Toast.makeText(LoginActivity.this, jsonObj.msg, Toast.LENGTH_SHORT).show();
                                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
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



        if (map != null && !map.isEmpty()) {
            name_view.setText(map.get("username").toString());

            pwd_view.setText(map.get("password").toString());

            if(map.get("username").toString()!=""&&map.get("password").toString()!="")
               //自动按下按钮，自动登录
                login.performClick();//要在clicklistener之后
            //Toast.makeText(LoginActivity.this, "自动", Toast.LENGTH_LONG).show();

        }

        //register按钮监听
        register.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

        }
        });




        see.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, MainActivity.class);
                startActivity(intent);

            }

        });

    }


}
