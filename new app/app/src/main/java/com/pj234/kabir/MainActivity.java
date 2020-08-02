package com.pj234.kabir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;
import de.nitri.gauge.Gauge;


public class MainActivity extends AppCompatActivity {

    TelephonyManager mTelephonyManager;
    MyPhoneStateListener mPhoneStatelistener;
    int mSignalStrength,signal;
    int times;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPhoneStatelistener = new MyPhoneStateListener();
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mTelephonyManager.listen(mPhoneStatelistener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        times=0;
    }


    public void gotodetials(View view) {
        Intent intent = new Intent(this, detials.class);
        startActivity(intent);
    }

    public void gotoneedle(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void gotomap(View view) {
        Intent intent = new Intent(this, map.class);
        startActivity(intent);
    }


    private class MyPhoneStateListener extends PhoneStateListener {
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            final Gauge gauge = (Gauge)findViewById(R.id.gauge);

            mSignalStrength = signalStrength.getGsmSignalStrength();
            signal = (2 * mSignalStrength) - 113; // -> dBm
            TextView ss = findViewById(R.id.ss);
            ss.setText(String.valueOf(mSignalStrength));
            times=times+1;
            TextView aa =findViewById(R.id.aa);
            aa.setText(String.valueOf(times));
            TextView bb =findViewById(R.id.bb);
            bb.setText(String.valueOf(signal));
            gauge.moveToValue(signal);



        }

    }
}