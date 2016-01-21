package cn.bmob.otaku.number_z.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.x;

import java.util.ArrayList;

import cn.bmob.otaku.number_z.Bean.DetailsBean;
import cn.bmob.otaku.number_z.R;


/**
 * Created by Administrator on 2015/11/4.
 */
public class HomeAapter extends BaseAdapter{

    private ArrayList<DetailsBean> commentBeans;
    private Context context;
    private LayoutInflater layoutInflater;

    public HomeAapter(Context context, ArrayList<DetailsBean> commentBeans){

        this.context=context;
        this.commentBeans=commentBeans;
        layoutInflater=LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return commentBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_list_main, null);
            holder = new ViewHolder();
            holder.img= (ImageView) convertView.findViewById(R.id.comment_image);
            holder.textView= (TextView) convertView.findViewById(R.id.tv_comment);
            holder.title= (TextView) convertView.findViewById(R.id.tv_title);
            holder.time= (TextView) convertView.findViewById(R.id.ll_right);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        x.image().bind(holder.img, commentBeans.get(position).getCover().getFileUrl(context));

        holder.title.setText(commentBeans.get(position).getName());
        holder.textView.setText(ToDBC(commentBeans.get(position).getIntroduction()));
        holder.time.setText(commentBeans.get(position).getCreatedAt().substring(0,10));

        return convertView;
    }

    public static String ToDBC(String input) {
        Log.i("input",input);
        char[] c = input.toCharArray();
        for (int i = 0; i< c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }if (c[i]> 65280&& c[i]< 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    static class ViewHolder {
        private ImageView img;
        private TextView textView;
        private TextView title;
        private TextView time;
    }
}
