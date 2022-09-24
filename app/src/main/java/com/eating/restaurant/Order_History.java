package com.eating.restaurant;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.eating.restaurant.Adapter.OrderHistory_Adapter;
import com.eating.restaurant.ApiClient.ApiClient;
import com.eating.restaurant.Interface.ApiInterface;
import com.eating.restaurant.Model.CurrentOrder_Model;
import com.eating.restaurant.Model.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Order_History extends AppCompatActivity {

    RecyclerView rv_current;
    List<CurrentOrder_Model> modelList;
    OrderHistory_Adapter adapter;
    LinearLayout lin_recycler,lin_to_date, lin_from_date,lin_calendar;
    Button button_apply, btn_driver;
    TextView txt_to_date, txt_from_date;
    ApiInterface apiInterface;
    User user;
    ProgressDialog progressDialog;
    EditText et_search;
    int start_year, start_month, start_day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__history);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        user = new User(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        rv_current = findViewById(R.id.rv_current);
        lin_to_date = findViewById(R.id.lin_to_date);
        lin_from_date = findViewById(R.id.lin_from_date);
        lin_recycler = findViewById(R.id.lin_recycler);
        lin_calendar = findViewById(R.id.lin_calendar);
        button_apply = findViewById(R.id.button_apply);
        txt_to_date = findViewById(R.id.txt_to_date);
        txt_from_date = findViewById(R.id.txt_from_date);
        et_search = findViewById(R.id.et_search);
        btn_driver = findViewById(R.id.button_drivers);

        //modelList = new ArrayList<>();
        rv_current.setHasFixedSize(true);
        rv_current.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
       /* modelList.add(new OrderHistory_Model("","4:14","2123 1/2 Ridge, Eva","Aid","2.4","Open"));
        modelList.add(new OrderHistory_Model("","4:21","2147 Ridge, Eva","Gemma","2.49","Open"));
        modelList.add(new OrderHistory_Model("","4:48","Oakton, Eva","Ken","0","Comp"));
        modelList.add(new OrderHistory_Model("","4:14","4837 louise, Sko","Ken","0","Comp"));
        modelList.add(new OrderHistory_Model("","4:14","2123 1/2 Ridge, Eva","Ken","6","Comp"));
        adapter = new OrderHistory_Adapter(Order_History.this,modelList);
        rv_current.setAdapter(adapter);*/

        lin_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                txt_from_date.setText("From Date");
                rv_current.setVisibility(View.GONE);

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(Order_History.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String month,day;
                                // set day of month , month and year value in the edit text
                                //delivery_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                monthOfYear= (monthOfYear + 1);
                                if(monthOfYear  < 10){

                                    month = "0" + monthOfYear;
                                }else{
                                    month= String.valueOf(monthOfYear);
                                }
                                if(dayOfMonth < 10){

                                    day  = "0" + dayOfMonth ;
                                }else{
                                    day= String.valueOf(dayOfMonth);
                                }
                                start_year=year;
                                start_day=dayOfMonth+1;
                                start_month=monthOfYear-1;
                                //button_start.setText(year + "-" +month + "-" + day);
                                txt_to_date.setText(year + "-" +month + "-" + day);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();


            }
        });




        lin_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                final Calendar c = Calendar.getInstance();
                c.set(start_year,start_month,start_day);
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(Order_History.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String month,day;
                                // set day of month , month and year value in the edit text
                                //delivery_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                monthOfYear= (monthOfYear + 1);
                                if(monthOfYear  < 10){

                                    month = "0" + monthOfYear;
                                }else{
                                    month= String.valueOf(monthOfYear);
                                }
                                if(dayOfMonth < 10){

                                    day  = "0" + dayOfMonth ;
                                }else{
                                    day= String.valueOf(dayOfMonth);
                                }

                                txt_from_date.setText(year + "-" +month + "-" + day);
                                fetchOrderDateWise();

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                datePickerDialog.show();


            }
        });




        btn_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fetchOrderDriverId();
            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(Order_History.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    private void fetchOrderDateWise()
    {

        modelList = new ArrayList<>();

        Call<String> call = apiInterface.fetch_order_history_date_wise(txt_to_date.getText().toString(), txt_from_date.getText().toString());
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONArray jsonArray = new JSONArray(response.body());

                    if (jsonArray.length()==0)
                    {
                        progressDialog.dismiss();
                        rv_current.setVisibility(View.GONE);
                        Toast.makeText(Order_History.this, "No Order History", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        modelList.clear();
                        rv_current.setVisibility(View.VISIBLE);
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String restaurant_name = jsonObject.getString("restaurant_name");
                            String delivery_time = jsonObject.getString("delivery_time");
                            String address = jsonObject.getString("user_address");
                            String address2 = jsonObject.getString("user_address2");
                            String tip = jsonObject.getString("tips");
                            String delivery_fee = jsonObject.getString("delivery_fee");
                            String checkout_total = jsonObject.getString("checkout_total");
                            String restaurant_price = jsonObject.getString("restaurant_price");
                            String user_name = jsonObject.getString("user_name");
                            String phone_no = jsonObject.getString("phone_no");
                            String delivery_date = jsonObject.getString("delivery_date");
                            String f_ready_time = jsonObject.getString("f_ready_time");
                            String status = jsonObject.getString("status");
                            String driver_id = jsonObject.getString("driver_id");
                            String driver_name = jsonObject.getString("driver_name");
                            String order_place_time = jsonObject.getString("order_place_time");

                            CurrentOrder_Model model = new CurrentOrder_Model(id,restaurant_name,delivery_time,address,status,tip,delivery_fee,checkout_total,restaurant_price,user_name,phone_no,delivery_date,f_ready_time,address2,driver_id,"",driver_name,"","","","",order_place_time);
                            modelList.add(model);
                        }

                        adapter = new OrderHistory_Adapter(Order_History.this,modelList);
                        rv_current.setAdapter(adapter);

                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {

                    Toast.makeText(Order_History.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(Order_History.this, "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }




    private void fetchOrderDriverId()
    {

        modelList = new ArrayList<>();

        Call<String> call = apiInterface.search_order_history_driver_id(et_search.getText().toString());
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONArray jsonArray = new JSONArray(response.body());

                    if (jsonArray.length()==0)
                    {
                        progressDialog.dismiss();
                        rv_current.setVisibility(View.GONE);
                        Toast.makeText(Order_History.this, "No Order History", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        modelList.clear();
                        rv_current.setVisibility(View.VISIBLE);
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String restaurant_name = jsonObject.getString("restaurant_name");
                            String delivery_time = jsonObject.getString("delivery_time");
                            String address = jsonObject.getString("user_address");
                            String address2 = jsonObject.getString("user_address2");
                            String tip = jsonObject.getString("tips");
                            String delivery_fee = jsonObject.getString("delivery_fee");
                            String checkout_total = jsonObject.getString("checkout_total");
                            String restaurant_price = jsonObject.getString("restaurant_price");
                            String user_name = jsonObject.getString("user_name");
                            String phone_no = jsonObject.getString("phone_no");
                            String delivery_date = jsonObject.getString("delivery_date");
                            String f_ready_time = jsonObject.getString("f_ready_time");
                            String status = jsonObject.getString("status");
                            String driver_id = jsonObject.getString("driver_id");
                            String driver_name = jsonObject.getString("driver_name");
                            String order_place_time = jsonObject.getString("order_place_time");

                            CurrentOrder_Model model = new CurrentOrder_Model(id,restaurant_name,delivery_time,address,status,tip,delivery_fee,checkout_total,restaurant_price,user_name,phone_no,delivery_date,f_ready_time,address2,driver_id,"",driver_name,"","","","",order_place_time);
                            modelList.add(model);
                        }

                        adapter = new OrderHistory_Adapter(Order_History.this,modelList);
                        rv_current.setAdapter(adapter);

                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {

                    Toast.makeText(Order_History.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(Order_History.this, "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }



}