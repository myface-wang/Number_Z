package cn.bmob.otaku.number_z.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.x;

import java.util.ArrayList;

import cn.bmob.otaku.number_z.Bean.CollectionBean;
import cn.bmob.otaku.number_z.R;
import cn.bmob.otaku.number_z.activity.DetailsActivity;
import cn.bmob.otaku.number_z.window.MyDialog;
import cn.bmob.v3.listener.DeleteListener;


/**
 * Created by Administrator on 2015/11/4.
 */
public class CollectionAapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private ArrayList<CollectionBean> commentBeans;
    private Context context;

    public CollectionAapter(Context context, ArrayList<CollectionBean> commentBeans){

        this.context=context;
        this.commentBeans=commentBeans;
        layoutInflater = LayoutInflater.from(context);

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_shoucang, null);
            holder = new ViewHolder();
            holder.img= (ImageView) convertView.findViewById(R.id.drawer_image);
            holder.textView= (TextView) convertView.findViewById(R.id.drawer_text);
            holder.drawer_time= (TextView) convertView.findViewById(R.id.drawer_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        x.image().bind(holder.img, commentBeans.get(position).getDetails().getCover().getFileUrl(context));


        holder.textView.setText(ToDBC(commentBeans.get(position).getDetails().getName()));

        holder.drawer_time.setText(commentBeans.get(position).getCreatedAt().substring(0,10));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("objectid", commentBeans.get(position).getDetails().getObjectId());
                intent.setClass(context, DetailsActivity.class);
                context.startActivity(intent);
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                MyDialog.Builder builder = new MyDialog.Builder(context);
                builder.setMessage("确定要删除吗？");
                builder.setTitle("执行删除操作");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        deleteshoucahng(position);
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("取消",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();

                return false;
            }
        });

        return convertView;
    }

    public static String ToDBC(String input) {
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
        private TextView drawer_time;
    }

    /*
    删除用户某条收藏
 */
    public void deleteshoucahng(final int position){

        CollectionBean collectionBean=new CollectionBean();
        collectionBean.setObjectId(commentBeans.get(position).getObjectId());
        collectionBean.delete(context, new DeleteListener() {
            @Override
            public void onSuccess() {
                commentBeans.remove(position);
                notifyDataSetChanged();

                Toast.makeText(context,"删除成功！",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(context,"删除失败！",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
