package com.eating.restaurant.Model;

import com.google.gson.annotations.SerializedName;

public class CurrentOrder_Model {

    String order_id,time,customer,status,tip, delivery_fee, checkout_total, restaurant_price, user_name, phone_no,
            delivery_date, f_ready_time, address, driver_id, waiting_status, driver_name, drinks, dispatcher_id, priority, chicago_date,
            order_place_time;

    private boolean expanded;

    @SerializedName("restaurat_name")
    String restaurant;

    public CurrentOrder_Model(String order_id, String restaurant, String time, String customer, String status, String tip,
                              String delivery_fee, String checkout_total, String restaurant_price, String user_name,
                              String phone_no, String delivery_date, String f_ready_time, String address, String driver_id,
                              String waiting_status, String driver_name, String drinks, String dispatcher_id, String priority,
                              String chicago_date, String order_place_time) {
        this.order_id = order_id;
        this.restaurant = restaurant;
        this.time = time;
        this.customer = customer;
        this.status = status;
        this.tip = tip;
        this.delivery_fee = delivery_fee;
        this.checkout_total = checkout_total;
        this.restaurant_price = restaurant_price;
        this.user_name = user_name;
        this.phone_no = phone_no;
        this.delivery_date = delivery_date;
        this.f_ready_time = f_ready_time;
        this.address = address;
        this.driver_id = driver_id;
        this.waiting_status = waiting_status;
        this.driver_name = driver_name;
        this.drinks = drinks;
        this.dispatcher_id = dispatcher_id;
        this.priority = priority;
        this.chicago_date = chicago_date;
        this.order_place_time = order_place_time;

        this.expanded = false;
    }





    public CurrentOrder_Model() {
    }


    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public void setDelivery_fee(String delivery_fee) {
        this.delivery_fee = delivery_fee;
    }

    public void setCheckout_total(String checkout_total) {
        this.checkout_total = checkout_total;
    }

    public void setRestaurant_price(String restaurant_price) {
        this.restaurant_price = restaurant_price;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public void setF_ready_time(String f_ready_time) {
        this.f_ready_time = f_ready_time;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public void setWaiting_status(String waiting_status) {
        this.waiting_status = waiting_status;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public void setDrinks(String drinks) {
        this.drinks = drinks;
    }

    public String getDispatcher_id() {
        return dispatcher_id;
    }

    public void setDispatcher_id(String dispatcher_id) {
        this.dispatcher_id = dispatcher_id;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public String getTime() {
        return time;
    }

    public String getCustomer() {
        return customer;
    }

    public String getStatus() {
        return status;
    }

    public String getTip() {
        return tip;
    }


    public String getDelivery_fee() {
        return delivery_fee;
    }

    public String getCheckout_total() {
        return checkout_total;
    }

    public String getRestaurant_price() {
        return restaurant_price;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public String getF_ready_time() {
        return f_ready_time;
    }

    public String getAddress() {
        return address;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public String getWaiting_status() {
        return waiting_status;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getDrinks() {
        return drinks;
    }

    public String getChicago_date() {
        return chicago_date;
    }

    public void setChicago_date(String chicago_date) {
        this.chicago_date = chicago_date;
    }

    public String getOrder_place_time() {
        return order_place_time;
    }

    public void setOrder_place_time(String order_place_time) {
        this.order_place_time = order_place_time;
    }


}
