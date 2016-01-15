package cn.bmob.otaku.number_z.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.otaku.number_z.Bean.CommentsBean;
import cn.bmob.otaku.number_z.Bean.DetailsBean;
import cn.bmob.otaku.number_z.Bean.MyUser;
import cn.bmob.otaku.number_z.R;
import cn.bmob.otaku.number_z.adapter.CommentsAdapter;
import cn.bmob.otaku.number_z.utils.ErrorReport;
import cn.bmob.otaku.number_z.utils.Regular;
import cn.bmob.otaku.number_z.window.ReplyActivity;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2015/12/10.
 */
public class CommentsActivity extends BaseActivity {

    private SwipeRefreshLayout swipeLayout;
    private Toolbar toolbar;
    private EditText et_comment;
    private Button pinglun;

    private ArrayList<CommentsBean> commentsBean=new ArrayList<CommentsBean>();
    private ListView list;
    private CommentsAdapter commentsAdapter;

    private String value;
    private String title="N/A";

    private int page=5;
    private int endpage=5;//每次翻页取的个数
    private boolean flag=true;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Intent intent=getIntent();
        value=intent.getStringExtra("objectid");
        if (intent.getStringExtra("title")!=null)
        {
            title=intent.getStringExtra("title");
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar_comment);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initview();
        initdate();

    }

    private void initdate() {

        commentsAdapter=new CommentsAdapter(this,commentsBean);
        list.setAdapter(commentsAdapter);
        initComment();

    }

    private void initview() {

        list= (ListView) findViewById(R.id.list_comments);
        et_comment= (EditText) findViewById(R.id.et_comment);
        pinglun= (Button) findViewById(R.id.pinglun);

        pinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BmobUser.getCurrentUser(CommentsActivity.this, MyUser.class)!=null){
                    pinglun();
                }else {
                    Toast.makeText(CommentsActivity.this,"请登录后进行评论",Toast.LENGTH_SHORT).show();
                }
            }
        });

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_comments);
        swipeLayout.setColorSchemeResources(R.color.color_bule2,
                R.color.color_bule,
                R.color.color_bule2,
                R.color.color_bule3);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                initComment();
                swipeLayout.setRefreshing(false);
            }
        });

        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (list != null && list.getChildCount() > 0) {
                    boolean enable = (firstVisibleItem == 0) && (view.getChildAt(firstVisibleItem).getTop() == 0);
                    swipeLayout.setEnabled(enable);
                    if (firstVisibleItem + visibleItemCount == totalItemCount && !isLoading && totalItemCount >= page) {
                        if (flag) {
                            load();
                        }
                    }
                }
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(CommentsActivity.this, ReplyActivity.class);
                intent.putExtra("objectid",value);
                intent.putExtra("name",commentsBean.get(position).getUser().getUsername());
                intent.putExtra("userid",commentsBean.get(position).getUser().getObjectId());
                startActivity(intent);

            }
        });

    }

    private void load() {

        isLoading = true;

        BmobQuery<CommentsBean> query = new BmobQuery<CommentsBean>();
        DetailsBean post = new DetailsBean();
        post.setObjectId(value);
        query.setSkip(page);
        query.setLimit(endpage);
        query.addWhereEqualTo("detail", new BmobPointer(post));
        query.include("user,recomment");
        query.order("-createdAt");
        query.findObjects(CommentsActivity.this, new FindListener<CommentsBean>() {
            @Override
            public void onSuccess(List<CommentsBean> object) {
                // TODO Auto-generated method stub

//                commentBeans.clear();
                commentsBean.addAll(object);
                if (object.size()==endpage)
                {
                    page=page+endpage;
                }else {
                    flag=false;
                }
                commentsAdapter.notifyDataSetChanged();
                isLoading = false;
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                ErrorReport.RrrorCode(code, CommentsActivity.this);

                isLoading = false;
            }
        });

    }

    private void initComment() {

        BmobQuery<CommentsBean> query = new BmobQuery<CommentsBean>();
        DetailsBean post = new DetailsBean();
        post.setObjectId(value);
        query.addWhereEqualTo("detail", new BmobPointer(post));
        query.setLimit(endpage);
        query.order("-createdAt");
        query.include("user,recomment");
        query.findObjects(this, new FindListener<CommentsBean>() {

            @Override
            public void onSuccess(List<CommentsBean> object) {
                // TODO Auto-generated method stub

                commentsBean.clear();
                commentsBean.addAll(object);
                commentsAdapter.notifyDataSetChanged();

                flag=true;
                page=endpage;

                isLoading = false;
                swipeLayout.setRefreshing(false);

            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                ErrorReport.RrrorCode(code, CommentsActivity.this);
                swipeLayout.setRefreshing(false);
                isLoading = false;
            }
        });

    }

    private void pinglun()
    {

        if (!Regular.isnull(et_comment.getText().toString()))
        {

            MyUser user = BmobUser.getCurrentUser(this, MyUser.class);

            DetailsBean detailsBean=new DetailsBean();
            detailsBean.setObjectId(value);

            final CommentsBean comment = new CommentsBean();
            comment.setContent(String.valueOf(et_comment.getText()));
            comment.setUser(user);
            comment.setDetail(detailsBean);

            comment.save(this, new SaveListener() {

                @Override
                public void onSuccess() {
                    // TODO Auto-generated method stub
                    et_comment.setText("");
                    et_comment.clearFocus();
                }

                @Override
                public void onFailure(int code, String msg) {
                    // TODO Auto-generated method stub
                    ErrorReport.RrrorCode(code,CommentsActivity.this);
                }
            });

        }else {

            Toast.makeText(this,"您输入的内容有误",Toast.LENGTH_SHORT).show();

        }

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
