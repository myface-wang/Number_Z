package cn.bmob.otaku.number_z.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.xutils.x;

import cn.bmob.otaku.number_z.Bean.MyUser;
import cn.bmob.otaku.number_z.R;
import cn.bmob.otaku.number_z.utils.BaseDate;
import cn.bmob.otaku.number_z.utils.NoDoubleClickListener;
import cn.bmob.otaku.number_z.utils.Regular;
import cn.bmob.otaku.number_z.view.CircleImageView;
import cn.bmob.otaku.number_z.window.CameraActivity;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2016/1/15.
 */
public class UpdateActivity extends BaseActivity{

    private Toolbar toolbar;

    private LinearLayout ll_headimage;
    private EditText tv_username,tv_email;
    private CircleImageView image_head;
    private Button btn_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        toolbar = (Toolbar) findViewById(R.id.toolbar_update);
        toolbar.setTitle("编辑资料");
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initview();
    }

    private void initview() {

        MyUser myUser= BmobUser.getCurrentUser(this, MyUser.class);

        ll_headimage= (LinearLayout) findViewById(R.id.ll_headimage);
        image_head= (CircleImageView) findViewById(R.id.image_head);
        tv_username= (EditText) findViewById(R.id.tv_username);
        tv_email= (EditText) findViewById(R.id.tv_email);
        btn_update= (Button) findViewById(R.id.btn_update);

        if (myUser.getImage()!=null){
            x.image().bind(image_head,myUser.getImage().getFileUrl(this), BaseDate.Head_OPTIONS());
        }
        tv_username.setText(myUser.getUsername());
        tv_email.setText(myUser.getEmail());

        ll_headimage.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                Intent intent = new Intent(UpdateActivity.this, CameraActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        btn_update.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                String name=tv_username.getText().toString();
                String email=tv_email.getText().toString();

                if (!Regular.isnull(email)&&Regular.isEmail(email)&&!Regular.isnull(name))
                {
                    update(name,email);
                }else {
                    Toast.makeText(UpdateActivity.this,"输入信息有误！"+name+email,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void update(String name,String email) {

        MyUser newUser = new MyUser();
        newUser.setUsername(name);
        newUser.setEmail(email);
        BmobUser bmobUser = BmobUser.getCurrentUser(this);
        newUser.update(this,bmobUser.getObjectId(),new UpdateListener() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Toast.makeText(UpdateActivity.this,"信息更新成功",Toast.LENGTH_SHORT).show();
                finish();
            }
            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                Toast.makeText(UpdateActivity.this,"信息更新失败",Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data!=null)
        {
            x.image().bind(image_head, data.getExtras().getString("url"), BaseDate.Head_OPTIONS());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
