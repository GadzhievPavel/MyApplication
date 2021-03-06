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
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.concurrent.Exchanger;

public class ListenAndSender implements Runnable {
    Thread thread;
    Handler handler;
    private byte[] controlMsg=new byte[2];
    private Exchanger<Bitmap> exchanger;
    DatagramSocket udpSocket;
    private String ip;
    private  int port;
    boolean run;
    private BitmapFactory.Options bitmap_options = new BitmapFactory.Options();
    public ListenAndSender(String ip, int port, Exchanger<Bitmap> exchanger){
        thread=new Thread(this,"first thread");
        this.ip=ip;
        this.port=port;
        this.exchanger=exchanger;
        bitmap_options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        thread.start();
    }
    public ListenAndSender(String ip, int port,Handler handler){
        thread=new Thread(this,"first thread");
        this.ip=ip;
        this.port=port;
        this.handler=handler;
        bitmap_options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        thread.start();
    }
    private void sendHandler(Bitmap bitmap){
        Message m = handler.obtainMessage();
        m.obj=bitmap;
        if(m.obj!=null)
        {
            handler.sendMessage(m);
        }
    }

    @Override
    public void run() {
        try {
            run=true;
            InetAddress serverAddr=InetAddress.getByName(ip);
            udpSocket=new DatagramSocket();
            udpSocket.setSoTimeout(3000);
            byte [] startMsg=("start").getBytes();
            DatagramPacket packet=new DatagramPacket(startMsg,startMsg.length,serverAddr,port);
            udpSocket.send(packet);
            Log.d("Connect","OK");
            while (run)
                try {
                    byte[] videoByte = new byte[57600];
                    DatagramPacket packetListener = new DatagramPacket(videoByte, videoByte.length);
                    udpSocket.receive(packetListener);
                    Log.e("resiver","i have packet");
                    byte[] data = packetListener.getData();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                    exchanger.exchange(bitmap);
                    DatagramPacket packetSend= new DatagramPacket(controlMsg,controlMsg.length,serverAddr,port);
                    udpSocket.send(packetSend);
                    //sendHandler(bitmap);

                } catch (SocketTimeoutException e){
                    Log.e("Timeout","300");
                    udpSocket.close();
                    run=false;
                } catch (IOException e) {
                    Log.e("UDPlinth'sIOException","error",e);
                } catch (InterruptedException e) {
                    e.printStackTrace();
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

    public byte[] getControlMsg() {
        return controlMsg;
    }

    public void setControlMsg(byte[] controlMsg) {
        this.controlMsg = controlMsg;
    }
}
