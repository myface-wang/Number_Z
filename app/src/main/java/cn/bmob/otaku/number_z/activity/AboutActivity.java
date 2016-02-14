package cn.bmob.otaku.number_z.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import cn.bmob.otaku.number_z.R;

/**
 * Created by Administrator on 2015/12/25.
 */
public class AboutActivity extends BaseActivity{

    private TextView tv_context,tv_vsersion;
    private ImageView app_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_about);
        toolbar.setTitle("关于我们");
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initview();
    }

    private void initview() {
//        String text2="我们不生产水,我们是大自然的搬运工。所有资源均来自网络，如有侵权请及时联系我们，审核后进行相关删除。邮箱xxxx@qq.com";
        tv_context= (TextView) findViewById(R.id.tv_context);
        tv_vsersion= (TextView) findViewById(R.id.tv_vsersion);

        app_img= (ImageView) findViewById(R.id.app_img);
        app_img.setImageResource(R.mipmap.ic_launcher);

        tv_vsersion.setText("版本号："+getVersionName());
//        tv_context.setText(text2);
    }

    private String getVersionName()
    {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        return version;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
