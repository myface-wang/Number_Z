package cn.bmob.otaku.number_z.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.x;

import java.util.ArrayList;

import cn.bmob.otaku.number_z.Bean.CodeBean;
import cn.bmob.otaku.number_z.Bean.MyUser;
import cn.bmob.otaku.number_z.R;
import cn.bmob.otaku.number_z.utils.BaseDate;
import cn.bmob.otaku.number_z.utils.NoDoubleClickListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2015/12/27.
 */
public class CodeUsAdapter extends BaseAdapter{

    private ArrayList<CodeBean> codeBeans=new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;

    private ListView list_code;

    public CodeUsAdapter(ArrayList<CodeBean> codeBeans,Context context,ListView list_code){

        this.codeBeans=codeBeans;
        this.context=context;
        this.list_code=list_code;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_code_us, null);
            holder = new ViewHolder();
            holder.img= (ImageView) convertView.findViewById(R.id.image_code_us);
            holder.title= (TextView) convertView.findViewById(R.id.code_title_us);
            holder.content= (TextView)convertView.findViewById(R.id.code_content_us);
            holder.tv_code_zan= (TextView) convertView.findViewById(R.id.tv_code_zan);
            holder.tv_code_cai= (TextView) convertView.findViewById(R.id.tv_code_cai);
            holder.image_code_zan= (ImageView) convertView.findViewById(R.id.image_code_zan);
            holder.image_code_cai= (ImageView) convertView.findViewById(R.id.image_code_cai);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (codeBeans.get(position).getUser().getImage()!=null)
        {
            x.image().bind(holder.img, codeBeans.get(position).getUser().getImage().getFileUrl(context), BaseDate.Head_OPTIONS());
        }else {
            holder.img.setImageResource(R.drawable.head);
        }

        holder.title.setText(codeBeans.get(position).getTitle());
        holder.content.setText(codeBeans.get(position).getCode());

        if (codeBeans.get(position).getStar()!=null){
            holder.tv_code_zan.setText(codeBeans.get(position).getStar() + "");
        }else {
            holder.tv_code_zan.setText("0");
        }
        if (codeBeans.get(position).getCai()!=null)
        {
            holder.tv_code_cai.setText(codeBeans.get(position).getCai() + "");
        }else {
            holder.tv_code_cai.setText("0");
        }

//        praise(codeBeans.get(position).getObjectId(),holder.image_code_zan);

        holder.image_code_cai.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
//                super.onNoDoubleClick(view);
                if (BmobUser.getCurrentUser(context, MyUser.class)!=null)
                {
                    cai(codeBeans.get(position).getObjectId(), holder.tv_code_cai);
                }else {
                    Toast.makeText(context,"请登录后进行操作！",Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.image_code_zan.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                if (BmobUser.getCurrentUser(context, MyUser.class) != null) {
                    zan(codeBeans.get(position).getObjectId(), holder.tv_code_zan);
                } else {
                    Toast.makeText(context, "请登录后进行操作！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }

    private void cai(String objectId, final TextView tv_code_cai) {

        if (true)
        {
            CodeBean codeBean=new CodeBean();
            codeBean.increment("cai"); // 分数递增1
            codeBean.update(context,objectId, new UpdateListener() {
                @Override
                public void onSuccess() {
                    try {
                        int a = Integer.parseInt(tv_code_cai.getText().toString());
                        tv_code_cai.setText((a+1)+"");
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        tv_code_cai.setText("N/A");
                    }
                }

                @Override
                public void onFailure(int i, String s) {
                }
            });
        }else {
            Toast.makeText(context,"您的点赞次数已用尽",Toast.LENGTH_SHORT).show();
        }

    }

    public static class ViewHolder {
        public ImageView img;
        public TextView title;
        public TextView content;
        public TextView tv_code_zan;
        public TextView tv_code_cai;
        public ImageView image_code_zan;
        public ImageView image_code_cai;
    }

    public void zan(String id,final TextView textView){

        if (true)
        {
            CodeBean codeBean=new CodeBean();
            codeBean.increment("star"); // 分数递增1
            codeBean.update(context,id, new UpdateListener() {
                @Override
                public void onSuccess() {
                    try {
                        int a = Integer.parseInt(textView.getText().toString());
                        textView.setText((a+1)+"");
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        textView.setText("N/A");
                    }
                }

                @Override
                public void onFailure(int i, String s) {
                }
            });
        }else {
            Toast.makeText(context,"您的点赞次数已用尽",Toast.LENGTH_SHORT).show();
        }

    }

//    private void praise(String ObjId, final ImageView img){
//
//        // 测试多对多关联查询数据
//        BmobQuery<CodeBean> queryPost = new BmobQuery<CodeBean>();
//        // 查询条件一 帖子是该帖子
//        BmobQuery<CodeBean> eq1 = new BmobQuery<>();
//        eq1.addWhereEqualTo("objectId", ObjId);
//        // 查询条件二 帖子的喜欢里面有这个用户
//        MyUser myUser= BmobUser.getCurrentUser(context, MyUser.class);
//        BmobQuery<CodeBean> eq2 = new BmobQuery<>();
//        eq2.addWhereContains("zan",myUser.getObjectId());
//        // 组装查询条件
//        List<BmobQuery<CodeBean>> queries = new ArrayList<>();
//        queries.add(eq1);
//        queries.add(eq2);
//        queryPost.and(queries);
//        queryPost.findObjects(context, new FindListener<CodeBean>() {
//            @Override
//            public void onSuccess(List<CodeBean> list) {
//
//                if (list.size()==1)
//                {
//                    img.setImageResource(R.drawable.pink);
//                }else {
//                    img.setImageResource(R.drawable.collection_no);
//                }
//            }
//
//            @Override
//            public void onError(int i, String s) {
//
//            }
//        });
//    }

}
