package cn.bmob.otaku.number_z.window;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.bmob.otaku.number_z.R;

/**
 * Created by Administrator on 2015/12/29.
 */
public class CodeTypeActivity extends Activity implements View.OnClickListener{


    private LinearLayout LL_outside;
    private TextView tv_url,tv_yun,tv_magnetic,tv_other;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_type);

        LL_outside= (LinearLayout) findViewById(R.id.LL_outside);

        tv_url= (TextView) findViewById(R.id.tv_url);
        tv_yun= (TextView) findViewById(R.id.tv_yun);
        tv_magnetic= (TextView) findViewById(R.id.tv_magnetic);
        tv_other= (TextView) findViewById(R.id.tv_other);

        LL_outside.setOnClickListener(this);
        tv_url.setOnClickListener(this);
        tv_yun.setOnClickListener(this);
        tv_magnetic.setOnClickListener(this);
        tv_other.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.LL_outside:
                finish();
                break;
            case R.id.tv_url:
                Intent intent1=new Intent(CodeTypeActivity.this,CodeInputActivity.class);
                intent1.putExtra("type", "url");
                startActivity(intent1);
                break;
            case R.id.tv_yun:
                Intent intent2=new Intent(CodeTypeActivity.this,CodeInputActivity.class);
                intent2.putExtra("type", "yun");
                startActivity(intent2);
                break;
            case R.id.tv_magnetic:
                Intent intent3=new Intent(CodeTypeActivity.this,CodeInputActivity.class);
                intent3.putExtra("type","magnetic");
                startActivity(intent3);
                break;
            case R.id.tv_other:
                finish();
                break;

            default:

                break;
        }
    }
}
