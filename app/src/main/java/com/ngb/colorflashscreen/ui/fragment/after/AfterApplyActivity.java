package com.ngb.colorflashscreen.ui.fragment.after;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.color.call.flash.screen.themes.R;
import com.ngb.colorflashscreen.ui.act.main.MainActivity;
import com.ngb.colorflashscreen.utils.App;

import java.io.IOException;

public class AfterApplyActivity extends AppCompatActivity {
    private String image;

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_after_apply);
        initViews();
    }

    private void initViews() {
        setData();

        findViewById(R.id.bt_back_list).setOnClickListener(v -> {
            Intent intent = new Intent(AfterApplyActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void setData() {
        ImageView ivScreen = findViewById(R.id.iv_screen_success);
        if (App.getInstance().getCheck("CHECK")) {
            image = App.getInstance().getUri("PHOTO");
            if (image != null) {
                Uri uri = Uri.parse(image);
                Glide.with(this).load(uri).into(ivScreen);
                Log.e("aaa", "true " + image);
            }
        } else if (!App.getInstance().getCheck("CHECK")) {
            image = App.getInstance().getPref("IMAGE");
            if (image != null) {
                try {
                    Glide.with(this).load(BitmapFactory.decodeStream(this.getAssets()
                            .open(image))).into(ivScreen);
                    Log.e("aaa", "false " + image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}