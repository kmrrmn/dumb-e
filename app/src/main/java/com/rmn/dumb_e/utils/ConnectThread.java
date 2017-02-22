package com.rmn.dumb_e.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.ParcelUuid;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.logging.Handler;

/**
 * Created by rmn on 09-09-2016.
 */
public class ConnectThread extends Thread {
    private BluetoothSocket mSocket;
    private BluetoothDevice mDevice;

    private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public ConnectThread(BluetoothDevice device, BluetoothAdapter adapter) {
        BluetoothSocket tmp = null;
        mDevice = device;
adapter.startDiscovery();
        Log.e("ConnectThread", "clld  " + device.getName());
        try {
            Log.e("ConnectThread", "  try called ");
            tmp = device.createRfcommSocketToServiceRecord(uuid);
         } catch (IOException e) {
            Log.e("ConnectThread", "  catch called ");
        }
        adapter.cancelDiscovery();
        mSocket = tmp;

    }

    @Override
    public void run() {
        super.run();

        Method m = null;

        try {


            m = mDevice.getClass().getMethod("createInsecureRfcommSocket", new Class[]{int.class});
            mSocket = (BluetoothSocket) m.invoke(mDevice, 1);

           // UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
           // mSocket = mDevice.createRfcommSocketToServiceRecord(uuid);

            mSocket.connect();
          Log.e("mSocket.getvice().gee()","== "+mSocket.getRemoteDevice().getName());
            if (mSocket.isConnected()) {

                Log.e("isconn ", "nj");
            }
            Log.e("mSocket.isConnected()","==" +mSocket.isConnected());
             InputStream tmpIn = null;
            OutputStream tmpOut = null;
            Log.e("mSocket ", "Connected 1");
            manageConnectedSocket(mSocket);

            Log.e("mSocket ", "Connected 2");
        } catch (IOException e) {
            Log.e("ConnectThread run  cth ", "e " + e.getMessage());
             try {

                Log.e("Connec run try c try ", "clld");
                mSocket.close();
            } catch (IOException e1) {
                Log.e("ConnectThread run try c", "e1 " + e1.getMessage());
                Log.e("ConnectThread run try c", "clld");
            }
            return;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void manageConnectedSocket(final BluetoothSocket socket) {


        Log.e("manageConnectedSocket ", "called");

        Thread listenerThread = new Thread(new Runnable() {
            @Override
            public void run() {

                InputStream inputStream = null;
                try {
                    inputStream = socket.getInputStream();
                    Log.e("  inputStream = socket ","---------------------------------------------------------------------------");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e("thread ruuning  ", "called 1");

                while (true) {

                    Log.e("thread ruuning  ", "called n91");

                    try {

                        Log.e("thread ruuning  ", "called 20");


                        Log.e("thread ruuning  ", "called 19");
                        assert inputStream != null;
                        int j=  inputStream.read();

                        Log.e("thread ruuning  ", "called 18 j " +j);

                        int bytesAvailable = inputStream.available();

                        byte[] bytes = new byte[bytesAvailable];

                            for (int i = 0; i < bytesAvailable; i++) {
                                Log.e("thread ruuning  ", "called 21");

                                byte b = bytes[i];
                                if (b == 10) {
                                    byte[] encodedByte = new byte[0];
                                    System.arraycopy(new byte[1024], 0, encodedByte, 0, encodedByte.length);
                                    final String data = new String(encodedByte, "US-ASCII");

                                }
                            }

                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("ghjgj", "msg " + e.getMessage());
Log.e("mSocket.isConnected() ",mSocket.isConnected()+"  ?? ");
                    }

                }
            }
        });
        listenerThread.start();
    }

    public void cancel() throws IOException {
        try {
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
