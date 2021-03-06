package com.example.myapplication;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.DatagramSocket;
import java.util.concurrent.Exchanger;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    TextView textView2;
    ImageView imageView;
    SurfaceActivity surfaceActivity;
    Handler handler;
    ListenAndSender listenAndSender;
    Exchanger<Bitmap> exchanger = new Exchanger<Bitmap>();
    int port=9000;
    String IP;
    String [] array=new String[2];
    DatagramSocket socket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("onCreated ", "SA");
        SurfaceActivity.exchanger=exchanger;
        setContentView(R.layout.lay);
        surfaceActivity=findViewById(R.id.frame);
        textView=findViewById(R.id.angel);
        textView2=findViewById(R.id.str);
        Log.e("INIT view","SA");
        IP=getIntent().getExtras().getString("IP");
        listenAndSender=new ListenAndSender(IP,port,exchanger);
        if(IP!=null) {
            Log.e("IP", IP);
        }
        else {
            Log.e("IP", "null");
        }
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

    protected void onPause() {
        super.onPause();
        surfaceActivity.fpvThread.setRunning(false);
        finish();
    }
}

