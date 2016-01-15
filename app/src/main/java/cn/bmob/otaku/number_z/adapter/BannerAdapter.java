package cn.bmob.otaku.number_z.adapter;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.xutils.x;

import java.util.ArrayList;

import cn.bmob.otaku.number_z.activity.MyApplication;

/**
 * Created by Administrator on 2015/12/24.
 */
public class BannerAdapter extends PagerAdapter{

    private ArrayList<String> image;
    private MyApplication application=null;

    public BannerAdapter(ArrayList<String> image,MyApplication application)
    {
        this.image=image;
        this.application=application;
    }

    @Override
    public int getCount() {
        return image.size();
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


        x.image().bind(view,image.get(position),application.getOpt());

        view.setScaleType(ImageView.ScaleType.CENTER_CROP);

        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("banner",image.get(position));
            }
        });
        return view;
    }


}
