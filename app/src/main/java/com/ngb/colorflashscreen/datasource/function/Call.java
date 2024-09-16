package com.ngb.colorflashscreen.datasource.function;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telecom.TelecomManager;

import com.ngb.colorflashscreen.utils.App;

public class Call {
    @SuppressLint("StaticFieldLeak")
    private static Call instance;

    private final Context context;

    private Call() {
        context = App.getContext();
    }

    public static Call getInstance() {
        if (instance == null) {
            instance = new Call();
        }
        return instance;
    }

    public void answer() {
        try {
            TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
            assert telecomManager != null;
            telecomManager.getClass().getMethod("acceptRingingCall").invoke(telecomManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismiss() {
        try {
            TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
            assert telecomManager != null;
            telecomManager.getClass().getMethod("endCall").invoke(telecomManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
