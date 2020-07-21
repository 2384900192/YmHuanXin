package com.example.ymhuanxin.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ymhuanxin.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter {
    Context context;
    List<String> list;

    public MyAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.activity_item, viewGroup, false);

        return new ViewHolder1(inflate);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        String s = list.get(i);
        ViewHolder1 viewHolder1= (ViewHolder1) viewHolder;
        viewHolder1.mNameTv.setText(s);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modeerssd!=null){
                    modeerssd.post(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder1 extends RecyclerView.ViewHolder {
        TextView mNameTv;

        public ViewHolder1(@NonNull View itemView) {
            super(itemView);
            mNameTv = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
    Modeerssd modeerssd;

    public void setModeerssd(Modeerssd modeerssd) {
        this.modeerssd = modeerssd;
    }

    public interface Modeerssd{
        void post(int i);
    }
}
