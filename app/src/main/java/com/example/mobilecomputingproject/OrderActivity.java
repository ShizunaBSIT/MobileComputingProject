package com.example.mobilecomputingproject;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

// location
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import android.location.Location;
import com.google.android.gms.tasks.OnSuccessListener;
import android.location.Geocoder;
import android.location.Address;
import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class OrderActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private FusedLocationProviderClient fusedLocationClient;

    TextView ProductNameTxt;
    EditText recipientInput, addressInput;
    Button placeOrderBtn, addressAutoBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        ProductNameTxt = findViewById(R.id.productName);
        recipientInput = findViewById(R.id.recipientInput);
        addressInput = findViewById(R.id.addressInput);
        placeOrderBtn = findViewById(R.id.placeOrderBtn);
        addressAutoBtn = findViewById(R.id.addressAutoBtn);

        // retrieve intent
        Intent intent = getIntent();

        // le location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // intent data retrieval and display
        if (intent != null) {
            String productName = intent.getStringExtra("productName");

            ProductNameTxt.setText(productName);
        }

        addressAutoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(OrderActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(OrderActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                }
                else {
                    getCurrentLocationAndSetAddress();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocationAndSetAddress();
            }
            else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void getCurrentLocationAndSetAddress() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if ( location != null ) {
                            Geocoder geocoder = new Geocoder(OrderActivity.this, Locale.getDefault());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                if (addresses != null && !addresses.isEmpty()) {
                                    Address address = addresses.get(0);
                                    String addressString = address.getAddressLine(0);
                                    EditText addressInput = findViewById(R.id.addressInput);
                                    addressInput.setText(addressString);
                                }
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(OrderActivity.this, "Error getting address", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            addressInput.setText("Location not available");
                        }

                    }
                });
    }

}