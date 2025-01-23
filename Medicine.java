package com.example.medi;

public class Medicine {
    private String id;
    private String name;
    private String brand;
    private String weight;

    // Empty constructor for Firestore
    public Medicine() {}

    public Medicine(String name, String brand, String weight) {
        this.name = name;
        this.brand = brand;
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
