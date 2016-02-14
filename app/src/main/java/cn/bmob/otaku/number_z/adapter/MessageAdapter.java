package cn.bmob.otaku.number_z.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xutils.x;

import java.util.ArrayList;

import cn.bmob.otaku.number_z.Bean.ReplyBean;
import cn.bmob.otaku.number_z.R;
import cn.bmob.otaku.number_z.activity.DetailsActivity;
import cn.bmob.otaku.number_z.utils.BaseDate;

/**
 * Created by Administrator on 2016/2/14.
 */
public class MessageAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<ReplyBean> replyBeans=new ArrayList<ReplyBean>();
    private LayoutInflater layoutInflater;

    public MessageAdapter(Context context,ArrayList<ReplyBean> replyBeans){
        this.context=context;
        this.replyBeans=replyBeans;
        layoutInflater= LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return replyBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return replyBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_message, null);
            holder = new ViewHolder();
            holder.image_head= (ImageView) convertView.findViewById(R.id.image_head);
            holder.reply_name= (TextView) convertView.findViewById(R.id.reply_name);
            holder.reply_content= (TextView)convertView.findViewById(R.id.reply_content);
            holder.reply_time= (TextView) convertView.findViewById(R.id.reply_time);

            holder.details_img= (ImageView) convertView.findViewById(R.id.details_img);
            holder.details_name= (TextView) convertView.findViewById(R.id.details_name);
            holder.details_content= (TextView) convertView.findViewById(R.id.details_content);

            holder.item_details= (RelativeLayout) convertView.findViewById(R.id.item_details);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ReplyBean replyBean= replyBeans.get(position);

        if (replyBean.getReplyid()!=null)
        {
            x.image().bind(holder.image_head,replyBean.getReplyid().getImage().getFileUrl(context), BaseDate.Head_OPTIONS());
        }else {
            holder.image_head.setImageResource(R.drawable.head);
        }

        holder.reply_name.setText(replyBean.getReplyid().getUsername());
        holder.reply_time.setText(replyBean.getCreatedAt().substring(5));
        holder.reply_content.setText("回复@"+replyBean.getUserid().getUsername()+":"+replyBean.getContent());

        if (replyBean.getDetailsid()!=null)
        {
            x.image().bind(holder.details_img,replyBean.getDetailsid().getCover().getFileUrl(context), BaseDate.Head_OPTIONS());
        }else {
            holder.details_img.setImageResource(R.drawable.head);
        }

        holder.details_name.setText(replyBean.getDetailsid().getName());
        holder.details_content.setText(replyBean.getDetailsid().getIntroduction());

        holder.item_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                Bundle bu = new Bundle();
                bu.putSerializable("commentBean",replyBean.getDetailsid());
                intent.putExtras(bu);
                intent.setClass(context, DetailsActivity.class);
                context.startActivity(intent);

            }
        });

        return convertView;
    }

    static class ViewHolder {
        private ImageView image_head;
        private TextView reply_name;
        private TextView reply_content;
        private TextView reply_time;

        private ImageView details_img;
        private TextView details_name;
        private TextView details_content;

        private RelativeLayout item_details;
    }
}
