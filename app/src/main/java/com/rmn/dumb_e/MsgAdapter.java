package com.rmn.dumb_e;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rmn on 11-09-2016.
 */

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.Holder> {
    Context mContext;
    private static int i=0;
    private List<String> msgList;
    MsgAdapter(Context context) {
        mContext = context;
        msgList=new ArrayList<>();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    public void setData(String msg){
        msgList.add(i++,msg);
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView msgView;

        public Holder(View itemView) {
            super(itemView);
            msgView = (TextView) itemView.findViewById(R.id.msg);
        }
    }
}
