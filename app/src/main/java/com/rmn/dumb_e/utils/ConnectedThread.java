package com.rmn.dumb_e.utils;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by rmn on 09-09-2016.
 */
public class ConnectedThread extends Thread {

    private BluetoothSocket mSocket;
    private InputStream mInputStream;
    private OutputStream mOutputStream;

    public ConnectedThread(BluetoothSocket socket) throws IOException {
        mSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("ConnectedThread ","called");
        Log.e("ConnectedThread ","mInputStream "+mInputStream);

        mInputStream = tmpIn;
        mOutputStream = tmpOut;
    }

    @Override
    public void run() {
        super.run();
Log.e("run ","cld");
        byte[] buffer = new byte[1024];
        int bytes=-1;
        while (true) {

            Log.e("while", "" + bytes);

            try {

                Log.e("Byte", "" + bytes);

                bytes = mInputStream.read(buffer);
            } catch (IOException e) {

                Log.e("Byte", "" + bytes);

                Log.e("exception ","msg "+ e.getMessage());

            }
        }
    }

    public void Write(byte[] bytes) {
        try {
            mOutputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cancel() {
        try {
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
