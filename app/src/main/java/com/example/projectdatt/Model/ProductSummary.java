package com.example.projectdatt.Model;

public class ProductSummary {

        private String productName;
        private int totalQuantity;
        private String imageURL;
        private int totalAmount;

        public ProductSummary(String productName, int totalQuantity, String imageURL, int totalAmount) {
            this.productName = productName;
            this.totalQuantity = totalQuantity;
            this.imageURL = imageURL;
            this.totalAmount = totalAmount;
        }

        public String getProductName() {
            return productName;
        }

        public int getTotalQuantity() {
            return totalQuantity;
        }

        public String getImageURL() {
            return imageURL;
        }

        public int getTotalAmount() {
            return totalAmount;
        }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }
}


