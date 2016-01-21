package cn.bmob.otaku.number_z.utils;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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


    /**
     * 获取sd路径
     * @return
     */
    public static String SDpath(){
        File i= Environment.getExternalStorageDirectory();
        return i.getPath();
    }


    /**
     * 获取当前系统时间戳
     * @return
     */
    public static long Time() throws ParseException {
        Date d = new Date();
        SimpleDateFormat sft  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sft.format(d);
        return sft.parse(sft.format(d)).getTime();
    }

}
