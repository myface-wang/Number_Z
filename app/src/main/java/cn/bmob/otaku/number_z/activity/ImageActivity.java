package cn.bmob.otaku.number_z.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.x;

import java.util.ArrayList;
import java.util.LinkedList;

import cn.bmob.otaku.number_z.Bean.CommentBean;
import cn.bmob.otaku.number_z.R;
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
    private ImageView load_image;
    private TextView page_id;
    private FrameLayout page_frameLayout;
    private int id;
    private int flag;
    private  boolean flag1=true;
    int x1 = 0;
    int x2=0;

    public ImageActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        toolbar = (Toolbar) findViewById(R.id.toolbar_image);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        commentBeanList.clear();
        commentBeanList=getIntent().getParcelableArrayListExtra("list");
        id=getIntent().getIntExtra("id", 0);


        initview();

        mViewPager.setCurrentItem(id);

    }

    private void initview() {

        mViewPager = (PinchImageViewPager) findViewById(R.id.view_pager);
        load_image= (ImageView) findViewById(R.id.load_image);
        page_id= (TextView) findViewById(R.id.page_id);
        page_frameLayout= (FrameLayout) findViewById(R.id.page_frameLayout);


        page_id.setText("1" + "/" + commentBeanList.size() + "");

        load_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("url", commentBeanList.get(flag).getUrl());

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

                x.image().bind(piv, commentBeanList.get(position).getUrl());
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
            }
            else {
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

}
