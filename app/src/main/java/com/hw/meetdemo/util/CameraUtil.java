package com.hw.meetdemo.util;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.util.Log;

/**
 * @author: 13105
 * @date: 2022/3/17 14:16
 * @description:
 */
public class CameraUtil {

    public static final int LENS_FACING_FRONT_ID = 0;
    public static final int LENS_FACING_BACK_ID = 1;
    private static final String TAG = "CameraUtil";

    /**
     * @param context context
     * @return camera2 api里面，0表示前置摄像头,1表示摄像后置
     */
    public static int judgeCameraCount(Context context) {
        int ret = 2;
        try {
            CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            String[] cameraIdList = cameraManager.getCameraIdList();
            switch (cameraIdList.length) {
                case 0:
                    Log.e(TAG, "CameraUtil judgeCameraCount: 当前没有可用摄像头");
                    ret = -1;
                    break;
                case 1:
                    //只有一个摄像头的时候，无论接前摄还是后摄，id都是0，因此通过LENS_FACING来判断摄像头的位置
                    int face = cameraManager.getCameraCharacteristics(cameraIdList[0]).get(CameraCharacteristics.LENS_FACING);
                    if (face == LENS_FACING_FRONT_ID) {
                        ret = LENS_FACING_FRONT_ID;
                        Log.d(TAG, "CameraUtil judgeCameraCount: 当前只有1个前摄像头");
                    }
                    if (face == LENS_FACING_BACK_ID) {
                        ret = LENS_FACING_BACK_ID;
                        Log.d(TAG, "CameraUtil judgeCameraCount: 当前只有1个后摄像头");
                    }
                    break;
                case 2:
                    Log.d(TAG, "CameraUtil judgeCameraCount: 当前有2个可用摄像头");
                    break;
                default:
                    break;
            }
        } catch (CameraAccessException e) {
            Log.e(TAG, "CameraUtil judgeCameraCount: error is " + e.getMessage());
            e.printStackTrace();
        }
        return ret;
    }
}
