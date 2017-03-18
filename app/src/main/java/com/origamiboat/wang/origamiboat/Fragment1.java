package com.origamiboat.wang.origamiboat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.origamiboat.wang.origamiboat.Model.ResponseJson;
import com.origamiboat.wang.origamiboat.common.ServerWebRoot;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang on 2016/7/14 0014.
 */
public class Fragment1 extends android.support.v4.app.Fragment {
    ListView listView;  //声明一个ListView对象
    String str = "";
    private List<info> mlistInfo = new ArrayList<info>();  //声明一个list，动态存储要显示的信息
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    String url;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg1, container, false);
        listView = (ListView) view.findViewById(R.id.listview);    //将listView与布局对象关联
        //创建默认的ImageLoader配置参数

        imageLoader = ImageLoader.getInstance();
//
        options = new DisplayImageOptions.Builder()

                .showImageOnLoading(null)//加载过程中显示的图片

                .showImageForEmptyUri(null)//加载内容为空显示的图片

                .showImageOnFail(null)//加载失败显示的图片

                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)

                .bitmapConfig(Bitmap.Config.RGB_565).displayer(new FadeInBitmapDisplayer(388)).build();
        url = ServerWebRoot.getServerWebRoot()+"artical/";

//请求服务端数据
        new Thread(new Runnable() {
            public void run() {
                try {
                    String requestUrl = ServerWebRoot.getServerWebRoot() + "TransformArtical";
                    URL url = new URL(requestUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(3000);//设置连接超时时间3s
                    conn.setReadTimeout(3000);
                    conn.setRequestMethod("POST");
                    InputStream in = conn.getInputStream();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] buf = new byte[1024];
                    while (true) {
                        int len = in.read(buf);
                        if (len == -1) break;
                        out.write(buf, 0, len);
                    }

                    final String json = new String(out.toByteArray(), "UTF-8");
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Gson g = new Gson();
                            ResponseJson jsonObj = g.fromJson(json, ResponseJson.class);
                            if (jsonObj.status == 200) {

                                str = jsonObj.msg;
                                //Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();
                                setInfo();  //给信息赋值函数，用来测试
                                listView.setAdapter(new ListViewAdapter(mlistInfo));
                            } else {
                                Toast.makeText(getActivity(), jsonObj.msg, Toast.LENGTH_SHORT).show();
                                Toast.makeText(getActivity(), "从服务器获取数据失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


        listView.setAdapter(new ListViewAdapter(mlistInfo));


        //处理Item的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                info getObject = mlistInfo.get(position);   //通过position获取所点击的对象

                String infoLink = getObject.getLink();    //获取链接
                infoLink = ServerWebRoot.getServerWebRoot() + "artical/" + infoLink;
                String infoTitle=getObject.getTitle();
                //Toast.makeText(getActivity(), "链接："+infoLink,Toast.LENGTH_SHORT).show();
                //点击跳转
                Intent intent = new Intent();
                intent.putExtra("Link", infoLink);
                intent.putExtra("Title",infoTitle);
                intent.setClass(getActivity().getApplicationContext(), DetailsActivity.class);//fragment获取intent
                startActivity(intent);
            }
        });

        //长按菜单显示
        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            public void onCreateContextMenu(ContextMenu conMenu, View view, ContextMenu.ContextMenuInfo info) {
                conMenu.setHeaderTitle("MENU");
                conMenu.add(0, 0, 0, "delete");
                //conMenu.add(0, 1, 1, "条目二");
                //conMenu.add(0, 2, 2, "条目三");
            }
        });
        return view;
    }

    //长按菜单处理函数
    public boolean onContextItemSelected(MenuItem aItem) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) aItem.getMenuInfo();
        switch (aItem.getItemId()) {
            case 0:
                //Toast.makeText(MainActivity.this, "你点击了条目一",Toast.LENGTH_SHORT).show();
                return true;
            case 1:
                Toast.makeText(getActivity(), "你点击了条目二", Toast.LENGTH_SHORT).show();

                return true;
            case 2:
                Toast.makeText(getActivity(), "你点击了条目三", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }


    public class ListViewAdapter extends BaseAdapter {
        View[] itemViews;

        public ListViewAdapter(List<info> mlistInfo) {
            // TODO Auto-generated constructor stub
            itemViews = new View[mlistInfo.size()];
            for (int i = 0; i < mlistInfo.size(); i++) {
                info getInfo = (info) mlistInfo.get(i);    //获取第i个对象
                //调用makeItemView，实例化一个Item
                itemViews[i] = makeItemView(getInfo.getTitle(), getInfo.getTime(), getInfo.getLink(), getInfo.getAvatar());
            }
        }

        public int getCount() {
            return itemViews.length;
        }

        public View getItem(int position) {
            return itemViews[position];
        }

        public long getItemId(int position) {
            return position;
        }

        //绘制Item的函数
        private View makeItemView(String strTitle, String strTime, String strLink, int resId) {
            LayoutInflater inflater = (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // 使用View的对象itemView与R.layout.item关联
            View itemView = inflater.inflate(R.layout.list_item, null);

            // 通过findViewById()方法实例R.layout.item内各组件
            TextView title = (TextView) itemView.findViewById(R.id.title);
            title.setText(strTitle);    //填入相应的值
            TextView text = (TextView) itemView.findViewById(R.id.info);
            text.setText(strTime);
            TextView artical_link = (TextView) itemView.findViewById(R.id.artical_link);
            artical_link.setText(strLink);
            ImageView image = (ImageView) itemView.findViewById(R.id.img);
            //获取图片名称
            int startIndex = 0;
            int endIndex = 0;
            endIndex = strLink.lastIndexOf(".");
            //Toast.makeText(getActivity(),strarray[i + 2], Toast.LENGTH_LONG).show();
            String picname =strLink.subSequence(startIndex, endIndex)+"_1.jpg";
            //Toast.makeText(getActivity(),picname, Toast.LENGTH_LONG).show();
            imageLoader.displayImage(url+picname, image, options);
            //image.setImageResource(resId);

            return itemView;
        }


        public View getView(int position, View convertView, ViewGroup parent) {
            return itemViews[position];
        }
    }

    public void setInfo() {
        mlistInfo.clear();
        int j = 0;
        int k = 0;
        //分割字符串
        //String str = read();

        //Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG).show();
        String[] strarray = str.split("[|]");

        if (strarray.length > 1) {
            for (int i = 0; i < strarray.length - 2; i++) {
                //System.out.println(strarray.length);
                //System.out.println(strarray[i]);
                //information.setId(0);
                info information = new info();
                information.setTitle(strarray[i]);
                //System.out.println(strarray[i]);
                strarray[i + 1]=strarray[i + 1].substring(0,10);
                information.setTime(strarray[i + 1]);
                information.setLink(strarray[i + 2]);
                //System.out.println(strarray[i + 1]);
                information.setAvatar(R.drawable.ssss);
                mlistInfo.add(information); //将新的info对象加入到信息列表中
                i = i + 2;
            }


            /*else if(j%3==0){
                information.setNum("Num" + strarray[i]);
                j++;
            }*/


        }
    }

    /**
     * @author chenzheng_java
     * 读取刚才用户保存的内容
     */
    public String read() {
        String content = "";
        try {
            FileInputStream inputStream = getActivity().openFileInput("ss.txt");
            byte[] bytes = new byte[1024];
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            while (inputStream.read(bytes) != -1) {
                arrayOutputStream.write(bytes, 0, bytes.length);
            }
            inputStream.close();
            arrayOutputStream.close();
            content = new String(arrayOutputStream.toByteArray());
            //showTextView.setText(content);
            //Toast.makeText(MainActivity.this,content, Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

}
