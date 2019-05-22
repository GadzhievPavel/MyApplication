package com.example.myapplication;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.concurrent.Exchanger;

import static android.content.ContentValues.TAG;

class FPVThread extends Thread {
    private boolean running;
    private SurfaceHolder surfaceHolder;
    private SurfaceActivity surfaceActivity;
    private Exchanger<Bitmap> exchanger;
    private Bitmap bitmap;

    private final static int MAX_FPS = 50;
    private final static int MAX_FRAME_SKIPS = 5;
    private final static int FRAME_PERIOD = 1000 / MAX_FPS;

    public FPVThread(SurfaceHolder surfaceHolder, SurfaceActivity surfaceActivity, Exchanger<Bitmap> exchanger) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.surfaceActivity = surfaceActivity;
        this.exchanger = exchanger;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @SuppressLint("WrongCall")
    @Override
    public void run() {
        Canvas canvas;
        Log.d(TAG, "Starting game loop");

        long beginTime;// Time start cycle
        long timeDiff;
        long sleepTime;// allowed sleeping time
        long frameSkipped;//skip frame

        sleepTime = 0;
        while (running) {
            canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas();
                beginTime = System.currentTimeMillis();
                frameSkipped = 0;
                this.bitmap = exchanger.exchange(bitmap);
                this.surfaceActivity.setBitmap(bitmap);
                this.surfaceActivity.onDraw(canvas);
                timeDiff = System.currentTimeMillis() - beginTime;
                sleepTime = (int) (FRAME_PERIOD - timeDiff);

                if (sleepTime > 0) {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        Log.e("Sleeping Thread", "err");
                    }
                }
                while (sleepTime < 0 && frameSkipped < MAX_FRAME_SKIPS) {
                    this.bitmap = exchanger.exchange(bitmap);
                    this.surfaceActivity.setBitmap(bitmap);
                    this.surfaceActivity.onDraw(canvas);
                    sleepTime += FRAME_PERIOD;
                    frameSkipped++;
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
