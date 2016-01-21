package cn.bmob.otaku.number_z.window;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.otaku.number_z.Bean.CodeBean;
import cn.bmob.otaku.number_z.Bean.MyUser;
import cn.bmob.otaku.number_z.R;
import cn.bmob.otaku.number_z.utils.ErrorReport;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2015/12/28.
 */
public class CodeInputActivity extends Activity{

    private LinearLayout ll_outside;
    private EditText ed_title,ed_password,ed_code;
    private TextView tv_cancel,tv_determine,tv_delete;

    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_code_input);

        Intent intent=getIntent();
        type=intent.getStringExtra("type");
        Log.i("type", type);

        ll_outside= (LinearLayout) findViewById(R.id.ll_outside);

        ed_title= (EditText) findViewById(R.id.ed_title);
        ed_password= (EditText) findViewById(R.id.ed_password);
        ed_code= (EditText) findViewById(R.id.ed_code);

        tv_cancel= (TextView) findViewById(R.id.tv_cancel);
        tv_determine= (TextView) findViewById(R.id.tv_determine);
        tv_delete= (TextView) findViewById(R.id.tv_delete);

        switch (type)
        {
            case "url":
                type="0";
                Log.i("type",type);
                break;
            case "yun":
                type="1";
                ed_code.setText("http://yun.baidu.com/s/");
                Log.i("type", type);
                break;
            case "magnetic":
                type="2";
                ed_code.setText("magnet:?xt=urn:btih:");
                Log.i("type", type);
                break;
            case "other":
//                type="3";
                break;
            default:
                break;
        }

        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ed_code.setText("");

            }
        });

        ll_outside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }

        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_determine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title=ed_title.getText().toString();
                String code=ed_code.getText().toString();
                String password=ed_password.getText().toString();
                if (code!=null&&title!=null)
                {
                    query(code,title,password);
                }else {
                    Toast.makeText(CodeInputActivity.this,"输入内容不全！",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void check() {

        String title=ed_title.getText().toString();
        String code=ed_code.getText().toString();

        if (!TextUtils.isEmpty(title)|!TextUtils.isEmpty(code))
        {
            if (!code.equals("http://yun.baidu.com/s/")&&!code.equals("magnet:?xt=urn:btih:"))
            {
                Toast.makeText(CodeInputActivity.this, "退出编辑就点取消吧", Toast.LENGTH_SHORT).show();
            }else {
                finish();
            }

        }else {
            finish();
        }
    }

    private void query(String code,String title,String password) {

        MyUser user = BmobUser.getCurrentUser(this, MyUser.class);
        CodeBean post = new CodeBean();
        post.setUser(user);
        post.setPassword(password);
        post.setCode(code);
        post.setType(Integer.parseInt(type));
        post.setTitle(title);
        post.setShare(false);
        post.save(this, new SaveListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Toast.makeText(CodeInputActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                ErrorReport.RrrorCode(code,CodeInputActivity.this);
            }
        });
    }


}
