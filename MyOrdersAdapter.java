package com.example.medi;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.OrderViewHolder> {

    private final List<MyOrderModel> orders;
    private final Context context;

    public MyOrdersAdapter(List<MyOrderModel> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.myorder_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        MyOrderModel order = orders.get(position);

        // Fetch pharmacy name using the reference
        DocumentReference pharmacyRef = order.getPharmacy();
        FirebaseFirestore.getInstance().document(pharmacyRef.getPath())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String pharmacyName = documentSnapshot.getString("name");
                        holder.pharmacyName.setText(pharmacyName != null ? pharmacyName : "Unknown Pharmacy");
                    } else {
                        holder.pharmacyName.setText("Unknown Pharmacy");
                    }
                });

        // Set order ID
        holder.orderId.setText("Order ID: " + order.getOrderId());

        // Set order status with color
        String status = order.getStatus();
        holder.orderStatus.setText("Status: " + status);
        if ("Pending".equalsIgnoreCase(status)) {
            holder.orderStatus.setTextColor(Color.BLUE);
        } else if ("Cancelled".equalsIgnoreCase(status)) {
            holder.orderStatus.setTextColor(Color.RED);
        } else {
            holder.orderStatus.setTextColor(Color.GREEN);
        }

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView pharmacyName, orderId, orderStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            pharmacyName = itemView.findViewById(R.id.pharmacyName);
            orderId = itemView.findViewById(R.id.orderId);
            orderStatus = itemView.findViewById(R.id.orderStatus);
        }
    }
}

