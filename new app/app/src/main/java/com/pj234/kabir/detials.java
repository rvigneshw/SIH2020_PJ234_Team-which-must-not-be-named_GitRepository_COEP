package com.pj234.kabir;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.widget.TextView;

import static android.telephony.TelephonyManager.PHONE_TYPE_CDMA;
import static android.telephony.TelephonyManager.PHONE_TYPE_GSM;

public class detials extends AppCompatActivity {
    int lac,cellID,mnc,mcc,linkSpeed;
    String simtype,wifi;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detials);

        genearl();


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void genearl() {

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        String simCountryIso = tm.getSimCountryIso();
        String simOperator = tm.getSimOperator();
        String simOperatorName = tm.getSimOperatorName();

        TextView noofsim = (TextView)findViewById(R.id.nosim);
        TextView CountryIso = (TextView)findViewById(R.id.simCountryIso);
        TextView Operator = (TextView)findViewById(R.id.simOperator);
        TextView OperatorName = (TextView)findViewById(R.id.simOperatorName);
        TextView type = (TextView)findViewById(R.id.simtype);
        TextView device = (TextView)findViewById(R.id.deviceid);
        TextView lactv = (TextView)findViewById(R.id.lac);
        TextView cellIDtv = (TextView)findViewById(R.id.cellID);
        TextView mnctv = (TextView)findViewById(R.id.mnc);
        TextView mcctv = (TextView)findViewById(R.id.mcc);
        TextView wifitv = (TextView)findViewById(R.id.wifi);
        TextView linkSpeedtv = (TextView)findViewById(R.id.linkSpeed);

        int nosim = tm.getPhoneCount();
        String deviceid = tm.getDeviceId();
        String deviceid1 = tm.getDeviceId(0);
        String deciceid2 = tm.getDeviceId(1);


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
                GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();
                cellID = location.getCid();
                lac= location.getLac();
                simtype="GSM";
                String networkOperator = tm.getNetworkOperator();

                if (!TextUtils.isEmpty(networkOperator)) {
                    mcc = Integer.parseInt(networkOperator.substring(0, 3));
                    mnc = Integer.parseInt(networkOperator.substring(3));
                }

                break;
            }
            case PHONE_TYPE_CDMA: {
                // Handle CDMA phone
//                CdmaCellLocation location = (CdmaCellLocation) tm.getCellLocation();
//                cellID = location.getBaseStationId();
//                lat = location.getBaseStationLatitude();
//                lon = location.getBaseStationLongitude();
//                netID = location.getNetworkId();
//                sysID = location.getSystemId();
                simtype="CDMA";
                break;
            }
            default: {
                // can't do cell location
            }}
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            // Do whatever
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                linkSpeed = wifiInfo.getLinkSpeed();
                wifi="yes";//measured using WifiInfo.LINK_SPEED_UNITS
            }
        }

        noofsim.setText(String.valueOf(nosim));
        CountryIso.setText(String.valueOf(simCountryIso));
        Operator.setText(String.valueOf(simOperator));
        OperatorName.setText(String.valueOf(simOperatorName));
        type.setText(String.valueOf(simtype));
        device.setText(String.valueOf(deviceid));
        lactv.setText(String.valueOf(lac));
        cellIDtv.setText(String.valueOf(cellID));
        mnctv.setText(String.valueOf(mnc));
        mcctv.setText(String.valueOf(mcc));
        wifitv.setText(String.valueOf(wifi));
        linkSpeedtv.setText(String.valueOf(linkSpeed +"Mbps"));

    }
}