package com.eating.restaurant;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import com.eating.restaurant.ApiClient.ApiClient;
import com.eating.restaurant.Interface.ApiInterface;
import com.eating.restaurant.Model.New_Order_Model;
import com.eating.restaurant.Model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class New_Order extends AppCompatActivity {

    EditText et_phone_no, et_customar_name, et_address, et_unit, et_restaurant_price, et_intruction, et_fees, et_tip, et_total;
    ApiInterface apiInterface;
    TextView txt_delivery_date, txt_delivery_time, txt_food_ready_time, txt_total, txt_title;
    User user;
    ProgressDialog progressDialog;
    Button btn_submit;
    String hold_id, hold_order="", hold_phone_no, hold_customar_name, hold_address, hold_delivery_fees, hold_delivery_tip,
            hold_f_ready_time, hold_checkout_total, hold_res_price, hold_delivery_time, hold_delivery_date;
    String current_place_time;

    String format;
    private int total =0;

    final int min = 11111111;
    final int max = 99999999;
    private int random;
    FirebaseDatabase database;
    DatabaseReference reference;
    String month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__order);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getStringExtra("update_order") !=null) {
            hold_order = getIntent().getExtras().getString("update_order");
        }

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        user = new User(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();


        et_phone_no = findViewById(R.id.phone_no);
        et_customar_name = findViewById(R.id.customar_name);
        et_address = findViewById(R.id.address);
        et_unit = findViewById(R.id.unit);
        et_restaurant_price = findViewById(R.id.et_restaurant_price);
        et_intruction = findViewById(R.id.instruction);
        et_fees = findViewById(R.id.fees);
        et_tip = findViewById(R.id.tip);
        txt_total = findViewById(R.id.txt_total);
        txt_food_ready_time = findViewById(R.id.txt_food_ready_time);
        btn_submit = findViewById(R.id.btn_submit);
        txt_title = findViewById(R.id.title);
        txt_delivery_date = findViewById(R.id.txt_delivery_date);
        txt_delivery_time = findViewById(R.id.txt_delivery_time);


        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String[] items1 = date.split("-");
        year = items1[0];
        month = items1[1];
        String day = items1[2];


        if (hold_order.equals("update_order"))
        {
            toolbar.setTitle("Update Order");
            hold_id = getIntent().getExtras().getString("id");

            hold_phone_no = getIntent().getExtras().getString("phone_no");
            hold_customar_name = getIntent().getExtras().getString("customar_name");
            hold_address = getIntent().getExtras().getString("address");
            hold_res_price = getIntent().getExtras().getString("res_price");
            hold_delivery_fees = getIntent().getExtras().getString("delivery_fees");
            hold_delivery_tip = getIntent().getExtras().getString("delivery_tip");
            hold_f_ready_time = getIntent().getExtras().getString("f_ready_time");
            hold_checkout_total = getIntent().getExtras().getString("checkout_total");
            hold_delivery_time = getIntent().getExtras().getString("delivery_time");
            hold_delivery_date = getIntent().getExtras().getString("delivery_date");

            et_phone_no.setText(hold_phone_no);
            et_customar_name.setText(hold_customar_name);
            et_address.setText(hold_address);
            et_restaurant_price.setText(hold_res_price);
            et_fees.setText(hold_delivery_fees);
            et_tip.setText(hold_delivery_tip);
            txt_total.setText(hold_checkout_total);
            txt_food_ready_time.setText(hold_f_ready_time);
            txt_delivery_date.setText(hold_delivery_date);
            txt_delivery_time.setText(hold_delivery_time);

            txt_title.setText("Update Order");
            btn_submit.setText("Update");
        }
        else
        {
            toolbar.setTitle("New Order");
        }



        txt_delivery_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Calendar mcurrentDate = Calendar.getInstance();

                int mDay   = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mYear  = mcurrentDate.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(New_Order.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        String fmonth, fDate;


                        try {
                            if (month < 10 && dayOfMonth < 10) {

                                fmonth = "0" + month;
                                month = Integer.parseInt(fmonth) + 1;
                                fDate = "0" + dayOfMonth;
                                String paddedMonth = String.format("%02d", month);
                                txt_delivery_date.setText(year + "-" + paddedMonth + "-" + fDate);

                            }
                            else if (month >= 10 && dayOfMonth < 10) {

                                fmonth = "0" + month;
                                month = Integer.parseInt(fmonth) + 1;
                                fDate = "0" + dayOfMonth;
                                String paddedMonth = String.format("%02d", month);
                                txt_delivery_date.setText(year + "-" + paddedMonth + "-" + fDate);

                            }
                            else {

                                fmonth = "0" + month;
                                month = Integer.parseInt(fmonth) + 1;
                                String paddedMonth = String.format("%02d", month);
                                txt_delivery_date.setText(year + "-" + paddedMonth + "-" + dayOfMonth);

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, mYear, mMonth, mDay);

                datePickerDialog.show();

            }
        });



        txt_delivery_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(New_Order.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        //txt_delivery_time.setText( selectedHour + ":" + selectedMinute);

                        showTime(selectedHour,selectedMinute, txt_delivery_time);
                    }
                }, hour, minute, false);//Yes true 24 hour time false is 12 hours
                //mTimePicker.setTitle("Select Time");
                mTimePicker.show();


            }
        });


        txt_food_ready_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(New_Order.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        //txt_delivery_time.setText( selectedHour + ":" + selectedMinute);

                        showTime(selectedHour,selectedMinute, txt_food_ready_time);
                    }
                }, hour, minute, false);//Yes true 24 hour time false is 12 hours
                //mTimePicker.setTitle("Select Time");
                mTimePicker.show();


            }
        });



        et_fees.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!editable.toString().equals("") && !et_restaurant_price.getText().toString().equals(""))
                {

                    total = Integer.parseInt(et_restaurant_price.getText().toString()) + Integer.parseInt(editable.toString());

                    txt_total.setText(String.valueOf(total));

                }
                else
                {
                    txt_total.setText("");
                }
            }
        });



        et_restaurant_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!editable.toString().equals("") && !et_fees.getText().toString().equals(""))
                {

                    total = Integer.parseInt(et_fees.getText().toString()) + Integer.parseInt(editable.toString());

                    txt_total.setText(String.valueOf(total));

                }
                else
                {
                    txt_total.setText("");
                }
            }
        });



        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mma");
                DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
                symbols.setAmPmStrings(new String[] { "am", "pm" });
                simpleDateFormat.setDateFormatSymbols(symbols);
                current_place_time = simpleDateFormat.format(new Date());
                Log.d("TAG", "onClick: "+current_place_time);

                if (et_phone_no.getText().toString().isEmpty())
                {
                    Toast.makeText(New_Order.this, "Phone No is required", Toast.LENGTH_SHORT).show();
                }
                else if (et_customar_name.getText().toString().isEmpty())
                {
                    Toast.makeText(New_Order.this, "Customar Name is required", Toast.LENGTH_SHORT).show();
                }
                else if (et_address.getText().toString().isEmpty())
                {
                    Toast.makeText(New_Order.this, "Address is required", Toast.LENGTH_SHORT).show();
                }
                else if (et_restaurant_price.getText().toString().isEmpty())
                {
                    Toast.makeText(New_Order.this, "Restaurant price is required", Toast.LENGTH_SHORT).show();
                }
                else if (et_fees.getText().toString().isEmpty())
                {
                    Toast.makeText(New_Order.this, "Delivery fees is required", Toast.LENGTH_SHORT).show();
                }
                else if (et_tip.getText().toString().isEmpty())
                {
                    Toast.makeText(New_Order.this, "Driver Tip is required", Toast.LENGTH_SHORT).show();
                }
                else if (txt_total.getText().toString().isEmpty())
                {
                    Toast.makeText(New_Order.this, "Checkout total is required", Toast.LENGTH_SHORT).show();
                }
                else if (txt_food_ready_time.getText().toString().isEmpty())
                {
                    Toast.makeText(New_Order.this, "Food Ready time is required", Toast.LENGTH_SHORT).show();
                }
                else if (txt_delivery_time.getText().toString().isEmpty())
                {
                    Toast.makeText(New_Order.this, "Delivery time is required", Toast.LENGTH_SHORT).show();
                }
                else if (txt_delivery_date.getText().toString().isEmpty())
                {
                    Toast.makeText(New_Order.this, "Delivery date is required", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (btn_submit.getText().toString().equals("Submit"))
                    {
                        addNewOrder();
                    }
                    else
                    {
                        updateOrder();
                    }

                }


            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(New_Order.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public static boolean isValid(String s)
    {

        // 1) Begins with 0 or 91
        // 2) Then contains 6 or 7 or 8 or 9.
        // 3) Then contains 9 digits
        Pattern p = Pattern.compile("(0|91)?[6-9][0-9]{9}");

        // Pattern class contains matcher() method
        // to find matching between given number
        // and regular expression
        Matcher m = p.matcher(s);
        return (m.find() && m.group().equals(s));
    }

    public void showTime(int hour, int min, TextView txt) {
        if (hour == 0) {
            //hour += 12;
            format = "am";
        } else if (hour == 12) {
            format = "pm";
        } else if (hour > 12) {
            //hour -= 12;
            format = "am";
        } else {
            format = "am";
        }

        txt.setText(String.format("%02d", hour)+":"+String.format("%02d", min)+format);
        /*txt.setText(new StringBuilder().append(String.format("%02d",hour)).append(":").append(String.format("%02d",min))
                .append("").append(format));*/
    }


    private void addNewOrder()
    {
        String order_id;

        if (!hold_order.equals(""))
        {
            order_id = hold_id;
        }
        else
        {
            random = new Random().nextInt((max - min) + 1) + min;
            order_id = "ORD"+random;
        }


        String phone_no = et_phone_no.getText().toString();
        String name = et_customar_name.getText().toString();
        String address = et_address.getText().toString();
        String res_price = et_restaurant_price.getText().toString();
        String delivery_fees = et_fees.getText().toString();
        String tip = et_tip.getText().toString();
        String total = txt_total.getText().toString();
        String f_ready_time = txt_food_ready_time.getText().toString();
        String delivery_time = txt_delivery_time.getText().toString();
        String delivery_date = txt_delivery_date.getText().toString();

        //SimpleDateFormat sdfchicago = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        SimpleDateFormat sdfchicago = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        sdfchicago.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
        String chicago_date = sdfchicago.format(currentDate);


        Call<String> call = apiInterface.add_new_order(order_id,user.getRestaurant_id(), phone_no,name,address,res_price,delivery_fees,
                tip,total,f_ready_time,delivery_date,delivery_time,month,year,current_place_time);

        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {

                        String landmark = jsonObject.getString("landmark");
                        //String dis_id = jsonObject.getString("dis_id");

                        New_Order_Model new_order_model = new New_Order_Model(order_id,"","","","","Unassign",user.getRestaurant_Name(),"5",chicago_date,landmark,"","0",phone_no,name,address,res_price,delivery_fees,tip,total,f_ready_time,delivery_time,delivery_date);

                        reference.child("order_list1").child(order_id).setValue(new_order_model);

                        Toast.makeText(New_Order.this, "Order Successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        startActivity(new Intent(New_Order.this,MainActivity.class));
                        finishAffinity();
                    }
                    else
                    {
                        Toast.makeText(New_Order.this, "Order Not Successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {

                    Toast.makeText(New_Order.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(New_Order.this, "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }



    private void updateOrder()
    {
        String phone_no = et_phone_no.getText().toString();
        String name = et_customar_name.getText().toString();
        String address = et_address.getText().toString();
        String res_price = et_restaurant_price.getText().toString();
        String delivery_fees = et_fees.getText().toString();
        String tip = et_tip.getText().toString();
        String total = txt_total.getText().toString();
        String f_ready_time = txt_food_ready_time.getText().toString();
        String delivery_time = txt_delivery_time.getText().toString();
        String delivery_date = txt_delivery_date.getText().toString();

        Call<String> call = apiInterface.update_order(hold_id,user.getRestaurant_id(),phone_no,name,address,res_price,delivery_fees,tip,total,f_ready_time,delivery_date,delivery_time);
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {

                        reference.child("order_list1").child(hold_id).child("phone_no").setValue(phone_no);
                        reference.child("order_list1").child(hold_id).child("customar_name").setValue(phone_no);
                        reference.child("order_list1").child(hold_id).child("address").setValue(address);
                        reference.child("order_list1").child(hold_id).child("delivery_fee").setValue(delivery_fees);
                        reference.child("order_list1").child(hold_id).child("driver_tip").setValue(tip);
                        reference.child("order_list1").child(hold_id).child("checkout_total").setValue(total);
                        reference.child("order_list1").child(hold_id).child("f_ready_time").setValue(f_ready_time);
                        reference.child("order_list1").child(hold_id).child("delivery_time").setValue(delivery_time);
                        reference.child("order_list1").child(hold_id).child("delivery_date").setValue(delivery_date);
                        Toast.makeText(New_Order.this, "Order Update Successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        startActivity(new Intent(New_Order.this,MainActivity.class));
                        finishAffinity();
                    }
                    else
                    {
                        Toast.makeText(New_Order.this, "Order Not Update", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {

                    Toast.makeText(New_Order.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(New_Order.this, "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    @Override
    public void onBackPressed() {

        startActivity(new Intent(New_Order.this,MainActivity.class));
        finish();
    }
}