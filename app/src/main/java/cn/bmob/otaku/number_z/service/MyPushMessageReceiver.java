package cn.bmob.otaku.number_z.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import cn.bmob.push.PushConstants;

/**
 * Created by Administrator on 2016/1/8.
 */
public class MyPushMessageReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            String msg=intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
//            Log.d("bmob", "BmobPushDemo收到消息：" + intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING));

            SharedPreferences share=context.getSharedPreferences("config", Context.MODE_PRIVATE);
            boolean pushflag=share.getBoolean("pushflag", true);
            if (pushflag)
            {
                send(context, msg);
            }
        }
    }

    public void send(Context context,String msg) {
        Intent intent = new Intent("android.intent.action.MY_BROADCAST");
        intent.putExtra("msg", msg);
        context.sendBroadcast(intent);
    }

}
