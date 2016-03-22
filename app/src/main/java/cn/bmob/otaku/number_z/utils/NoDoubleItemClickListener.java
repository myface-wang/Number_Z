package cn.bmob.otaku.number_z.utils;

import android.view.View;
import android.widget.AdapterView;

import java.util.Calendar;

/**
 * Created by Administrator on 2016/1/29.
 */
public class NoDoubleItemClickListener implements AdapterView.OnItemClickListener{

    public static final int MIN_CLICK_DELAY_TIME = 2000;
    private long lastClickTime = 0;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleItemClick(parent,view,position,id);
        }
    }

    public void onNoDoubleItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        //点击后延迟2秒，防止二次点击
    }

}
