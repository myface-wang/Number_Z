package cn.bmob.otaku.number_z.utils;

import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2015/12/28.
 */
public class SysUtils {

    /**
     * 实现文本复制功能
     */
    public static void copy(String content, Context context)
    {
        if (content!=null)
        {
            ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(content.trim());
            Toast.makeText(context,"复制成功",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context,"N/A",Toast.LENGTH_SHORT).show();
        }

    }
    /**
     * 实现粘贴功能
     */
    public static String paste(Context context)
    {
        ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getText().toString().trim();
    }

}
