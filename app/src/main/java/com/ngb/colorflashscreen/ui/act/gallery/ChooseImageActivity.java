package com.ngb.colorflashscreen.ui.act.gallery;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.color.call.flash.screen.themes.R;
import com.ngb.colorflashscreen.utils.App;

import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;

public class ChooseImageActivity extends AppCompatActivity {
    private ImageView ivPhoto;
    private TextView btApply;
    private Uri mUri;
    private FrameLayout frameLayout;
    private FirebaseAnalytics analytics;

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);
        initViews();
        analytics = FirebaseAnalytics.getInstance(this);
    }

    @SuppressLint("SetTextI18n")
    private void initViews() {
        frameLayout = findViewById(R.id.ad_frame);
        btApply = findViewById(R.id.bt_apply);
        ivPhoto = findViewById(R.id.iv_photo);
        findViewById(R.id.bt_gallery).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("Click_Apply", "Click_Choose Photo From Galler");
            analytics.logEvent("prox_rating_layout", bundle);
            requestPermission();
            btApply.setVisibility(View.VISIBLE);
        });
        btApply.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("click_apply", "click_Apply");
            analytics.logEvent("prox_rating_layout", bundle);

            App.setCheck(true);
            App.getInstance().saveCheck("CHECK", App.isCheck());
            App.getInstance().saveUri("PHOTO", String.valueOf(mUri));
            Log.e("aaa", App.getInstance().getCheck("CHECK") + "");

            Intent intent = new Intent(ChooseImageActivity.this, DoneScreenAct.class);
            startActivity(intent);
            finish();
            Toast.makeText(this, "Successfully!", Toast.LENGTH_SHORT).show();
            btApply.setText("Applied");
        });

    }

    private void requestPermission() {
        PermissionListener permissionlistener = new PermissionListener() {

            @Override
            public void onPermissionGranted() {
                selectImageFromGallery();
            }

            @Override
            public void onPermissionDenied( List<String> deniedPermissions ) {

            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\n" +
                        "Please turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .check();
    }

    private void selectImageFromGallery() {
        TedBottomPicker.with(ChooseImageActivity.this)
                .show(uri -> {
                    mUri = uri;
                    Glide.with(ChooseImageActivity.this)
                            .load(uri).into(ivPhoto);
                });
    }
}
