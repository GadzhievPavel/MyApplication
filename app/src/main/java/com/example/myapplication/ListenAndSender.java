package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class ListenAndSender implements Runnable {
    Thread thread;
    Handler handler;
    private String ip;
    private  int port;
    private BitmapFactory.Options bitmap_options = new BitmapFactory.Options();
    public ListenAndSender(String ip,int port,Handler handler){
        thread=new Thread(this,"first thread");
        this.ip=ip;
        this.port=port;
        this.handler=handler;
        bitmap_options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        thread.start();

    }

    private Bitmap decodeStream(byte [] array){
        Bitmap bitmap=Bitmap.createBitmap(60,640,Bitmap.Config.ARGB_8888);
        int [] arrayBuf=new int[array.length];
        int color;
        int bCount=2;
        int gCount=1;
        int rCount=0;
        for (int i=0;i<40;i++)
        {
            for(int j=0;j<640;j++)
            {

                color=Color.argb(255,arrayBuf[bCount],arrayBuf[gCount],arrayBuf[rCount]);
                bitmap.setPixel(i,j,color);
                bCount++;
                gCount++;
                rCount++;
            }
        }
        return bitmap;
    }
    @Override
    public void run() {
        boolean run=true;
        try {
            InetAddress serverAddr=InetAddress.getByName(ip);
            DatagramSocket udpSocket=new DatagramSocket();
            byte [] startMsg=("start").getBytes();
            DatagramPacket packet=new DatagramPacket(startMsg,startMsg.length,serverAddr,port);
            udpSocket.send(packet);
            Log.d("Connect","OK");
            while (run)
                try {
                    byte[] videoByte = new byte[57600];
                    DatagramPacket packetListener = new DatagramPacket(videoByte, videoByte.length);
                    udpSocket.receive(packetListener);
                    int [] data=new int[packetListener.getData().length];
                    ByteBuffer buffer= ByteBuffer.wrap(packetListener.getData());
                    for (int i=0;i<buffer.limit();i++)
                    {
                        data[i]=buffer.getInt(i);
                    }
                    Log.d("buf size", String.valueOf(data.length));
                    String text =String.valueOf(data);
                    Log.d("Video", String.valueOf(data));
                    Message m = handler.obtainMessage();
                    m.obj=text;
                    if(m.obj!=null)
                    {
                        handler.sendMessage(m);
                    }

                } catch (IOException e) {
                    Log.e("UDPlinth'sIOException","error",e);
                }
        } catch (SocketException e) {
            Log.e("Socket Open","Error",e);

        } catch (UnknownHostException e) {
            Log.e("HOST","Error",e);
        } catch (IOException e) {
            Log.e("SEND ","No sent",e);
        }
        catch (IndexOutOfBoundsException e)
        {
            Log.e("buffer","index limit",e);
        }
    }
}
