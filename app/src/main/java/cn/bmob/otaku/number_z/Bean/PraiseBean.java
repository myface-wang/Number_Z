package cn.bmob.otaku.number_z.Bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2015/12/27.
 */
public class PraiseBean extends BmobObject{

    private MyUser userid;
    private CodeBean codeid;

    public MyUser getUserid() {
        return userid;
    }

    public void setUserid(MyUser userid) {
        this.userid = userid;
    }

    public CodeBean getCodeid() {
        return codeid;
    }

    public void setCodeid(CodeBean codeid) {
        this.codeid = codeid;
    }
}
