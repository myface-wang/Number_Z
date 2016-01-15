package cn.bmob.otaku.number_z.Bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2015/12/30.
 */
public class FeedbackBean extends BmobObject{

    private String content;
    private Boolean flag;//是否显示
    private Boolean state;//当前状态
    private MyUser user;

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

}
