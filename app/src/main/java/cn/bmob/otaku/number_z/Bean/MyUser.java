package cn.bmob.otaku.number_z.Bean;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Administrator on 2015/11/16.
 */
public class MyUser extends BmobUser implements Serializable{

    private BmobFile image;
    private BmobRelation collection;

    public BmobRelation getCollection() {
        return collection;
    }

    public void setCollection(BmobRelation collection) {
        this.collection = collection;
    }

    public BmobFile getImage() {
        return image;
    }

    public void setImage(BmobFile image) {
        this.image = image;
    }

}
