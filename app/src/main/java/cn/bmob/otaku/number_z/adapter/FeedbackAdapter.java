package cn.bmob.otaku.number_z.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.bmob.otaku.number_z.Bean.FeedbackBean;
import cn.bmob.otaku.number_z.R;

/**
 * Created by Administrator on 2015/12/30.
 */
public class FeedbackAdapter extends BaseAdapter{

    private ArrayList<FeedbackBean> feedbackBeans;
    private Context context;

    public FeedbackAdapter(ArrayList<FeedbackBean> feedbackBeans,Context context){
        this.feedbackBeans=feedbackBeans;
        this.context=context;
    }

    @Override
    public int getCount() {
        return feedbackBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return feedbackBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView==null)
        {
            convertView= LayoutInflater.from(context).inflate(R.layout.item_feedback,null);
            viewHolder=new ViewHolder();
            viewHolder.name= (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.content= (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.oop= (ImageView) convertView.findViewById(R.id.oop);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        if (feedbackBeans.get(position).getUser()!=null)
        {
            viewHolder.name.setText(feedbackBeans.get(position).getUser().getUsername());
        }else {
            viewHolder.name.setText("访客");
        }

        viewHolder.content.setText(feedbackBeans.get(position).getContent());

        if (feedbackBeans.get(position).getState())
        {
            viewHolder.oop.setImageResource(R.drawable.share);
        }else {
            viewHolder.oop.setImageResource(R.drawable.share_no);
        }

        return convertView;
    }

    static class ViewHolder{
        private TextView name;
        private TextView content;
        private ImageView oop;
    }

}
