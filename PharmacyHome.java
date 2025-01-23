package com.example.medi;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PharmacyHome extends AppCompatActivity {

    private TextView name;
    private OrdersAdapter ordersAdapter;
    private FirebaseFirestore myStore;
    private FirebaseAuth myAuth;
    private String pharmacyUid;
    private RecyclerView ordersRecyclerView;
    private List<Order> ordersList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    private Button btnPending, btnCanceled, btnConfirmed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_home);

        myAuth = FirebaseAuth.getInstance();
        myStore = FirebaseFirestore.getInstance();

        btnPending = findViewById(R.id.btnPending);
        btnCanceled = findViewById(R.id.btnCanceled);
        btnConfirmed = findViewById(R.id.btnConfirmed);

        FloatingActionButton stock = findViewById(R.id.cameraButton);

        stock.setOnClickListener(v -> {

            startActivity(new Intent(getApplicationContext(), ViewActivity.class));
            finish();

            Toast.makeText(PharmacyHome.this, "Floating button clicked!", Toast.LENGTH_SHORT).show();
        });

        FirebaseUser currentUser = myAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(PharmacyHome.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        pharmacyUid = currentUser.getUid();

        name = findViewById(R.id.pharmacy_name);
        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        // Highlight Pending button and fetch pending orders by default
        highlightButton(btnPending);
        fetchOrders(pharmacyUid, "Pending");

        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Refresh Pending orders by default
            highlightButton(btnPending);
            fetchOrders(pharmacyUid, "Pending");
            swipeRefreshLayout.setRefreshing(false);
        });

        // Set pharmacy name (if stored in Firestore)
        myStore.collection("Pharmacies").document(pharmacyUid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String pharmacyName = documentSnapshot.getString("name");
                        name.setText(pharmacyName != null ? pharmacyName : "Pharmacy");
                    } else {
                        Log.e("PharmacyHome", "Pharmacy document not found");
                    }
                });

        // Overflow menu
        ImageView overflowButton = findViewById(R.id.overflowButton);
        overflowButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, v);
            popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.icon_profile) {
                    startActivity(new Intent(PharmacyHome.this, PharmacyProfileActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.icon_logout) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(PharmacyHome.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.icon_not) {
                    startActivity(new Intent(PharmacyHome.this, MailActivity.class));
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });

        // Button click listeners with color highlighting
        btnPending.setOnClickListener(v -> {
            highlightButton(btnPending);
            fetchOrders(pharmacyUid, "Pending");
        });

        btnCanceled.setOnClickListener(v -> {
            highlightButton(btnCanceled);
            fetchOrders(pharmacyUid, "Cancelled");
        });

        btnConfirmed.setOnClickListener(v -> {
            highlightButton(btnConfirmed);
            fetchOrders(pharmacyUid, "Confirmed");
        });
    }

    private void fetchOrders(String pharmacyUid, String status) {
        myStore.collection("Orders")
                .whereEqualTo("pharmacyUid", pharmacyUid)
                .whereEqualTo("status", status)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("PharmacyHome", "Error fetching orders", error);
                        return;
                    }

                    ordersList.clear();
                    if (value != null) {
                        for (QueryDocumentSnapshot doc : value) {
                            Order order = doc.toObject(Order.class);
                            order.setOrderId(doc.getId());
                            ordersList.add(order);
                        }
                    }

                    if (ordersAdapter == null) {
                        ordersAdapter = new OrdersAdapter(ordersList, myStore);
                        ordersRecyclerView.setAdapter(ordersAdapter);
                    } else {
                        ordersAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void highlightButton(Button selectedButton) {
        // Reset all buttons to default
        resetButtonStyle(btnPending);
        resetButtonStyle(btnCanceled);
        resetButtonStyle(btnConfirmed);

        // Highlight the selected button
        selectedButton.setBackgroundColor(Color.parseColor("#007a8f"));
        selectedButton.setTextColor(Color.WHITE);
    }

    private void resetButtonStyle(Button button) {
        // Reset to default style
        button.setBackgroundColor(Color.WHITE);
        button.setTextColor(Color.parseColor("#007a8f"));
    }
}
