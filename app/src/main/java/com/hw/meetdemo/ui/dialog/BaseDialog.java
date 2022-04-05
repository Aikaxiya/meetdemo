package com.hw.meetdemo.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.WindowManager;

import com.hw.meetdemo.R;
import com.hw.meetdemo.base.AppManager;

public class BaseDialog {
    /**
     * 显示弹窗
     */
    protected static Dialog showDialog(View dialogView) {
        return showDialog(dialogView, false, false);
    }

    /**
     * 显示弹窗
     */
    protected static Dialog showDialog(View dialogView, boolean isTouchOutside, boolean isCancelable) {
        return showDialog(dialogView, isTouchOutside, isCancelable, null, null);
    }

    /**
     * 显示弹窗
     *
     * @param dialogView        dialog显示的页面
     * @param isTouchOutside    是否允许点击外围区域消失
     * @param isCancelable      是否点击返回键消失
     * @param onDismissListener dismiss监听
     * @param onCancelListener  cancel监听
     * @return dialog对象
     */
    protected static DialogChild showDialog(View dialogView, boolean isTouchOutside, boolean isCancelable, DialogInterface.OnDismissListener onDismissListener, DialogInterface.OnCancelListener onCancelListener) {
        Activity currentActivity = AppManager.getInstance().currentActivity();

        if (currentActivity != null && !currentActivity.isFinishing() && !currentActivity.isDestroyed()) {
            DialogChild dialog = new DialogChild(currentActivity, R.style.BaseDialog);
            //设置点击屏幕不消失
            dialog.setCanceledOnTouchOutside(isTouchOutside);
            dialog.setOnDismissListener(onDismissListener);
            //设置点击返回键不消失
            dialog.setCancelable(isCancelable);
            dialog.setOnCancelListener(onCancelListener);
            //设置View
            dialog.setContentView(dialogView);
            //显示弹窗
            dialog.show();
            //返回Dialog
            return dialog;
        } else {
            return null;
        }
    }

    /**
     * 显示弹窗，可设置宽高
     */
    protected static Dialog showDialog(View dialogView, float width, float height) {
        return showDialog(dialogView, false, false, width, height);
    }

    /**
     * 显示弹窗，可设置宽高
     */
    protected static Dialog showDialog(View dialogView, boolean isTouchOutside, boolean isCancelable, float width, float height) {
        return showDialog(dialogView, isTouchOutside, isCancelable, null, null, width, height);
    }

    /**
     * 显示弹窗，可设置宽高
     *
     * @param dialogView        元素
     * @param isTouchOutside    点击屏幕是否消失
     * @param isCancelable      点击返回键是否消失
     * @param onDismissListener 关闭回调
     * @param onCancelListener  取消回调
     * @param width             宽
     * @param height            高
     * @return Dialog
     */
    protected static Dialog showDialog(View dialogView, boolean isTouchOutside, boolean isCancelable, DialogInterface.OnDismissListener onDismissListener, DialogInterface.OnCancelListener onCancelListener, float width, float height) {
        Dialog dialog = showDialog(dialogView, isTouchOutside, isCancelable, onDismissListener, onCancelListener);
        if (dialog != null) {
            //设置宽高
            WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
            layoutParams.width = (int) width;
            layoutParams.height = (int) height;
            dialog.getWindow().setAttributes(layoutParams);
        }

        //返回Dialog
        return dialog;
    }

}
