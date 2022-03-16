package com.hw.meetdemo.ui;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * @author: 13105
 * @date: 2022/3/16 14:36
 * @description:
 */
public class Rotate3dAnimation extends Animation {

    // 中心点
    private final float mCenterX;
    private final float mCenterY;
    // 3D变换处理camera（不是摄像头）
    private final Camera mCamera = new Camera();

    /**
     * @param centerX 翻转中心x坐标
     * @param centerY 翻转中心y坐标
     */
    public Rotate3dAnimation(float centerX, float centerY) {
        mCenterX = centerX;
        mCenterY = centerY;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        // 生成中间角度
        final Camera camera = mCamera;
        final Matrix matrix = t.getMatrix();
        camera.save();
        camera.rotateY(180);
        // 取得变换后的矩阵
        camera.getMatrix(matrix);
        camera.restore();
        matrix.preTranslate(-mCenterX, -mCenterY);
        matrix.postTranslate(mCenterX, mCenterY);
    }
}
