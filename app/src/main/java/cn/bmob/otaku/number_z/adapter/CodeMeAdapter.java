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
import cn.bmob.otaku.number_z.R;

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

        if (codeBeans.get(position).getType()==0)
        {
           holder.img.setImageResource(R.drawable.head);
        }else if (codeBeans.get(position).getType()==1){
            holder.img.setImageResource(R.drawable.yun);
        }else if (codeBeans.get(position).getType()==2){
            holder.img.setImageResource(R.drawable.xunlei);
        }

        if (codeBeans.get(position).getShare()){

            holder.image_code_share.setImageResource(R.drawable.share);
        }else {

            holder.image_code_share.setImageResource(R.drawable.share_no);
        }

        if (codeBeans.get(position).getStar()!=null){
            holder.tv_code_zan.setText(codeBeans.get(position).getStar()+"");
        }else {
            holder.tv_code_zan.setText("0");
        }


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
}
