package com.pj234.kabir;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import de.nitri.gauge.Gauge;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.telephony.CellSignalStrength.SIGNAL_STRENGTH_GOOD;
import static android.telephony.CellSignalStrength.SIGNAL_STRENGTH_GREAT;
import static android.telephony.CellSignalStrength.SIGNAL_STRENGTH_MODERATE;
import static android.telephony.CellSignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
import static android.telephony.CellSignalStrength.SIGNAL_STRENGTH_POOR;
import static android.telephony.TelephonyManager.PHONE_TYPE_CDMA;
import static android.telephony.TelephonyManager.PHONE_TYPE_GSM;
import static com.pj234.kabir.MapsActivity.MY_PERMISSIONS_REQUEST_LOCATION;


public class MainActivity extends AppCompatActivity {

    TelephonyManager mTelephonyManager;
    MyPhoneStateListener mPhoneStatelistener;
    int mSignalStrength, signal,mcc,mnc;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    Button usermap;
    int locationcheck=0;
    TextView txtJson;
    public int cellid,dbm;
    int lac;
    String platitude,plogitude,bstlati,bstlongi,nettype;
    public int times;
    public static Location userlocation,bstlocation;
    Double upsSpeed,downsSpeed;


    ProgressDialog pd;


    //the client
    private FusedLocationProviderClient mFusedLocationClient;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setup();
        senddata();
        final Handler handler = new Handler();
        final int delay = 3000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                //do something
                senddata();
                handler.postDelayed(this, delay);
            }
        }, delay);

//        Button buttonOne = (Button) findViewById(R.id.signaldirection);
//        buttonOne.setOnClickListener(new Button.OnClickListener() {
//            public void onClick(View v) {
//
//            }
//        });

    }
public void setup(){
    if (allpermgrant()) {

        mPhoneStatelistener = new MyPhoneStateListener();
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mTelephonyManager.listen(mPhoneStatelistener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getgeneral();
//        final Handler handler = new Handler();
//        final int delay = 30000; //milliseconds
//
//        handler.postDelayed(new Runnable(){
//            public void run(){
//                //do something
//                senddata();
//                handler.postDelayed(this, delay);
//            }
//        }, delay);



        if(dbm>-90){
            TextView healthtip = findViewById(R.id.healthtip);
            healthtip.setText("Your signal strength is very weak so,please make calls later as low signal strength produce more cell radiation");

        }
        else{
            TextView healthtip = findViewById(R.id.healthtip);
            healthtip.setText("Signal strength is good,It's safe to make a call");
        }




    }
    else{
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
//            Intent i = new Intent(this,exit.class);
//            startActivity(i);
        setup();

    }

}
    private void getgeneral() {
        String simCountryIso = mTelephonyManager.getSimCountryIso();
        String simOperator = mTelephonyManager.getSimOperator();
        String simOperatorName = mTelephonyManager.getSimOperatorName();
        String imei = mTelephonyManager.getDeviceId();
        String MNC = String.valueOf(mnc);
        String MCC = String.valueOf(mcc);
        String operatorcode = MNC+MCC;
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            // Do whatever
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                int linkSpeed = wifiInfo.getLinkSpeed();
                String wifi = String.valueOf(linkSpeed);
                String wificon="yes";//measured using WifiInfo.LINK_SPEED_UNITS
            }
        }

    }

    public void senddata(){
         String COUNTRY_CODE = mTelephonyManager.getSimCountryIso();
         String OPERATOR_CODE = mTelephonyManager.getSimOperator();
        String OPERATOR_NAME = mTelephonyManager.getSimOperatorName();
         String IMEI = mTelephonyManager.getDeviceId();
         String MNC = String.valueOf(mnc);
         String MCC = String.valueOf(mcc);
         String LAC = String.valueOf(lac);
         String NETWORK_SPEED_UP = String.valueOf(upsSpeed);
         String NETWORK_SPEED_DOWN = String.valueOf(downsSpeed);
         String SIGNAL_STRENGTH = String.valueOf(dbm);
         String BST_LAT = bstlati;
         String BST_LON = bstlongi;
         String LATITUDE = platitude;
         String LONGITUDE = plogitude;
         String NETWORK_TYPE = nettype;
        String WIFI="";

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            // Do whatever
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                int linkSpeed = wifiInfo.getLinkSpeed();
                 WIFI = String.valueOf(linkSpeed);
                String wificon="yes";//measured using WifiInfo.LINK_SPEED_UNITS
            }
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("LATITUDE", LATITUDE);
            jsonObject.put("LONGITUDE", LONGITUDE);
            jsonObject.put("LAC", LAC);
            jsonObject.put("MCC", MCC);
            jsonObject.put("MNC", MNC);
            jsonObject.put("BST_LAT", BST_LAT);
            jsonObject.put("BST_LON", BST_LON);
            jsonObject.put("SIGNAL_STRENGTH", SIGNAL_STRENGTH);
            jsonObject.put("NETWORK_TYPE", NETWORK_TYPE);
            jsonObject.put("OPERATOR_NAME", OPERATOR_NAME);
            jsonObject.put("NETWORK_SPEED_UP", NETWORK_SPEED_UP);
            jsonObject.put("NETWORK_SPEED_DOWN", NETWORK_SPEED_DOWN);
            jsonObject.put("COUNTRY_CODE", COUNTRY_CODE);
            jsonObject.put("OPERATOR_CODE", OPERATOR_CODE);
            jsonObject.put("IMEI", IMEI);
            jsonObject.put("WIFI", WIFI);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        // put your json here
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url("https://api-dps-sih.herokuapp.com/prod/add")
                .post(body)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String resStr = response.body().string();
            Log.e("api",resStr);
            Log.e("heroku",body.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }




    }
