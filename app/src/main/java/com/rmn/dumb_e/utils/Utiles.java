package com.rmn.dumb_e.utils;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.Random;

/**
 * Created by rmn on 08-09-2016.
 */
public class Utiles {

    public static ValueAnimator getValueAnimator(final View mback) {

        final GradientDrawable gradientDrawable = new GradientDrawable();

        int colorFrom = Color.parseColor("#F44336");
        int color1 = Color.parseColor("#E91E63");
        int color2 = Color.parseColor("#BA68C8");
        int color3 = Color.parseColor("#D500F9");
        int color4 = Color.parseColor("#26C6DA");
        int color5 = Color.parseColor("#0097A7");
        int color6 = Color.parseColor("#00ACC1");
        int color7 = Color.parseColor("#EF9A9A");
        int color8 = Color.parseColor("#EF5350");
        int color9 = Color.parseColor("#EF9A9A");


        int colorTo = Color.parseColor("#EF5350");

        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, color1, color2, color3, color4, color5, color6, color7, color8, color9);
        colorAnimation.setDuration(40000);
        // milliseconds
        colorAnimation.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimation.setRepeatCount(1000);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                Random random = new Random();
                random.nextInt();

                GradientDrawable.Orientation[] orientations = GradientDrawable.Orientation.values();
                Random random1 = new Random();
                gradientDrawable.setOrientation(orientations[4]);
                int color = (int) animator.getAnimatedValue();
                int colors[] = {color, color - 120};
                gradientDrawable.setColors(colors);
//                mback.setBackgroundColor((int) animator.getAnimatedValue());
                mback.setBackground(gradientDrawable);
            }
        });
        return colorAnimation;
    }
}
