package com.example.myapplication;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
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

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    TextView textView2;
    ImageView imageView;
    SurfaceActivity surfaceActivity;
    Handler handler;
    //Bitmap frame;
    //MyHandler myHandler=new MyHandler(this);
    Exchanger<Bitmap> exchanger = new Exchanger<Bitmap>();
    int port=9000;
    //String IP="192.168.2.49";
    //String IP="192.168.1.108";
    //String IP="192.168.114.165";
    //String IP="192.168.114.165";
    String IP="192.168.0.161";
    //String IP="192.168.0.108";
    //String IP = "192.168.1.113";
    String [] array=new String[2];
    DatagramSocket socket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ListenAndSender listenAndSender=new ListenAndSender(IP,port,exchanger);
        SurfaceActivity.exchanger=exchanger;
//        getSupportActionBar().hide();
        setContentView(R.layout.lay);
        surfaceActivity=findViewById(R.id.frame);
        textView=findViewById(R.id.angel);
        textView2=findViewById(R.id.str);
        JoystickView joystickView=findViewById(R.id.joystick);
        joystickView.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                byte[] sendPacket = new byte[2];
                array[0]= String.valueOf(angle);
                array[1]= String.valueOf(strength);
                sendPacket[0]= (byte) angle;
                sendPacket[1]= (byte) strength;
                listenAndSender.setControlMsg(sendPacket);
                Message msg=handler.obtainMessage();
                msg.obj=array;
                handler.sendMessage(msg);
                Log.e("angel", String.valueOf(angle));
                Log.e("str", String.valueOf(strength));
            }
        });
        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                array= (String[]) msg.obj;
                textView.setText("Angel"+array[0]);
                textView2.setText("STR"+array[1]);
            };
        };
    }





}
