package com.example.medi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText searchLocation, searchDestination;
    private ImageButton currentLocationButton;
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Initialize UI
        searchLocation = findViewById(R.id.search_location);
        searchDestination = findViewById(R.id.search_destination);
        currentLocationButton = findViewById(R.id.current_location_button);

        // Initialize Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Listener for the current location button
        currentLocationButton.setOnClickListener(v -> fetchCurrentLocation());

        // Request location permissions if not granted
        checkLocationPermissions();

        // Listen for search input
        searchLocation.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchLocation, InputMethodManager.SHOW_IMPLICIT);
        });

        // Set the listener to handle "Enter" key press in searchLocation EditText
        searchLocation.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH ||
                    actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                // Trigger search or other action
                String location = searchLocation.getText().toString();
                if (!location.isEmpty()) {
                    // For now, let's just display a toast
                    Toast.makeText(MapsActivity.this, "Searching for: " + location, Toast.LENGTH_SHORT).show();
                    searchLocation.clearFocus();
                    // Hide the keyboard after search
                    InputMethodManager immHide = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    immHide.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return true;
            }
            return false;
        });

        // Check if location is enabled
        checkLocationEnabled();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    // Function to fetch current location
    private void fetchCurrentLocation() {
        // Use a dummy location for simplicity (real implementation uses FusedLocationProviderClient)
        double latitude = 6.9271; // Example: Colombo
        double longitude = 79.8612;

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                searchLocation.setText(address.getAddressLine(0));
                Toast.makeText(this, "Current location added", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Unable to fetch location", Toast.LENGTH_SHORT).show();
        }
    }

    // Check if location services are enabled
    private void checkLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGpsEnabled && !isNetworkEnabled) {
            showEnableLocationDialog();
        }
    }

    // Prompt the user to enable location services
    private void showEnableLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Location is disabled, please enable it.")
                .setCancelable(false)
                .setPositiveButton("Enable", (dialog, id) -> {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, 1); // Request code for enabling location
                })
                .setNegativeButton("Cancel", (dialog, id) -> {
                    dialog.dismiss();
                });
        builder.create().show();
    }

    // Check location permissions
    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with location fetching
                fetchCurrentLocation();
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied, unable to fetch location", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

