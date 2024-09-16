package com.ngb.colorflashscreen.ui.act.gallery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.color.call.flash.screen.themes.R;
import com.ngb.colorflashscreen.ui.act.main.MainActivity;
import com.ngb.colorflashscreen.utils.App;

public class DoneScreenAct extends AppCompatActivity {

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_screen);

        ImageView ivPhoto = findViewById(R.id.iv_photo);
        if (App.getInstance().getCheck("CHECK")) {
            String image = App.getInstance().getUri("PHOTO");
            Uri uri = Uri.parse(image);
            Glide.with(DoneScreenAct.this).load(uri).into(ivPhoto);
        }

        findViewById(R.id.bt_back_home).setOnClickListener(v -> {
            Intent intent = new Intent(DoneScreenAct.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
