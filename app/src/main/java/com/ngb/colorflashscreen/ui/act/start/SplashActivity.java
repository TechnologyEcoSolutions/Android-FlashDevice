package com.ngb.colorflashscreen.ui.act.start;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.color.call.flash.screen.themes.R;
import com.ngb.colorflashscreen.ui.act.main.MainActivity;
import com.ngb.colorflashscreen.utils.AdManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        goToMainAct();
    }

    private void goToMainAct() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
