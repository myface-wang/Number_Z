package cn.bmob.otaku.number_z.window;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.otaku.number_z.Bean.CodeBean;
import cn.bmob.otaku.number_z.Bean.MyUser;
import cn.bmob.otaku.number_z.R;
import cn.bmob.otaku.number_z.utils.ErrorReport;
import cn.bmob.otaku.number_z.utils.SysUtils;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2015/12/28.
 */
public class CodeChooseUsActivity extends Activity{

    private CodeBean codeBean;

    private TextView tv_included,tv_copy,tv_password,tv_report;
    private LinearLayout LL_outside;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_code_choose_us);

        Intent intent = this.getIntent();
        codeBean= (CodeBean) intent.getSerializableExtra("codebean");

        LL_outside= (LinearLayout) findViewById(R.id.LL_outside);
        tv_included= (TextView) findViewById(R.id.tv_included);
        tv_copy= (TextView) findViewById(R.id.tv_copy);
        tv_password= (TextView) findViewById(R.id.tv_password);
        tv_report= (TextView) findViewById(R.id.tv_report);

        tv_password.setText("复制密码："+codeBean.getPassword());

        LL_outside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        tv_included.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (codeBean.getUser().getObjectId().equals(BmobUser.getCurrentUser(CodeChooseUsActivity.this, MyUser.class).getObjectId()))
                {
                    Toast.makeText(CodeChooseUsActivity.this, "这条好像是自己的哟", Toast.LENGTH_SHORT).show();
                }else {
                    include();
                }
            }
        });

        tv_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SysUtils.copy(codeBean.getCode(), CodeChooseUsActivity.this);
                finish();

            }
        });

        tv_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SysUtils.copy(codeBean.getPassword(), CodeChooseUsActivity.this);
                finish();
            }
        });

        tv_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                report();

            }
        });

    }

    //举报
    private void report() {

        finish();
    }

    //收录
    private void include() {
        MyUser user = BmobUser.getCurrentUser(this, MyUser.class);
        CodeBean post = new CodeBean();
        post.setUser(user);
        post.setPassword(codeBean.getPassword());
        post.setCode(codeBean.getCode());
        post.setType(codeBean.getType());
        post.setTitle(codeBean.getTitle()+"—副本");
        post.setShare(false);
        post.save(this, new SaveListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Toast.makeText(CodeChooseUsActivity.this, "收录成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                ErrorReport.RrrorCode(code, CodeChooseUsActivity.this);
            }
        });

    }
}
