package com.ngb.colorflashscreen.ui.act.call;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.color.call.flash.screen.themes.R;
import com.color.call.flash.screen.themes.databinding.ActivityScreenRingingBinding;
import com.ngb.colorflashscreen.datasource.function.Call;
import com.ngb.colorflashscreen.datasource.model.Contact;
import com.ngb.colorflashscreen.ui.act.main.MainActivity;
import com.ngb.colorflashscreen.utils.App;

import java.io.IOException;

public class RingingWindow extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";

    private WindowManager mWindowManager;
    private View view;

    private LayoutParams paramsRinging;

    private TextView tvName, tvPhone, tvStatus;
    private ImageButton btDismiss, btReject, btAnswer;

    @Override
    public void onCreate() {
        super.onCreate();
        mWindowManager = (WindowManager) this.getSystemService(WINDOW_SERVICE);
        createRingingView();

    }

    private void initViews() {
        tvStatus = view.findViewById(R.id.tv_status);
        tvPhone = view.findViewById(R.id.phone_number);
        tvName = view.findViewById(R.id.tv_name);
        btAnswer = view.findViewById(R.id.bt_answer);
        btAnswer.startAnimation(AnimationUtils.loadAnimation(this, R.anim.bounce));
        btAnswer.setOnClickListener(view -> {
            Call.getInstance().answer();
            initEventAfter();
        });
        btDismiss = view.findViewById(R.id.bt_dismiss);
        btDismiss.setOnClickListener(view -> {
            Call.getInstance().dismiss();
            new Handler(getMainLooper()).postDelayed(this::hide, 1000);
        });
        btReject = view.findViewById(R.id.bt_reject);
        tvStatus = view.findViewById(R.id.tv_status);

    }

    private void initEventAfter() {
        tvStatus.setText(R.string.on_going_call);
        btAnswer.setVisibility(View.INVISIBLE);
        btAnswer.clearAnimation();
        btDismiss.setVisibility(View.INVISIBLE);
        btReject.setVisibility(View.VISIBLE);
        btReject.setOnClickListener(v -> {
            Call.getInstance().dismiss();
            new Handler(getMainLooper()).postDelayed(this::hide, 1000);
        });
    }

    private void createRingingView() {
        ActivityScreenRingingBinding inflate = ActivityScreenRingingBinding.inflate(LayoutInflater.from(this));
        View root = inflate.getRoot();
        view = root;
        root.setVisibility(View.INVISIBLE);
        initViews();
        paramsRinging = new LayoutParams();
        paramsRinging.width = LayoutParams.MATCH_PARENT;
        paramsRinging.height = LayoutParams.MATCH_PARENT;
        paramsRinging.format = PixelFormat.TRANSLUCENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.paramsRinging.type = LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            this.paramsRinging.type = LayoutParams.TYPE_PHONE;
        }
        this.paramsRinging.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
        this.paramsRinging.flags = LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        this.paramsRinging.flags = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        this.paramsRinging.flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        try {
            this.mWindowManager.addView(this.view, this.paramsRinging);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand( Intent intent, int flags, int startId ) {
        if (intent.getAction() == null) {
            String input = intent.getStringExtra("inputExtra");
            createNotificationChannel();
            buildStartNotification(input);
        } else if (intent.getAction().equals("RINGING")) {
            String phoneNumber = intent.getStringExtra("phoneNumber");
            Contact contact = new Contact(phoneNumber);
            setData(contact);
            setScreen();
            show();
        } else if (intent.getAction().equals("IDLE")) {
            Call.getInstance().dismiss();
            tvStatus.setText(R.string.end_call);
            new Handler(getMainLooper()).postDelayed(this::hide, 1000);
        }
        return START_NOT_STICKY;
    }

    private void setScreen() {
        ImageView ivScreen = view.findViewById(R.id.iv_screen_call);
        String image;
        if (App.getInstance().getCheck("CHECK")) {
            image = App.getInstance().getUri("PHOTO");
            if (image != null) {
                Uri uri = Uri.parse(image);
                Glide.with(this).load(uri).into(ivScreen);
            }
        } else if (!App.getInstance().getCheck("CHECK")) {
            image = App.getInstance().getPref("IMAGE");
            if (image != null) {
                try {
                    Glide.with(this).load(BitmapFactory.decodeStream(this.getAssets()
                            .open(image))).into(ivScreen);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setData( Contact contact ) {
        switch (contact.type) {
            case FULL:
                tvName.setText(contact.name);
                tvName.setVisibility(View.VISIBLE);
                tvPhone.setText(contact.number);
                break;

            case HIDDEN:
                tvName.setText(R.string.hidden_number);
                break;

            case JUST_PHONE:
                tvName.setText(R.string.unknown);
                tvPhone.setText(contact.number);
                break;

            default:
                break;
        }
    }

    private void buildStartNotification( String input ) {
        Intent notificationIntent = new Intent(getBaseContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Notification Title")
                .setContentText(input)
                .setShowWhen(false)
                .setSmallIcon(R.drawable.ic_call)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
    }

    @Nullable
    @Override
    public IBinder onBind( Intent intent ) {
        return null;
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    public void hideRingingWindow() {
        view.setVisibility(View.INVISIBLE);
        ActivityScreenRingingBinding inflate = ActivityScreenRingingBinding.inflate(LayoutInflater.from(this));
        View root = inflate.getRoot();
        view = root;
        root.setVisibility(View.INVISIBLE);
        initViews();
        try {
            mWindowManager.addView(view, paramsRinging);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show() {
        new Handler(getMainLooper()).post(() -> view.setVisibility(View.VISIBLE));
    }

    public void hide() {
        try {
            view.post(this::hideRingingWindow);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mWindowManager.removeView(this.view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