//    public int getGsmLevel() {
//        int level;
//        // ASU ranges from 0 to 31 - TS 27.007 Sec 8.5
//        // asu = 0 (-113dB or less) is very weak
//        // signal, its better to show 0 bars to the user in such cases.
//        // asu = 99 is a special case, where the signal strength is unknown.
//        int asu = getGsmSignalStrength();
//        if (asu <= 2 || asu == 99) level = SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
//        else if (asu >= 12) level = SIGNAL_STRENGTH_GREAT;
//        else if (asu >= 8)  level = SIGNAL_STRENGTH_GOOD;
//        else if (asu >= 5)  level = SIGNAL_STRENGTH_MODERATE;
//        else level = SIGNAL_STRENGTH_POOR;
//        if (DBG) log("getGsmLevel=" + level);
//        return level;
//    }

private class MyPhoneStateListener extends PhoneStateListener {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            times=times+1;
            super.onSignalStrengthsChanged(signalStrength);
            final Gauge gauge = (Gauge)findViewById(R.id.gauge);

//            mSignalStrength = signalStrength.getGsmSignalStrength();
//            signal = (2 * mSignalStrength) - 113; // -> dBm
//
//            TextView ss = findViewById(R.id.bb);
//            ss.setText(String.valueOf(mSignalStrength));
//            TextView bb =findViewById(R.id.ss);
//            bb.setText("SIGNAL  :"+String.valueOf(signal)+"db");

            if (signalStrength.isGsm()) {

                if (signalStrength.getGsmSignalStrength() <= 2 ||
                        signalStrength.getGsmSignalStrength() == NeighboringCellInfo.UNKNOWN_RSSI) {
                    // Unknown signal strength, get it another way
                    String[] bits = signalStrength.toString().split(" ");
                    dbm = Integer.parseInt(bits[9]);
                } else {
                    dbm = signalStrength.getGsmSignalStrength();
                }
//                mDevice.setSignalDbm(dbm);
                TextView ss = findViewById(R.id.ss);
                ss.setText("SIGNAL:"+String.valueOf(dbm));
                gauge.moveToValue(dbm);
            } else {
                int evdoDbm = signalStrength.getEvdoDbm();
                int cdmaDbm = signalStrength.getCdmaDbm();
                // Use lowest signal to be conservative
//                mDevice.setSignalDbm((cdmaDbm < evdoDbm) ? cdmaDbm : evdoDbm);
                TextView ss = findViewById(R.id.ss);
                if(cdmaDbm<evdoDbm){
                    dbm=cdmaDbm;
                    ss.setText("SIGNAL:"+String.valueOf(dbm));
                    gauge.moveToValue(dbm);
                }
                else{
                    dbm=evdoDbm;
                    ss.setText("SIGNAL:"+String.valueOf(dbm));
                    gauge.moveToValue(dbm);
                }

            }
            // Send it to signal tracker
//            signalStrengthTracker.registerSignalStrength(mDevice.mCell.getCID(), mDevice.getSignalDBm());
            //signalStrengthTracker.isMysterious(mDevice.mCell.getCID(), mDevice.getSignalDBm());
//            gauge.moveToValue(signal);
            getlocat();
            getnetupdown();

            getphonetype();
            if (times<2){
                try {
                    getbasestationlocation();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//            senddata();




        }

    }

