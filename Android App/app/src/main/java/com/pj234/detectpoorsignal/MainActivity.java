package com.pj234.detectpoorsignal;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import de.nitri.gauge.Gauge;

import static android.Manifest.permission.READ_PHONE_STATE;
import static android.telephony.TelephonyManager.PHONE_TYPE_CDMA;
import static android.telephony.TelephonyManager.PHONE_TYPE_GSM;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE =200 ;
    TelephonyManager tm;
    MyPhoneStateListener MyListener;
    private static int signal_strength;
    public String phonetype;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.start);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tm.listen(MyListener,PhoneStateListener.LISTEN_SIGNAL_STRENGTH);

            }

        });


        MyListener = new MyPhoneStateListener();
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        Context context;


        getnetworktype();
        general();
        getphonetype();
        networkspeed();


    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    private void networkspeed() {
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkCapabilities nc = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        int downSpeed = nc.getLinkDownstreamBandwidthKbps();
        int upSpeed = nc.getLinkUpstreamBandwidthKbps();

    }

    private void getphonetype() {
        switch (tm.getPhoneType()) {

            case PHONE_TYPE_GSM: {
                // Handle GSM phone
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                phonetype ="gsm";
                GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();
                int cellID = location.getCid();
                int lac= location.getLac();
                String networkOperator = tm.getNetworkOperator();

                if (!TextUtils.isEmpty(networkOperator)) {
                   int  mcc = Integer.parseInt(networkOperator.substring(0, 3));
                    int mnc = Integer.parseInt(networkOperator.substring(3));
                }

                break;
            }
            case PHONE_TYPE_CDMA: {
                // Handle CDMA phone
                phonetype="cdma";
                CdmaCellLocation location = (CdmaCellLocation) tm.getCellLocation();
                int cellID = location.getBaseStationId();
                double lat = location.getBaseStationLatitude();
                double lon = location.getBaseStationLongitude();
                int netID = location.getNetworkId();
                int sysID = location.getSystemId();
                break;
            }
            default: {
                // can't do cell location
            }}


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void general() {

        if (ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        String softwareVersion = tm.getDeviceSoftwareVersion();
        String voiceMailNumber=tm.getVoiceMailNumber();
        int nosim = tm.getPhoneCount();
        String IMEINumber = tm.getDeviceId();
        String deviceid1 = tm.getDeviceId(0);
        String deciceid2 = tm.getDeviceId(1);
        String SIMSerialNumber = tm.getSimSerialNumber();
        String networkCountryISO = tm.getNetworkCountryIso();
        String SIMCountryISO = tm.getSimCountryIso();
//               TextView net = findViewById(R.id.network);
//        net.setText("no.of.sim's   :"+nosim+
//                "\nIMEMI    :"+IMEINumber+
//                "\ndevice id 1  :"+deviceid1+
//                "\ndevice id 2   :"+deciceid2+
//                "\nSIM serial number"+SIMSerialNumber+
//                "\nnetwork country iso   :"+networkCountryISO+
//                "\nSIM country iso   :"+SIMCountryISO);
    }

    private void getnetworktype() {
        if (ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        int networkType = tm.getNetworkType();
        String netType;

        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                netType="2G";
                return;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                netType= "3G";
                return;
            case TelephonyManager.NETWORK_TYPE_LTE:
                netType="4G";
                return;
            case TelephonyManager.NETWORK_TYPE_NR:
                netType="5G";
                return;
            default:
                netType="Unknown";
        }
//        TextView net= findViewById(R.id.network);
//        net.setText(netType);
    }


    private class MyPhoneStateListener extends PhoneStateListener {

        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);

            final Gauge gauge = (Gauge)findViewById(R.id.gauge);



            if (signalStrength.isGsm()) {
                if (signalStrength.getGsmSignalStrength() != 99)
                    signal_strength = signalStrength.getGsmSignalStrength() * 2 -113;
                else
                    signal_strength = signalStrength.getGsmSignalStrength();
            } else {
                signal_strength = signalStrength.getCdmaDbm();
            }
            TextView signal = findViewById(R.id.signalstrength);
            signal.setText("signal strength :"+signal_strength);
            gauge.moveToValue(signal_strength);


//            signal_strength = signalStrength.getGsmSignalStrength();
//            signal_strength = (2 * signal_strength) - 113; // -> dBm
//            TextView signal =findViewById(R.id.signalstrength);
//            signal.setText(signal_strength);
        }






    }
}