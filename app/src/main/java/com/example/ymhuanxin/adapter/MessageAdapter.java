package com.example.ymhuanxin.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import com.example.ymhuanxin.R;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;

/**
 * @Auther: hchen
 * @Date: 2020/7/20 0020
 * @Description:
 */
public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<EMMessage> mMessages;
    private String mMyName;

    public MessageAdapter(Context context, ArrayList<EMMessage> messages,String myName) {
        mContext = context;
        mMessages = messages;
        mMyName = myName;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0){
            View view0 = View.inflate(mContext, R.layout.item_message0, null);
            return new ViewHolder0(view0);
        }else {
            View view1 = View.inflate(mContext, R.layout.item_message1, null);
            return new ViewHolder1(view1);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        EMMessage emMessage = mMessages.get(position);
        int type = getItemViewType(position);
        String from = emMessage.getFrom();//消息的来源
        String body = emMessage.getBody().toString();//消息体
        if(type == 0){
            ViewHolder0 holder0 = (ViewHolder0) holder;
            holder0.name.setText(from+":::"+body);
        }else {
            ViewHolder1 holder1 = (ViewHolder1) holder;
            holder1.name.setText(from+":::"+body);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickLis.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        EMMessage emMessage = mMessages.get(position);
        String from = emMessage.getFrom();//消息来源
        if(!from.equals(mMyName)){//对方发的消息，放左边
            return 0;
        }else {
            return 1;
        }
    }

    public class ViewHolder0 extends RecyclerView.ViewHolder {
        TextView name;
        public ViewHolder0(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txt_name);
        }
    }
    public class ViewHolder1 extends RecyclerView.ViewHolder {
        TextView name;
        public ViewHolder1(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txt_name);
        }
    }

    public void setOnItemClickLis(OnItemClickLis onItemClickLis) {
        mOnItemClickLis = onItemClickLis;
    }

    public OnItemClickLis mOnItemClickLis;
    public interface OnItemClickLis{
        void onItemClick(int position);
    }
}
