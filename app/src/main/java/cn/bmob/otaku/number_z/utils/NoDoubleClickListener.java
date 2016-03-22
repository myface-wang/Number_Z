package cn.bmob.otaku.number_z.utils;

import android.view.View;

import java.util.Calendar;

/**
 * Created by Administrator on 2016/1/29.
 */
public abstract class NoDoubleClickListener implements View.OnClickListener {

    public static final int MIN_CLICK_DELAY_TIME = 2000;
    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        }
    }

    public void onNoDoubleClick(View view)
    {
        //点击后延迟2秒，防止二次点击
    }
}