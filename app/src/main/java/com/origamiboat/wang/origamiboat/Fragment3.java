package com.origamiboat.wang.origamiboat;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.origamiboat.wang.origamiboat.common.ServerWebRoot;

/**
 * Created by wang on 2016/7/14 0014.
 */
public class Fragment3 extends android.support.v4.app.Fragment {
    ImageView head_pic;
    LinearLayout button_set;
    LinearLayout button_about;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fg3, container,false);
        button_about=(LinearLayout) view.findViewById(R.id.button_about);
        button_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity().getApplicationContext(),DetailsActivity.class);//fragment获取intent
                startActivity(intent);
            }
        });
        button_set=(LinearLayout) view.findViewById(R.id.button_set);
        button_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity().getApplicationContext(), AccountActivity.class);//fragment获取intent
                startActivity(intent);
            }
        });


        head_pic = (ImageView) view.findViewById(R.id.head_pic);
        head_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity().getApplicationContext(), AccountActivity.class);//fragment获取intent
                startActivity(intent);
            }
        });
        //创建默认的ImageLoader配置参数

        imageLoader = ImageLoader.getInstance();
//
        options = new DisplayImageOptions.Builder()

                .showImageOnLoading(null)//加载过程中显示的图片

                .showImageForEmptyUri(null)//加载内容为空显示的图片

                .showImageOnFail(null)//加载失败显示的图片

                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)

                .bitmapConfig(Bitmap.Config.RGB_565).displayer(new FadeInBitmapDisplayer(388)).build();
        String url = ServerWebRoot.getServerWebRoot()+"upload/PHOTOIMAGE_ANSWER.jpg";
        imageLoader.displayImage(url, head_pic, options);
        return view;

    }
@Override
    public void  onResume(){
    String url = ServerWebRoot.getServerWebRoot()+"upload/PHOTOIMAGE_ANSWER.jpg";
    imageLoader.displayImage(url, head_pic, options);
    super.onResume();
}

}
