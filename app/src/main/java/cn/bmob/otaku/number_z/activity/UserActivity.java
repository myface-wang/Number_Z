package cn.bmob.otaku.number_z.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.x;

import cn.bmob.otaku.number_z.Bean.MyUser;
import cn.bmob.otaku.number_z.R;
import cn.bmob.otaku.number_z.view.CircleImageView;
import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2015/12/30.
 */
public class UserActivity extends BaseActivity implements View.OnClickListener{

    private static final int PHOTO_WITH_DATA = 18;  //从SD卡中得到图片
    private static final int PHOTO_WITH_CAMERA = 37;// 拍摄照片

    private String imgPath  = "";
    private String imgName = "";

    private Toolbar toolbar;
    private TextView logout,tv_username,tv_email;

    private LinearLayout ll_headimage,ll_name,ll_email,ll_password;
    private CircleImageView image_head;
    private MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user);
        myApplication= (MyApplication) getApplication();
        toolbar = (Toolbar) findViewById(R.id.toolbar_user);
        toolbar.setTitle("个人中心");
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initview();

    }

    private void initview() {

        logout= (TextView) findViewById(R.id.logout);
        ll_headimage= (LinearLayout) findViewById(R.id.ll_headimage);
        ll_name= (LinearLayout) findViewById(R.id.ll_name);
        ll_email= (LinearLayout) findViewById(R.id.ll_email);
        ll_password= (LinearLayout) findViewById(R.id.ll_password);
        image_head= (CircleImageView) findViewById(R.id.image_head);
        tv_username= (TextView) findViewById(R.id.tv_username);
        tv_email= (TextView) findViewById(R.id.tv_email);
        ll_email.setOnClickListener(this);
        ll_headimage.setOnClickListener(this);
        ll_name.setOnClickListener(this);
        logout.setOnClickListener(this);
        ll_password.setOnClickListener(this);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {

            finish();
            return true;
        }else if (id==R.id.action_update)
        {
            Intent intent=new Intent(this,UpdateActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.update, menu);
        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.logout:
                logout();
                break;

            case R.id.ll_email:

                break;
            case R.id.ll_headimage:

                break;
            case R.id.ll_name:

                break;
            case R.id.ll_password:
                Intent intent=new Intent(this,ForgetActivity.class);
                startActivity(intent);
                Toast.makeText(this,"为了您账号的安全考虑，目前仅支持邮箱修改密码！",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        MyUser myUser=BmobUser.getCurrentUser(this, MyUser.class);
        if (myUser.getImage()!=null){
            x.image().bind(image_head,myUser.getImage().getFileUrl(this), myApplication.getOpt());
        }
        tv_username.setText(myUser.getUsername());
        tv_email.setText(myUser.getEmail());
    }

    //退出登录
    //清除缓存用户对象
    private void logout() {
        BmobUser.logOut(this);
        finish();
    }
}
