package cn.bmob.otaku.number_z.activity;

import android.app.WallpaperManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;

import cn.bmob.otaku.number_z.Bean.CommentBean;
import cn.bmob.otaku.number_z.R;
import cn.bmob.otaku.number_z.utils.SysUtils;
import cn.bmob.otaku.number_z.view.imagepager.PinchImageView;
import cn.bmob.otaku.number_z.view.imagepager.PinchImageViewPager;


/**
 * Created by Administrator on 2015/11/24.
 */
public class ImageActivity extends BaseActivity{

    final LinkedList<PinchImageView>  viewCache  = new LinkedList<PinchImageView>();
    private Toolbar toolbar;

    private PinchImageViewPager mViewPager;
    private ArrayList<CommentBean> commentBeanList=new ArrayList<CommentBean>();
    private TextView load_image,tv_wall;
    private TextView page_id;
    private FrameLayout page_frameLayout;
    private int id;
    private int flag;
    private  boolean flag1=true;
    int x1 = 0;
    int x2=0;

    private MyApplication myApplication;
    private  WallpaperManager wallpaperManager;

    public ImageActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        myApplication= (MyApplication) getApplication();

        toolbar = (Toolbar) findViewById(R.id.toolbar_image);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        commentBeanList.clear();
//        commentBeanList=getIntent().getParcelableArrayListExtra("list");
        commentBeanList= (ArrayList<CommentBean>) getIntent().getExtras().getSerializable("list");
        id=getIntent().getExtras().getInt("id", 0);

        wallpaperManager = WallpaperManager.getInstance(this);

        initview();

        mViewPager.setCurrentItem(id);

    }

    private void initview() {

        mViewPager = (PinchImageViewPager) findViewById(R.id.view_pager);
        load_image= (TextView) findViewById(R.id.load_image);
        tv_wall= (TextView) findViewById(R.id.tv_wall);
        page_id= (TextView) findViewById(R.id.page_id);
        page_frameLayout= (FrameLayout) findViewById(R.id.page_frameLayout);


        page_id.setText("1" + "/" + commentBeanList.size() + "");

        load_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                down(commentBeanList.get(flag).getUrl());

            }
        });

        tv_wall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wall(commentBeanList.get(flag).getUrl());
            }
        });

        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return commentBeanList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view == o;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                PinchImageView piv;
                if (viewCache.size() > 0) {
                    piv = viewCache.remove();
                    piv.reset();
                } else {
                    piv = new PinchImageView(ImageActivity.this);
                }

                x.image().bind(piv, commentBeanList.get(position).getUrl(), myApplication.getOpt());
                container.addView(piv);

                return piv;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                PinchImageView piv = (PinchImageView) object;
                container.removeView(piv);
                viewCache.add(piv);
            }

            @Override
            public void setPrimaryItem(ViewGroup container, int position, Object object) {
                mViewPager.setMainPinchImageView((PinchImageView) object);
            }
        });

        mViewPager.setOnPageChangeListener(new PinchImageViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                flag = position;
                page_id.setText((position + 1) + "/" + commentBeanList.size() + "");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }
    private void wall(String url) {

        x.image().loadDrawable(url, myApplication.getOpt(), new Callback.CommonCallback<Drawable>() {
            @Override
            public void onSuccess(Drawable result) {
                BitmapDrawable bd = (BitmapDrawable) result;
                try {
                    wallpaperManager.setBitmap(bd.getBitmap());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction()==MotionEvent.ACTION_DOWN)
        {
            x1=(int) ev.getX();
        }

        if (ev.getAction()==MotionEvent.ACTION_UP)
        {

            x2= (int) ev.getX();
            if (flag1&&Math.abs(x2 - x1)<40){
                page_frameLayout.setVisibility(View.GONE);
                flag1=false;
            }else if (!flag1&&Math.abs(x2 - x1)>40){
                //滑动中不做处理
            }else if (!flag1){
                page_frameLayout.setVisibility(View.VISIBLE);
                flag1=true;
            }

        }

        return super.dispatchTouchEvent(ev);
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

    private void down(String url){
        // 请求参数

        String name="图片";
        try {
            name= String.valueOf(SysUtils.Time());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String type =url.substring(url.lastIndexOf("."));

        RequestParams params = new RequestParams(url);
        params.setSaveFilePath(SysUtils.SDpath()+"/Otaku/"+name+type);
        x.http().get(params, new Callback.CommonCallback<File>() {
            @Override
            public void onSuccess(File result) {
                Toast.makeText(ImageActivity.this,"下载成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(ImageActivity.this,"下载异常",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

}
