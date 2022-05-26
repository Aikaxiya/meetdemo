package com.hw.mediasoup.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import com.hw.mediasoup.R;
import com.hw.mediasoup.databinding.ViewPeerBinding;
import com.hw.mediasoup.lib.PeerConnectionUtils;
import com.hw.mediasoup.lib.RoomClient;
import com.hw.mediasoup.vm.PeerProps;

import org.webrtc.RendererCommon;
import org.webrtc.SurfaceViewRenderer;

public class PeerView extends RelativeLayout {

    public PeerView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PeerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PeerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PeerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    ViewPeerBinding mBinding;

    private void init(Context context) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_peer, this, true);
        mBinding.peerView.videoRenderer.init(PeerConnectionUtils.getEglContext(), null);
    }

    public void setProps(PeerProps props, RoomClient roomClient) {
        // set view model into included layout
        mBinding.peerView.setPeerViewProps(props);

        // register click listener.
        mBinding.peerView.info.setOnClickListener(
                view -> {
                    Boolean showInfo = props.getShowInfo().get();
                    props.getShowInfo().set(showInfo != null && showInfo ? Boolean.FALSE : Boolean.TRUE);
                });

        mBinding.peerView.stats.setOnClickListener(
                view -> {
                    // TODO(HaiyangWU): Handle inner click event;
                });

        // set view model
        mBinding.setPeerProps(props);
    }

    public void close() {
        if (mBinding.peerView.videoRenderer != null) {
            mBinding.peerView.videoRenderer.clearImage();
            mBinding.peerView.videoRenderer.release();
        }
    }

    public void clearImage() {
        mBinding.peerView.videoRenderer.clearImage();
    }

    public void setMirror(boolean mirror) {
        mBinding.peerView.videoRenderer.setMirror(mirror);
    }

    public void setScanType(RendererCommon.ScalingType scanType) {
        SurfaceViewRenderer surfaceViewRenderer = mBinding.peerView.videoRenderer;
        surfaceViewRenderer.setScalingType(scanType);
    }
}
