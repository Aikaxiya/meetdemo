package com.hw.meetdemo.databind;

/**
 * @author: Andrew chen
 * @date: 2022/2/15 10:53
 * @description:
 */
public class RoomBean {

    //房间名
    public static String roomName = "房间102";
    //房间号
    public static String roomCode = "";
    //server服务器地址
    public static String mediaSoupServerIp = "192.168.0.100";
    //public static String mediaSoupServerIp = "v3demo.mediasoup.org";

    public static final String REGISTER = "register";//注册
    public static final String REGISTER_RESPONSE = "register_response";//注册回复
    public static final int RESPONSE_SUCCEED = 1;//1成功
    public static final String HANGUP = "hangUp";//挂断
    public static final String TEXTALERT = "textAlert";//文字告警
    public static final String SPEAKALERT = "speakAlert";//喊话告警
    public static final String AUDIOVIDEOCONTROL = "audioVideoControl";//音视频控制
    public static final String ISFORCEOFFLINE = "isForceOffLine";//强制断线
    public static final String ISPAUSE = "isPause";//强制断线
    public static final String HJCONFIRM = "hjConfirm";//会见前确认
    public static final String FINISHHJ = "finishHj";//有人退出时记录倒计时
    public static final String editPlanDuration = "editPlanDuration";//修改会见时长

    //webSocket
    public static String webSocketUrl = "ws://192.168.0.245:8080/FamilyMeetingWebSocket";
    public static String hjSysNum = "220217002";
    public static String FROM = "";
    public static String TO = "";

    //当前是家属还是对象 1家属 2对象
    public static int appointMethod = 3;
    //会见方式 2单向视频会见，只有家属端可以看以看到对象端视频，对象端看不到家属端视频 3双向视频会见
    public static int currentRole = 0;

    public static final String ToastSPHJFailed01Message = "您输入的家属会见编号有误或不存在，请重新输入或联系民警！";
    public static final String ToastSPHJFailed02Message = "您输入的在押人员编号有误或不存在，请重新输入或联系民警！";
    public static final String ToastSPHJFailed03Message = "您已被强制断线！";
    public static final String ToastSPHJFailed05Message = "家属会见已暂停！";
    public static final String ToastSPHJFailed04Message = "家属会见已结束！";
    public static final String ToastSPHJHangUpMessage01 = "您已挂断，视频会见结束";
    public static final String ToastSPHJHangUpMessage02 = "对方拒绝了你的视频会见请求";
    public static final String ToastSPHJHangUpMessage03 = "对方已挂断";
    public static final String ToastSPHJHangUpMessage04 = "操作过于频繁,请稍后~";
    public static final String ToastSPHJMessage01 = "视频会见已取消";
    public static final String ToastSPHJMessage02 = "民警审核通过，会见开始";
    public static final String ToastSPHJMessage03 = "本次会见已结束，感谢您的使用";
    public static final String ToastSPHJMessage04 = "会见身份确认成功，请进行视频通话";

    public static final long SPHJ_Interval = 100;//挂断后延迟回到首页时间

    public static Boolean audioControl = true; //视频控制
    public static Boolean videoControl = true; //视频控制
    public static Boolean isForceOffLine = false; //强制断线
    public static Boolean isPause = false; //暂停
    public static int state = 1; //会见状态
    public static String hjMeeting_id = null;
    public static String hjAudit_id = null;
    public static long countDown = 0; //视频会见倒计时开始时间
    public static long countDownTime = 0; //视频会见倒计时
    public static long SPHJ_LastMilliSecond = -1;

}
