package com.rmn.dumb_e;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.rmn.dumb_e.model.Device;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rmn on 09-09-2016.
 */
public class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

   Context mContext;
    List<BluetoothDevice> mDevices;
    public Adapter(Context context){
        mContext=context;
        mDevices=new ArrayList<BluetoothDevice>();
    }
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.device_name_lay,null);
        Holder holder=new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
       BluetoothDevice device= mDevices.get(position);
        holder.deviceName.setText(device.getName());
        holder.macAdd.setText(device.getAddress());
        holder.bluetoothDevice=device;
    }


    @Override
    public int getItemCount() {
        if (mDevices!=null)
        return mDevices.size();
        return 0;
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView deviceName;
        TextView macAdd;
        BluetoothDevice bluetoothDevice;
        public Holder(View itemView) {
            super(itemView);
            deviceName=(TextView)itemView.findViewById(R.id.device);
            macAdd=(TextView)itemView.findViewById(R.id.mac);
        }
    }

    public void setDevice(BluetoothDevice device){
        mDevices.add(device);
        notifyDataSetChanged();
    }
}
