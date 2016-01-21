package cn.bmob.otaku.number_z.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.xutils.x;

import java.util.ArrayList;

import cn.bmob.otaku.number_z.Bean.BannerBean;
import cn.bmob.otaku.number_z.activity.DetailsActivity;
import cn.bmob.otaku.number_z.activity.MyApplication;

/**
 * Created by Administrator on 2015/12/24.
 */
public class BannerAdapter extends PagerAdapter{

    private ArrayList<BannerBean> bannerBeans;
    private MyApplication application=null;
    private Context context;

    public BannerAdapter(ArrayList<BannerBean> bannerBeans,MyApplication application,Context context)
    {
        this.bannerBeans=bannerBeans;
        this.application=application;
        this.context=context;
    }

    @Override
    public int getCount() {
        return bannerBeans.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0==arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = getView(container,position);
        container.addView(itemView);
        return itemView;
    }

    public View getView(ViewGroup container, final int position) {
        ImageView view = new ImageView(container.getContext());

        x.image().bind(view,bannerBeans.get(position).getImage().getFileUrl(context),application.getOpt());

        view.setScaleType(ImageView.ScaleType.CENTER_CROP);

        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bannerBeans.get(position).isType()) {
                    Intent intent = new Intent();
                    Bundle bu = new Bundle();
                    bu.putSerializable("commentBean", bannerBeans.get(position).getDetails());
                    intent.putExtras(bu);
                    intent.setClass(context, DetailsActivity.class);
                    context.startActivity(intent);
                }else {
                    //跳转到特殊页面。专题资讯之类的
                }
            }
        });
        return view;
    }

}
