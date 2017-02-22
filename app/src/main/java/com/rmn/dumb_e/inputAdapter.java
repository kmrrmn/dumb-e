package com.rmn.dumb_e;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rmn on 24-01-2017.
 */

public class inputAdapter extends RecyclerView.Adapter<inputAdapter.inputHolder> {
     private Context mContext;
    private List<String> inputDataList;

    inputAdapter(Context context){
        mContext=context;
        inputDataList=new ArrayList<>();
    }

    @Override
    public inputHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new inputHolder(LayoutInflater.from(mContext).inflate(R.layout.data_view,null));
    }


    @Override
    public void onBindViewHolder(inputHolder holder, int position) {
          holder.input.setText( inputDataList.get(position));
    }


    @Override
    public int getItemCount() {
        return inputDataList.size();
    }

    public void addInput(String in){
        inputDataList.add(in);
        notifyDataSetChanged();
    }

    public class inputHolder extends RecyclerView.ViewHolder{
        TextView input;
        public inputHolder(View itemView) {
            super(itemView);
            input=(TextView)itemView.findViewById(R.id.input);
        }
    }
}
