package com.hw.meetdemo.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import com.hw.meetdemo.MenuActivity;

import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * @author: Andrew chen
 * @date: 2022/2/16 10:26
 * @description:
 */
public class AppManager {
    private static AppManager instance;
    //(Java的Stack是线程安全的)
    private static Stack<Activity> activityStack;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static synchronized AppManager getInstance() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 打开Activity
     */
    public void startActivity(Class<?> cls) {
        currentActivity().startActivity(new Intent(currentActivity(), cls));
    }

    /**
     * 添加Activity到栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（栈中最后一个压入的）
     */
    public Activity currentActivity() {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        try {
            return activityStack.lastElement();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    /**
     * 结束当前Activity（栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = currentActivity();
        if (activity != null) {
            if (!activity.getClass().equals(MenuActivity.class)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activityStack != null) {
            if (activity != null) {
                activityStack.remove(activity);
                activity.finish();
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        if (activityStack != null) {
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    finishActivity(activity);
                }
            }
        }
    }


    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (activityStack != null) {
            for (int i = 0, size = activityStack.size(); i < size; i++) {
                if (null != activityStack.get(i)) {
                    activityStack.get(i).finish();
                }
            }
            activityStack.clear();
        }
    }

    /**
     * 结束一些Activity直到栈顶为指定Class
     */
    public void finishSomeActivity(Class<?> cls) {
        if (activityStack != null) {
            for (int i = activityStack.size() - 1, size = activityStack.size(); i >= 0; i--) {
                if (null != activityStack.get(i)) {
                    if (activityStack.get(i).getClass().equals(cls)) {
                        return;
                    } else {
                        activityStack.get(i).finish();
                        activityStack.remove(i);
                    }
                }
            }
        }
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        //退出应用程序
        finishAllActivity();
        //正常退出0，异常退出非0
        System.exit(0);
        //android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 重启应用程序
     */
    public void AppRestart(Context context) {
        //重启应用程序
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        currentActivity().startActivity(intent);
        //正常退出0，异常退出非0
        System.exit(0);
        //android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 重启设备(需要系统签名)
     */
    public void BootRestart(Context context) {
        //重启设备
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        powerManager.reboot("reboot");
    }
}
