package com.eating.restaurant.Interface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("login.php")
    Call<String> login(@Field("restaurant_id") String restaurant_id, @Field("pass") String pass);

    @FormUrlEncoded
    @POST("add_new_order.php")
    Call<String> add_new_order(@Field("order_id") String order_id, @Field("res_id") String res_id, @Field("phone") String phone,
                               @Field("customar_name") String customar_name, @Field("address") String address, @Field("res_price") String res_price,
                               @Field("delivery_fees") String delivery_fees, @Field("driver_tip") String driver_tip, @Field("total") String total,
                               @Field("f_ready_time") String f_ready_time, @Field("delivery_date") String delivery_date, @Field("delivery_time") String delivery_time,
                               @Field("month") String month, @Field("year") String year, @Field("current_order_place_time") String current_order_place_time);

    @FormUrlEncoded
    @POST("fetch_order.php")
    Call<String> fetch_open_order(@Field("res_name") String res_name);

    @FormUrlEncoded
    @POST("fetch_order_by_order_id.php")
    Call<String> fetch_order_by_order_id(@Field("order_id") String order_id);

    @FormUrlEncoded
    @POST("fetch_num_row.php")
    Call<String> fetch_num_row(@Field("res_name") String res_name);

    @FormUrlEncoded
    @POST("fetch_complet_order.php")
    Call<String> fetch_complet_order(@Field("restaurant_id") String restaurant_id);

    @FormUrlEncoded
    @POST("fetch_order_history_date_wise.php")
    Call<String> fetch_order_history_date_wise(@Field("to_date") String to_date, @Field("form_date") String form_date);

    @FormUrlEncoded
    @POST("search_order_history_driver_id.php")
    Call<String> search_order_history_driver_id(@Field("driver_id") String driver_id);

    @FormUrlEncoded
    @POST("fetch_order_details.php")
    Call<String> fetch_order_details(@Field("driver_id") String driver_id, @Field("res_name") String res_name,
                                     @Field("id") String id);


    @FormUrlEncoded
    @POST("insert_token.php")
    Call<String> insert_token(@Field("res_name") String res_name, @Field("token") String token);

    @FormUrlEncoded
    @POST("order_cancel.php")
    Call<String> cancel_order(@Field("id") String id, @Field("reason") String reason);

    @FormUrlEncoded
    @POST("drinks.php")
    Call<String> drinks(@Field("order_id") String order_id);

    @FormUrlEncoded
    @POST("food_ready_status.php")
    Call<String> food_ready_status(@Field("order_id") String order_id, @Field("status") String status);


    @FormUrlEncoded
    @POST("fetch_pdt_time.php")
    Call<String> fetch_pdt_time(@Field("restaurant_name") String restaurant_name);


    @FormUrlEncoded
    @POST("update_order.php")
    Call<String> update_order(@Field("order_id") String order_id, @Field("res_id") String res_id, @Field("phone") String phone,
                               @Field("customar_name") String customar_name, @Field("address") String address, @Field("res_price") String res_price,
                               @Field("delivery_fees") String delivery_fees, @Field("driver_tip") String driver_tip, @Field("total") String total,
                              @Field("f_ready_time") String f_ready_time, @Field("delivery_date") String delivery_date, @Field("delivery_time") String delivery_time);


    @FormUrlEncoded
    @POST("check_auto_approve.php")
    Call<String> check_auto_approve(@Field("res_name") String res_name);

    @FormUrlEncoded
    @POST("approve_order.php")
    Call<String> approve_order(@Field("order_id") String order_id);

}
