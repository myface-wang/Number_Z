package cn.bmob.otaku.number_z.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cn.bmob.otaku.number_z.Bean.CommentsBean;
import cn.bmob.otaku.number_z.R;

/**
 * Created by Administrator on 2016/1/7.
 */
public class ReplyAdapter extends BaseAdapter{

    private ArrayList<CommentsBean> commentBeans;
    private LayoutInflater layoutInflater;

    public ReplyAdapter(ArrayList<CommentsBean> commentBeans,Context context){
        this.commentBeans=commentBeans;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public boolean isEnabled(int position) {
        return super.isEnabled(position);
    }

    @Override
    public int getCount() {
        return commentBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return commentBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Viewholder viewHolder;
        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.item_reply, null);
            viewHolder = new Viewholder();
            viewHolder.name= (TextView) convertView.findViewById(R.id.comment_name);
            viewHolder.content= (TextView) convertView.findViewById(R.id.comment_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (Viewholder) convertView.getTag();
        }

        viewHolder.name.setText(commentBeans.get(position).getUser().getUsername());
        viewHolder.content.setText(commentBeans.get(position).getContent());

        return convertView;
    }


    static class Viewholder{

        private TextView name;
        private TextView content;

    }


}
