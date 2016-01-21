package cn.bmob.otaku.number_z.Bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/11/18.
 */
public class CommentBean implements Serializable {

    private String objectId;
    private String authorname;
    private String artname;
    private String from;
    private String url;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }

    public String getArtname() {
        return artname;
    }

    public void setArtname(String artname) {
        this.artname = artname;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//
//        dest.writeString(url);
//        dest.writeString(authorname);
//        dest.writeString(from);
//        dest.writeString(artname);
//
//    }
//
//    public static final Creator<CommentBean> CREATOR = new Creator<CommentBean>()
//    {
//        public CommentBean createFromParcel(Parcel source)
//        {
//            CommentBean commentBean = new CommentBean();
//            commentBean.url=source.readString();
//            commentBean.artname=source.readString();
//            commentBean.authorname=source.readString();
//            commentBean.from=source.readString();
//
//            return commentBean;
//        }
//
//        public CommentBean[] newArray(int size)
//        {
//            return new CommentBean[size];
//        }
//    };

}
