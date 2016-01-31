package cn.bmob.otaku.number_z.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.Serializable;
import java.util.List;

import cn.bmob.otaku.number_z.Bean.CommentBean;
import cn.bmob.otaku.number_z.R;
import cn.bmob.otaku.number_z.activity.ImageActivity;
import cn.bmob.otaku.number_z.view.MyImageView;


/**
 * Created by Administrator on 2015/11/4.
 */
public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder>{

    private Context context;
    private LayoutInflater inf;
    private List<CommentBean> list;
//    private OnItemActionListener mOnItemActionListener;

    public DetailsAdapter(Context context, List<CommentBean> list) {

        this.context = context;
        this.list = list;
        inf = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //绑定布局
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.img_txt_item, null);
        //创建ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

//        if(mOnItemActionListener!=null)
//        {
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    //注意这里必须使用viewHolder.getPosition()而不能用i，因为为了保证动画，没有使用NotifyDatasetChanged更新位置数据
//                    mOnItemActionListener.onItemClickListener(v,holder.getPosition());
//                }
//            });
//            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    //注意这里必须使用viewHolder.getPosition()而不能用i，因为为了保证动画，没有使用NotifyDatasetChanged更新位置数据
//                    return mOnItemActionListener.onItemLongClickListener(v, holder.getPosition());
//                }
//            });
//        }


        ImageOptions  imageOptions = new ImageOptions.Builder()
                // 是否忽略GIF格式的图片
                .setIgnoreGif(false)
                        // 图片缩放模式
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                        // 下载中显示的图片
                .setLoadingDrawableId(R.drawable.load)
                        // 下载失败显示的图片
//                .setFailureDrawableId(R.drawable.head)
                        // 得到ImageOptions对象
                .build();


        x.image().bind(holder.img,list.get(position).getUrl(),imageOptions, new Callback.CommonCallback<Drawable>() {
            @Override
            public void onSuccess(Drawable result) {

                int h= result.getIntrinsicHeight();
                int w= result.getIntrinsicWidth();

                holder.img.setHeightRatio(h, w);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

        holder.txt1.setText(list.get(position).getArtname());
        holder.txt2.setText(list.get(position).getAuthorname());
        holder.txt3.setText(list.get(position).getFrom());

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.putExtra("id", position);
//                intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) list);
//                intent.setClass(context, ImageActivity.class);
//                context.startActivity(intent);

                Intent intent = new Intent();
                Bundle bu = new Bundle();
                bu.putInt("id", position);
                bu.putSerializable("list", (Serializable) list);
                intent.putExtras(bu);
//                intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) list);
                intent.setClass(context, ImageActivity.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        MyImageView img;
        TextView txt1;
        TextView txt2;
        TextView txt3;

        public ViewHolder(View view) {
            super(view);

            img= (MyImageView) view.findViewById(R.id.img);
            txt1= (TextView) view.findViewById(R.id.txt1);
            txt2= (TextView) view.findViewById(R.id.txt2);
            txt3= (TextView) view.findViewById(R.id.txt3);
        }
    }


//    /**********定义点击事件**********/
//    public   interface OnItemActionListener
//    {
//        public   void onItemClickListener(View v,int pos);
//        public   boolean onItemLongClickListener(View v,int pos);
//    }
//
//
//    public void setOnItemActionListener(OnItemActionListener onItemActionListener) {
//        this.mOnItemActionListener = onItemActionListener;
//    }


}
