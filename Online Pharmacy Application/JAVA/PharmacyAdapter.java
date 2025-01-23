package com.example.medi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PharmacyAdapter extends RecyclerView.Adapter<PharmacyAdapter.PharmacyViewHolder> {

    private List<Pharmacy> pharmacyList;
    private Context context;
    private double userLatitude;
    private double userLongitude;

    public PharmacyAdapter(Context context, List<Pharmacy> pharmacyList, double userLatitude, double userLongitude) {
        this.context = context;
        this.pharmacyList = pharmacyList;
        this.userLatitude = userLatitude;
        this.userLongitude = userLongitude;
    }

    @Override
    public PharmacyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pharmacy_item, parent, false);
        return new PharmacyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PharmacyViewHolder holder, int position) {
        Pharmacy pharmacy = pharmacyList.get(position);
        holder.pharmacyName.setText(pharmacy.getName());
        holder.contactNumber.setText(pharmacy.getPhone());

        holder.navButton.setOnClickListener(v -> {
            double pharmacyLatitude = pharmacy.getLocation().getLatitude();
            double pharmacyLongitude = pharmacy.getLocation().getLongitude();
            openMapForDirections(pharmacyLatitude, pharmacyLongitude);
        });

        holder.placebtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, PlaceOrderActivity.class);
            intent.putExtra("pharmacyName", pharmacy.getName());
            intent.putExtra("pharmacyPhone", pharmacy.getPhone());
            intent.putExtra("pharmacyUid", pharmacy.getId());
            Log.d("PharmacyAdapter", "Passing Pharmacy UID: " + pharmacy.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return pharmacyList.size();
    }

    private void openMapForDirections(double pharmacyLatitude, double pharmacyLongitude) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + pharmacyLatitude + "," + pharmacyLongitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(mapIntent);
        } else {
            Toast.makeText(context, "Google Maps is not available", Toast.LENGTH_SHORT).show();
        }
    }

    public static class PharmacyViewHolder extends RecyclerView.ViewHolder {
        public TextView pharmacyName;
        public TextView contactNumber;
        public Button navButton;
        public TextView placebtn;

        public PharmacyViewHolder(View itemView) {
            super(itemView);
            pharmacyName = itemView.findViewById(R.id.pharmacyName);
            contactNumber = itemView.findViewById(R.id.contactNumber);
            navButton = itemView.findViewById(R.id.navButton);
            placebtn = itemView.findViewById(R.id.placeTxte);
        }
    }
}
