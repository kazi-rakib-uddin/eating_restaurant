package com.eating.restaurant.Model;

public class New_Order_Model {

    String order_id,  dispatcher_id, driver_id, waiting_status, drinks, status, restaurant_name, priority, chicago_date, landmark,
            food_ready_status, pin_to_top;
    String phone_no, customar_name, address, res_price, delivery_fee, driver_tip, checkout_total, f_ready_time, delivery_time, delivery_date;

    public New_Order_Model() {
    }


    public New_Order_Model(String order_id, String dispatcher_id, String driver_id,
                           String waiting_status, String drinks, String status, String restaurant_name,
                           String priority, String chicago_date, String landmark, String food_ready_status,
                           String pin_to_top, String phone_no, String customar_name, String address, String res_price,
                           String delivery_fee, String driver_tip, String checkout_total, String f_ready_time,
                           String delivery_time, String delivery_date) {
        this.order_id = order_id;
        this.dispatcher_id = dispatcher_id;
        this.driver_id = driver_id;
        this.waiting_status = waiting_status;
        this.drinks = drinks;
        this.status = status;
        this.restaurant_name = restaurant_name;
        this.priority = priority;
        this.chicago_date = chicago_date;
        this.landmark = landmark;
        this.food_ready_status = food_ready_status;
        this.pin_to_top = pin_to_top;
        this.phone_no = phone_no;
        this.customar_name = customar_name;
        this.address = address;
        this.res_price = res_price;
        this.delivery_fee = delivery_fee;
        this.driver_tip = driver_tip;
        this.checkout_total = checkout_total;
        this.f_ready_time = f_ready_time;
        this.delivery_time = delivery_time;
        this.delivery_date = delivery_date;
    }


    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getDispatcher_id() {
        return dispatcher_id;
    }

    public void setDispatcher_id(String dispatcher_id) {
        this.dispatcher_id = dispatcher_id;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getWaiting_status() {
        return waiting_status;
    }

    public void setWaiting_status(String waiting_status) {
        this.waiting_status = waiting_status;
    }

    public String getDrinks() {
        return drinks;
    }

    public void setDrinks(String drinks) {
        this.drinks = drinks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }


    public String getChicago_date() {
        return chicago_date;
    }

    public void setChicago_date(String chicago_date) {
        this.chicago_date = chicago_date;
    }


    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getFood_ready_status() {
        return food_ready_status;
    }

    public void setFood_ready_status(String food_ready_status) {
        this.food_ready_status = food_ready_status;
    }

    public String getPin_to_top() {
        return pin_to_top;
    }

    public void setPin_to_top(String pin_to_top) {
        this.pin_to_top = pin_to_top;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getCustomar_name() {
        return customar_name;
    }

    public void setCustomar_name(String customar_name) {
        this.customar_name = customar_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRes_price() {
        return res_price;
    }

    public void setRes_price(String res_price) {
        this.res_price = res_price;
    }

    public String getDelivery_fee() {
        return delivery_fee;
    }

    public void setDelivery_fee(String delivery_fee) {
        this.delivery_fee = delivery_fee;
    }

    public String getDriver_tip() {
        return driver_tip;
    }

    public void setDriver_tip(String driver_tip) {
        this.driver_tip = driver_tip;
    }

    public String getCheckout_total() {
        return checkout_total;
    }

    public void setCheckout_total(String checkout_total) {
        this.checkout_total = checkout_total;
    }

    public String getF_ready_time() {
        return f_ready_time;
    }

    public void setF_ready_time(String f_ready_time) {
        this.f_ready_time = f_ready_time;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }
}
