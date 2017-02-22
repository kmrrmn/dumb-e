package com.rmn.dumb_e;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rmn.dumb_e.utils.Utiles;

import java.util.Random;
import java.util.TimerTask;

/**
 * Created by rmn on 31-08-2016.
 */
public class HolderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    View rootView;
    TextView textView;
    int i = 0;
    RelativeLayout mback;

    public static HolderFragment newInstance(int sectionNumber) {
        HolderFragment fragment = new HolderFragment();
        Bundle args = new Bundle();
        // Log.e("Pos",sectionNumber+"");
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().getInt(ARG_SECTION_NUMBER) == 4) {


        }
    }

     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        textView = (TextView) rootView.findViewById(R.id.section_label);
        mback = (RelativeLayout) rootView.findViewById(R.id.back);

        if (getArguments().getInt(ARG_SECTION_NUMBER) != 4) {
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        } else {
            mback.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        i = 0;
    }

    @Override
    public void onStop() {
        super.onStop();
        i = -9;
    }

    @Override
    public void onStart() {
        super.onStart();
        i = 0;

        if (getArguments().getInt(ARG_SECTION_NUMBER) != 4) {

            ValueAnimator colorAnimation = Utiles.getValueAnimator(mback);
            colorAnimation.start();
        }
    }
}