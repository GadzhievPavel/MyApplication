package com.example.myapplication;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

public class MyHandler extends Handler {
    private final WeakReference<MainActivity> mainActivityWeakReference;

    public MyHandler(MainActivity activity)
    {
        mainActivityWeakReference=new WeakReference<MainActivity>(activity);
    }
    @Override
    public void  handleMessage(android.os.Message m)
    {
        MainActivity activity = mainActivityWeakReference.get();
        if(activity!=null)
        {
            try {
                String s=(String)m.obj;
                activity.textView.setText(s);
            }
            catch (Exception e)
                {

                }
                super.handleMessage(m);
        }
    }
}
