package cn.bmob.otaku.number_z.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.otaku.number_z.Bean.DetailsBean;
import cn.bmob.otaku.number_z.Bean.MyUser;
import cn.bmob.otaku.number_z.R;
import cn.bmob.otaku.number_z.adapter.BannerAdapter;
import cn.bmob.otaku.number_z.adapter.HomeAapter;
import cn.bmob.otaku.number_z.utils.ErrorReport;
import cn.bmob.otaku.number_z.view.CircleImageView;
import cn.bmob.otaku.number_z.view.rollviewpager.RollPagerView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private CircleImageView img_head;
    private ImageView rightbtn;
    private NavigationView navigationView;
    private LinearLayout drawerhead;//先获取navigationView的头部view才能正确找到里面的控件设置点击事件

    private RollPagerView mRollViewPager;
    private ArrayList<String> image=new ArrayList<String>();
    private ListView list;
    private View banner;
    private SwipeRefreshLayout swipeLayout;
    private HomeAapter homeAapter;
    private ArrayList<DetailsBean> commentBeans=new ArrayList<DetailsBean>();
    private  BannerAdapter bannerAdapter;

    private int page=5;
    private int endpage=5;//每次翻页取的个数
    private boolean flag=true;
    private boolean isLoading = false;

    private ImageOptions imageOptions;
    private MyApplication myApplication=null;
    private MyUser userInfo;
    private TextView tv_name,tv_createtime;

    private MenuItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        myApplication= (MyApplication) getApplication();
        userInfo = BmobUser.getCurrentUser(this, MyUser.class);
        imageOptions = new ImageOptions.Builder()
                // 是否忽略GIF格式的图片
                .setIgnoreGif(false)
                        // 图片缩放模式
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                        // 下载中显示的图片
//                .setLoadingDrawableId(R.drawable.ic_launcher)
                        // 下载失败显示的图片
