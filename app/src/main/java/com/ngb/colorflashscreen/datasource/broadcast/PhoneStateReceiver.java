package com.ngb.colorflashscreen.datasource.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import androidx.core.content.ContextCompat;

import com.ngb.colorflashscreen.ui.act.call.RingingWindow;

public class PhoneStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive( Context context, Intent intent ) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged( int state, String phoneNumber ) {
                super.onCallStateChanged(state, phoneNumber);
                Intent intent1 = new Intent(context, RingingWindow.class);
                intent1.putExtra("phoneNumber", phoneNumber);
                intent1.putExtra("inputExtra", "Screen rights");
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING:
                       intent1.setAction("RINGING");
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        intent1.setAction("IDLE");
                        break;
                    default:
                        break;
                }
                ContextCompat.startForegroundService(context, intent1);
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }
}
