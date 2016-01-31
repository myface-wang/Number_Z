package cn.bmob.otaku.number_z.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.otaku.number_z.Bean.CollectionBean;
import cn.bmob.otaku.number_z.Bean.CommentBean;
import cn.bmob.otaku.number_z.Bean.CommentsBean;
import cn.bmob.otaku.number_z.Bean.DetailsBean;
import cn.bmob.otaku.number_z.Bean.MyUser;
import cn.bmob.otaku.number_z.R;
import cn.bmob.otaku.number_z.adapter.DetailsAdapter;
import cn.bmob.otaku.number_z.view.SpacesItemDecoration;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2015/12/25.
 */
public class DetailsActivity extends BaseActivity implements View.OnClickListener{

    private RecyclerView mRecycler;

    public List<CommentBean> commentBeanList=new ArrayList<CommentBean>();
    private DetailsAdapter detailsAdapter;
    private String value;
    private String title;

    private ImageView head_details_image;
//    private TextView head_details_title;
//    private TextView head_details_content;
//    private TextView head_details_time;

    private Toolbar toolbar;

    private ImageView img_collection,image_comments;
    private TextView tv_comments,tv_title;
    private DetailsBean detailsBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        toolbar = (Toolbar) findViewById(R.id.toolbar_details);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "预期跳转到话题页面", Snackbar.LENGTH_LONG)
                        .setAction("Go", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                //预期跳转到话题页面

                            }
                        }).show();
            }
        });

        Intent intent=getIntent();
        detailsBean= (DetailsBean) intent.getExtras().getSerializable("commentBean");
        value=detailsBean.getObjectId();
        initview();
        loaddate();

    }


    private void initview() {

        mRecycler = (RecyclerView) findViewById(R.id.recycler_details);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(layoutManager);
        SpacesItemDecoration decoration=new SpacesItemDecoration(7);
        mRecycler.addItemDecoration(decoration);
        detailsAdapter=new DetailsAdapter(this, commentBeanList);
        mRecycler.setAdapter(detailsAdapter);


        head_details_image= (ImageView) findViewById(R.id.details_image);
//        head_details_title= (TextView) findViewById(R.id.details_title);
//        head_details_content= (TextView) findViewById(R.id.details_content);
//        head_details_time= (TextView) findViewById(R.id.details_time);

        img_collection= (ImageView) findViewById(R.id.img_collection);
        image_comments= (ImageView) findViewById(R.id.image_comments);
        tv_comments= (TextView) findViewById(R.id.tv_comments);
        img_collection.setOnClickListener(this);
        image_comments.setOnClickListener(this);
        tv_comments.setOnClickListener(this);

        tv_title= (TextView) findViewById(R.id.tv_title);


    }

    private void loaddate() {

        commentsSize();
        collectioned();

        ImageOptions options= new ImageOptions.Builder()
                .setSize(-1,-1)
                .setIgnoreGif(false)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .build();

        tv_title.setText(detailsBean.getName());
        x.image().bind(head_details_image, detailsBean.getHeadimg().getFileUrl(DetailsActivity.this), options);
        commentBeanList.clear();
        commentBeanList.addAll(detailsBean.getContent());
        detailsAdapter.notifyDataSetChanged();
        title=detailsBean.getName();

    }

    private void commentsSize() {

        BmobQuery<CommentsBean> query = new BmobQuery<CommentsBean>();

        DetailsBean post1 = new DetailsBean();
        post1.setObjectId(value);
        query.addWhereEqualTo("detail", new BmobPointer(post1));

        query.findObjects(this, new FindListener<CommentsBean>() {

            @Override
            public void onSuccess(List<CommentsBean> object) {
                // TODO Auto-generated method stub
                tv_comments.setText(object.size() + "");
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                tv_comments.setText("N/A");
            }

        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.img_collection:
                MyUser user = BmobUser.getCurrentUser(this, MyUser.class);
                if (user!=null)
                {
                    collection();
                }else {
                    Toast.makeText(this,"请先登录！",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.image_comments:
                Intent intent=new Intent(this,CommentsActivity.class);
                intent.putExtra("objectid", value);
                intent.putExtra("title",title);
                startActivity(intent);
                break;
            case R.id.tv_comments:
                Intent intent2=new Intent(this,CommentsActivity.class);
                intent2.putExtra("objectid", value);
                intent2.putExtra("title",title);
                startActivity(intent2);
                break;

            default:
                break;
        }

    }

    private void collectioned(){

        BmobQuery<CollectionBean> query = new BmobQuery<CollectionBean>();
        DetailsBean post1 = new DetailsBean();
        post1.setObjectId(value);
        query.addWhereEqualTo("details", new BmobPointer(post1));

        MyUser user = BmobUser.getCurrentUser(this, MyUser.class);

        query.addWhereEqualTo("userid", new BmobPointer(user));
        query.findObjects(this, new FindListener<CollectionBean>() {

            @Override
            public void onSuccess(List<CollectionBean> object) {
                // TODO Auto-generated method stub

                if (object.size()==0)
                {
                    img_collection.setImageResource(R.drawable.collection_details);
                }else {
                    img_collection.setImageResource(R.drawable.pink);
                }

            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
            }
        });

    }

    private void collection() {

        BmobQuery<CollectionBean> query = new BmobQuery<CollectionBean>();
        DetailsBean post1 = new DetailsBean();
        post1.setObjectId(value);
        query.addWhereEqualTo("details", new BmobPointer(post1));

        MyUser user = BmobUser.getCurrentUser(this, MyUser.class);

//        MyUser User=new MyUser();
//        User.setObjectId("gHj3777X");
        query.addWhereEqualTo("userid", new BmobPointer(user));
        query.findObjects(this, new FindListener<CollectionBean>() {

            @Override
            public void onSuccess(List<CollectionBean> object) {
                // TODO Auto-generated method stub

                if (object.size()==0)
                {
                    collectionGo();
                }else {
                    collectionBack(object.get(0).getObjectId());
                }

            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
            }
        });

    }

    private void collectionBack(String ObjId) {

        CollectionBean collectionBean = new CollectionBean();
        collectionBean.setObjectId(ObjId);
        collectionBean.delete(this, new DeleteListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Toast.makeText(DetailsActivity.this,"收藏取消啦~",Toast.LENGTH_SHORT).show();
                img_collection.setImageResource(R.drawable.collection_details);
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                Toast.makeText(DetailsActivity.this,"取消收藏失败！",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void collectionGo(){

        MyUser user = BmobUser.getCurrentUser(this, MyUser.class);


        DetailsBean post = new DetailsBean();
        post.setObjectId(value);

//        MyUser myUser=new MyUser();
//        myUser.setObjectId("gHj3777X");

        final CollectionBean collectionBean = new CollectionBean();
        collectionBean.setDetails(post);
        collectionBean.setUserid(user);
        collectionBean.save(this, new SaveListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Toast.makeText(DetailsActivity.this,"为您收藏咯！",Toast.LENGTH_SHORT).show();
                img_collection.setImageResource(R.drawable.pink);
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                Toast.makeText(DetailsActivity.this,"收藏失败！",Toast.LENGTH_SHORT).show();

            }
        });

    }

}
