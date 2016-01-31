package cn.bmob.otaku.number_z.utils;

import android.widget.ImageView;

import org.xutils.image.ImageOptions;

import cn.bmob.otaku.number_z.R;

/**
 * Created by Administrator on 2015/12/2.
 */
public class BaseDate {

    public static final String APPID="3e3b315a592af25e5d6b3efb1a931905";

    public static final ImageOptions Head_OPTIONS(){

        ImageOptions options=new ImageOptions.Builder()
                .setPlaceholderScaleType(ImageView.ScaleType.CENTER_CROP)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.drawable.head)
                .setFailureDrawableId(R.drawable.head)
                .build();

        return options;
    }

}
