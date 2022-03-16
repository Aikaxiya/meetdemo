package com.hw.meetdemo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hw.mediasoup.lib.RoomClient;
import com.hw.mediasoup.lib.model.Info;
import com.hw.mediasoup.view.PeerView;
import com.hw.mediasoup.vm.PeerProps;
import com.hw.meetdemo.R;
import com.hw.meetdemo.databinding.MemberItemLayoutBinding;

import java.util.List;

/**
 * @author: 13105
 * @date: 2022/3/11 16:20
 * @description: 成员适配器
 */
public class MeetMemberRecycleAdapter extends RecyclerView.Adapter<MeetMemberRecycleAdapter.MemberViewHolder> {

    private final Context context;
    private final List<PeerProps> peerProps;
    private final RoomClient roomClient;
    private final int width, height;

    public MeetMemberRecycleAdapter(Context context, List<PeerProps> peerProps, RoomClient roomClient, int width, int height) {
        this.context = context;
        this.peerProps = peerProps;
        this.roomClient = roomClient;
        this.width = width;
        this.height = height;
    }

    static class MemberViewHolder extends RecyclerView.ViewHolder {
        MemberItemLayoutBinding memberBinding;

        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public MeetMemberRecycleAdapter.MemberViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        MeetMemberRecycleAdapter.MemberViewHolder viewHolder;
        MemberItemLayoutBinding memberBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.member_item_layout, null, false);
        viewHolder = new MemberViewHolder(memberBinding.getRoot());
        viewHolder.memberBinding = memberBinding;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MeetMemberRecycleAdapter.MemberViewHolder memberViewHolder, int position) {
        RelativeLayout.LayoutParams lp = calcSize(peerProps.size());
        PeerView peerView = memberViewHolder.memberBinding.member;
        peerView.setLayoutParams(lp);
        PeerProps peerProp = this.peerProps.get(position);
        peerView.setProps(peerProp, roomClient);
        Info info = peerProp.getPeer().get();
        if (info != null) {
            memberViewHolder.memberBinding.displayName.setText(info.getDisplayName());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return peerProps.size();
    }

    //计算宽高
    private RelativeLayout.LayoutParams calcSize(int size) {
        RelativeLayout.LayoutParams lp;
        switch (size) {
            case 1:
                lp = new RelativeLayout.LayoutParams(width, height);
                break;
            case 2:
                lp = new RelativeLayout.LayoutParams(width / 2, height);
                break;
            case 3:
            case 4:
                lp = new RelativeLayout.LayoutParams(width / 2, height / 2);
                break;
            case 5:
            case 6:
                lp = new RelativeLayout.LayoutParams(width / 3, height / 2);
                break;
            case 7:
            case 8:
            case 9:
                lp = new RelativeLayout.LayoutParams(width / 3, height / 3);
                break;
            case 10:
            case 11:
            case 12:
                lp = new RelativeLayout.LayoutParams(width / 4, height / 3);
                break;
            case 13:
            case 14:
            case 15:
            case 16:
                lp = new RelativeLayout.LayoutParams(width / 4, height / 4);
                break;
            case 17:
            case 18:
            case 19:
            case 20:
                lp = new RelativeLayout.LayoutParams(width / 5, height / 4);
                break;
            default:
                lp = new RelativeLayout.LayoutParams(width / 5, height / 5);
                break;
        }
        return lp;
    }
}
