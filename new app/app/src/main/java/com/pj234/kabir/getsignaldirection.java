package com.pj234.kabir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.TextView;

public class getsignaldirection extends AppCompatActivity  {

    private CompassSensorManager compassSensorManager;
    private static Location userlocation,bstloction;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getsignaldirection);
        String passedArg = getIntent().getExtras().getString("location");
//        enteredValue.setText(passedArg);

        compassSensorManager = new CompassSensorManager(this);

        CompassView compassView = new CompassView(this);
        compassView.init(compassSensorManager, userlocation, bstloction, R.drawable.icon_arrow);


    }

    @Override
    protected void onResume() {
        super.onResume();
        compassSensorManager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        compassSensorManager.onPause();
    }



}
