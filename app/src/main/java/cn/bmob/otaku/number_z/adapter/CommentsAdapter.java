package cn.bmob.otaku.number_z.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cn.bmob.otaku.number_z.Bean.CommentsBean;
import cn.bmob.otaku.number_z.R;
import cn.bmob.otaku.number_z.view.MyListView;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.GetServerTimeListener;


/**
 * Created by Administrator on 2015/12/10.
 */
public class CommentsAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<CommentsBean> commentsBean;
    private LayoutInflater layoutInflater;
    private long time;

    public CommentsAdapter(Context context, ArrayList<CommentsBean> commentsBean){

        this.context=context;
        this.commentsBean=commentsBean;
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public void notifyDataSetChanged() {
        getTime();
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return commentsBean.size();
    }

    @Override
    public Object getItem(int position) {
        return commentsBean.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.item_comments, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView= (ImageView) convertView.findViewById(R.id.image_head);
            viewHolder.name= (TextView) convertView.findViewById(R.id.comment_name);
            viewHolder.time= (TextView) convertView.findViewById(R.id.comment_time);
            viewHolder.content= (TextView) convertView.findViewById(R.id.comment_content);
//            viewHolder.listview_reply= (MyListView) convertView.findViewById(R.id.listview_reply);
            viewHolder.addview= (LinearLayout) convertView.findViewById(R.id.addview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ImageOptions options=new ImageOptions.Builder()
                .setPlaceholderScaleType(ImageView.ScaleType.CENTER_CROP)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.drawable.head)
                .setFailureDrawableId(R.drawable.head)
                .build();

        x.image().bind(viewHolder.imageView, commentsBean.get(position).getUser().getImage().getFileUrl(context), options);

        viewHolder.name.setText(commentsBean.get(position).getUser().getUsername());

        viewHolder.content.setText(commentsBean.get(position).getContent());

        viewHolder.time.setText(timeout(commentsBean.get(position).getCreatedAt()));

        return convertView;
    }

    static class ViewHolder
    {
        private ImageView imageView;
        private TextView name;
        private TextView time;
        private TextView content;
        private MyListView listview_reply;
        private LinearLayout addview;
    }

    private String timeout(String linktime){

        long beginTime = 0;

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            beginTime= sf.parse(linktime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long endTime = time;
        long betweenDays =endTime - beginTime;

        long day=betweenDays/(24*60*60*1000);
        long hour=(betweenDays/(60*60*1000)-day*24);
        long min=((betweenDays/(60*1000))-day*24*60-hour*60);
        long s=(betweenDays/1000-day*24*60*60-hour*60*60-min*60);

        String date;
        if (day>0)
        {
            date=day+"天前";
            return date;
        }else if (hour>0)
        {
            date=hour+ "小时前";
            return date;
        }else if (min>0)
        {
            date=min+"分钟前";
            return date;
        }else {
            if (s<0){
                s=0;
            }
            date=s+"秒前";
        }

        return date;
    }

    private void getTime(){

        Bmob.getServerTime(context, new GetServerTimeListener() {

            @Override
            public void onSuccess(long time1) {
                // TODO Auto-generated method stub
                time=time1* 1000l;
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
            }
        });

    }

}
