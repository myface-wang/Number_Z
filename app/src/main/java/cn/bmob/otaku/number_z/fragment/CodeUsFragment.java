package cn.bmob.otaku.number_z.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.otaku.number_z.Bean.CodeBean;
import cn.bmob.otaku.number_z.R;
import cn.bmob.otaku.number_z.adapter.CodeUsAdapter;
import cn.bmob.otaku.number_z.utils.ErrorReport;
import cn.bmob.otaku.number_z.window.CodeChooseUsActivity;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2015/12/27.
 */
public class CodeUsFragment extends Fragment {

    private View view;
    private ArrayList<CodeBean> codeBeans=new ArrayList<>();
    private CodeUsAdapter codeUsAdapter;
    private SwipeRefreshLayout swipeLayout;
    private  ListView list_code;

    private int page=10;
    private int endpage=10;//每次翻页取的个数
    private boolean flag=true;
    private boolean isLoading = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_code_us, container, false);

        list_code= (ListView) view.findViewById(R.id.list_code);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_code_us);
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

        codeUsAdapter=new CodeUsAdapter(codeBeans,getActivity(),list_code);

        list_code.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), CodeChooseUsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("codebean", codeBeans.get(position));
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        list_code.setAdapter(codeUsAdapter);


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

        BmobQuery<CodeBean> query = new BmobQuery<CodeBean>();
        query.addWhereEqualTo("share", true);
        query.include("user");
        query.order("-updatedAt");
        query.setLimit(endpage);
        query.setSkip(page);
        query.findObjects(getActivity(), new FindListener<CodeBean>() {
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
                codeUsAdapter.notifyDataSetChanged();
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

        BmobQuery<CodeBean> query = new BmobQuery<CodeBean>();
        query.addWhereEqualTo("share", true);
        query.include("user");
        query.order("-updatedAt");
        query.setLimit(endpage);
        query.findObjects(getActivity(), new FindListener<CodeBean>() {
            @Override
            public void onSuccess(List<CodeBean> list) {

                codeBeans.clear();
                codeBeans.addAll(list);
                codeUsAdapter.notifyDataSetChanged();
                swipeLayout.setRefreshing(false);
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }


}
