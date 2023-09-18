package com.example.projectdatt.Model;

public class Products {
    private String id;
    private String product_name;
    private String image;
    private String type_product;
    private int quantity;

    private int price;
    private String description;

    public Products() {
    }

    public Products(String product_name, String image, String type_product, int quantity, int price, String description) {
        this.product_name = product_name;
        this.image = image;
        this.type_product = type_product;
        this.quantity = quantity;
        this.price = price;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType_product() {
        return type_product;
    }

    public void setType_product(String type_product) {
        this.type_product = type_product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
