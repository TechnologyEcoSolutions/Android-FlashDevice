package com.ngb.colorflashscreen.ui.act.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.color.call.flash.screen.themes.R;
import com.ngb.colorflashscreen.base.BaseActivity;
import com.ngb.colorflashscreen.datasource.inte.OnActionCallBack;
import com.ngb.colorflashscreen.datasource.model.ScreenModel;
import com.ngb.colorflashscreen.ui.act.call.RingingWindow;
import com.ngb.colorflashscreen.ui.act.gallery.ChooseImageActivity;
import com.ngb.colorflashscreen.ui.act.main.viewmodel.MainViewModel;
import com.ngb.colorflashscreen.ui.fragment.callscreen.ListScreenFragment;
import com.ngb.colorflashscreen.ui.fragment.detailscreen.DetailScreenFragment;
import com.ngb.colorflashscreen.ui.fragment.home.HomeFragment;
import com.ngb.colorflashscreen.utils.App;
import com.xuandq.rate.ProxRateDialog;
import com.xuandq.rate.RatingDialogListener;

public class MainActivity extends BaseActivity<MainViewModel> implements OnActionCallBack {
    private static final int REQ = 1;
    final int OVERLAY_PERMISSION_REQUEST_CODE = 2;
    String[] permissions = {
            Manifest.permission.ANSWER_PHONE_CALLS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.DISABLE_KEYGUARD,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.RECEIVE_BOOT_COMPLETED
    };
    private FirebaseAnalytics analytics;
    private Intent intent1;

    @Override
    protected void initViews() {
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setCallBack(this);
        showFragment(R.id.contaner, homeFragment, false);
        requestPermissions();
        analytics = FirebaseAnalytics.getInstance(this);

        intent1 = new Intent(this, RingingWindow.class);
        intent1.putExtra("inputExtra", "Screen rights");

        ProxRateDialog.Config config = new ProxRateDialog.Config();
        config.setListener(new RatingDialogListener() {
            @Override
            public void onSubmitButtonClicked( int rate, String comment ) {
                Bundle params = new Bundle();
                params.putInt("rate", rate);
                params.putString("comment", comment);
                analytics.logEvent("prox_rating_layout", params);
                finishAndRemoveTask();
            }

            @Override
            public void onLaterButtonClicked() {

            }

            @Override
            public void onChangeStar( int rate ) {
                if (rate >= 4) {
                    finishAndRemoveTask();
                }
            }
        });
        ProxRateDialog.init(this, config);
    }


    @Override
    public void onBackPressed() {
        if (App.getInstance().getFlagCheck() == 2) {
            ProxRateDialog.showAlways(getSupportFragmentManager());

        } else {
            super.onBackPressed();
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private void requestPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;
        for (String permission : permissions) {
            if (permission.equals(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
                continue;
            }
            if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, permissions, REQ);
                return;
            }
        }
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE);
        }

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult( int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults ) {
        switch (requestCode) {
            case REQ:
            case OVERLAY_PERMISSION_REQUEST_CODE:
                requestPermissions();
        }
    }

    @Override
    protected Class getClassViewModel() {
        return MainViewModel.class;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    public void OnCallBack( String key, Object... listObj ) {
        switch (key) {
            case HomeFragment.KEY_SHOW_CALL_SCREEN:
                ContextCompat.startForegroundService(this, intent1);
                ListScreenFragment screenFragment = new ListScreenFragment();
                screenFragment.setCallBack(this);
                showFragment(R.id.contaner, screenFragment, true);
                break;

            case ListScreenFragment.KEY_BACK_HOME:

            case DetailScreenFragment.KEY_BACK_LIST_SCREEN:

                getSupportFragmentManager().popBackStack();
                break;

            case ListScreenFragment.KEY_TO_DETAIL_SCREEN:
                DetailScreenFragment screenFragment1 = new DetailScreenFragment();
                screenFragment1.setCallBack(this);
                ScreenModel screenModel = (ScreenModel) listObj[0];
                boolean b = (boolean) listObj[1];
                screenFragment1.setScreenModel(screenModel, b);
                showFragment(R.id.contaner, screenFragment1, true);
                break;

            case HomeFragment.KEY_TO_GALLERY:
                ContextCompat.startForegroundService(this, intent1);
                Intent intent = new Intent(MainActivity.this, ChooseImageActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
