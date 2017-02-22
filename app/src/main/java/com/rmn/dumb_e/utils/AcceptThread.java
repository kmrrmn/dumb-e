package com.rmn.dumb_e.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by rmn on 09-09-2016.
 */
public class AcceptThread extends Thread {
    private BluetoothServerSocket mServerSocket;
    private static final UUID uuid = UUID.fromString("00001203-0000-1000-8000-00805f9b34fb");

    public AcceptThread(BluetoothAdapter adapter) {
        BluetoothServerSocket tmp = null;
        try {
            tmp = adapter.listenUsingRfcommWithServiceRecord("NAME", uuid);
        } catch (IOException e1) {
        }
        mServerSocket = tmp;
    }

    @Override
    public void run() {
        super.run();
        BluetoothSocket socket = null;
        while (true) {
            Log.e("mkm ","kn");
            try {
                Log.e("socket ","connecting..........."+" ??");
                socket=mServerSocket.accept();
                Log.e("socket ",socket.isConnected()+" ??");
            } catch (IOException e) {
                Log.e("IOEXCEPTIOON ",e.getMessage());
                break;
            }
            if (socket!=null){
                try {
                    Log.e("Socket ","!= null");
                ConnectedThread thread=new ConnectedThread(socket);
                thread.start();
                    mServerSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }


    public void cancel(){
        try {
            mServerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void manageConnectedSocket(BluetoothSocket socket){

    }
}
