package com.hw.mediasoup.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.hw.mediasoup.R;
import com.hw.mediasoup.databinding.ViewMeBindingImpl;
import com.hw.mediasoup.lib.PeerConnectionUtils;
import com.hw.mediasoup.lib.RoomClient;
import com.hw.mediasoup.vm.MeProps;

import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoTrack;

public class MeView extends RelativeLayout {

    public MeView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public MeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    ViewMeBindingImpl mBinding;

    private void init(Context context) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_me, this, true);
        mBinding.peerView.videoRenderer.init(PeerConnectionUtils.getEglContext(), null);
    }

    public void setProps(MeProps props, final RoomClient roomClient) {

        // set view model.
        mBinding.peerView.setPeerViewProps(props);

        // register click listener.
        mBinding.peerView.info.setOnClickListener(view -> {
            Boolean showInfo = props.getShowInfo().get();
            props.getShowInfo().set(showInfo != null && showInfo ? Boolean.FALSE : Boolean.TRUE);
        });

        mBinding.peerView.meDisplayName.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                roomClient.changeDisplayName(textView.getText().toString().trim());
                return true;
            }
            return false;
        });
        mBinding.peerView.stats.setOnClickListener(view -> {
        });

        mBinding.peerView.videoRenderer.setZOrderMediaOverlay(true);

        // set view model.
        mBinding.setMeProps(props);

        // register click listener.
        mBinding.mic.setOnClickListener(view -> {
            if (MeProps.DeviceState.ON.equals(props.getMicState().get())) {
                roomClient.muteMic();
            } else {
                roomClient.unmuteMic();
            }
        });
        mBinding.cam.setOnClickListener(view -> {
            if (MeProps.DeviceState.ON.equals(props.getCamState().get())) {
                roomClient.disableCam();
            } else {
                roomClient.enableCam();
            }
        });
        mBinding.changeCam.setOnClickListener(view -> roomClient.changeCam());
        mBinding.share.setOnClickListener(view -> {
            if (MeProps.DeviceState.ON.equals(props.getShareState().get())) {
                roomClient.disableShare();
            } else {
                roomClient.enableShare();
            }
        });
    }

    public void close() {
        mBinding.peerView.videoRenderer.clearImage();
        mBinding.peerView.videoRenderer.release();
    }

    public void setMirror(boolean mirror) {
        mBinding.peerView.videoRenderer.setMirror(mirror);
    }
}
