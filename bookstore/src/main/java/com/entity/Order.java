package com.entity;

import java.util.List;
import java.util.Map;

public class Order {
    private String orderId;
    private String email;
    private String createdAt;
    private List<Map<String, String>> items;
    private String status;
    private double total;

    // NEW: shipping / contact fields
    private String phoneNumber;
    private String address;
    private String landmark;
    private String city;
    private String pincode;

    // NEW: payment method (Credit/Debit, UPI, COD, NetBanking)
    private String paymentMethod;

    // getters / setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public List<Map<String, String>> getItems() { return items; }
    public void setItems(List<Map<String, String>> items) { this.items = items; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    // contact fields
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getLandmark() { return landmark; }
    public void setLandmark(String landmark) { this.landmark = landmark; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
