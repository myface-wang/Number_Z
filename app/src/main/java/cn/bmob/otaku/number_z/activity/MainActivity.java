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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.otaku.number_z.Bean.BannerBean;
import cn.bmob.otaku.number_z.Bean.DetailsBean;
import cn.bmob.otaku.number_z.Bean.MyUser;
import cn.bmob.otaku.number_z.R;
import cn.bmob.otaku.number_z.adapter.BannerAdapter;
import cn.bmob.otaku.number_z.adapter.HomeAapter;
import cn.bmob.otaku.number_z.utils.BaseDate;
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
    private ListView list;
    private View banner;
    private SwipeRefreshLayout swipeLayout;
    private HomeAapter homeAapter;
    private ArrayList<DetailsBean> commentBeans=new ArrayList<DetailsBean>();
    private ArrayList<BannerBean> bannerBeans=new ArrayList<BannerBean>();
    private  BannerAdapter bannerAdapter;

    private int page=5;
    private int endpage=5;//每次翻页取的个数
    private boolean flag=true;
    private boolean isLoading = false;

    private ImageOptions imageOptions;
    private MyApplication myApplication=null;
    private MyUser userInfo;
    private TextView tv_name,tv_createtime;
    private Button btn_register;
    private FloatingActionButton fab;

    private MenuItem item;
    private long firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        myApplication= (MyApplication) getApplication();
        userInfo = BmobUser.getCurrentUser(this, MyUser.class);
        imageOptions = new ImageOptions.Builder()
                // 是否忽略GIF格式的图片
                .setSize(-1,-1)
                .setIgnoreGif(false)
                        // 图片缩放模式
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                        // 下载中显示的图片
                .setLoadingDrawableId(R.drawable.head)
                        // 下载失败显示的图片
                .setFailureDrawableId(R.drawable.head)
                        // 得到ImageOptions对象
                .build();


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,"积分系统建设中", Snackbar.LENGTH_LONG)
                        .setAction("关闭", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                fab.setImageResource(R.drawable.gift);

                                //需要增加业务逻辑
                            }
                        }).show();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        initview();
        loadbanner();
        refresh();

    }

    private void loadbanner() {
        BmobQuery<BannerBean> Query = new BmobQuery<BannerBean>();
        Query.order("-createdAt");
        Query.include("details");
        Query.findObjects(MainActivity.this, new FindListener<BannerBean>() {

            @Override
            public void onSuccess(List<BannerBean> list) {
                bannerBeans.addAll(list);
                bannerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private String timeout(String linktime){

        long beginTime = 0;
        long endTime = 0;

        Date d = new Date();
        SimpleDateFormat sft  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sft.format(d);

//        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            beginTime= sft.parse(linktime).getTime();
            endTime= sft.parse(sft.format(d)).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        long betweenDays =endTime - beginTime;

        long day=betweenDays/(24*60*60*1000);
//        long hour=(betweenDays/(60*60*1000)-day*24);
//        long min=((betweenDays/(60*1000))-day*24*60-hour*60);
//        long s=(betweenDays/1000-day*24*60*60-hour*60*60-min*60);

        String date;
        if (day>0)
        {
            date=day+"天";
        }else {
            date="0天";
        }

        return date;
    }

    private void inituser() {

        MyUser userInfo = BmobUser.getCurrentUser(this, MyUser.class);
        if (userInfo!=null)
        {
            tv_name.setText(userInfo.getUsername());
            tv_createtime.setText("您注册了："+timeout(userInfo.getCreatedAt()));
            if (userInfo.getImage()!=null)
            {
                x.image().bind(img_head, userInfo.getImage().getFileUrl(this), BaseDate.Head_OPTIONS());
            }
            tv_createtime.setVisibility(View.VISIBLE);
        }else {
            tv_name.setText("游客");
            tv_createtime.setText("登录体验更多功能哟~");
            img_head.setImageResource(R.drawable.head);
            btn_register.setVisibility(View.VISIBLE);
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
        btn_register= (Button) drawerhead.findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        img_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BmobUser.getCurrentUser(MainActivity.this, MyUser.class) == null) {
                    Intent login = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(login);
                } else {
                    Intent user = new Intent(MainActivity.this, UserActivity.class);
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

        list= (ListView) findViewById(R.id.list_main);
        banner = getLayoutInflater().inflate(R.layout.headview_banner, null);

        mRollViewPager= (RollPagerView) banner.findViewById(R.id.roll_view_pager);
        mRollViewPager.setAnimationDurtion(1000);
        bannerAdapter=new BannerAdapter(bannerBeans, myApplication,this);
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
                    Bundle bu = new Bundle();
                    bu.putSerializable("commentBean", commentBeans.get(position - 1));//activity里的数据是数据源，在这取其实是一样的
                    intent.putExtras(bu);
//                intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) list);
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
        Query.order("-createdAt");
        Query.setMaxCacheAge(TimeUnit.DAYS.toMillis(7));//缓存7天
        //判断是否有缓存，该方法必须放在查询条件（如果有的话）都设置完之后再来调用才有效，就像这里一样。
        boolean isCache = Query.hasCachedResult(this, DetailsBean.class);
        if(isCache){
            Query.setCachePolicy(BmobQuery.CachePolicy.CACHE_THEN_NETWORK);
        }else{
            Query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
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
            Intent intent=new Intent(this,MessageActivity.class);
            startActivity(intent);
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


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {

                Snackbar sb = Snackbar.make(drawer, "再按一次退出", Snackbar.LENGTH_SHORT);
                sb.getView().setBackgroundColor(getResources().getColor(R.color.pink));
                sb.show();
                firstTime = secondTime;
            } else {
                finish();
            }
        }

    }
}
