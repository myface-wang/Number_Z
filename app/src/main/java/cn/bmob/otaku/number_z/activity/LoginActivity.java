package cn.bmob.otaku.number_z.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
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
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2015/12/21.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private TextInputLayout et_email;
    private TextInputLayout et_password;
    private Button btn_login;
    private Button btn_register;
    private Toolbar toolbar;

    private EditText email;
    private EditText password;
    public static LoginActivity instance = null;

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if (msg.what==0)
            {

                if (Regular.isEmail(email.getText().toString()) == false)
                {
                    et_email.setError("请输入正确的邮箱~");
                    et_email.setErrorEnabled(true);
                }else {
                    et_email.setErrorEnabled(false);
                }
            }else if (msg.what==1)
            {
                if (Regular.isPassword(password.getText().toString()) == false)
                {
                    et_password.setError("密码不符合规范");
                    et_password.setErrorEnabled(true);
                }else {
                    et_password.setErrorEnabled(false);
                }

            }

        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        instance=this;

        toolbar = (Toolbar) findViewById(R.id.toolbar_login);
        toolbar.setTitle("登录");
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initview();

    }

    private void initview() {

        et_email= (TextInputLayout) findViewById(R.id.et_email);
        et_password= (TextInputLayout) findViewById(R.id.et_password);
        btn_login= (Button) findViewById(R.id.btn_login);
        btn_register= (Button) findViewById(R.id.btn_register);
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);

        et_password.setHint("请输入密码");
        et_email.setHint("请输入邮箱");

        email=et_email.getEditText();
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

        password=et_password.getEditText();
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

    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btn_login:

                if (Regular.isEmail(email.getText().toString())&&Regular.isPassword(password.getText().toString()))
                {
                    login(email.getText().toString(), password.getText().toString());
                }

                break;
            case R.id.btn_register:

                register();

                break;
            default:
                break;
        }

    }


    public void login(String email,String password)
    {

        BmobUser bu2 = new BmobUser();
        bu2.setUsername(email);
        bu2.setPassword(password);
        bu2.login(this, new SaveListener() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                bind();
                Toast.makeText(LoginActivity.this, "登录成功!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                ErrorReport.RrrorCode(code,LoginActivity.this);
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
                    mbi.setUserid(BmobUser.getCurrentUser(LoginActivity.this, MyUser.class).getObjectId());
                    mbi.update(LoginActivity.this, new UpdateListener() {

                        @Override
                        public void onSuccess() {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void onFailure(int code, String msg) {
                            // TODO Auto-generated method stub
                            ErrorReport.RrrorCode(code, LoginActivity.this);
                        }
                    });
                }
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                ErrorReport.RrrorCode(code, LoginActivity.this);
            }
        });
    }

    public void register()
    {
        Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {

            finish();
            return true;
        }else if (id==R.id.action_forget)
        {
            Intent intent=new Intent(this,ForgetActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.login_menu, menu);
        return true;
    }

}
