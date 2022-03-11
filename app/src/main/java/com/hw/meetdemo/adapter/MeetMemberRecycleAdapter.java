package com.hw.meetdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hw.mediasoup.lib.model.Peer;
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
    private final List<String> peers;
    private MemberItemLayout1Binding memberItemLayout1Binding;

    public MeetMemberRecycleAdapter(Context context, List<String> peers) {
        this.context = context;
        this.peers = peers;
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
        memberItemLayout1Binding.member1.setText(peers.get(position));
    }

    @Override
    public int getItemCount() {
        return peers.size();
    }

}
