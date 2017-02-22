package com.rmn.dumb_e;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by rmn on 28-12-2016.
 */

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.Holder> {

    List<BluetoothDevice> mDeviceList;
    Context mContext;
    DeviceAdapter(Context context){
        mContext=context;
        mDeviceList=new ArrayList<>();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(mContext).inflate(R.layout.paired_device,null,false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Log.e("adddevice  ","cld"+mDeviceList.size());
        BluetoothDevice device=mDeviceList.get(position);
        holder.bd=device;
        holder.name.setText(device.getName());
        holder.add.setText(device.getAddress());
    }


    public int getItemCount() {

        Log.e("getItemCount  ","cld"+mDeviceList.size());
        return mDeviceList.size();
    }

    public void addDevice(Set<BluetoothDevice> list){
        mDeviceList.clear();
        mDeviceList.addAll(list);
        notifyDataSetChanged();
        Log.e("adddevice  ","cld"+mDeviceList.size());
    }
    public class Holder extends RecyclerView.ViewHolder{
         BluetoothDevice bd;
        TextView name,add;
        public Holder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.device);
            add=(TextView)itemView.findViewById(R.id.add);
        }
    }
}
