package cn.bmob.otaku.number_z.Bean;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2015/11/4.
 */
public class DetailsBean extends BmobObject{

    private List<CommentBean> content;
    private BmobFile cover;
    private String name;
    private BmobFile headimg;
    private String introduction;

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public BmobFile getHeadimg() {
        return headimg;
    }

    public void setHeadimg(BmobFile headimg) {
        this.headimg = headimg;
    }

    public BmobFile getCover() {
        return cover;
    }

    public void setCover(BmobFile cover) {
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CommentBean> getContent() {
        return content;
    }

    public void setContent(List<CommentBean> content) {
        this.content = content;
    }
}
