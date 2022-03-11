package com.hw.meetdemo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hw.mediasoup.lib.RoomClient;
import com.hw.mediasoup.view.PeerView;
import com.hw.mediasoup.vm.PeerProps;
import com.hw.meetdemo.R;
import com.hw.meetdemo.databinding.MemberItemLayout1Binding;

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
    private MemberItemLayout1Binding memberItemLayout1Binding;
    private final int width, height;

    public MeetMemberRecycleAdapter(Context context, List<PeerProps> peerProps, RoomClient roomClient, int width, int height) {
        this.context = context;
        this.peerProps = peerProps;
        this.roomClient = roomClient;
        this.width = width;
        this.height = height;
    }

    static class MemberViewHolder extends RecyclerView.ViewHolder {

        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        memberItemLayout1Binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.member_item_layout_1, null, false);
        return new MemberViewHolder(memberItemLayout1Binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder memberViewHolder, int position) {
        PeerView peerView = memberItemLayout1Binding.member1;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
        peerView.setLayoutParams(lp);
        Log.d("setProps", "onBindViewHolder: "+peerProps.get(position));
        peerView.setProps(peerProps.get(position), roomClient);
    }

    @Override
    public int getItemCount() {
        return peerProps.size();
    }

}
