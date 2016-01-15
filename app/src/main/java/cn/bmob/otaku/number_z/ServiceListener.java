package cn.bmob.otaku.number_z;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Administrator on 2015/12/21.
 */
public class ServiceListener extends Thread{

    @Override
    public void run() {
        super.run();

        try {
            //1-65535
            ServerSocket serverSocket=new ServerSocket(12345);
            //建立连接
            Socket socket=serverSocket.accept();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //socket
    Socket socket=null;
    BufferedReader br=null;
    BufferedWriter bw=null;

    public void connect(){

        //ip和端口号

        AsyncTask<Void,String,Void> read=new AsyncTask<Void, String, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    socket=new Socket("ip",12345);
                    br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    bw=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                } catch (IOException e1) {
//                    Toast.makeText(MainActivity.this, "无法建立连接", Toast.LENGTH_SHORT).show();
                    e1.printStackTrace();
                }

                String line;
                try {
                    while ((line=br.readLine())!=null)
                    {
                        publishProgress();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;

            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
//                Toast.makeText(MainActivity.this,"建立连接成功",Toast.LENGTH_SHORT).show();
//                textView.append(values[0]);
            }
        };
        read.execute();

    }
}
