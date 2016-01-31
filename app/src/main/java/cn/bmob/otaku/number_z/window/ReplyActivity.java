package cn.bmob.otaku.number_z.window;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.bmob.otaku.number_z.Bean.CommentsBean;
import cn.bmob.otaku.number_z.Bean.DetailsBean;
import cn.bmob.otaku.number_z.Bean.MyBmobInstallation;
import cn.bmob.otaku.number_z.Bean.MyUser;
import cn.bmob.otaku.number_z.Bean.PushBean;
import cn.bmob.otaku.number_z.R;
import cn.bmob.otaku.number_z.utils.ErrorReport;
import cn.bmob.otaku.number_z.utils.Regular;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2016/1/7.
 */
public class ReplyActivity extends Activity implements View.OnClickListener{

    private EditText ed_reply;
    private LinearLayout ll_outside;
    private TextView tv_cancel,tv_reply;
    private String value,name,userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reply);

        Intent intent=getIntent();
        value=intent.getStringExtra("objectid");
        name=intent.getStringExtra("name");
        userid=intent.getStringExtra("userid");
        initview();

    }

    private void initview() {

        ed_reply= (EditText) findViewById(R.id.ed_reply);
        ll_outside= (LinearLayout) findViewById(R.id.ll_outside);
        tv_cancel= (TextView) findViewById(R.id.tv_cancel);
        tv_reply= (TextView) findViewById(R.id.tv_reply);
        ed_reply.setOnClickListener(this);
        ll_outside.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        tv_reply.setOnClickListener(this);

        ed_reply.setHint("回复" + name + ":");

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.ed_reply:
                break;
            case R.id.ll_outside:
                if (ed_reply.getText().toString()!=null)
                {
                    finish();
                }else {
                    Toast.makeText(this,"试试取消按钮",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_reply:
                if (Regular.isnull(ed_reply.getText().toString()))
                {
                    Toast.makeText(this,"输入信息有误",Toast.LENGTH_SHORT).show();
                }else if ( BmobUser.getCurrentUser(ReplyActivity.this, MyUser.class)!=null)
                {
                    reply();
                }else {
                    Toast.makeText(this,"请登陆后进行评论",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }

    }

    private void reply() {

        final MyUser user = BmobUser.getCurrentUser(this, MyUser.class);

        DetailsBean detailsBean=new DetailsBean();
        detailsBean.setObjectId(value);

        final CommentsBean comment = new CommentsBean();
        comment.setContent("回复"+name+":"+ed_reply.getText().toString());
        comment.setUser(user);
        comment.setDetail(detailsBean);

        comment.save(this, new SaveListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                search(userid, user.getUsername());
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                ErrorReport.RrrorCode(code, ReplyActivity.this);
            }
        });

    }

    private void search(final String objid, final String name){

        BmobQuery<MyBmobInstallation> query = new BmobQuery<MyBmobInstallation>();
        query.addWhereEqualTo("userid", objid);
        query.findObjects(ReplyActivity.this, new FindListener<MyBmobInstallation>() {
            @Override
            public void onSuccess(List<MyBmobInstallation> list) {
                push(list.get(0).getInstallationId(), name);
            }

            @Override
            public void onError(int i, String s) {
                ErrorReport.RrrorCode(i, ReplyActivity.this);
            }
        });
    }

    private void push(String installationId,String name){

        BmobPushManager bmobPush = new BmobPushManager(this);
        BmobQuery<MyBmobInstallation> query = MyBmobInstallation.getQuery();
        query.addWhereEqualTo("installationId", installationId);
        bmobPush.setQuery(query);
        Gson gson=new Gson();
        PushBean pushBean=new PushBean();
        pushBean.setType(1);
        pushBean.setContent(ed_reply.getText().toString());
        pushBean.setUser(name);
        String jsonObject= gson.toJson(pushBean);
        try {
            JSONObject myJsonObject = new JSONObject(jsonObject);
            bmobPush.pushMessage(myJsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finish();
    }
}
