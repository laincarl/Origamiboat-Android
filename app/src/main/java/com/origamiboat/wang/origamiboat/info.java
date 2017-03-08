package com.origamiboat.wang.origamiboat;

/**
 * Created by wang on 2017/3/5.
 */

public class info {
    private int id; //信息ID
    private String title;   //信息标题
    private String total; //详细信息
    private int avatar; //图片ID
    private  String num;


    //信息ID处理函数
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    //标题
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    //详细信息
    public void setTotal(String info) {
        this.total = info;
    }
    public String getTotal() {
        return total;
    }

    public void setNum(String info) {
        this.num = num;
    }
    public String getNum() {
        return num;
    }
    //图片
    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }
    public int getAvatar() {
        return avatar;
    }


}