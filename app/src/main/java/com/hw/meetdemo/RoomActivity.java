package com.hw.meetdemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
import com.hw.meetdemo.util.CameraUtil;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.ScreenCapturerAndroid;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoDecoderFactory;
import org.webrtc.VideoEncoderFactory;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

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
    private SurfaceTextureHelper surfaceTextureHelper;
    //?????????????????????
    private VideoSource screenVideoSource;
    private VideoCapturer videoCapturer;
    //???????????????
    private int cameraCount = 0;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cameraCount = CameraUtil.judgeCameraCount(this);
        mediasoupActivityBinding = DataBindingUtil.setContentView(this, R.layout.mediasoup_activity);
        mediasoupActivityBinding.setRoomObserver(roomObserver);
        mediasoupActivityBinding.memberParent.post(() -> {
            int width = mediasoupActivityBinding.memberParent.getWidth();
            int height = mediasoupActivityBinding.memberParent.getHeight();
            //??????recycleView
            FlexboxLayoutManager manager = new FlexboxLayoutManager(this);
            manager.setFlexDirection(FlexDirection.ROW);
            manager.setJustifyContent(JustifyContent.CENTER);
            mediasoupActivityBinding.memberContainerRecycle.setLayoutManager(manager);
            meetMemberRecycleAdapter = new MeetMemberRecycleAdapter(this, memberProps, mRoomClient, width, height);
            mediasoupActivityBinding.memberContainerRecycle.setAdapter(meetMemberRecycleAdapter);
            //????????????
            mediasoupActivityBinding.memberContainerRecycle.getRecycledViewPool().setMaxRecycledViews(0, 0);
            //??????room
            createRoom();
            //????????????
            checkPermission();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void createRoom() {
        //??????????????????
        mOptions = new RoomOptions();
        loadRoomConfig();
        //????????????????????????
        mRoomStore = new RoomStore();
        initRoomClient();
        getViewModelStore().clear();
        //???????????????
        initViewModel();
    }

    private void loadRoomConfig() {
        // ??????????????????
        mOptions.setProduce(true);//????????????????????????
        mOptions.setConsume(true);//????????????????????????
        mOptions.setForceTcp(true);//??????tcp??????
        if (StrUtil.isNotBlank(RoomBean.mediaSoupServerIp)) {
            UrlFactory.setHOSTNAME(RoomBean.mediaSoupServerIp);
        }
        // ?????????(?????????back ?????????front)
        switch (cameraCount) {
            case 1:
                PeerConnectionUtils.setPreferCameraFace("back");
                break;
            case 2:
                PeerConnectionUtils.setPreferCameraFace("front");
                break;
            default:
                throw new IllegalArgumentException("??????????????????????????????");
        }
    }

    private void initRoomClient() {
        mRoomClient = new RoomClient(this, mRoomStore, RoomBean.roomCode, RoomBean.FROM, RoomBean.FROM, true, false, mOptions);
    }

    private MeProps meProps;

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initViewModel() {
        EdiasProps.Factory factory = new EdiasProps.Factory(getApplication(), mRoomStore);
        RoomProps roomProps = new ViewModelProvider(this, factory).get(RoomProps.class);
        roomProps.connect(this);
        mediasoupActivityBinding.setRoomProps(roomProps);
        //??????
        meProps = new ViewModelProvider(this, factory).get(MeProps.class);
        meProps.connect(this);
        mediasoupActivityBinding.meVideo.setProps(meProps, mRoomClient);
        mediasoupActivityBinding.meVideo.setMirror(true);
        // ????????????????????????
        findViewById(R.id.mic).setVisibility(View.GONE);
        findViewById(R.id.cam).setVisibility(View.GONE);
        findViewById(R.id.change_cam).setVisibility(View.GONE);
        //???????????????
        mediasoupActivityBinding.disabledMic.setEnabled(false);
        roomObserver.disableMic.set(false);
        AtomicReference<Boolean> isMute = new AtomicReference<>(false);
        mRoomStore.getProducers().observe(this, producers -> {
            //???????????? ???????????????
            if (!isMute.get() && producers.filter("audio") != null && producers.filter("audio").getScore() != null) {
                mRoomClient.muteMic();
                isMute.set(true);
            }
        });
        //???????????????
        mediasoupActivityBinding.muteMic.setOnClickListener(v -> {
            mRoomClient.muteMic();
            roomObserver.muteMic.set(true);
            Toast.makeText(this, "?????????", Toast.LENGTH_SHORT).show();
        });
        //?????????????????????
        mediasoupActivityBinding.unMuteMic.setOnClickListener(v -> {
            mRoomClient.unmuteMic();
            roomObserver.muteMic.set(false);
            Toast.makeText(this, "???????????????", Toast.LENGTH_SHORT).show();
        });
        roomObserver.openCamera.set(false);
        //???????????????
        mediasoupActivityBinding.openCamera.setOnClickListener(v -> {
            mediasoupActivityBinding.meVideo.setProps(meProps, mRoomClient);
            mRoomClient.enableCam();
            roomObserver.openCamera.set(false);
        });
        mediasoupActivityBinding.closeCamera.setOnClickListener(v -> {
            mediasoupActivityBinding.meVideo.setProps(null, mRoomClient);
            mRoomClient.disableCam();
            roomObserver.openCamera.set(true);
        });
        mediasoupActivityBinding.changeCamera.setOnClickListener(v -> mRoomClient.changeCam());
        //????????????
        mediasoupActivityBinding.shareScreen.setOnClickListener(v -> {
            //mRoomClient.enableShare();
            roomObserver.openCamera.set(true);
            roomObserver.enableShare.set(true);
            startScreenShare();
        });
        mediasoupActivityBinding.noShareScreen.setOnClickListener(v -> {
            //mRoomClient.disableShare();
            roomObserver.openCamera.set(true);
            roomObserver.enableShare.set(false);
            Toast.makeText(this, "?????????...", Toast.LENGTH_SHORT).show();
            stopScreenShare();
        });
        //????????????
        final AtomicBoolean hangUp = new AtomicBoolean(false);
        //??????
        mediasoupActivityBinding.hangUp.setOnClickListener(v -> {
            if (hangUp.get()) {
                Toast.makeText(this, RoomBean.ToastSPHJHangUpMessage04, Toast.LENGTH_SHORT).show();
            } else {
                //??????????????????
                new Handler(Looper.myLooper()).postDelayed(() -> {
                    if (activity != AppManager.getInstance().currentActivity()) return;
                    AppManager.getInstance().finishSomeActivity(MenuActivity.class);
                    hangUp.set(true);
                }, RoomBean.SPHJ_Interval);
            }
        });
        //????????????
        AtomicReference<Boolean> connected = new AtomicReference<>(true);
        mRoomStore.getProducers().observe(this, producers -> {
            //???????????? ?????????????????????
            if (connected.get() && producers.filter("video") != null && producers.filter("video").getScore() != null) {
                //startVideoRecord();
                connected.set(false);
            }
        });
        //??????
        mRoomStore.getPeers().observe(this, this::setPeerViewLayout);
        // ??????
        final Observer<Notify> notifyObserver = notify -> {
            if (notify == null) return;
            if ("error".equals(notify.getType())) {
                Log.d(TAG, "mediaSoupClient????????????: " + notify.getText());
            }
            Toast.makeText(this, notify.getText(), Toast.LENGTH_SHORT).show();
        };
        mRoomStore.getNotify().observe(this, notifyObserver);
    }


    public void checkPermission() {
        PermissionHandler permissionHandler = new PermissionHandler() {

            @Override
            public void onGranted() {
                Log.d(TAG, mRoomClient + "????????????");
                if (mRoomClient != null) {
                    mRoomClient.join();
                }
                connectWebSocket();//??????WebSocket
                faceVerify();//????????????
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                super.onDenied(context, deniedPermissions);
                Log.d(TAG, mRoomClient + "????????????");
            }
        };
        String[] permissions = {
                Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        String rationale = "???????????????";
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK) {
            screenCapture(data);
        }
    }

    public void connectWebSocket() {

    }

    public void faceVerify() {

    }

    //mediaSoup??????
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

    //??????????????????
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
                runOnUiThread(() -> Toast.makeText(this, "?????????????????????,??????????????????", Toast.LENGTH_SHORT).show());
            }
        });
    }

    //private final EglBase eglBase = EglBase.create();

    private PeerConnectionFactory createPeerConnectionFactory(Context context) {
        final VideoEncoderFactory encoderFactory;
        final VideoDecoderFactory decoderFactory;
        //????????????H264?????????(??????????????????), Vp8?????????????????????
        encoderFactory = new DefaultVideoEncoderFactory(PeerConnectionUtils.getEglContext(), false, true);
        decoderFactory = new DefaultVideoDecoderFactory(PeerConnectionUtils.getEglContext());
        PeerConnectionFactory.initialize(PeerConnectionFactory.InitializationOptions.builder(context).setEnableInternalTracer(true).createInitializationOptions());
        PeerConnectionFactory.Builder builder = PeerConnectionFactory.builder().setVideoEncoderFactory(encoderFactory).setVideoDecoderFactory(decoderFactory);
        builder.setOptions(null);
        return builder.createPeerConnectionFactory();
    }

    //??????????????????
    private void startScreenShare() {
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        if (mediaProjectionManager == null) {
            runOnUiThread(() -> Toast.makeText(this, "?????????????????????", Toast.LENGTH_SHORT).show());
            return;
        }
        Intent intent = mediaProjectionManager.createScreenCaptureIntent();
        startActivityForResult(intent, 200);
    }

    private void screenCapture(Intent captureIntent) {
        //??????VideoCapture
        videoCapturer = new ScreenCapturerAndroid(captureIntent, new MediaProjection.Callback() {
            @Override
            public void onStop() {
                super.onStop();
            }
        });
        //??????peerConnectionFactory
        PeerConnectionFactory peerConnectionFactory = createPeerConnectionFactory(this);
        //???videoSource?????????mVideoCapture????????????
        surfaceTextureHelper = SurfaceTextureHelper.create("CaptureThread2", PeerConnectionUtils.getEglContext());
        //???????????????????????????
        screenVideoSource = peerConnectionFactory.createVideoSource(videoCapturer.isScreencast());
        videoCapturer.initialize(surfaceTextureHelper, getApplicationContext(), screenVideoSource.getCapturerObserver());
        //??????????????????????????????wsa????????????
        videoCapturer.startCapture(1200, 900, 30);
       /* //??????
        MediaConstraints mediaConstraints = new MediaConstraints();
        mediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googEchoCancellation", "true"));
        mediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googEchoCancellation2", "true"));
        mediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googDAEchoCancellation", "true"));
        mediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googTypingNoiseDetection", "true"));
        mediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googAutoGainControl", "false"));
        mediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googAutoGainControl2", "false"));
        //????????????
        mediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googNoiseSuppression", "true"));
        mediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googNoiseSuppression2", "true"));
        //????????????
        mediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googAudioMirroring", "false"));
        mediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googHighpassFilter", "true"));
        AudioSource audioSource = peerConnectionFactory.createAudioSource(mediaConstraints);
        AudioTrack screenAudioTrack = peerConnectionFactory.createAudioTrack("ARDAMSa0", audioSource);
        screenAudioTrack.setEnabled(true);
        screenAudioTrack.setVolume(10);*/
        //?????????
        VideoTrack screenVideoTrack = peerConnectionFactory.createVideoTrack("ARDAMSv1", screenVideoSource);
        PeerProps peerProps = new PeerProps(getApplication(), mRoomStore);
        peerProps.getVideoTrack().set(screenVideoTrack);
        mediasoupActivityBinding.screenVideo.setProps(peerProps, mRoomClient);
        mediasoupActivityBinding.meVideo.setProps(null, mRoomClient);
        mRoomClient.screenSharing(screenVideoTrack);
    }

    //??????????????????
    private void stopScreenShare() {
        if (surfaceTextureHelper != null) {
            surfaceTextureHelper.stopListening();
        }
        if (videoCapturer != null) {
            try {
                videoCapturer.stopCapture();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (screenVideoSource != null) {
            screenVideoSource.dispose();
        }
        mediasoupActivityBinding.screenVideo.clearImage();
        mRoomClient.stopScreenSharing();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("NotifyDataSetChanged")
    public void setPeerViewLayout(Peers peers) {
        //????????????????????????
        int lastPosition = memberProps.size() - 1;
        memberProps.clear();
        List<Peer> peerList = peers.getAllPeers();
        for (Peer peer : peerList) {
            PeerProps peerProps = new PeerProps(getApplication(), mRoomStore);
            peerProps.connect(this, peer.getId());
            memberProps.add(peerProps);
        }
        meetMemberRecycleAdapter.notifyItemRangeChanged(lastPosition, memberProps.size());
    }
}
