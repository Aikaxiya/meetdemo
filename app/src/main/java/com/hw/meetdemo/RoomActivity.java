package com.hw.meetdemo;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.alibaba.fastjson.JSONObject;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.hw.mediasoup.lib.PeerConnectionUtils;
import com.hw.mediasoup.lib.RoomClient;
import com.hw.mediasoup.lib.RoomOptions;
import com.hw.mediasoup.lib.UrlFactory;
import com.hw.mediasoup.lib.lv.RoomStore;
import com.hw.mediasoup.lib.model.Notify;
import com.hw.mediasoup.lib.model.Peer;
import com.hw.mediasoup.lib.model.Peers;
import com.hw.mediasoup.view.MeView;
import com.hw.mediasoup.view.PeerView;
import com.hw.mediasoup.vm.EdiasProps;
import com.hw.mediasoup.vm.MeProps;
import com.hw.mediasoup.vm.PeerProps;
import com.hw.mediasoup.vm.RoomProps;
import com.hw.meetdemo.adapter.MeetMemberRecycleAdapter;
import com.hw.meetdemo.base.AppManager;
import com.hw.meetdemo.base.BaseActivity;
import com.hw.meetdemo.databind.RecordParam;
import com.hw.meetdemo.databind.RoomBean;
import com.hw.meetdemo.databind.RoomObserver;
import com.hw.meetdemo.databinding.MediasoupActivityBinding;
import com.hw.meetdemo.ui.Rotate3dAnimation;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;

/**
 * @author: Andrew chen
 * @date: 2022/2/15 10:56
 * @description:
 */
public class RoomActivity extends BaseActivity {

    private final String TAG = RoomActivity.class.getSimpleName();

