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
                    byte[] data = packetListener.getData();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                    Message m = handler.obtainMessage();
                    m.obj=bitmap;
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
