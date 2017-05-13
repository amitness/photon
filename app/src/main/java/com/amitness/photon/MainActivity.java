package com.amitness.photon;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: Create an alert if flashlight is not present in device
        // TODO: Exit the app if no flashlight
        boolean hasFlash = this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        Log.d("Has Flashlight:", Boolean.toString(hasFlash));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.id_about) {
            //Intent to another activity
            Intent intentAbout=new Intent(MainActivity.this,AboutActivity.class);
            startActivity(intentAbout);
            return true;
        }
        return true;
    }

    public void gotoTransmitActivity(View view) {
        Intent nextPage = new Intent(MainActivity.this, TransmitActivity.class);
        startActivity(nextPage);
    }

    public void gotoReceiveActivity(View view) {
        Intent nextPage = new Intent(MainActivity.this, ReceiveActivity.class);
        startActivity(nextPage);
    }
}
