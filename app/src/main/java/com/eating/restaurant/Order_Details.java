package com.eating.restaurant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.eating.restaurant.ApiClient.ApiClient;
import com.eating.restaurant.Interface.ApiInterface;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Order_Details extends AppCompatActivity {

    String hold_driver_id, hold_res_id, hold_customar_name, hold_customar_address, hold_customar_phone, hold_id;
    ApiInterface apiInterface;
    ProgressDialog progressDialog;
    TextView txt_driver_id, txt_driver_phone, txt_res_name, txt_res_area, txt_res_address, txt_res_phone, txt_customar_name,
            txt_customar_address, txt_customar_phone, txt_driver_name, txt_arrived_res_time, txt_pickup_time, txt_delivery_time,
            txt_collect_form_res, txt_order_recived_time, txt_food_ready_time, txt_per_order_fee, txt_accapt_order_time,
            txt_dispatcher_approve_time;
    LinearLayout lin_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        hold_id = getIntent().getExtras().getString("id");
        hold_driver_id = getIntent().getExtras().getString("driver_id");
        hold_res_id = getIntent().getExtras().getString("res_name");
        hold_customar_name = getIntent().getExtras().getString("customar_name");
        hold_customar_address = getIntent().getExtras().getString("customar_address");
        hold_customar_phone= getIntent().getExtras().getString("customar_phone");

        //Mapping
        txt_driver_id = findViewById(R.id.driver_id);
        txt_driver_name = findViewById(R.id.driver_name);
        txt_driver_phone = findViewById(R.id.driver_phone);
        txt_res_name = findViewById(R.id.res_name);
        txt_res_area = findViewById(R.id.res_area);
        txt_res_address = findViewById(R.id.res_address);
        txt_res_phone = findViewById(R.id.res_phone);
        txt_customar_name = findViewById(R.id.customar_name);
        txt_customar_address = findViewById(R.id.customar_address);
        txt_customar_phone = findViewById(R.id.customar_phone);
        txt_arrived_res_time = findViewById(R.id.arrived_res_time);
        txt_pickup_time = findViewById(R.id.pickup_time);
        txt_delivery_time = findViewById(R.id.delivery_time);
        txt_collect_form_res = findViewById(R.id.collect_from_res);
        txt_order_recived_time = findViewById(R.id.order_recived_time);
        txt_food_ready_time = findViewById(R.id.food_ready_time);
        txt_accapt_order_time = findViewById(R.id.accapt_order_time);
        txt_per_order_fee = findViewById(R.id.per_order_fee);
        txt_dispatcher_approve_time = findViewById(R.id.dispatcher_approve_time);
        lin_header = findViewById(R.id.lin_header);


        fetchOrderDetails();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(Order_Details.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchOrderDetails()
    {
        Call<String> call = apiInterface.fetch_order_details(hold_driver_id,hold_res_id,hold_id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {
                        lin_header.setVisibility(View.VISIBLE);

                        txt_driver_id.setText(hold_driver_id);
                        txt_driver_phone.setText(jsonObject.getString("driver_phone"));
                        txt_driver_name.setText(jsonObject.getString("driver_name"));
                        txt_res_name.setText(jsonObject.getString("res_name"));
                        txt_res_phone.setText(jsonObject.getString("res_phone"));
                        txt_res_area.setText(jsonObject.getString("area"));
                        txt_res_address.setText(jsonObject.getString("address"));
                        txt_accapt_order_time.setText(jsonObject.getString("driver_accapt_time"));
                        txt_arrived_res_time.setText(jsonObject.getString("arrived_res_time"));
                        txt_pickup_time.setText(jsonObject.getString("pickup_time"));
                        txt_delivery_time.setText(jsonObject.getString("delivery_time"));
                        txt_collect_form_res.setText(jsonObject.getString("collect_from_res"));
                        txt_order_recived_time.setText(jsonObject.getString("order_recived_time"));
                        txt_food_ready_time.setText(jsonObject.getString("f_ready_time"));
                        txt_per_order_fee.setText(jsonObject.getString("per_order_fee"));
                        txt_dispatcher_approve_time.setText(jsonObject.getString("dispatcher_approve_time"));

                        txt_customar_name.setText(hold_customar_name);
                        txt_customar_address.setText(hold_customar_address);
                        txt_customar_phone.setText(hold_customar_phone);
                    }
                    else
                    {
                        lin_header.setVisibility(View.GONE);
                        Toast.makeText(Order_Details.this, "Details not Found", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {

                    Toast.makeText(Order_Details.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(Order_Details.this, "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
    }


}