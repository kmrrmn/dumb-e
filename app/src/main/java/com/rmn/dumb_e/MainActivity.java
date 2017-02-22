package com.rmn.dumb_e;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.RuleBasedCollator;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelUuid;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.transition.Scene;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.rmn.dumb_e.utils.AcceptThread;
import com.rmn.dumb_e.utils.ConnectThread;
import com.rmn.dumb_e.utils.Constant;
import com.rmn.dumb_e.utils.Utiles;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity   {

    HomeFragment mHomeFragment;
    BroadcastReceiver mReceiver;
    private PagerAdapter mPagerAdapter;
    int token = 0, mCount;
    FrameLayout mFrontLay, mContainerLay;
    static int i = 0;
    Toolbar toolbar;
    LinearLayout btnLayout;
    Button skipView;
    DrawerLayout mDrawerLayout;
    ImageView lImageView, mImageView, rImageView;
    View bottomSheet;
    BottomSheetBehavior behavior;
    Adapter mAdapter;
    BluetoothAdapter adapter;
    private ViewPager mViewPager;
    Button startButton;
    Scene mSceneDemo, mSceneHome;
    String mAddress;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialization();
        mHomeFragment=HomeFragment.newInstance("rmn");
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void OnSlide1(int pos) {

        switch (pos) {
            case 0:
                lImageView.setImageResource(R.drawable.circlelight);
                mImageView.setImageResource(R.drawable.circledark);
                rImageView.setImageResource(R.drawable.circledark);
                skipView.setVisibility(View.VISIBLE);
                btnLayout.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.GONE);
                break;

            case 1:
                lImageView.setImageResource(R.drawable.circledark);
                mImageView.setImageResource(R.drawable.circlelight);
                rImageView.setImageResource(R.drawable.circledark);
                skipView.setVisibility(View.VISIBLE);
                btnLayout.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.GONE);
                break;

            case 2:
                lImageView.setImageResource(R.drawable.circledark);
                mImageView.setImageResource(R.drawable.circledark);
                rImageView.setImageResource(R.drawable.circlelight);
                skipView.setVisibility(View.GONE);
                btnLayout.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.VISIBLE);
                break;

        }
    }


    public void initialization() {

        mCount = 3;
        btnLayout = (LinearLayout) findViewById(R.id.btn);
        skipView = (Button) findViewById(R.id.skip);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        lImageView = (ImageView) findViewById(R.id.left);
        mImageView = (ImageView) findViewById(R.id.mid);
        rImageView = (ImageView) findViewById(R.id.right);
        mFrontLay = (FrameLayout) findViewById(R.id.front);
        startButton=(Button)findViewById(R.id.start);

        bottomSheet = findViewById(R.id.bottom_sheet);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        SharedPreferences preferences = getSharedPreferences(Constant.PREF_FILE_NAME, Context.MODE_PRIVATE);
        if (preferences.getAll().get(Constant.PREF_IS_FIRSTTIME) != Constant.YES) {

            mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), mCount, 0);
            mViewPager.arrowScroll(View.FOCUS_RIGHT);
            mViewPager.setAdapter(mPagerAdapter);

            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    OnSlide1(position);
                }

                @Override
                public void onPageSelected(int position) {
                    OnSlide1(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });

            mFrontLay.setVisibility(View.VISIBLE);
            SharedPreferences.Editor edit = preferences.edit();
            edit.putString(Constant.PREF_IS_FIRSTTIME, Constant.YES);
            edit.apply();
        }

    }


    public void ChangeActivity(View view) {
        Log.e("ChangeActivity ","called");
        startActivity(new Intent(this,FrontActivity.class));
        finish();
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(mReceiver);
    }

    @Override
    public void onStart() {
        super.onStart();

        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.rmn.dumb_e/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);

    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.rmn.dumb_e/http/host/path")
        );

        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }



}
