package com.origamiboat.wang.origamiboat.data_storage;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by wang on 2016/8/19 0019.
 */
public class LoginService {

    private Context context;
    public LoginService(Context context) {
        // TODO Auto-generated constructor stub
        this.context=context;
    }

    public boolean saveLoginMsg(String name,String password){
        boolean flag=false;
        //文件名不要加后缀名，系统会自动以.xml的文件保存
        SharedPreferences preferences=context.getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("username", name);
        editor.putString("password", password);
        flag=editor.commit();//只有调用commit方法才会把传回来的参数保存在文件上
        return flag;

    }
    public boolean saveSharedPreferences(String fileName,Map<String, Object> map){
        boolean flag=false;
        SharedPreferences preferences=context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        for(Map.Entry<String, Object> entry:map.entrySet()){
            String key=entry.getKey();
            Object object=entry.getValue();
            if(object instanceof Boolean){
                Boolean new_name=(Boolean) object;
                editor.putBoolean(key, new_name);
            }else if(object instanceof Integer){
                Integer integer =(Integer)object;
                editor.putInt(key, integer);
            }else if(object instanceof Float){
                Float f=(Float) object;
                editor.putFloat(key, f);
            }else if(object instanceof Long){
                Long l=(Long) object;
                editor.putLong(key, l);
            }else if(object instanceof String){
                String s=(String) object;
                editor.putString(key, s);
            }
        }
        flag=editor.commit();
        return flag;

    }
    public Map<String, ?> getSharePreference(String fileName) {
        Map<String, ?> map = null;
        SharedPreferences preferences = context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        map = preferences.getAll();
        return map;
    }
}