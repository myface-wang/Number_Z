package cn.bmob.otaku.number_z.Bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/2/14.
 */
public class ReplyBean extends BmobObject{

    private String content;
    private MyUser userid;
    private MyUser replyid;
    private DetailsBean detailsid;
    private boolean state;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MyUser getUserid() {
        return userid;
    }

    public void setUserid(MyUser userid) {
        this.userid = userid;
    }

    public MyUser getReplyid() {
        return replyid;
    }

    public void setReplyid(MyUser replyid) {
        this.replyid = replyid;
    }

    public DetailsBean getDetailsid() {
        return detailsid;
    }

    public void setDetailsid(DetailsBean detailsid) {
        this.detailsid = detailsid;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
