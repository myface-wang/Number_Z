package cn.bmob.otaku.number_z.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.otaku.number_z.Bean.MyUser;
import cn.bmob.otaku.number_z.Bean.ReplyBean;
import cn.bmob.otaku.number_z.R;
import cn.bmob.otaku.number_z.adapter.MessageAdapter;
import cn.bmob.otaku.number_z.utils.ErrorReport;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2016/2/14.
 */
public class MessageActivity extends BaseActivity{

    private ListView message_list;
    private SwipeRefreshLayout swipeLayout;
    private ArrayList<ReplyBean> replyBeans=new ArrayList<ReplyBean>();
    private MessageAdapter messageAdapter;

    private int page=10;
    private int endpage=10;//每次翻页取的个数
    private boolean flag=true;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_message);
        toolbar.setTitle("我的消息");
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initview();
        initdata();
    }

    private void initview() {

        message_list= (ListView) findViewById(R.id.list_message);

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_code_me);
        swipeLayout.setColorSchemeResources(R.color.color_bule2,
                R.color.color_bule,
                R.color.color_bule2,
                R.color.color_bule3);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                initdata();

            }
        });

        message_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (message_list != null && message_list.getChildCount() > 0) {
                    boolean enable = (firstVisibleItem == 0) && (view.getChildAt(firstVisibleItem).getTop() == 0);
                    swipeLayout.setEnabled(enable);
                    if (firstVisibleItem + visibleItemCount == totalItemCount && !isLoading && totalItemCount >= page) {
                        if (flag) {
                            loaddate();
                        }
                    }
                }
            }
        });

        message_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent(MessageActivity.this,CommentsActivity.class);
                intent.putExtra("objectid", replyBeans.get(position).getDetailsid().getObjectId());
                intent.putExtra("title",replyBeans.get(position).getDetailsid().getName());
                startActivity(intent);
            }
        });

        messageAdapter=new MessageAdapter(this,replyBeans);
        message_list.setAdapter(messageAdapter);

    }

    private void initdata() {
        isLoading = true;
        MyUser myUser= BmobUser.getCurrentUser(MessageActivity.this, MyUser.class);
        BmobQuery<ReplyBean> reply=new BmobQuery<>();
        reply.setLimit(endpage);
        reply.order("-createdAt");
        reply.addWhereEqualTo("userid", new BmobPointer(myUser));
        reply.include("userid,replyid,detailsid");
        reply.findObjects(MessageActivity.this, new FindListener<ReplyBean>() {
            @Override
            public void onSuccess(List<ReplyBean> list) {

                replyBeans.clear();
                replyBeans.addAll(list);
                messageAdapter.notifyDataSetChanged();
                swipeLayout.setRefreshing(false);
                isLoading = false;
            }

            @Override
            public void onError(int i, String s) {
                ErrorReport.RrrorCode(i, MessageActivity.this);
                swipeLayout.setRefreshing(false);
                isLoading = false;
            }
        });
    }


    private void loaddate(){
        isLoading = true;
        MyUser myUser= BmobUser.getCurrentUser(MessageActivity.this, MyUser.class);
        BmobQuery<ReplyBean> Query = new BmobQuery<ReplyBean>();
        Query.order("-createdAt");
        Query.setSkip(page);
        Query.addWhereEqualTo("userid", new BmobPointer(myUser));
        Query.include("userid,replyid,detailsid");
        Query.findObjects(MessageActivity.this, new FindListener<ReplyBean>() {
            @Override
            public void onSuccess(List<ReplyBean> object) {
                replyBeans.addAll(object);
                if (object.size()==endpage)
                {
                    page=page+endpage;
                }else {
                    flag=false;
                }
                messageAdapter.notifyDataSetChanged();
                isLoading = false;
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                ErrorReport.RrrorCode(code, MessageActivity.this);
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
