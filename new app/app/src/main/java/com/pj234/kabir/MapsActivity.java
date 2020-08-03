package com.pj234.kabir;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public Location lastLocation;
    private static String loca;
//    String bstlat = getIntent().getExtras().getString("blat");
//    Double bstlati=Double.parseDouble(bstlat);
//    String bstlog = getIntent().getExtras().getString("blog");
//    Double bstlogi=Double.parseDouble(bstlog);




    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    //the client
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        lastLocation=null;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (checkLocationPermission()) {
            getLocation();
        }

    }

    private boolean checkLocationPermission() {
        //check the location permissions and return true or false.
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //permissions granted
            Toast.makeText(getApplicationContext(), "permissions granted", Toast.LENGTH_LONG).show();
            return true;
        } else {
            //permissions NOT granted
            //if permissions are NOT granted, ask for permissions
            Toast.makeText(getApplicationContext(), "Please enable permissions", Toast.LENGTH_LONG).show();
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Permissions request")
                        .setMessage("we need your permission for location in order to show you this example")
                        .setPositiveButton("Ok, I agree", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        }
    }
    public void getLocation() {
        Toast.makeText(getApplicationContext(), "getLocation", Toast.LENGTH_LONG).show();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //request the last location and add a listener to get the response. then update the UI.
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location.
//                    TextView locationText = findViewById(R.id.locationtext);
                    if (location != null) {
                        Log.d("location",location.toString());
                        loca = location.toString();
                        LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(sydney).title("you are HERE"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//                        LatLng bst = new LatLng(bstlati, bstlogi);
//
//                        mMap.addMarker(new MarkerOptions().position(bst).title("you are HERE"));
//                        mMap.moveCamera(CameraUpdateFactory.newLatLng(bst));
lastLocation=location;
//                        locationText.setText("location: " + location.toString());
                    } else {
//                        locationText.setText("location: IS NULL");
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "getLocation ERROR", Toast.LENGTH_LONG).show();
//            TextView locationText = findViewById(R.id.locationtext);
//            locationText.setText("location: ERROR");
        }
    }

    public static String getloca(){
        return loca;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(32, 13);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("you are here"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }
}