//                .setFailureDrawableId(R.drawable.ic_launcher)
                        // 得到ImageOptions对象
                .build();


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Log.i("11", "11");
                            }
                        }).show();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        initview();
        refresh();

    }

    private void inituser() {

        MyUser userInfo = BmobUser.getCurrentUser(this, MyUser.class);
        if (userInfo!=null)
        {
            tv_name.setText(userInfo.getUsername());
            tv_createtime.setText(userInfo.getCreatedAt());
            if (userInfo.getImage()!=null)
            {
                x.image().bind(img_head, userInfo.getImage().getFileUrl(this), myApplication.getOpt());
            }
            tv_createtime.setVisibility(View.VISIBLE);
        }else {
            tv_name.setText("游客");
            tv_createtime.setVisibility(View.GONE);
            img_head.setImageResource(R.drawable.head);
        }

    }

    private void initview() {

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawerhead= (LinearLayout) navigationView.getHeaderView(0);
        img_head= (CircleImageView) drawerhead.findViewById(R.id.img_head);
        rightbtn= (ImageView) drawerhead.findViewById(R.id.rightbtn);
        tv_name= (TextView) drawerhead.findViewById(R.id.tv_name);
        tv_createtime= (TextView) drawerhead.findViewById(R.id.tv_createtime);

        img_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BmobUser.getCurrentUser(MainActivity.this, MyUser.class)==null)
                {
                    Intent login = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(login);
                }else {
                    Intent user=new Intent(MainActivity.this,UserActivity.class);
                    startActivity(user);
                }
            }
        });

        rightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);

            }
        });


        image.add(0, "https://33.media.tumblr.com/7d1cc263d9b8ad6a055d9cfd6c48c5e4/tumblr_nrxwhw5PHF1tq4of6o1_400.gif");
        image.add(0, "http://d.hiphotos.baidu.com/zhidao/pic/item/0b55b319ebc4b745e0d0dac2cdfc1e178b821599.jpg");
        image.add(0, "http://d.hiphotos.baidu.com/zhidao/pic/item/0b55b319ebc4b745e0d0dac2cdfc1e178b821599.jpg");

        list= (ListView) findViewById(R.id.list_main);
        banner = getLayoutInflater().inflate(R.layout.headview_banner, null);

        mRollViewPager= (RollPagerView) banner.findViewById(R.id.roll_view_pager);
        mRollViewPager.setAnimationDurtion(1000);
        bannerAdapter=new BannerAdapter(image, myApplication);
        mRollViewPager.setAdapter(bannerAdapter);

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mRollViewPager.setSwipeRefreshLayout(swipeLayout);  //控制SwipeRefreshLayout与viewpager的滑动冲突

        swipeLayout.setColorSchemeResources(R.color.color_bule2,
                R.color.color_bule,
                R.color.color_bule2,
                R.color.color_bule3);


        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refresh();

            }
        });


        list.addHeaderView(banner);

        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (list != null && list.getChildCount() > 0) {
                    boolean enable = (firstVisibleItem == 0) && (view.getChildAt(firstVisibleItem).getTop() == 0);
                    swipeLayout.setEnabled(enable);

                    if (firstVisibleItem + visibleItemCount == totalItemCount && !isLoading && totalItemCount >= (page + 1)) {

                        if (flag) {
//                            Log.i("是否执行加载更多", String.valueOf(flag));
                            load();
                        }
                    }
                }
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position >= 1) {
                    Intent intent = new Intent();
                    intent.putExtra("objectid", commentBeans.get(position - 1).getObjectId());//activity里的数据是数据源，在这取其实是一样的
                    intent.setClass(MainActivity.this, DetailsActivity.class);
                    startActivity(intent);
                }
            }
        });

        homeAapter = new HomeAapter(this, commentBeans);
        list.setAdapter(homeAapter);

    }

    private void refresh() {

        isLoading = true;
        BmobQuery<DetailsBean> Query = new BmobQuery<DetailsBean>();

        Query.setLimit(endpage);
        Query.addQueryKeys("objectId,name,cover");
        Query.order("-createdAt");
        Query.setMaxCacheAge(TimeUnit.DAYS.toMillis(7));//缓存7天
        //判断是否有缓存，该方法必须放在查询条件（如果有的话）都设置完之后再来调用才有效，就像这里一样。
        boolean isCache = Query.hasCachedResult(this, DetailsBean.class);
        if(isCache){//此为举个例子，并不一定按这种方式来设置缓存策略
            Query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK

        }else{
            Query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE

        }
        Query.findObjects(MainActivity.this, new FindListener<DetailsBean>() {
            @Override
            public void onSuccess(List<DetailsBean> object) {
                // TODO Auto-generated method stub

                commentBeans.clear();
                commentBeans.addAll(object);
                homeAapter.notifyDataSetChanged();
                flag=true;
                page=endpage;

                isLoading = false;
                swipeLayout.setRefreshing(false);

            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                ErrorReport.RrrorCode(code, MainActivity.this);
                swipeLayout.setRefreshing(false);
                isLoading = false;
            }
        });

    }


    /**
     *  最后一个item显示在页面上的时候请求下一页，请求成功后，有下一页就加载，没有下一页则不会再请求
     *  请求不成功会一直请求（不成功目前只有断网的情况，不影响api数量）
     */
    private void load() {

        isLoading = true;
        BmobQuery<DetailsBean> Query = new BmobQuery<DetailsBean>();
        Query.order("-createdAt");
        Query.setSkip(page);
        Query.addQueryKeys("objectId,name,cover");
        Query.findObjects(MainActivity.this, new FindListener<DetailsBean>() {
            @Override
            public void onSuccess(List<DetailsBean> object) {
                // TODO Auto-generated method stub

//                commentBeans.clear();
                commentBeans.addAll(object);
                if (object.size()==endpage)
                {
                    page=page+endpage;
                }else {
                    flag=false;
                }
                homeAapter.notifyDataSetChanged();
                isLoading = false;
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                ErrorReport.RrrorCode(code, MainActivity.this);

                isLoading = false;
            }
        });

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

//        getMenuInflater().inflate(R.menu.main, menu);

        item=menu.add(Menu.NONE,0,0, "消息").setIcon(R.drawable.chat);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == 0) {



            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_index) {
            // Handle the camera action
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_collection) {

            if (BmobUser.getCurrentUser(MainActivity.this, MyUser.class)!=null)
            {
                Intent intent=new Intent(MainActivity.this,CollectionActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(this,"请登录后操作",Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_code) {

            if (BmobUser.getCurrentUser(MainActivity.this, MyUser.class)!=null)
            {
                Intent intent=new Intent(MainActivity.this,CodeActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(this,"请登录后操作",Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_feedback) {
            Intent intent=new Intent(MainActivity.this,FeedbackActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

            Intent intent=new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
            intent.putExtra(Intent.EXTRA_TEXT, "测试一下");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Intent.createChooser(intent, getTitle()));

        }else if (id == R.id.nav_us) {
            Intent intent=new Intent(MainActivity.this,AboutActivity.class);
            startActivity(intent);
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        inituser();

    }
}
