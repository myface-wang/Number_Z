package cn.bmob.otaku.number_z.Bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2015/12/10.
 */
public class CommentsBean extends BmobObject {

    private String content;
    private MyUser user;
    private DetailsBean detail;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }

    public DetailsBean getDetail() {
        return detail;
    }

    public void setDetail(DetailsBean detail) {
        this.detail = detail;
    }

}
