package cn.bmob.otaku.number_z.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.otaku.number_z.Bean.FeedbackBean;
import cn.bmob.otaku.number_z.Bean.MyUser;
import cn.bmob.otaku.number_z.R;
import cn.bmob.otaku.number_z.adapter.FeedbackAdapter;
import cn.bmob.otaku.number_z.utils.ErrorReport;
import cn.bmob.otaku.number_z.utils.Regular;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2015/12/30.
 */
public class FeedbackActivity extends BaseActivity{

    private TextInputLayout et_feedback;
    private EditText feedback;
    private Button bt_feedback;
    private ListView list_feedback;
    private Toolbar toolbar;

    private ArrayList<FeedbackBean> feedbackBeans=new ArrayList<FeedbackBean>();
    private FeedbackAdapter feedbackAdapter;

    private int page=5;
    private int endpage=6;//每次翻页取的个数
    private boolean flag=true;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feedback);

        toolbar = (Toolbar) findViewById(R.id.toolbar_feedback);
        toolbar.setTitle("建议反馈");
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list_feedback= (ListView) findViewById(R.id.list_feedback);
        feedbackAdapter=new FeedbackAdapter(feedbackBeans,this);
        list_feedback.setAdapter(feedbackAdapter);

        et_feedback= (TextInputLayout) findViewById(R.id.et_feedback);
        et_feedback.setHint("三言两语，说不定就被采纳了呢!");
        feedback=et_feedback.getEditText();

        bt_feedback= (Button) findViewById(R.id.bt_feedback);

        bt_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Regular.isnull(feedback.getText().toString()))
                {
                    et_feedback.setErrorEnabled(false);
                    feedback(feedback.getText().toString());
                }else {
                    et_feedback.setErrorEnabled(true);
                    et_feedback.setError("不能为空或特殊字符");
                }

            }
        });

        list_feedback.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (list_feedback != null && list_feedback.getChildCount() > 0) {
                    if (firstVisibleItem + visibleItemCount == totalItemCount && !isLoading && totalItemCount >= page) {
                        if (flag) {
                            load();
                        }
                    }
                }
            }
        });

        initdata();

    }

    private void initdata() {

        BmobQuery<FeedbackBean> query = new BmobQuery<FeedbackBean>();

        query.addWhereEqualTo("flag", true);
        query.addWhereEqualTo("state", true);
        query.setLimit(endpage);
        query.findObjects(this, new FindListener<FeedbackBean>() {
            @Override
            public void onSuccess(List<FeedbackBean> list) {
                feedbackBeans.clear();
                feedbackBeans.addAll(list);
                feedbackAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {

            }
        });


    }

    /**
     *这里可能需要添加用户信息，进行提交
     * @param ed
     */
    private void feedback(String ed) {

        MyUser user = BmobUser.getCurrentUser(this, MyUser.class);
        FeedbackBean feedbackBean = new FeedbackBean();
        feedbackBean.setContent(ed);
        feedbackBean.setFlag(false);
        feedbackBean.setState(false);
        feedbackBean.setUser(user);
        feedbackBean.save(this, new SaveListener() {

            @Override
            public void onSuccess() {
                Toast.makeText(FeedbackActivity.this, "已提交", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int code, String arg0) {
                Toast.makeText(FeedbackActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void load() {

        isLoading = true;
        BmobQuery<FeedbackBean> query = new BmobQuery<FeedbackBean>();

        query.addWhereEqualTo("flag", true);
        query.addWhereEqualTo("state", true);
        query.setSkip(page);
        query.setLimit(endpage);
        query.order("-createdAt");
        query.findObjects(FeedbackActivity.this, new FindListener<FeedbackBean>() {
            @Override
            public void onSuccess(List<FeedbackBean> object) {
                // TODO Auto-generated method stub

//                commentBeans.clear();
                feedbackBeans.addAll(object);
                if (object.size() == endpage) {
                    page = page + endpage;
                } else {
                    flag = false;
                }
                feedbackAdapter.notifyDataSetChanged();
                isLoading = false;
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                ErrorReport.RrrorCode(code, FeedbackActivity.this);

                isLoading = false;
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
