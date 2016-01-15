package cn.bmob.otaku.number_z.Bean;

import android.content.Context;

import cn.bmob.v3.BmobInstallation;

/**
 * Created by Administrator on 2016/1/8.
 */
public class MyBmobInstallation extends BmobInstallation{


    private String userid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public MyBmobInstallation(Context context) {
        super(context);
    }



}
