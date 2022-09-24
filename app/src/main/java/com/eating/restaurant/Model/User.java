package com.eating.restaurant.Model;

import android.content.Context;
import android.content.SharedPreferences;

public class User {

    private Context context;
    private String restaurant_id, mobile_no, restaurant_name, name;
    private SharedPreferences sharedPreferences;

    public User(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("login_details",Context.MODE_PRIVATE);
    }

    public String getRestaurant_id() {

        restaurant_id = sharedPreferences.getString("restaurant_id","");
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {

        this.restaurant_id = restaurant_id;
        sharedPreferences.edit().putString("restaurant_id",restaurant_id).commit();
    }

    public String getMobile_no() {

        mobile_no = sharedPreferences.getString("mobile_no","");
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {

        this.mobile_no = mobile_no;
        sharedPreferences.edit().putString("mobile_no",mobile_no).commit();
    }

    public String getRestaurant_Name() {

        restaurant_name = sharedPreferences.getString("restaurant_name","");
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {

        this.restaurant_name = restaurant_name;
        sharedPreferences.edit().putString("restaurant_name",restaurant_name).commit();
    }

    public String getName() {

        name = sharedPreferences.getString("name","");
        return name;
    }

    public void setName(String name) {

        this.name = name;
        sharedPreferences.edit().putString("name",name).commit();
    }




    public void removeUser()
    {
        sharedPreferences.edit().clear().commit();
    }


}
