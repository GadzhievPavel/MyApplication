package com.example.myapplication;

import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import android.os.Handler;

public class ReciverUDP implements Runnable {
    public String string;
    private DatagramSocket datagramSocket;
    private Handler handler;
    private BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
    ReciverUDP(DatagramSocket socket, Handler handler)
    {
        this.datagramSocket=socket;
        this.handler=handler;

    }
    @Override
    public void run() {
        try {
            while (true)
            {
                byte[] buffer = new byte[65536];
                DatagramPacket recivGram = new DatagramPacket(buffer,buffer.length);

                datagramSocket.receive(recivGram);
                byte[] data = recivGram.getData();
                string= new String(data,0,recivGram.getLength());
                Message m = handler.obtainMessage();
                m.obj=string;
                if(m.obj!=null)
                {
                    handler.sendMessage(m);
                }

            }
            }
        catch (SocketException e) {
            e.printStackTrace();
            } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
