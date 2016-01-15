package cn.bmob.otaku.number_z.activity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ImageView;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import cn.bmob.otaku.number_z.BuildConfig;
import cn.bmob.otaku.number_z.R;
import cn.bmob.otaku.number_z.fragment.CodeMeFragment.Myhandler;
import cn.bmob.otaku.number_z.service.MyPushService;
import cn.bmob.otaku.number_z.utils.BaseDate;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;

/**
 * Created by wyouflf on 15/10/28.
 */
public class MyApplication extends Application {

    private Myhandler handler=null;

    public Myhandler getHandler() {
        return handler;
    }

    public void setHandler(Myhandler handler) {
        this.handler = handler;
    }

    private ImageOptions opt;

    public ImageOptions getOpt() {
       Log.i("gif", String.valueOf(opt.getImageScaleType()));
        return opt;
    }

    public void setOpt(ImageOptions opt) {
        this.opt = opt;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
        Bmob.initialize(this, BaseDate.APPID);
        // 使用推送服务时的初始化操作
        BmobInstallation.getCurrentInstallation(this).save();
        // 启动推送服务
        BmobPush.startWork(this, BaseDate.APPID);

        Intent intent=new Intent(this, MyPushService.class);
        startService(intent);

        SharedPreferences share=getSharedPreferences("config", Context.MODE_PRIVATE);
        setOpt(setOptions(share.getBoolean("imageflag",true)));

    }

    public ImageOptions  setOptions(boolean flag) {

        ImageOptions options;

        if (flag)
        {
            options= new ImageOptions.Builder()
                    // 是否忽略GIF格式的图片
                    .setIgnoreGif(false)
                            // 图片缩放模式
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                            // 下载中显示的图片
                    .setLoadingDrawableId(R.mipmap.ic_launcher)
                            // 下载失败显示的图片
                    .setFailureDrawableId(R.mipmap.ic_launcher)
                            // 得到ImageOptions对象
                    .build();
        }else {
            options= new ImageOptions.Builder()
                    // 是否忽略GIF格式的图片
                    .setIgnoreGif(true)
                            // 图片缩放模式
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                            // 下载中显示的图片
                    .setLoadingDrawableId(R.mipmap.ic_launcher)
                            // 下载失败显示的图片
                    .setFailureDrawableId(R.mipmap.ic_launcher)
                            // 得到ImageOptions对象
                    .build();
        }

        setImageFlag(flag);
        return options;

    }

    private void setImageFlag(boolean flag){

        SharedPreferences sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putBoolean("imageflag",flag);
        editor.commit();//提交修改
    }

}
