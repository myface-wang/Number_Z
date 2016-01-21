package cn.bmob.otaku.number_z.Bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2015/12/27.
 */
public class CodeBean extends BmobObject implements Serializable{

    private String title;
    private String code;
    private int type;
    private MyUser user;
    private Boolean share;
    private String password;
    private Integer star;
    private Integer cai;

    public Integer getCai() {
        return cai;
    }

    public void setCai(Integer cai) {
        this.cai = cai;
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getShare() {
        return share;
    }

    public void setShare(Boolean share) {
        this.share = share;
    }

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
