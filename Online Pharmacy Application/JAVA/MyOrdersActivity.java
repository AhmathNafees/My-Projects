package com.example.medi;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersActivity extends AppCompatActivity {

    private RecyclerView ordersRecyclerView;
    private MyOrdersAdapter ordersAdapter; // Updated Adapter Name
    private List<MyOrderModel> ordersList = new ArrayList<>(); // Updated Model Name
    private FirebaseFirestore db;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        db = FirebaseFirestore.getInstance();

        fetchOrders();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Refresh the orders list
            fetchOrders();

            // Stop the refreshing animation
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void fetchOrders() {
        String customerUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("Orders")
                .whereEqualTo("customerUid", customerUid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ordersList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        MyOrderModel order = doc.toObject(MyOrderModel.class); // Updated Model Class
                        order.setOrderId(doc.getId());
                        ordersList.add(order);
                    }
                    ordersAdapter = new MyOrdersAdapter(ordersList, this); // Updated Adapter
                    ordersRecyclerView.setAdapter(ordersAdapter);
                })
                .addOnFailureListener(e -> Log.e("MyOrdersActivity", "Error fetching orders", e));
    }
}