    private MediasoupActivityBinding mediasoupActivityBinding;
    public RoomObserver roomObserver = new RoomObserver();
    private RoomOptions mOptions;
    private RoomStore mRoomStore;
    private RoomClient mRoomClient;
    private final ExecutorService recordService = Executors.newSingleThreadExecutor();
    private final List<PeerProps> memberProps = new ArrayList<>();
    private MeetMemberRecycleAdapter meetMemberRecycleAdapter = null;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediasoupActivityBinding = DataBindingUtil.setContentView(this, R.layout.mediasoup_activity);
        mediasoupActivityBinding.setRoomObserver(roomObserver);
        mediasoupActivityBinding.memberParent.post(() -> {
            int width = mediasoupActivityBinding.memberParent.getWidth();
            int height = mediasoupActivityBinding.memberParent.getHeight();
            //生成recycleView
            FlexboxLayoutManager manager = new FlexboxLayoutManager(this);
            manager.setFlexDirection(FlexDirection.ROW);
            manager.setJustifyContent(JustifyContent.CENTER);

            mediasoupActivityBinding.memberContainerRecycle.setLayoutManager(manager);
            meetMemberRecycleAdapter = new MeetMemberRecycleAdapter(this, memberProps, mRoomClient, width, height);
            mediasoupActivityBinding.memberContainerRecycle.setAdapter(meetMemberRecycleAdapter);
            //停止复用
            mediasoupActivityBinding.memberContainerRecycle.getRecycledViewPool().setMaxRecycledViews(0, 0);
            //创建room
            createRoom();
            //验证权限
            checkPermission();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void createRoom() {
        //加载房间配置
        mOptions = new RoomOptions();
        loadRoomConfig();
        //初始化房间客户端
        mRoomStore = new RoomStore();
        initRoomClient();
        getViewModelStore().clear();
        //初始化页面
        initViewModel();
    }

    private void loadRoomConfig() {
        // 房间初始配置
        mOptions.setProduce(true);//可以发送音视频。
        mOptions.setConsume(true);//可以接收音视频。
        mOptions.setForceTcp(true);//启用tcp传输
        if (StrUtil.isNotBlank(RoomBean.mediaSoupServerIp)) {
            UrlFactory.setHOSTNAME(RoomBean.mediaSoupServerIp);
        }
        // 摄像头(背面：back 前置：front)
        PeerConnectionUtils.setPreferCameraFace("front");
    }

    private void initRoomClient() {
        mRoomClient = new RoomClient(this, mRoomStore, "familyMeeting_" + RoomBean.roomCode, RoomBean.FROM, RoomBean.FROM, true, false, mOptions);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initViewModel() {
        EdiasProps.Factory factory = new EdiasProps.Factory(getApplication(), mRoomStore);
        RoomProps roomProps = new ViewModelProvider(this, factory).get(RoomProps.class);
        roomProps.connect(this);
        mediasoupActivityBinding.setRoomProps(roomProps);
        //自己
        MeProps meProps = new ViewModelProvider(this, factory).get(MeProps.class);
        meProps.connect(this);
        mediasoupActivityBinding.meVideo.setProps(meProps, mRoomClient);
        // 禁用麦克风等按钮
        findViewById(R.id.mic).setVisibility(View.GONE);
        findViewById(R.id.cam).setVisibility(View.GONE);
        findViewById(R.id.change_cam).setVisibility(View.GONE);
        //禁用麦克风
        mediasoupActivityBinding.disabledMic.setEnabled(false);
        roomObserver.disableMic.set(false);
        AtomicReference<Boolean> isMute = new AtomicReference<>(false);
        mRoomStore.getProducers().observe(this, producers -> {
            //监听音频 如有则静音
            if (!isMute.get() && producers.filter("audio") != null && producers.filter("audio").getScore() != null) {
                mRoomClient.muteMic();
                isMute.set(true);
            }
        });
        //麦克风静音
        mediasoupActivityBinding.muteMic.setOnClickListener(v -> {
            mRoomClient.muteMic();
            roomObserver.muteMic.set(true);
            Toast.makeText(this, "已静音", Toast.LENGTH_SHORT).show();
        });
        //麦克风解除静音
        mediasoupActivityBinding.unMuteMic.setOnClickListener(v -> {
            mRoomClient.unmuteMic();
            roomObserver.muteMic.set(false);
            Toast.makeText(this, "已取消静音", Toast.LENGTH_SHORT).show();
        });
        roomObserver.openCamera.set(false);
        //摄像头开关
        mediasoupActivityBinding.openCamera.setOnClickListener(v -> {
            mRoomClient.enableCam();
            roomObserver.openCamera.set(false);
        });
        mediasoupActivityBinding.closeCamera.setOnClickListener(v -> {
            mRoomClient.disableCam();
            roomObserver.openCamera.set(true);
        });
        mediasoupActivityBinding.changeCamera.setOnClickListener(v -> {
            mRoomClient.changeCam();
        });
        //分享屏幕
        mediasoupActivityBinding.shareScreen.setOnClickListener(v -> {
            mRoomClient.enableShare();
            roomObserver.enableShare.set(true);
            Toast.makeText(this, "开发中...", Toast.LENGTH_SHORT).show();
        });
        mediasoupActivityBinding.noShareScreen.setOnClickListener(v -> {
            mRoomClient.disableShare();
            roomObserver.enableShare.set(false);
            Toast.makeText(this, "开发中...", Toast.LENGTH_SHORT).show();
        });
        //是否禁用
        final AtomicBoolean hangUp = new AtomicBoolean(false);
        //挂断
        mediasoupActivityBinding.hangUp.setOnClickListener(v -> {
            if (hangUp.get()) {
                Toast.makeText(this, RoomBean.ToastSPHJHangUpMessage04, Toast.LENGTH_SHORT).show();
            } else {
                //延迟跳转界面
                new Handler(Looper.myLooper()).postDelayed(() -> {
                    if (activity != AppManager.getInstance().currentActivity()) return;
                    AppManager.getInstance().finishSomeActivity(MenuActivity.class);
                    hangUp.set(true);
                }, RoomBean.SPHJ_Interval);
            }
        });
        //开始录像
        AtomicReference<Boolean> connected = new AtomicReference<>(true);
        mRoomStore.getProducers().observe(this, producers -> {
            //监听视频 如有则开始录制
            if (connected.get() && producers.filter("video") != null && producers.filter("video").getScore() != null) {
//                startVideoRecord();
                connected.set(false);
            }
        });
        mRoomStore.getPeers().observe(this, this::setPeerViewLayout);
        // 通知
        final Observer<Notify> notifyObserver = notify -> {
            if (notify == null) return;
            if ("error".equals(notify.getType())) {
                Log.d(TAG, "mediaSoupClient发生错误: " + notify.getText());
            }
            Toast.makeText(this, notify.getText(), Toast.LENGTH_SHORT).show();
        };
        mRoomStore.getNotify().observe(this, notifyObserver);
    }

    public void checkPermission() {
        PermissionHandler permissionHandler = new PermissionHandler() {

            @Override
            public void onGranted() {
                Log.d(TAG, mRoomClient + "许可授予");
                if (mRoomClient != null) {
                    mRoomClient.join();
                }
                connectionWebSocket();//连接WebSocket
                faceVerify();//人脸检测
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                super.onDenied(context, deniedPermissions);
                Log.d(TAG, mRoomClient + "拒绝授予");
            }
        };
        String[] permissions = {
                Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        String rationale = "请提供权限";
        Permissions.Options options = new Permissions.Options().setRationaleDialogTitle("Info").setSettingsDialogTitle("Warning");
        Permissions.check(this, permissions, rationale, options, permissionHandler);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyRoom();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        destroyRoom();
    }

    public void connectionWebSocket() {

    }

    public void faceVerify() {

    }

    //mediaSoup断开
    private void destroyRoom() {
        if (mRoomClient != null) {
            mRoomClient.close();
            mRoomClient = null;
        }
        if (mRoomStore != null) {
            mRoomStore = null;
        }
        mediasoupActivityBinding.meVideo.close();
    }

    //发送录像请求
    private void startVideoRecord() {
        recordService.submit(() -> {
            String url = StrUtil.format("https://{}:4443/rooms/familyMeeting_{}/startRecord", RoomBean.mediaSoupServerIp, RoomBean.roomCode);
            RecordParam param = RecordParam.builder()
                    .filePath(RoomBean.hjSysNum)
                    .peerIds(Collections.singletonList(RoomBean.FROM))
                    .build();
            HttpRequest httpRequest = HttpUtil.createPost(url);
            HttpResponse response = httpRequest.body(JSONObject.toJSONString(param)).execute();
            if (!response.isOk()) {
                runOnUiThread(() -> Toast.makeText(this, "服务器连接失败,请联系管理员", Toast.LENGTH_SHORT).show());
            }
        });
    }

    public void setPeerViewLayout(Peers peers) {
        memberProps.clear();
        List<Peer> peerList = peers.getAllPeers();
        for (Peer peer : peerList) {
            PeerProps peerProps = new PeerProps(getApplication(), mRoomStore);
            peerProps.connect(this, peer.getId());
            memberProps.add(peerProps);
        }
        meetMemberRecycleAdapter.notifyDataSetChanged();
    }
}
