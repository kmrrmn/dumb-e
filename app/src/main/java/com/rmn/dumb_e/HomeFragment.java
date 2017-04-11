package com.rmn.dumb_e;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;


public class HomeFragment extends Fragment implements DeviceListDialog.deviceListCallbackListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String TAG = "HomeFragment";
    static String deviceName = "not connected";
    private static BluetoothDevice mDevice;
    private static OnFragmentInteractionListener mListener;
    View bottomSheet, sendBottomSheet;
    ProgressDialog dialog;
    boolean connected;
    BluetoothSocket mmBluetoothSocket;
    TextView receiveView;
    ThreadConnected mThreadConnected;
    Task task;
    inputAdapter adapter;
    FloatingActionButton fab;
    DeviceListDialog deviceListDialog;
    TextToSpeech t1;
    int nodatasend = 0;
    private String mParam1;
    private String mParam2;
    private TextInputEditText msgView;
    private FloatingActionButton sendButton;
    private RecyclerView mRecyclerView, mSentDataRecyclerView;
    private String mConnectedDeviceName = null;
    private ArrayAdapter<String> mConversationArrayAdapter;
    private StringBuffer mOutStringBuffer;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothDevice mBluetoothDevice = null;
    private ConnectionService mChatService = null;
    private int REQUEST_ENABLE_BT = 1;
    private DeviceAdapter mDeviceAdapter;
    private BottomSheetBehavior mBottomSheetBehavior, mSendBottomSheetBehavior;


    public static HomeFragment newInstance(String param1) {
            HomeFragment fragment = new HomeFragment();
            Bundle args = new Bundle();
            args.putString(ARG_PARAM1, param1);
            fragment.setArguments(args);
            return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.e("fer", "dsd");
        t1 = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    Log.e("TextToSpeech", "----------------------" + "nice");
                    t1.setLanguage(Locale.UK);
                    t1.setPitch(0.7f);
                } else
                    Log.e("TextToSpeech", "----------------------" + "error");
            }
        });
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        fab = (FloatingActionButton) view.findViewById(R.id.speak);
        receiveView = (TextView) view.findViewById(R.id.receive);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
          t1.speak(receiveView.getText().toString(), TextToSpeech.ERROR_NETWORK, null);

             }
         });

         mSentDataRecyclerView = (RecyclerView) view.findViewById(R.id.input_recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setAutoMeasureEnabled(true);
        mSentDataRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new inputAdapter(getContext());
        mSentDataRecyclerView.setAdapter(adapter);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycle);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mDeviceAdapter = new DeviceAdapter(getContext());
        mRecyclerView.setAdapter(mDeviceAdapter);

        bottomSheet = view.findViewById(R.id.bottom_sheet);
        sendBottomSheet = view.findViewById(R.id.send_bottomSheet);
        BottomSheet();
        setHasOptionsMenu(true);
        Log.e("qwerty", "uiop");
        deviceListDialog = new DeviceListDialog();
        return view;
    }

