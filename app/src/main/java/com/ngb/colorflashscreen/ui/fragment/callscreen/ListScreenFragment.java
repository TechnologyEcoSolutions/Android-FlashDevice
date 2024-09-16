package com.ngb.colorflashscreen.ui.fragment.callscreen;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.color.call.flash.screen.themes.R;
import com.ngb.colorflashscreen.base.BaseFragment;
import com.ngb.colorflashscreen.datasource.inte.OnActionCallBack;
import com.ngb.colorflashscreen.datasource.model.ScreenModel;
import com.ngb.colorflashscreen.ui.fragment.callscreen.viewmodel.ListScreenViewModel;
import com.ngb.colorflashscreen.utils.App;

import java.util.ArrayList;
import java.util.List;

public class ListScreenFragment extends BaseFragment<ListScreenViewModel> {
    public static final String KEY_BACK_HOME = "KEY_BACK_HOME";
    public static final String KEY_TO_DETAIL_SCREEN = "KEY_TO_DETAIL_SCREEN";
    private OnActionCallBack callBack;
    private FrameLayout frameLayout;
    private FirebaseAnalytics analytics;

    public void setCallBack( OnActionCallBack callBack ) {
        this.callBack = callBack;
    }

    @Override
    protected void initViews() {
        analytics = FirebaseAnalytics.getInstance(requireContext());
        App.getInstance().setFlagCheck(3);
        List<ScreenModel> listScreen = new ArrayList<>();
        ImageView btBack = rootView.findViewById(R.id.bt_back);
        frameLayout = rootView.findViewById(R.id.ad_frame);
        RecyclerView rvListScreen = rootView.findViewById(R.id.rv_list_screen);
        rvListScreen.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        ListScreenAdapter adapter = new ListScreenAdapter(listScreen, getContext());
        adapter.loadImage(mModel.initImage(getContext()));
        rvListScreen.setAdapter(adapter);
        adapter.setOnItemClick(new ListScreenAdapter.OnItemClick() {
            @Override
            public void onItemClick( ScreenModel screenModel, boolean b ) {
                Bundle params = new Bundle();
                params.putString("event_type", "click_item");
                analytics.logEvent("prox_rating_layout", params);
                callBack.OnCallBack(KEY_TO_DETAIL_SCREEN, screenModel, b);
                Log.e("aaa", b + "");
            }
        });

        btBack.setOnClickListener(v -> callBack.OnCallBack(KEY_BACK_HOME, (Object) null));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected Class<ListScreenViewModel> getClassViewModel() {
        return ListScreenViewModel.class;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_list_screen;
    }

}
