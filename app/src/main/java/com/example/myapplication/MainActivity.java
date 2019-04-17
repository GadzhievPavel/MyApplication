package com.example.myapplication;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.Exchanger;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    ImageView imageView;
    //Bitmap frame;
    //MyHandler myHandler=new MyHandler(this);
    Exchanger<Bitmap> exchanger = new Exchanger<Bitmap>();

    int port=9000;
    //String IP="192.168.2.49";
    //String IP="192.168.1.108";
    //String IP="192.168.114.165";
    //String IP="192.168.114.165";
    //String IP="192.168.0.161";
    //String IP="192.168.0.108";
    String IP = "192.168.1.100";
    DatagramSocket socket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListenAndSender listenAndSender=new ListenAndSender(IP,port,exchanger);
        getSupportActionBar().hide();
        setContentView(new SurfaceActivity(this,exchanger));

        //imageView=findViewById(R.id.image);
        //textView=findViewById(R.id.text);

        //imageView.setImageBitmap(frame);
    }

}
