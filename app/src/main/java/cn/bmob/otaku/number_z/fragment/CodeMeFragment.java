package cn.bmob.otaku.number_z.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.otaku.number_z.Bean.CodeBean;
import cn.bmob.otaku.number_z.Bean.MyUser;
import cn.bmob.otaku.number_z.R;
import cn.bmob.otaku.number_z.activity.MyApplication;
import cn.bmob.otaku.number_z.adapter.CodeMeAdapter;
import cn.bmob.otaku.number_z.utils.ErrorReport;
import cn.bmob.otaku.number_z.window.CodeChooseMeActivity;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2015/12/27.
 */
public class CodeMeFragment extends Fragment{

    private View view;
    private ArrayList<CodeBean> codeBeans=new ArrayList<>();
    private  CodeMeAdapter codeMeAdapter;
    private SwipeRefreshLayout swipeLayout;

    private Myhandler handler=null;
    private MyApplication application=null;

    private int codeidposition;

    private int page=10;
    private int endpage=10;//每次翻页取的个数
    private boolean flag=true;
    private boolean isLoading = false;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_code_me, container, false);

        application= (MyApplication) getActivity().getApplication();

        handler=new Myhandler();

        final ListView list_code= (ListView) view.findViewById(R.id.list_code);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_code_me);
        swipeLayout.setColorSchemeResources(R.color.color_bule2,
                R.color.color_bule,
                R.color.color_bule2,
                R.color.color_bule3);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                initdata();

            }
        });

        codeMeAdapter=new CodeMeAdapter(codeBeans,getActivity());

        list_code.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                application.setHandler(handler);
                Intent intent = new Intent(getActivity(), CodeChooseMeActivity.class);
                intent.putExtra("code", codeBeans.get(position).getCode());
                intent.putExtra("objid", codeBeans.get(position).getObjectId());
                intent.putExtra("flag", codeBeans.get(position).getShare());
                intent.putExtra("type", codeBeans.get(position).getType());
                startActivity(intent);
                codeidposition = position;

                Log.i("position",position+"+"+codeBeans.get(position).getType());
            }
        });

        list_code.setAdapter(codeMeAdapter);

        list_code.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (list_code != null && list_code.getChildCount() > 0) {
                    boolean enable = (firstVisibleItem == 0) && (view.getChildAt(firstVisibleItem).getTop() == 0);
                    swipeLayout.setEnabled(enable);
                    if (firstVisibleItem + visibleItemCount == totalItemCount && !isLoading && totalItemCount >= page) {
                        if (flag) {
                            load();
                        }
                    }
                }
            }
        });

        initdata();

        return view;
    }

    private void load() {

        isLoading = true;
        BmobQuery<CodeBean> Query = new BmobQuery<CodeBean>();
        MyUser myUser= BmobUser.getCurrentUser(getActivity(), MyUser.class);
        Query.include("user");
        Query.order("-createdAt");
        Query.setSkip(page);
        Query.setLimit(endpage);
        Query.addWhereEqualTo("user", myUser);
        Query.findObjects(getActivity(), new FindListener<CodeBean>() {
            @Override
            public void onSuccess(List<CodeBean> object) {
                // TODO Auto-generated method stub

                codeBeans.addAll(object);
                if (object.size()==endpage)
                {
                    page=page+endpage;
                }else {
                    flag=false;
                }
                codeMeAdapter.notifyDataSetChanged();
                isLoading = false;
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                ErrorReport.RrrorCode(code, getActivity());
                isLoading = false;
            }
        });

    }

    private void initdata() {

        MyUser user = BmobUser.getCurrentUser(getActivity(), MyUser.class);
        BmobQuery<CodeBean> query = new BmobQuery<CodeBean>();
        query.addWhereEqualTo("user", user);
        query.include("user");
        query.order("-createdAt");
        query.setLimit(endpage);
        query.findObjects(getActivity(), new FindListener<CodeBean>() {
            @Override
            public void onSuccess(List<CodeBean> list) {

                codeBeans.clear();
                codeBeans.addAll(list);
                codeMeAdapter.notifyDataSetChanged();
                swipeLayout.setRefreshing(false);

            }

            @Override
            public void onError(int i, String s) {
                ErrorReport.RrrorCode(i,getActivity());
                swipeLayout.setRefreshing(false);
            }
        });

    }

    private void delete() {

        final CodeBean codebean = new CodeBean();
        codebean.setObjectId(codeBeans.get(codeidposition).getObjectId());
        codebean.delete(getActivity(), new DeleteListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub

                codeBeans.remove(codeidposition);
                codeMeAdapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                ErrorReport.RrrorCode(code,getActivity());
            }
        });

    }


    public class Myhandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            delete();
        }
    }
}
