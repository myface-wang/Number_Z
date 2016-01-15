package cn.bmob.otaku.number_z.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.otaku.number_z.R;
import cn.bmob.otaku.number_z.utils.Regular;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.ResetPasswordByEmailListener;

/**
 * Created by Administrator on 2015/12/23.
 */
public class ForgetActivity extends BaseActivity{

    private Toolbar toolbar;
    private TextInputLayout et_email;
    private Button btn_forget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forget);

        toolbar = (Toolbar) findViewById(R.id.toolbar_forget);
        toolbar.setTitle("忘记密码");
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initview();
    }

    private void initview() {

        et_email= (TextInputLayout) findViewById(R.id.et_email);
        final EditText email= et_email.getEditText();

        btn_forget= (Button) findViewById(R.id.btn_forget);

        btn_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Regular.isEmail(email.getText().toString()) == false) {
                    et_email.setError("您输入的邮箱有误~");
                    et_email.setErrorEnabled(true);
                } else {
                    et_email.setErrorEnabled(false);

                    forget(email.getText().toString());
                }
            }
        });
    }

    private void forget(String email) {

            //需要一个diolog完成动画


        BmobUser.resetPasswordByEmail(this, email, new ResetPasswordByEmailListener() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Toast.makeText(ForgetActivity.this, "重置密码请求成功，请到邮箱进行密码重置操作", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(int code, String e) {
                // TODO Auto-generated method stub
                Log.i("ForgetActivity",code+e);
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
