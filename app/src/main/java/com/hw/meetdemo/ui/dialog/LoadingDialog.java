package com.hw.meetdemo.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import com.hw.meetdemo.R;
import com.hw.meetdemo.base.BaseApplication;

/**
 * 加载弹窗
 */
public class LoadingDialog extends BaseDialog {
    //加载弹窗
    private static Dialog loadingDialog = null;

    /**
     * 显示加载弹窗
     */
    public static void showLoadingDialog() {
        showLoadingDialog(false, false);
    }

    /**
     * 显示加载弹窗
     *
     * @param isTouchOutside
     * @param isCancelable
     */
    public static void showLoadingDialog(boolean isTouchOutside, boolean isCancelable) {
        showLoadingDialog(isTouchOutside, isCancelable, null, null);
    }

    /**
     * 显示加载弹窗
     *
     * @param isTouchOutside
     * @param isCancelable
     * @param onDismissListener
     * @param onCancelListener
     */
    public static void showLoadingDialog(boolean isTouchOutside, boolean isCancelable, DialogInterface.OnDismissListener onDismissListener, DialogInterface.OnCancelListener onCancelListener) {
        //获取界面
        View dialogView = LayoutInflater.from(BaseApplication.getContext()).inflate(R.layout.loading_dialog, null);
        //初始化控件
        //显示弹窗
        if (loadingDialog == null) {
            loadingDialog = showDialog(dialogView, isTouchOutside, isCancelable, new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (onDismissListener != null) {
                        onDismissListener.onDismiss(dialog);
                    }
                }
            }, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (onCancelListener != null) {
                        onCancelListener.onCancel(dialog);
                    }
                }
            });
        }
    }

    /**
     * 关闭加载弹窗
     */
    public static void closeLoadingDialog() {
        if (loadingDialog != null) {
            //防止Activity消失
            try {
                loadingDialog.cancel();
            } catch (Exception ignore) {
            }
            loadingDialog = null;
        }
    }
}
