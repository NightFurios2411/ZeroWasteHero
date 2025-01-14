package com.example.zerowastehero.DataBinding.Model;

public class ProductResponse {
    private String productName;
    private String productDescription;
    private String category;
    private String brand;
    private String ecoFriendly;

    public ProductResponse() {}

    public ProductResponse(String product_name, String product_description, String category, String brand, String eco_friendly) {
        this.productName = product_name;
        this.productDescription = product_description;
        this.category = category;
        this.brand = brand;
        this.ecoFriendly = eco_friendly;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getEcoFriendly() {
        return ecoFriendly;
    }

    public void setEcoFriendly(String ecoFriendly) {
        this.ecoFriendly = ecoFriendly;
    }
}
