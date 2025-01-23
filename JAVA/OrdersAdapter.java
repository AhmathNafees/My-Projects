package com.example.medi;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private List<Order> orders;
    private FirebaseFirestore db;

    public OrdersAdapter(List<Order> orders, FirebaseFirestore db) {
        this.orders = orders;
        this.db = db;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_items, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);

        // Fetch customer details using customerUid from Firestore
        db.collection("Customers").document(order.getCustomerUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String customerName = documentSnapshot.getString("name");
                        holder.customerName.setText(customerName);
                    }
                });

        holder.customerName.setText(order.getCustomerUid());
        holder.orderDate.setText(order.getDate().toDate().toString());

        // Handle button click to view order details
        holder.viewOrderButton.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), OrderDetailsActivity.class);
            intent.putExtra("orderId", order.getOrderId());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView customerName, orderDate;
        Button viewOrderButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customerName);
            orderDate = itemView.findViewById(R.id.orderDate);
            viewOrderButton = itemView.findViewById(R.id.viewOrderButton);
        }
    }
}
