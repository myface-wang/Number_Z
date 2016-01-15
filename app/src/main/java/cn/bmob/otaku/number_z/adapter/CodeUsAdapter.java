package cn.bmob.otaku.number_z.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.otaku.number_z.Bean.CodeBean;
import cn.bmob.otaku.number_z.Bean.MyUser;
import cn.bmob.otaku.number_z.R;
import cn.bmob.otaku.number_z.activity.MyApplication;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2015/12/27.
 */
public class CodeUsAdapter extends BaseAdapter{

    private ArrayList<CodeBean> codeBeans=new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;
    private MyApplication myApplication;

    private ListView list_code;

    public CodeUsAdapter(ArrayList<CodeBean> codeBeans,Context context,ListView list_code){

        this.codeBeans=codeBeans;
        this.context=context;
        this.list_code=list_code;
        layoutInflater= LayoutInflater.from(context);
        myApplication= (MyApplication) context.getApplicationContext();
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
            holder.image_code_zan= (ImageView) convertView.findViewById(R.id.image_code_zan);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (codeBeans.get(position).getUser().getImage()!=null)
        {
            x.image().bind(holder.img, codeBeans.get(position).getUser().getImage().getFileUrl(context),myApplication.getOpt());
        }

        holder.title.setText(codeBeans.get(position).getTitle());
        holder.content.setText(codeBeans.get(position).getCode());

        size(codeBeans.get(position).getObjectId(), holder.tv_code_zan);

        praise(codeBeans.get(position).getObjectId(),holder.image_code_zan);

        holder.image_code_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zan(BmobUser.getCurrentUser(context, MyUser.class).getObjectId(),
                        codeBeans.get(position).getObjectId(),holder.image_code_zan, holder.tv_code_zan);
            }
        });

        return convertView;
    }

    public static class ViewHolder {
        public ImageView img;
        public TextView title;
        public TextView content;
        public TextView tv_code_zan;
        public ImageView image_code_zan;
    }

    public void zan(String MyObjId, String ObjId, final ImageView img, final TextView textView){

        MyUser user = new MyUser();
        user.setObjectId(MyObjId);

        CodeBean codeBean=new CodeBean();
        codeBean.setObjectId(ObjId);

        BmobRelation relation = new BmobRelation();
        relation.add(user);

        codeBean.setZan(relation);
        codeBean.update(context, new UpdateListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                img.setImageResource(R.drawable.pink);
                try {
                    int a = Integer.parseInt(textView.getText().toString());
                    textView.setText((a+1)+"");
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    textView.setText("N/A");
                }
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                // TODO Auto-generated method stub
            }
        });
    }


    private void praise(String ObjId, final ImageView img){

        // 测试多对多关联查询数据
        BmobQuery<CodeBean> queryPost = new BmobQuery<CodeBean>();
        // 查询条件一 帖子是该帖子
        BmobQuery<CodeBean> eq1 = new BmobQuery<>();
        eq1.addWhereEqualTo("objectId", ObjId);
        // 查询条件二 帖子的喜欢里面有这个用户
        MyUser myUser= BmobUser.getCurrentUser(context, MyUser.class);
        BmobQuery<CodeBean> eq2 = new BmobQuery<>();
        eq2.addWhereContains("zan",myUser.getObjectId());
        // 组装查询条件
        List<BmobQuery<CodeBean>> queries = new ArrayList<>();
        queries.add(eq1);
        queries.add(eq2);
        queryPost.and(queries);
        queryPost.findObjects(context, new FindListener<CodeBean>() {
            @Override
            public void onSuccess(List<CodeBean> list) {

                if (list.size()==1)
                {
                    img.setImageResource(R.drawable.pink);
                }else {
                    img.setImageResource(R.drawable.collection_no);
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });


    }

    private void size(String ObjId, final TextView zan)
    {

        BmobQuery<MyUser> query1 = new BmobQuery<MyUser>();
        CodeBean post = new CodeBean();
        post.setObjectId(ObjId);

        query1.addWhereRelatedTo("zan", new BmobPointer(post));
        query1.findObjects(context, new FindListener<MyUser>() {

            @Override
            public void onSuccess(List<MyUser> object) {
                // TODO Auto-generated method stub
                zan.setText(object.size() + "");
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
            }
        });

    }

}
