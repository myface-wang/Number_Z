package cn.bmob.otaku.number_z.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.xutils.x;

import cn.bmob.otaku.number_z.R;
import cn.bmob.otaku.number_z.view.SwitchView;
import cn.bmob.v3.BmobQuery;

/**
 * Created by Administrator on 2016/1/6.
 */
public class SettingActivity extends BaseActivity{

    private Toolbar toolbar;
    private MyApplication myApplication;

    private LinearLayout ll_data,ll_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);

        myApplication= (MyApplication) getApplication();

        toolbar = (Toolbar) findViewById(R.id.toolbar_user);
        toolbar.setTitle("设置");
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initview();

    }

    private void initview() {

        SharedPreferences share=getSharedPreferences("config", Context.MODE_PRIVATE);
        final boolean flag= share.getBoolean("imageflag",true);

        final SwitchView viewSwitch = (SwitchView) findViewById(R.id.sv_image);
        // 设置初始状态。true为开;false为关[默认]。set up original status. true for open and false for close[default]
        viewSwitch.setOpened(flag);
        viewSwitch.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override public void toggleToOn(View view) {
                // 原本为关闭的状态，被点击后 originally present close status after clicking

                myApplication.setOpt(myApplication.setOptions(true));
                viewSwitch.toggleSwitch(true);
                // 执行一些耗时的业务逻辑操作 implement some time-consuming logic operation
//                viewSwitch.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        viewSwitch.toggleSwitch(true); //以动画效果切换到打开的状态 through changing animation effect to open status
//                    }
//                },1000);

            }
            @Override public void toggleToOff(View view) {
                // 原本为打开的状态，被点击后 originally present the status of open after clicking
                myApplication.setOpt(myApplication.setOptions(false));
                viewSwitch.toggleSwitch(false);
            }
        });


        final SwitchView sv_push = (SwitchView) findViewById(R.id.sv_push);
        sv_push.setOpened(flag);//写完后，注意这里的flag！！
        sv_push.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(View view) {
                sv_push.toggleSwitch(true);
                Toast.makeText(SettingActivity.this, "建设中。。。", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void toggleToOff(View view) {
                sv_push.toggleSwitch(false);
                Toast.makeText(SettingActivity.this, "建设中。。。", Toast.LENGTH_SHORT).show();
            }
        });


        ll_image= (LinearLayout) findViewById(R.id.ll_image);
        ll_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x.image().clearCacheFiles();
            }
        });

        ll_data= (LinearLayout) findViewById(R.id.ll_data);
        ll_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobQuery.clearAllCachedResults(SettingActivity.this);
            }
        });
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
