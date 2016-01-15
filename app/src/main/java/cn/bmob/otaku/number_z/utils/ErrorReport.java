package cn.bmob.otaku.number_z.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/1/3.
 */
public class ErrorReport {

    public static void RrrorCode(int code,Context context)
    {

        switch (code)
        {
            case 301:
                //email Must be a valid email address
                Toast.makeText(context, "请正确输入邮箱", Toast.LENGTH_SHORT).show();
                break;
            case 304:
                //username or password is null
                Toast.makeText(context,"账号或密码不能为空",Toast.LENGTH_SHORT).show();
                break;
            case 203:
                //邮箱已经被注册email '240298530@qq.com' already taken
                Toast.makeText(context,"该邮箱已被注册",Toast.LENGTH_SHORT).show();
                break;
            case 202:
                //username '1' already taken
                Toast.makeText(context,"该用户名已被注册",Toast.LENGTH_SHORT).show();
                break;
            case 9010:
                //The network is not normal.(Time out)
                Toast.makeText(context,"网络连接超时",Toast.LENGTH_SHORT).show();
                break;
            case 1:
                //内部错误
                Toast.makeText(context,"内部错误",Toast.LENGTH_SHORT).show();
                break;
            case 101:
                // 用户名或密码不正确
                Toast.makeText(context,"用户名或密码不正确",Toast.LENGTH_SHORT).show();
                break;
            case 205:
                //no user found with email 'pppp@qq.com'.
                Toast.makeText(context,"输入有误，请重试",Toast.LENGTH_SHORT).show();
                break;
            case 9016:
                Toast.makeText(context,"网络链接中断，请检查网络",Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(context,"未知错误，请重试。",Toast.LENGTH_SHORT).show();
                break;


        }


    }

}
