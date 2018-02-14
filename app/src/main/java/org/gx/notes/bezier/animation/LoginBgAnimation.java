package org.gx.notes.bezier.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * 自定义登录界面的矩阵缩放效果动画
 * 通过Camera类，加上动画的插值器变化率更改xyz三个方法的运动轨迹，达到效果
 * Created by Administrator on 2018/2/2 0002.
 */

public class LoginBgAnimation extends Animation {

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        Matrix matrix = t.getMatrix();
        Camera camera = new Camera();
        camera.save();
        camera.translate(-100*interpolatedTime,50*interpolatedTime,120*interpolatedTime);
        camera.getMatrix(matrix);
        camera.restore();

    }


    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }
}
