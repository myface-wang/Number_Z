package cn.bmob.otaku.number_z.window;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.otaku.number_z.Bean.CodeBean;
import cn.bmob.otaku.number_z.R;
import cn.bmob.otaku.number_z.activity.MyApplication;
import cn.bmob.otaku.number_z.fragment.CodeMeFragment;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2015/12/28.
 */
public class CodeChooseMeActivity extends Activity{

    private LinearLayout LL_outside;
    private TextView tv_copy;
    private TextView tv_delete;
    private TextView tv_share,tv_down;
    private ImageView img_down;

    private String code,objid;
    private MyApplication myApplication=null;
    private CodeMeFragment.Myhandler myhandler=null;

    private boolean flag;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_choose_me);

        LL_outside= (LinearLayout) findViewById(R.id.LL_outside);

        Intent intent = getIntent();
        code= intent.getStringExtra("code");
        objid=intent.getStringExtra("objid");
        flag=intent.getBooleanExtra("flag", true);
        type=intent.getIntExtra("type", 0);

        Log.i("type",type+"");

        myApplication= (MyApplication) getApplication();
        myhandler= myApplication.getHandler();

        tv_copy= (TextView) findViewById(R.id.tv_copy);
        tv_delete= (TextView) findViewById(R.id.tv_delete);
        tv_share= (TextView) findViewById(R.id.tv_share);
        tv_down= (TextView) findViewById(R.id.tv_down);
        img_down= (ImageView) findViewById(R.id.img_down);

        if (type!=2)
        {
            tv_down.setText("在浏览器中打开");
        }

        tv_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (type==2)
                {
                    //应该遍历，获取磁力链的条数
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(code));
                    intent.addCategory("android.intent.category.DEFAULT");
                    try {
                        startActivity(intent);
                        finish();
                    }catch (Exception e)
                    {
                        Toast.makeText(CodeChooseMeActivity.this,"未找到可供下载的app",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    try{
                        Uri uri = Uri.parse(code);
                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(intent);
                    }catch (Exception e)
                    {
                        Toast.makeText(CodeChooseMeActivity.this,"异常网址，请自行打开浏览器",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        if (flag)
        {
            tv_share.setText("撤回分享");
        }else {
            tv_share.setText("分享");
        }

        LL_outside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               copy(code,CodeChooseMeActivity.this);
            }
        });

        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myhandler.sendEmptyMessage(787);
                finish();

            }
        });

        tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });

    }

    private void share() {

        if (flag)
        {
            tv_share.setText("分享");
        }else {
            tv_share.setText("撤回分享");

        }
        CodeBean codebean = new CodeBean();
        codebean.setShare(!flag);
        codebean.update(this, objid, new UpdateListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Log.i("bmob", "更新成功：");
                flag=!flag;
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                Log.i("bmob","更新失败："+msg);
            }
        });

    }

    /**
     * 实现文本复制功能
     */
    public void copy(String content, Context context)
    {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());

        Toast.makeText(context,"复制成功",Toast.LENGTH_SHORT).show();
        finish();
    }
}