    private void getphonetype() {
        switch (mTelephonyManager.getPhoneType()) {

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
                GsmCellLocation location = (GsmCellLocation) mTelephonyManager.getCellLocation();
                cellid = location.getCid();
                lac= location.getLac();
                nettype = "GSM";


                break;
            }
            case PHONE_TYPE_CDMA: {
                nettype="CDMA";

                break;
            }
            default: {
                // can't do cell location
            }}
    }

    private void getbasestationlocation() throws IOException {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String networkOperator = tm.getNetworkOperator();

        if (!TextUtils.isEmpty(networkOperator)) {
            mcc = Integer.parseInt(networkOperator.substring(0, 3));
            mnc = Integer.parseInt(networkOperator.substring(3));

            getHttpResponse();
        }

    }
    public void getHttpResponse() throws IOException {

        String url = "https://api.mylnikov.org/geolocation/cell?v=1.1&data=open&mcc="+mcc+"&mnc="+mnc+"&lac="+lac+"&cellid="+cellid;
        Log.e("http",url);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

//        Response response = client.newCall(request).execute();
//        Log.e(TAG, response.body().string());

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
                //call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String mMessage = response.body().string();

                Log.e("value u got :", mMessage);
                JSONObject obj = null;
                JSONObject obj1 = null;
                try {
                    obj = new JSONObject(mMessage);
                    String data = obj.getString("data");
                    obj1 = new JSONObject(data);
                     bstlati = obj1.getString("lat");
                     bstlongi = obj1.getString("lon");
//                    bstlocation.setLatitude(Double.parseDouble(bstlati));
//                    bstlocation.setLongitude(Double.parseDouble(bstlongi));
                    Log.e("value of latitude :", bstlati);
                    Log.e("value of longitude :", bstlongi);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("My App", obj.toString());
            }
        });
    }




    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getnetupdown() {

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkCapabilities nc = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        double downSpeed = nc.getLinkDownstreamBandwidthKbps();
        double upSpeed = nc.getLinkUpstreamBandwidthKbps();
//        final Gauge gauge1 = (Gauge) findViewById(R.id.gauge3);
//        final Gauge gauge2 = (Gauge) findViewById(R.id.gauge4);


        TextView netup = findViewById(R.id.aa);
        netup.setText("UPSPEED  :" + (int) upSpeed + " bytes/sec");
        TextView netdown = findViewById(R.id.bb);
        netdown.setText("DOWNSPEED  :" + (int) downSpeed + " bytes/sec");
        downsSpeed = (Double) (downSpeed / 1024);
        upsSpeed = (Double) (upSpeed / 1024);


//        gauge1.moveToValue((float) upsSpeed);
//        gauge2.moveToValue((float) downsSpeed);


    }

    private void getlocat() {
//        Toast.makeText(getApplicationContext(), "getLocation", Toast.LENGTH_LONG).show();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //request the last location and add a listener to get the response. then update the UI.
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location.
                    TextView locationText = findViewById(R.id.cc);
                    if (location != null) {
                        locationText.setText("latitude: " + location.getLatitude()+"\nlongitude:  "+location.getLongitude());
                        platitude=String.valueOf(location.getLatitude());
                        plogitude=String.valueOf(location.getLongitude());
                       userlocation=location;
                        locationcheck=1;
                    }
                    else if(location == null){
                        locationText.setText("your location is loading " +
                                "and please on your location form settings");

                    }
                    else {
                        locationText.setText("location: IS NULL");
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "getLocation ERROR", Toast.LENGTH_LONG).show();
            TextView locationText = findViewById(R.id.cc);
            locationText.setText("location: ERROR");
        }

    }

    private boolean allpermgrant() {



        //check the location permissions and return true or false.
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) {
            //permissions granted
//            Toast.makeText(getApplicationContext(), "permissions granted", Toast.LENGTH_LONG).show();
            return true;
        } else {
            // No explanation needed, we can request the permission.
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
//            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED){
//                return true;
            return  false;


        }

    }





    public void gotodetials(View view) {
        Intent intent = new Intent(this, detials.class);
        startActivity(intent);
    }

    public void getsignaldirection(View view){
        Intent intent = new Intent(this,getsignaldirection.class);

        intent.putExtra("ulat", userlocation.getLatitude()); // getText() SHOULD NOT be static!!!
        intent.putExtra("ulon", userlocation.getLongitude()); // getText() SHOULD NOT be static!!!

        startActivity(intent);

    }

    public void gotoneedle(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        if(locationcheck != 0){
//            intent.putExtra("blat",bstlati);
//            intent.putExtra("blon",bstlongi);
            startActivity(intent);

        }
        else{
            Toast.makeText(getApplicationContext(), "your location is loading", Toast.LENGTH_LONG).show();


        }

    }

    public void gotomap(View view) {
        Intent intent = new Intent(this, map.class);
        startActivity(intent);
    }

    public static Location getuserlocation() {
        return userlocation;

    }
    public static Location getbstlocation() {
        return bstlocation;

    }
    private class SendfeedbackJob extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String[] params) {
            senddata();
            return "some message";
        }

        @Override
        protected void onPostExecute(String message) {
            //process message
        }
    }



}