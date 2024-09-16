package com.ngb.colorflashscreen.ui.fragment.home;

import android.net.Uri;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.android.gms.ads.LoadAdError;
import com.color.call.flash.screen.themes.R;
import com.ngb.colorflashscreen.base.BaseFragment;
import com.ngb.colorflashscreen.datasource.inte.OnActionCallBack;
import com.ngb.colorflashscreen.utils.AdManager;
import com.ngb.colorflashscreen.utils.App;

public class HomeFragment extends BaseFragment<HomeViewModel> implements View.OnClickListener {
    public static final String KEY_SHOW_CALL_SCREEN = "KEY_SHOW_CALL_SCREEN";
    public static final String KEY_TO_GALLERY = "KEY_TO_GALLERY";
    private OnActionCallBack callBack;
    private VideoView videoView;

    public void setCallBack( OnActionCallBack callBack ) {
        this.callBack = callBack;
    }

    @Override
    protected void initViews() {
        App.getInstance().setFlagCheck(2);
        videoView = rootView.findViewById(R.id.video_view);
        videoView.setVisibility(View.VISIBLE);
        MediaController mediaController = new MediaController(getContext());
        mediaController.setAnchorView(videoView);
        videoView.setVideoURI(Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.raw.video3));

        rootView.findViewById(R.id.btn_call_screen).setOnClickListener(this);
        rootView.findViewById(R.id.btn_download).setOnClickListener(this);
        rootView.findViewById(R.id.btn_gif).setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        videoView.start();
    }

    @Override
    protected Class<HomeViewModel> getClassViewModel() {
        return HomeViewModel.class;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_home;
    }

    @Override
    public void onClick( View v ) {
        if (v.getId() == R.id.btn_call_screen) {
            callBack.OnCallBack(KEY_SHOW_CALL_SCREEN, (Object) null);
        }

        if (v.getId() == R.id.btn_download) {
            callBack.OnCallBack(KEY_TO_GALLERY, (Object) null);
        }
    }
}