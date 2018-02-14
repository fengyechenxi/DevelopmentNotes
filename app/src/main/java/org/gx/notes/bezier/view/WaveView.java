package org.gx.notes.bezier.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 贝塞尔曲线画的重叠波浪
 * 关于贝塞尔曲线的用法，请参考：http://blog.csdn.net/harvic880925/article/details/50995587
 * Created by Administrator on 2018/2/2 0002.
 */

public class WaveView extends View {

    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private float mWaveHeight = 70;

    public WaveView(Context context) {
        super(context);
        init();
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#99ffffff"));
    }


    public void setWaveHeight(float waveHeight) {
        mWaveHeight = waveHeight;
        postInvalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getWidth();
        mHeight = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawWave(canvas);
    }

    private void drawWave(Canvas canvas) {
        //一个完整波的高度

        //一个完整波的宽度
        float waveWidth = mWidth - 30;

        //第一种
        float halfWave = waveWidth*0.6f;
        float y = mWaveHeight *0.5f;
        Path path = createWavePath(halfWave,y,-halfWave*0.6f);
        canvas.drawPath(path,mPaint);

        //第二种
        halfWave = waveWidth*0.55f;
        y = mWaveHeight *0.5f;
        path = createWavePath(halfWave,y,-halfWave);
        canvas.drawPath(path,mPaint);

        //第三种
        halfWave = waveWidth*0.7f;
        y = mWaveHeight *0.5f;
        path = createWavePath(halfWave,y,0);
        canvas.drawPath(path,mPaint);

    }

    private Path createWavePath(float halfWave, float startY, float xOffset){
        Path path = new Path();
        path.moveTo(xOffset,startY);

        path.rQuadTo(halfWave/2,-2*startY,halfWave,0);
        path.rQuadTo(halfWave/2,2*startY,halfWave,0);
        path.rQuadTo(halfWave/2,-2*startY,halfWave,0);
        path.rQuadTo(halfWave/2,2*startY,halfWave,0);

        path.lineTo(mWidth,mHeight);
        path.lineTo(0,mHeight);
        path.close();
        return path;
    }



//    public void startWave(){
//
//        ValueAnimator animator = ValueAnimator.ofFloat(0,mWaveLen);
//        animator.setDuration(2000);
//        animator.setRepeatCount(ValueAnimator.INFINITE);
//        animator.setInterpolator(new LinearInterpolator());
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                mXOffset = (float) animation.getAnimatedValue();
//                postInvalidate();
//            }
//        });
//
//        animator.start();
//
//    }



}
