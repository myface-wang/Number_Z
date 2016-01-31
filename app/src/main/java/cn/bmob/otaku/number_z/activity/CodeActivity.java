package cn.bmob.otaku.number_z.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.otaku.number_z.R;
import cn.bmob.otaku.number_z.adapter.CodePageAdapter;
import cn.bmob.otaku.number_z.fragment.CodeMeFragment;
import cn.bmob.otaku.number_z.fragment.CodeUsFragment;
import cn.bmob.otaku.number_z.window.CodeTypeActivity;

/**
 * Created by Administrator on 2015/12/27.
 */
public class CodeActivity extends BaseActivity{

    private ViewPager viewpager_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);


        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar_code);
        toolbar.setTitle("代码仓库");
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TabLayout tab= (TabLayout) findViewById(R.id.tab_code);

        viewpager_code= (ViewPager) findViewById(R.id.viewpager_code);


        List<String> titles=new ArrayList<>();
        titles.add(0,"我的");
        titles.add(1,"我们的");

        List<Fragment> fragments=new ArrayList<>();

        tab.addTab(tab.newTab().setText(titles.get(0)));
        Fragment fragmentme=new CodeMeFragment();
        fragments.add(fragmentme);

        tab.addTab(tab.newTab().setText(titles.get(0)));
        Fragment fragmentus=new CodeUsFragment();
        fragments.add(fragmentus);


        CodePageAdapter adapter=new CodePageAdapter(getSupportFragmentManager(),fragments, titles);
        viewpager_code.setAdapter(adapter);
        tab.setupWithViewPager(viewpager_code);
        tab.setTabsFromPagerAdapter(adapter);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {

            finish();
            return true;
        }else if (id==R.id.code_add)
        {
            Intent intent=new Intent(this, CodeTypeActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.code_menu, menu);
        return true;
    }
}
