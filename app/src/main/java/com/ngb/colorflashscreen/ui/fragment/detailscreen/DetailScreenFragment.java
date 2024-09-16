package com.ngb.colorflashscreen.ui.fragment.detailscreen;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.color.call.flash.screen.themes.R;
import com.ngb.colorflashscreen.base.BaseFragment;
import com.ngb.colorflashscreen.datasource.inte.OnActionCallBack;
import com.ngb.colorflashscreen.datasource.model.ScreenModel;
import com.ngb.colorflashscreen.ui.fragment.after.AfterApplyActivity;
import com.ngb.colorflashscreen.utils.AdManager;
import com.ngb.colorflashscreen.utils.App;

import java.io.IOException;

public class DetailScreenFragment extends BaseFragment<DetailScreenViewModel> implements View.OnClickListener {
    public static final String KEY_BACK_LIST_SCREEN = "KEY_BACK_LIST_SCREEN";
    private OnActionCallBack callBack;
    private ScreenModel screenModel;
    private String image;
    private ImageButton btCall;
    private FrameLayout frameLayout;
    private TextView btDownloadScreen;
    private boolean aBoolean;
    private FirebaseAnalytics analytics;

    public void setScreenModel( ScreenModel screenModel, boolean b ) {
        this.screenModel = screenModel;
        this.aBoolean = b;
    }

    public void setCallBack( OnActionCallBack callBack ) {
        this.callBack = callBack;
    }

    @Override
    protected void initViews() {
        analytics = FirebaseAnalytics.getInstance(requireContext());
        App.getInstance().setFlagCheck(4);
        btDownloadScreen = rootView.findViewById(R.id.bt_download_screen);
        if (aBoolean) {
            btDownloadScreen.setText("Applied");
        } else {
            btDownloadScreen.setText("Apply");
        }
        Log.e("aaa", aBoolean + ":aBoolean");
        ImageView btBack = rootView.findViewById(R.id.bt_back);
        btBack.setOnClickListener(this);
        frameLayout = rootView.findViewById(R.id.ad_frame);
        ImageView ivScreen = rootView.findViewById(R.id.iv_screen);
        try {
            Glide.with(mContext).load(BitmapFactory.decodeStream(mContext.getAssets()
                    .open(screenModel.getImage()))).into(ivScreen);
        } catch (IOException e) {
            e.printStackTrace();
        }

        btDownloadScreen.setOnClickListener(v -> {
            Bundle params = new Bundle();
            params.putString("apply", "click_apply");
            analytics.logEvent("prox_rating_layout", params);

            String txt = "Applied";
            image = screenModel.getImage();
            App.setCheck(false);
            App.getInstance().saveCheck("CHECK", App.isCheck());
            App.getInstance().savePref("IMAGE", image);
            btDownloadScreen.setText(txt);
            screenModel.setApply(true);
            Log.e("Nhandz", screenModel.isApply() + "");
            Intent intent = new Intent(getActivity(), AfterApplyActivity.class);
            startActivity(intent);
            getActivity().finish();
            Toast.makeText(mContext, "Successfully", Toast.LENGTH_SHORT).show();
        });
        btCall = rootView.findViewById(R.id.bt_call);
        Animation utils = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
        btCall.startAnimation(utils);
    }

    @Override
    protected Class<DetailScreenViewModel> getClassViewModel() {
        return DetailScreenViewModel.class;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_detail_screen;
    }

    @Override
    public void onClick( View v ) {
        callBack.OnCallBack(KEY_BACK_LIST_SCREEN, (Object) null);
    }

}
