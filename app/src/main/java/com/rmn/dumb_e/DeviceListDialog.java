package com.rmn.dumb_e;

/**
 * Created by rmn on 26-01-2017.
 */

import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.Set;

/**
 * Created by Nikola D. on 2/25/2016.
 */
public class DeviceListDialog extends BottomSheetDialogFragment {

    private TextView mOffsetText;
    private TextView mStateText;
    RecyclerView mRecyclerView;
    DeviceAdapter mDeviceAdapter;
    deviceListCallbackListener mListener;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {



        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
//            setStateText(newState);
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//            setOffsetText(slideOffset);
        }
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onViewCreated(View contentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(contentView, savedInstanceState);


//
//        recyclerView.setLayoutManager(mLinearLayoutManager);
//        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.devicelist, null);
        dialog.setContentView(contentView);
         dialog.setTitle("Select Device");

        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.recycle);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mDeviceAdapter=new DeviceAdapter(getContext());
        mRecyclerView.setAdapter(mDeviceAdapter);


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
//                        Log.e("clicked ","to connect ");
//                        mDevice=holder.bd;
//                        deviceName=holder.name.getText().toString();
//
//                        if (task==null){
//                            task=new HomeFragment.Task();
//                            task.execute(mDevice);
//                            task=null;
//                        }

                        mListener.ConnectTo(holder.bd,holder.name.getText().toString());
                        dismiss();
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


    public void addDeviceList(Set<BluetoothDevice> list,deviceListCallbackListener listener){
        mDeviceAdapter=new DeviceAdapter(getContext());
        mDeviceAdapter.addDevice(list);
        mListener=listener;
    }


    interface deviceListCallbackListener{
        public void ConnectTo(BluetoothDevice bd,String name);
    }

//    private void setOffsetText(final float slideOffset) {
//        ViewCompat.postOnAnimation(mOffsetText, new Runnable() {
//            @Override
//            public void run() {
//                mOffsetText.setText(getString(R.string.offset, slideOffset));
//            }
//        });
//    }

//    private void setStateText(int newState) {
//        mStateText.setText(BottomSheetActivity.getStateAsString(newState));
//    }
}