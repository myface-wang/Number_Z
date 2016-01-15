package cn.bmob.otaku.number_z.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;

import cn.bmob.otaku.number_z.Bean.PushBean;
import cn.bmob.otaku.number_z.R;
import cn.bmob.otaku.number_z.activity.MainActivity;

/**
 * Created by Administrator on 2016/1/8.
 */
public class MyPushService extends Service{

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private  MyReceiver receiver;
    /** Notification的ID */
    private int notifyId = 102;
    private String title;
    private String content;
    private String name;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //动态注册广播
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.MY_BROADCAST");
        registerReceiver(receiver, filter);

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void setMsgNotification() {

        Resources res = getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.head);
        //第一步：获取状态通知栏管理：
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //第二步：实例化通知栏构造器NotificationCompat.Builder：
        mBuilder = new NotificationCompat.Builder(this);
        //第三步：对Builder进行配置：
        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        mBuilder.setContentTitle(title)//设置通知栏标题
                .setContentText(content) //设置通知栏显示内容
                .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL)) //设置通知栏点击意图
//  .setNumber(number) //设置通知集合的数量
                .setTicker("新消息") //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                        //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.drawable.chat)//设置通知小ICON
                .setLargeIcon(bmp)
                .setContentIntent(pendingIntent);

    }

    /**
     * @获取默认的pendingIntent,为了防止2.3及以下版本报错
     * @flags属性:
     * 在顶部常驻:Notification.FLAG_ONGOING_EVENT
     * 点击去除： Notification.FLAG_AUTO_CANCEL
     */
    public PendingIntent getDefalutIntent(int flags){
        PendingIntent pendingIntent= PendingIntent.getActivity(this, 1, new Intent(), flags);
        return pendingIntent;
    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String msg= intent.getStringExtra("msg");
            Log.i("msg", msg);
            Gson gson=new Gson();
            PushBean pushBean= gson.fromJson(msg, PushBean.class);
            name=pushBean.getUser();
            content=pushBean.getContent();
            int type=pushBean.getType();
            if (type==0)
            {
                title= pushBean.getAps().getAlert();
                content=pushBean.getText();
            }else if (type==1){

                title=pushBean.getUser() + "@你";
                content=pushBean.getContent();
            }
            //加载通知栏
            setMsgNotification();

            mNotificationManager.notify(notifyId, mBuilder.build());
            notifyId++;

        }
    }
}
