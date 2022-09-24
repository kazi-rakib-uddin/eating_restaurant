package com.eating.restaurant.Model;

public class OrderHistory_Model {

    String id,time,customer,driver,tip,status;

    public OrderHistory_Model(String id, String time, String customer, String driver, String tip, String status) {
        this.id = id;
        this.time = time;
        this.customer = customer;
        this.driver = driver;
        this.tip = tip;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public String getCustomer() {
        return customer;
    }

    public String getDriver() {
        return driver;
    }

    public String getTip() {
        return tip;
    }

    public String getStatus() {
        return status;
    }
}
