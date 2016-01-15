package cn.bmob.otaku.number_z.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.bmob.otaku.number_z.Bean.CodeBean;
import cn.bmob.otaku.number_z.Bean.MyUser;
import cn.bmob.otaku.number_z.R;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.CountListener;

/**
 * Created by Administrator on 2015/12/27.
 */
public class CodeMeAdapter extends BaseAdapter{

    private ArrayList<CodeBean> codeBeans;
    private Context context;
    private LayoutInflater layoutInflater;


    public CodeMeAdapter(ArrayList<CodeBean> codeBeans,Context context){
        this.codeBeans=codeBeans;
        this.context=context;
        layoutInflater= LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return codeBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return codeBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_code_me, null);
            holder = new ViewHolder();
            holder.img= (ImageView) convertView.findViewById(R.id.image_code_me);
            holder.title= (TextView) convertView.findViewById(R.id.code_title_me);
            holder.content= (TextView)convertView.findViewById(R.id.code_content_me);
            holder.image_code_share= (ImageView) convertView.findViewById(R.id.image_code_share);
            holder.tv_code_zan= (TextView) convertView.findViewById(R.id.tv_code_zan);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (codeBeans.get(position).getType()==1)
        {
           holder.img.setImageResource(R.drawable.head);
        }else if (codeBeans.get(position).getType()==2){
            holder.img.setImageResource(R.drawable.yun);
        }else if (codeBeans.get(position).getType()==3){
            holder.img.setImageResource(R.drawable.xunlei);
        }

        if (codeBeans.get(position).getShare()){

            holder.image_code_share.setImageResource(R.drawable.share);
        }else {

            holder.image_code_share.setImageResource(R.drawable.share_no);
        }

        size(codeBeans.get(position).getObjectId(),holder.tv_code_zan);
        holder.title.setText(codeBeans.get(position).getTitle());
        holder.content.setText(codeBeans.get(position).getCode());

        return convertView;
    }

    static class ViewHolder {
        private ImageView img;
        private TextView title;
        private TextView content;
        private ImageView image_code_share;
        private TextView tv_code_zan;
    }

    private void size(String ObjId, final TextView share){

        BmobQuery<MyUser> query = new BmobQuery<MyUser>();

        CodeBean post = new CodeBean();
        post.setObjectId(ObjId);

        query.addWhereRelatedTo("zan", new BmobPointer(post));

        query.count(context, MyUser.class, new CountListener() {
            @Override
            public void onSuccess(int count) {
                // TODO Auto-generated method stub

                share.setText(String.valueOf(count));
            }
            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub

            }
        });
    }

}
