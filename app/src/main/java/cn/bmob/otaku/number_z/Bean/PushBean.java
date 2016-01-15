package cn.bmob.otaku.number_z.Bean;

/**
 * Created by Administrator on 2016/1/8.
 */
public class PushBean {

    private Aps aps;
    private String text;
    private String user;
    private String content;
    private int type;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Aps getAps() {
        return aps;
    }

    public void setAps(Aps aps) {
        this.aps = aps;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public class Aps {
        private String sound;
        private String alert;
        private int bage;

        public String getSound() {
            return sound;
        }

        public void setSound(String sound) {
            this.sound = sound;
        }

        public String getAlert() {
            return alert;
        }

        public void setAlert(String alert) {
            this.alert = alert;
        }

        public int getBage() {
            return bage;
        }

        public void setBage(int bage) {
            this.bage = bage;
        }
    }

}

