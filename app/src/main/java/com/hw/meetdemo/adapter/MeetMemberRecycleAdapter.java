package com.hw.meetdemo.adapter;

import android.content.Context;
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
import com.hw.meetdemo.databinding.MemberItemLayoutBinding;

import java.util.List;

/**
 * @author: 13105
 * @date: 2022/3/11 16:20
 * @description: 成员适配器
 */
public class MeetMemberRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final List<PeerProps> peerProps;
    private final RoomClient roomClient;
    private MemberItemLayoutBinding memberBinding;
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        memberBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.member_item_layout, null, false);
        viewHolder = new MemberViewHolder(memberBinding.getRoot());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder memberViewHolder, int position) {
        LinearLayout.LayoutParams lp = calcSize(peerProps.size());
        PeerView peerView = memberBinding.member;
        peerView.setLayoutParams(lp);
        peerView.setProps(peerProps.get(position), roomClient);
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
    private LinearLayout.LayoutParams calcSize(int size) {
        LinearLayout.LayoutParams lp;
        switch (size) {
            case 1:
                lp = new LinearLayout.LayoutParams(width, height);
                break;
            case 2:
                lp = new LinearLayout.LayoutParams(width / 2, height);
                break;
            case 3:
            case 4:
                lp = new LinearLayout.LayoutParams(width / 2, height / 2);
                break;
            case 5:
            case 6:
                lp = new LinearLayout.LayoutParams(width / 3, height / 2);
                break;
            case 7:
            case 8:
            case 9:
                lp = new LinearLayout.LayoutParams(width / 3, height / 3);
                break;
            case 10:
            case 11:
            case 12:
                lp = new LinearLayout.LayoutParams(width / 4, height / 3);
                break;
            case 13:
            case 14:
            case 15:
            case 16:
                lp = new LinearLayout.LayoutParams(width / 4, height / 4);
                break;
            case 17:
            case 18:
            case 19:
            case 20:
                lp = new LinearLayout.LayoutParams(width / 5, height / 4);
                break;
            default:
                lp = new LinearLayout.LayoutParams(width / 5, height / 5);
                break;
        }
        return lp;
    }
}
