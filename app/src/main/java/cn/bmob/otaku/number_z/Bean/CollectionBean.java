package cn.bmob.otaku.number_z.Bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2015/12/29.
 */
public class CollectionBean extends BmobObject{

    private DetailsBean details;
    private MyUser userid;

    public DetailsBean getDetails() {
        return details;
    }

    public void setDetails(DetailsBean details) {
        this.details = details;
    }

    public MyUser getUserid() {
        return userid;
    }

    public void setUserid(MyUser userid) {
        this.userid = userid;
    }
}
