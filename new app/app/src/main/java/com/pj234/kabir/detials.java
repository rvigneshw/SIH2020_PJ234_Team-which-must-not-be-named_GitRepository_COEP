package com.pj234.kabir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class detials extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detials);

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://dps-sih.herokuapp.com/index.php?T=VS")));

    }
}