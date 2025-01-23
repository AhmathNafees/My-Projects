package com.example.medi;

import com.google.firebase.firestore.GeoPoint;

public class Pharmacy {
    private String name;
    private String phone;
    private GeoPoint location;
    private String id;
    public Pharmacy() {}

    public Pharmacy(String name, String phone, String id, GeoPoint location) {
        this.name = name;
        this.phone = phone;
        this.id = id;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public String getId() {
        return id;
    }
}
