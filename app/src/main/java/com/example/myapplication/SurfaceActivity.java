package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.Exchanger;

import static android.content.ContentValues.TAG;

public class SurfaceActivity extends SurfaceView implements SurfaceHolder.Callback {
    private FPVThread fpvThread;
    private Bitmap bitmap;

    public SurfaceActivity(Context context, Exchanger<Bitmap> exchanger){
        super(context);
        getHolder().addCallback(this);
        fpvThread= new FPVThread(getHolder(),this,exchanger);
        setFocusable(true);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        fpvThread.setRunning(true);
        fpvThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
            boolean retry = true;
            while (retry){
                try {
                    fpvThread.join();
                    retry=false;
                }catch (InterruptedException e){
                    Log.e(TAG,"no stoped treadFPV");
                }
            }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
     return super.onTouchEvent(event);
    }
    public void setBitmap(Bitmap bitmap){
        this.bitmap=bitmap;
    }
    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawBitmap(this.bitmap,0,0,null);
    }
}