//int index,lastSpeechIndex=0;
//    public void setValueAt( ){
//        StringBuilder sb=new StringBuilder(receiveView.getText().toString());
//         index= sb.indexOf(".",lastSpeechIndex);
//        String  speak=sb.substring(lastSpeechIndex+1,index).toString();
//        sb.setCharAt(index,' ');
//        receiveView.setText(sb.toString());
//        t1.speak(speak.toString(), TextToSpeech.ERROR_NETWORK, null);
//        index=lastSpeechIndex;
//    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home_frag, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search) {
            BluetoothOpen();


            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void ConnectTo(BluetoothDevice bd, String name) {
        Log.e("frg-----", "ConnectTo cld");
        task = new HomeFragment.Task();
        task.execute(mDevice);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        msgView = (TextInputEditText) view.findViewById(R.id.data);
        sendButton = (FloatingActionButton) view.findViewById(R.id.send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });
    }

    public boolean BluetoothOpen() {
        if (mBluetoothAdapter != null)
            if (!mBluetoothAdapter.isEnabled()) {
                startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BT);
            } else
                setup(0);
        else
            Toast.makeText(getContext(), "bluetooth not support", Toast.LENGTH_LONG).show();

        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("OnActivityResult ", "---> " + requestCode + " : " + resultCode);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                setup(1);
            } else {
                Toast.makeText(getContext(), "System problem occur", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setup(int i) {
        Log.e("setup  ", "called");
        deviceListDialog.addDeviceList(mBluetoothAdapter.getBondedDevices(), this);
        //  deviceListDialog.show(getActivity().getSupportFragmentManager(), deviceListDialog.getTag());
        mDeviceAdapter.addDevice(mBluetoothAdapter.getBondedDevices());
        bottomSheet.setVisibility(View.VISIBLE);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        mBottomSheetBehavior.setHideable(true);
    }

    public void BottomSheet() {
        mSendBottomSheetBehavior = BottomSheetBehavior.from(sendBottomSheet);
        mSendBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mSendBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (slideOffset > .5f) {
                    fab.hide();
                } else if (slideOffset < .2f) {
                    fab.show();
                }
                Log.e("mBottomSheetBehavior", "slidoffst " + slideOffset);
            }
        });

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.devicelist);
        bottomSheetDialog.setTitle("Select Device");
        bottomSheetDialog.setCancelable(true);

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (slideOffset > .5f) {
                    fab.hide();
                } else if (slideOffset < .2f) {
                    fab.show();
                }
                Log.e("mBottomSheetBehavior", "slidoffst " + slideOffset);
            }
        });

        final GestureDetector gestureDetector =
                new GestureDetector(getContext()
                        , new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }
                });

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    if (rv.getId() == R.id.recycle) {
                        final DeviceAdapter.Holder holder = (DeviceAdapter.Holder) rv.getChildViewHolder(child);
                        Log.e("clicked ", "to connect ");
                        mDevice = holder.bd;
                        deviceName = holder.name.getText().toString();

                        if (mThreadConnected== null) {
                            task = new Task();
                            task.execute(mDevice);
                            task = null;
                        }
                    }
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
    }



    public void send() {
        Log.e("send ", "data ");
        if (mThreadConnected != null) {
            Log.e("Off g", "data sent ");
            mThreadConnected.write(msgView.getText().toString().getBytes());
            adapter.addInput(msgView.getText().toString());
            mSentDataRecyclerView.scrollTo(0, ++nodatasend);
            msgView.setText("");
        } else {
            Toast.makeText(getContext(), "Connection problem", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mThreadConnected!=null)
        mThreadConnected.cancel();
    }

    public interface OnFragmentInteractionListener {
        String onFragmentInteraction(Uri uri);
    }

    public class Task extends AsyncTask<BluetoothDevice, Void, String> {
        String UUID = "00001101-0000-1000-8000-00805F9B34FB";
        BluetoothSocket tmpBluetoothSocket;
        String msg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getContext());
            dialog.setCancelable(false);
            dialog.setMessage("connecting...");
            dialog.show();
        }

        @Override
        protected String doInBackground(BluetoothDevice... device) {
            BluetoothDevice bd = device[0];
            msg = bd.getName();
            String status = "p";
            try {
                tmpBluetoothSocket = bd.createRfcommSocketToServiceRecord(java.util.UUID.fromString(UUID));
            } catch (IOException e) {
                e.printStackTrace();
            }

            mmBluetoothSocket = tmpBluetoothSocket;
            try {
                mmBluetoothSocket.connect();
                connected = true;
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    connected = true;
                    status = "connecting";
                    startCommunicationThread(mmBluetoothSocket);

             } catch (IOException e) {
                e.printStackTrace();
                try {
                    mmBluetoothSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                connected = false;
                status = "error";
                Log.e("Connecting : ", "error 1  " + e.getMessage());
            }


            return status;
        }

        @Override
        protected void onProgressUpdate(Void... status) {
            super.onProgressUpdate(status);
            dialog.setMessage("Connecting to " + msg);
            Log.e("Connecting : ", "error " + "update");
        }

        @Override
        protected void onPostExecute(String aVoid) {

            super.onPostExecute(aVoid);
            //   Log.e("Connecting : ","error "+"post");
            if (!connected)
                Toast.makeText(getContext(), "connection failed", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(), "connected", Toast.LENGTH_SHORT).show();


            dialog.dismiss();
        }
    }

    public void startCommunicationThread(BluetoothSocket socket) {


        Log.e("startCommunicationThrad", "Called");
            mThreadConnected = new ThreadConnected();
            mThreadConnected.execute(socket);


    }

    public class ThreadConnected extends AsyncTask<BluetoothSocket, Void, Void> {
        private BluetoothSocket connectedBluetoothSocket = null;
        private InputStream connectedInputStream = null;
        private OutputStream connectedOutputStream = null;
        int lastSpeechIndex=0,index;

        @Override
        protected Void doInBackground(BluetoothSocket... bluetoothSockets) {

            char specialChar='.';
            connectedBluetoothSocket = bluetoothSockets[0];
            byte[] buffer = new byte[1024];
            int bytes;
            InputStream in = null;
            OutputStream out = null;

            try {
                in = connectedBluetoothSocket.getInputStream();
                out = connectedBluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            connectedInputStream = in;
            connectedOutputStream = out;

            while (true) {

                try {
                    assert connectedInputStream != null;
                    bytes = connectedInputStream.read(buffer);
                     final String strReceived = new String(buffer, 0, bytes);

                    final String msgReceived = String.valueOf(bytes) +
                            " bytes received:\n"
                            + strReceived;

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           // Toast.makeText(getContext(), msgReceived, Toast.LENGTH_LONG).show();
                            //setValueAt( strReceived);
                            receiveView.setText(receiveView.getText().toString()+strReceived);
                        }
                    });

                } catch (IOException e) {

                    e.printStackTrace();

                    final String msgConnectionLost = "Connection lost";

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
//                            toolbar.setTitle(msgConnectionLost);
                        }
                    });
                }
            }

        }


        public void write(byte[] buffer) {
            try {
                assert connectedOutputStream != null;
                connectedOutputStream.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


//        public void setValueAt(String received){
//            StringBuilder sb=new StringBuilder(receiveView.getText().toString());
//            sb.append(received);
//            index= sb.indexOf(".",lastSpeechIndex);
//            String  speak=sb.substring(lastSpeechIndex+1,index).toString();
//            sb.setCharAt(index,' ');
//            receiveView.setText(sb.toString());
//            t1.speak(speak, TextToSpeech.ERROR_NETWORK, null);
//            index=lastSpeechIndex;
//        }

        public void read(byte[] buffer) {
            try {
                assert connectedOutputStream != null;
                int i = connectedInputStream.read(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void cancel() {
            try {
                connectedBluetoothSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }


}
