package com.example.medi;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.PropertyName;

public class MyOrderModel {
    private String orderId;
    private String status;
    private DocumentReference pharmacy;

    @PropertyName("status")
    public String getStatus() {
        return status;
    }

    @PropertyName("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @PropertyName("pharmacy")
    public DocumentReference getPharmacy() {
        return pharmacy;
    }

    @PropertyName("pharmacy")
    public void setPharmacy(DocumentReference pharmacy) {
        this.pharmacy = pharmacy;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}

