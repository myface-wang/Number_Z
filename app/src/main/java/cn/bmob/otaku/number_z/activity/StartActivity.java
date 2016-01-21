package cn.bmob.otaku.number_z.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import cn.bmob.otaku.number_z.R;

/**
 * Created by Administrator on 2016/1/17.
 */
public class StartActivity extends BaseActivity{

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Intent intent=new Intent(StartActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        TextView tv_vc= (TextView) findViewById(R.id.tv_vc);
        try {
            tv_vc.setText("版本号："+getVersionName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mHandler.sendEmptyMessageDelayed(0, 5000);

    }

    private String getVersionName() throws Exception
    {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
        String version = packInfo.versionName;
        return version;
    }

}
