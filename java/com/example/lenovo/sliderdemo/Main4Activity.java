package com.example.lenovo.sliderdemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import android.Manifest;



public class Main4Activity extends AppCompatActivity implements View.OnClickListener {


    private String fulladdress;
    private Uri fullPhotoUri;
    private static final int REQUEST_CODE = 1000;
    private TextView textview;
    private EditText issue;
    private LocationManager locationManager;
    Button start;
    Context context;
    boolean GpsStatus;

    Double latitude,longitude;


    Geocoder geocoder;
    List<Address> addresses;


    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {

                    }

                }
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        textview = (TextView) findViewById(R.id.lat);
        issue = (EditText)findViewById(R.id.text);
        geocoder = new Geocoder(this, Locale.getDefault());
        CheckGpsStatus();

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        } else {
            buildLocationRequest();
            buildLocationCallBack();

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            if (ActivityCompat.checkSelfPermission(Main4Activity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Main4Activity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Main4Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                return;
            }
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            //start.setEnabled(!start.isEnabled());
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);
            layout.setBackgroundColor(Color.rgb(0, 0, 0));
            Button btn = (Button) findViewById(R.id.button3);
            //final Uri fullPhotoUri = null;

            btn.setOnClickListener(this);
        }
    }

    private void CheckGpsStatus() {
        locationManager=(LocationManager)Main4Activity.this.getSystemService(Context.LOCATION_SERVICE);
        GpsStatus=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(GpsStatus==true)
        {
            Toast.makeText(this, "GPS Enable", Toast.LENGTH_SHORT).show();
        }else if(GpsStatus==false)
        {
            Intent intent1=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent1);
        }
    }

    private void buildLocationCallBack() {

        locationCallback=new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for(Location location:locationResult.getLocations()){
                    latitude=location.getLatitude();
                    longitude=location.getLongitude();

                    try {
                        addresses=geocoder.getFromLocation(latitude,longitude,1);

                        String address=addresses.get(0).getAddressLine(0);
                        String area=addresses.get(0).getLocality();
                        String city=addresses.get(0).getAdminArea();
                        String country=addresses.get(0).getCountryName();
                        String postalcode=addresses.get(0).getPostalCode();

                        fulladdress=address+","+area+","+city+","+country+","+postalcode;

                        textview.setText(fulladdress);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }


                // textview.setText(String.valueOf(location.getLatitude())+"/"+String.valueOf(location.getLongitude()));

            }
        };
    }

    @SuppressLint("RestrictedApi")
    private void buildLocationRequest() {

        locationRequest=new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10);
    }

    static final int REQUEST_IMAGE_OPEN = 1;

    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // Only the system receives the ACTION_OPEN_DOCUMENT, so no need to test.
        startActivityForResult(intent, REQUEST_IMAGE_OPEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_OPEN && resultCode == RESULT_OK) {
            Uri fullPhotoUri = data.getData();
            // Do work with full size photo saved at fullPhotoUri
        }
    }
    @Override
    public void onClick(View v) {

        String additionalIssue = (String) issue.getText().toString();
        String address = (String) textview.getText();
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"kavimitraisalso@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT,address);
        intent.putExtra(Intent.EXTRA_STREAM,fullPhotoUri );
        intent.putExtra(Intent.EXTRA_TEXT, additionalIssue+"\n This mail is sent by using Esafaai");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);

    }
}


}
