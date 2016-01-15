package cn.bmob.otaku.number_z.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.otaku.number_z.Bean.CollectionBean;
import cn.bmob.otaku.number_z.Bean.MyUser;
import cn.bmob.otaku.number_z.R;
import cn.bmob.otaku.number_z.adapter.CollectionAapter;
import cn.bmob.otaku.number_z.utils.ErrorReport;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2015/11/23.
 */

public class CollectionActivity extends BaseActivity {

    private ArrayList<CollectionBean> commentBeans=new ArrayList<CollectionBean>();
    private CollectionAapter collectionAapter;
    private ListView list_collection;

    private SwipeRefreshLayout swipeLayout;

    private int page=6;
    private int endpage=6;//每次翻页取的个数
    private boolean flag=true;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_collection);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_collection);
        toolbar.setTitle("收藏");
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initview();
        initdate();

    }

    private void initview() {

        list_collection= (ListView) findViewById(R.id.list_collection);

        list_collection.setOverScrollMode(View.OVER_SCROLL_NEVER);//去掉listview下拉时头部的效果，魅族的逗比样式

        collectionAapter=new CollectionAapter(this,commentBeans);
        list_collection.setAdapter(collectionAapter);

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_collection);
        swipeLayout.setColorSchemeResources(R.color.color_bule2,
                R.color.color_bule,
                R.color.color_bule2,
                R.color.color_bule3);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                initdate();

            }
        });


        list_collection.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (list_collection != null && list_collection.getChildCount() > 0 && commentBeans.size() >= visibleItemCount) {
                    boolean enable = (firstVisibleItem == 0) && (view.getChildAt(firstVisibleItem).getTop() == 0);
                    swipeLayout.setEnabled(enable);

                    if (firstVisibleItem + visibleItemCount == totalItemCount && !isLoading && totalItemCount >=page) {
                        if (flag) {
                            load();
                        }
                    }
                }
            }
        });

    }

    private void initdate() {

        isLoading = true;
        BmobQuery<CollectionBean> collectionBeanBmobQuery=new BmobQuery<CollectionBean>();
        MyUser myUser= BmobUser.getCurrentUser(CollectionActivity.this, MyUser.class);
        collectionBeanBmobQuery.include("details,userid");
        collectionBeanBmobQuery.order("-createdAt");
        collectionBeanBmobQuery.setLimit(endpage);
        collectionBeanBmobQuery.addWhereEqualTo("userid", new BmobPointer(myUser));
        collectionBeanBmobQuery.findObjects(this, new FindListener<CollectionBean>() {
            @Override
            public void onSuccess(List<CollectionBean> list) {

                commentBeans.clear();

                commentBeans.addAll(list);

                collectionAapter.notifyDataSetChanged();
                swipeLayout.setRefreshing(false);
                flag = true;
                page = endpage;
                isLoading = false;

            }

            @Override
            public void onError(int i, String s) {
                swipeLayout.setRefreshing(false);
                isLoading = false;
            }
        });

    }

    private void load() {

        isLoading = true;
        BmobQuery<CollectionBean> Query = new BmobQuery<CollectionBean>();
        MyUser myUser= BmobUser.getCurrentUser(CollectionActivity.this, MyUser.class);
        Query.include("details,userid");
        Query.order("-createdAt");
        Query.setSkip(page);
        Query.setLimit(endpage);
        Query.addWhereEqualTo("userid", new BmobPointer(myUser));
        Query.findObjects(CollectionActivity.this, new FindListener<CollectionBean>() {
            @Override
            public void onSuccess(List<CollectionBean> object) {
                // TODO Auto-generated method stub

                commentBeans.addAll(object);
                if (object.size()==endpage)
                {
                    page=page+endpage;
                }else {
                    flag=false;
                }
                collectionAapter.notifyDataSetChanged();
                isLoading = false;
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                ErrorReport.RrrorCode(code, CollectionActivity.this);
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
