package cn.bmob.otaku.number_z.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import cn.bmob.otaku.number_z.R;

/**
 * Created by Administrator on 2015/12/25.
 */
public class AboutActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_about);
        toolbar.setTitle("关于我们");
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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


}
