package cn.bmob.otaku.number_z.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import cn.bmob.otaku.number_z.Bean.MyBmobInstallation;
import cn.bmob.otaku.number_z.Bean.MyUser;
import cn.bmob.otaku.number_z.R;
import cn.bmob.otaku.number_z.utils.ErrorReport;
import cn.bmob.otaku.number_z.utils.Regular;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2015/12/23.
 */
public class RegisterActivity extends BaseActivity {

    private TextInputLayout et_email,et_username,et_password,et_password_next;

    private Button btn_register;
    private Toolbar toolbar;

    private EditText email;
    private EditText password;
    private EditText username;
    private EditText password_next;

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if (msg.what==0)
            {
                if (Regular.isEmail(email.getText().toString()) == false)
                {
                    et_email.setError("有效的邮箱才能通过哟~");
                    et_email.setErrorEnabled(true);
                }else {
                    et_email.setErrorEnabled(false);
                }
            }else if (msg.what==1)
            {
                if (Regular.isPassword(password.getText().toString())==false)
                {
                    et_password.setError("只能输入6到15位的英文或数字啦~");
                    et_password.setErrorEnabled(true);
                }else {
                    et_password.setErrorEnabled(false);
                }

            }else if (msg.what==2)
            {

                if (Regular.isnull(username.getText().toString()))
                {
                    et_username.setErrorEnabled(true);
                    et_username.setError("不允许为空");

                }else {
                    et_username.setErrorEnabled(false);
                }

            }

        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar = (Toolbar) findViewById(R.id.toolbar_register);
        toolbar.setTitle("注册");
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initview();

    }

    private void initview() {

        et_email= (TextInputLayout) findViewById(R.id.et_email);
        et_username= (TextInputLayout) findViewById(R.id.et_username);
        et_password= (TextInputLayout) findViewById(R.id.et_password);
        et_password_next= (TextInputLayout) findViewById(R.id.et_password_next);

        email=et_email.getEditText();
        username=et_username.getEditText();
        password=et_password.getEditText();
        password_next=et_password_next.getEditText();

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus == false) {
                    Message msg = new Message();
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                }
            }
        });


        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus == false) {
                    Message msg = new Message();
                    msg.what = 2;
                    mHandler.sendMessage(msg);
                }
            }
        });


        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 6) {
                    Message msg = new Message();
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btn_register= (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!password.getText().toString().equals(password_next.getText().toString())) {
                    et_password_next.setError("两次密码不一致");
                    et_password_next.setErrorEnabled(true);
                } else {
                    et_password_next.setErrorEnabled(false);
                    Registered(email.getText().toString(), username.getText().toString(), password_next.getText().toString());

                }
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

    private void Registered(final String email,String username, final String password){

        //添加用户设备编号
        BmobInstallation.getCurrentInstallation(this).save();

        MyUser bu = new MyUser();
        bu.setUsername(username);
        bu.setPassword(password);
        bu.setEmail(email);
        bu.signUp(this, new SaveListener() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                login(email, password);
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                ErrorReport.RrrorCode(code, RegisterActivity.this);
            }
        });
    }

    private void login(String email,String password){

        BmobUser.loginByAccount(this, email, password, new LogInListener<MyUser>() {

            @Override
            public void done(MyUser user, BmobException e) {
                // TODO Auto-generated method stub
                if (user != null) {
                    bind();

                    Intent intent = new Intent();
                    intent.setClassName( getPackageName(), LoginActivity.class.getName());
                    if (getPackageManager().resolveActivity(intent, 0) == null) {
                        // 说明系统中不存在这个activity
                    }else {
                        LoginActivity.instance.finish();
                    }
                    finish();
                    Toast.makeText(RegisterActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void bind(){

        BmobQuery<MyBmobInstallation> query = new BmobQuery<MyBmobInstallation>();
        query.addWhereEqualTo("installationId", BmobInstallation.getInstallationId(this));
        query.findObjects(this, new FindListener<MyBmobInstallation>() {

            @Override
            public void onSuccess(List<MyBmobInstallation> object) {
                // TODO Auto-generated method stub
                if (object.size() > 0) {
                    MyBmobInstallation mbi = object.get(0);
                    mbi.setUserid(BmobUser.getCurrentUser(RegisterActivity.this, MyUser.class).getObjectId());
                    mbi.update(RegisterActivity.this, new UpdateListener() {

                        @Override
                        public void onSuccess() {
                            // TODO Auto-generated method stub
                            Log.i("bmob", "设备信息更新成功");
                        }

                        @Override
                        public void onFailure(int code, String msg) {
                            // TODO Auto-generated method stub
                            Log.i("bmob", "设备信息更新失败:" + msg);
                        }
                    });
                }else {
                    Toast.makeText(RegisterActivity.this,"您的手机信息",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
            }
        });
    }
}
