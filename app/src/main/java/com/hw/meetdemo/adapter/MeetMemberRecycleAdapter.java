package com.hw.meetdemo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.hw.mediasoup.lib.RoomClient;
import com.hw.mediasoup.view.PeerView;
import com.hw.mediasoup.vm.PeerProps;
import com.hw.meetdemo.R;
import com.hw.meetdemo.databinding.MemberItemLayout1Binding;
import com.hw.meetdemo.databinding.MemberItemLayout2Binding;

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
    private ViewDataBinding memberBinding;
    private final int width, height;

    public MeetMemberRecycleAdapter(Context context, List<PeerProps> peerProps, RoomClient roomClient, int width, int height) {
        this.context = context;
        this.peerProps = peerProps;
        this.roomClient = roomClient;
        this.width = width;
        this.height = height;
    }

    static class OneMemberViewHolder extends RecyclerView.ViewHolder {

        public OneMemberViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    static class TwoMemberViewHolder extends RecyclerView.ViewHolder {

        public TwoMemberViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        int size = peerProps.size();
        if (size == 1) {
            memberBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.member_item_layout_1, null, false);
            viewHolder = new OneMemberViewHolder(memberBinding.getRoot());
        } else if (size == 2) {
            memberBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.member_item_layout_2, null, false);
            viewHolder = new TwoMemberViewHolder(memberBinding.getRoot());
        } else {
            viewHolder = new OneMemberViewHolder(memberBinding.getRoot());
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder memberViewHolder, int position) {
        LinearLayout.LayoutParams lp = calcSize(peerProps.size());
        Log.d("peerProps0", peerProps.size() + "");
        if (memberViewHolder instanceof OneMemberViewHolder) {
            PeerView peerView = ((MemberItemLayout1Binding) memberBinding).member1;
            peerView.setLayoutParams(lp);
            peerView.setProps(peerProps.get(position), roomClient);
        } else if (memberViewHolder instanceof TwoMemberViewHolder) {
            PeerView peerView2_1 = ((MemberItemLayout2Binding) memberBinding).member21;
            PeerView peerView2_2 = ((MemberItemLayout2Binding) memberBinding).member22;
            peerView2_1.setLayoutParams(lp);
            peerView2_2.setLayoutParams(lp);
            Log.d("peerProps0", peerProps.get(0) + "");
            Log.d("peerProps1", peerProps.get(1) + "");
            peerView2_1.setProps(peerProps.get(0), roomClient);
            peerView2_2.setProps(peerProps.get(1), roomClient);
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
            default:
                lp = new LinearLayout.LayoutParams(width, width);
                break;

        }
        return lp;
    }
}
