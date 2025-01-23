package com.example.medi;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CustomerHome extends AppCompatActivity {
    private TextView name, pharmacyname, pharmacynumber;
    private Button navigate;
    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView recyclerView;
    private PharmacyAdapter adapter;
    private List<Pharmacy> pharmacyList = new ArrayList<>();
    private FirebaseFirestore myStore;
    private FusedLocationProviderClient fusedLocationClient;
    private double userLatitude;
    private double userLongitude;

    private LocationManager locationManager;

    private String userId;
    private FirebaseAuth myAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_customer_home); // Fixed typo here: acivity -> activity


        name = findViewById(R.id.textView);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        FloatingActionButton scan = findViewById(R.id.cameraButton);

        scan.setOnClickListener(v -> {

            startActivity(new Intent(getApplicationContext(), Home.class));
            finish();

            Toast.makeText(CustomerHome.this, "Floating button clicked!", Toast.LENGTH_SHORT).show();
        });


        myAuth = FirebaseAuth.getInstance();
        myStore = FirebaseFirestore.getInstance();
        userId = myAuth.getCurrentUser().getUid();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Refresh the orders list
            fetchPharmaciesAndSortByLocation();

            // Stop the refreshing animation
            swipeRefreshLayout.setRefreshing(false);
        });


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }


        DocumentReference documentReference = myStore.collection("Customers").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    name.setText(documentSnapshot.getString("name"));
                }
            }
        });

        // Set up the overflow menu
        ImageView overflowButton = findViewById(R.id.overflowButton);
        overflowButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, v); // Use the view (v) for anchoring the popup menu
            popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu()); // Inflate your menu here

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.icon_profile) {
                    Intent intent = new Intent(CustomerHome.this, CustomerProfileActivity.class);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.icon_logout) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(CustomerHome.this, LoginActivity.class);  // Replace LoginActivity with your login screen
                    startActivity(intent);
                    finish();
                }else if (item.getItemId() == R.id.icon_not) {
                    startActivity(new Intent(CustomerHome.this, MailActivity.class));
                    return true;
                }
                return false;
            });

            // Show the menu
            popupMenu.show();
        });

        ImageView OrdersButton = findViewById(R.id.ordersButton);
        OrdersButton.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerHome.this, MyOrdersActivity.class);
            startActivity(intent);
        });
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        userLatitude = location.getLatitude();
                        userLongitude = location.getLongitude();
                        fetchPharmaciesAndSortByLocation();
                    } else {
                        Toast.makeText(CustomerHome.this, "Unable to get location. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchPharmaciesAndSortByLocation() {
        myStore.collection("Pharmacies")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    pharmacyList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Pharmacy pharmacy = document.toObject(Pharmacy.class);
                        pharmacyList.add(pharmacy);
                    }

                    // Sort pharmacies based on proximity to user
                    Collections.sort(pharmacyList, (p1, p2) -> {
                        double distance1 = calculateDistance(userLatitude, userLongitude, p1.getLocation().getLatitude(), p1.getLocation().getLongitude());
                        double distance2 = calculateDistance(userLatitude, userLongitude, p2.getLocation().getLatitude(), p2.getLocation().getLongitude());
                        return Double.compare(distance1, distance2);
                    });

                    adapter = new PharmacyAdapter(CustomerHome.this, pharmacyList, userLatitude, userLongitude);
                    recyclerView.setAdapter(adapter);
                });
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        float[] results = new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);
        return results[0]; // Distance in meters
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
