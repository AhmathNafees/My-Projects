package com.example.medi;

import android.content.Context;
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

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {

    private List<Medicine> medicineList;
    private Context context;

    public MedicineAdapter(List<Medicine> medicineList, Context context) {
        this.medicineList = medicineList;
        this.context = context;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.medicine_item, parent, false);
        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine medicine = medicineList.get(position);
        holder.nameTextView.setText(medicine.getName());
        holder.brandTextView.setText(medicine.getBrand());
        holder.weightTextView.setText(medicine.getWeight());

        holder.deleteButton.setOnClickListener(v -> deleteMedicine(medicine.getId()));
        holder.updateButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddMedicineActivity.class);
            intent.putExtra("medicineId", medicine.getId());
            intent.putExtra("name", medicine.getName());
            intent.putExtra("brand", medicine.getBrand());
            intent.putExtra("weight", medicine.getWeight());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    private void deleteMedicine(String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("medicines").document(id)
                .delete()
                .addOnSuccessListener(aVoid -> notifyDataSetChanged());
    }

    static class MedicineViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, brandTextView, weightTextView;
        Button deleteButton, updateButton;

        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.medicine_name);
            brandTextView = itemView.findViewById(R.id.medicine_brand);
            weightTextView = itemView.findViewById(R.id.medicine_weight);
            deleteButton = itemView.findViewById(R.id.delete_button);
            updateButton = itemView.findViewById(R.id.update_button);
        }
    }
}
