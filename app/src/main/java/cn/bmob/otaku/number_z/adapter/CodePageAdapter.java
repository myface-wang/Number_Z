package cn.bmob.otaku.number_z.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2015/12/27.
 */
public class CodePageAdapter extends FragmentStatePagerAdapter{

    private List<Fragment> fragments;
    private List<String> title;

    public CodePageAdapter(FragmentManager fm, List<Fragment> fragments, List<String> title) {
        super(fm);
        this.fragments=fragments;
        this.title=title;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }
}
