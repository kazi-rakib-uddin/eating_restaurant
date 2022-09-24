package com.eating.restaurant.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.eating.restaurant.ApiClient.ApiClient;
import com.eating.restaurant.Interface.ApiInterface;
import com.eating.restaurant.MainActivity;
import com.eating.restaurant.Model.CurrentOrder_Model;
import com.eating.restaurant.Model.User;
import com.eating.restaurant.New_Order;
import com.eating.restaurant.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OpenOrder_Fragment extends Fragment {

    RecyclerView rv_current;
    List<CurrentOrder_Model> modelList = new ArrayList<>();
    CurrentOrder_Adapter adapter;
    ApiInterface apiInterface;
    User user;
    ProgressDialog progressDialog;
    int REQUEST_PHONE_CALL = 110;
    SwipeRefreshLayout swipyRefreshLayout;
    DatabaseReference databaseReference;
    Query query;
    String current_date;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_open,container,false);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
        current_date = sdf.format(new Date());

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        user = new User(getContext());
        databaseReference= FirebaseDatabase.getInstance().getReference("order_list1");
        query=databaseReference.orderByChild("restaurant_name").equalTo(user.getRestaurant_Name().trim());
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        rv_current = view.findViewById(R.id.rv_current);
        swipyRefreshLayout = view.findViewById(R.id.swipe_refresh);


        rv_current.setHasFixedSize(true);
        rv_current.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));


        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
        }

        adapter = new CurrentOrder_Adapter(getActivity(), modelList);
        rv_current.setAdapter(adapter);

        swipyRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                fetch_orders();
            }
        });


       //fetchOrder();
        fetch_orders();

        return view;
    }



    private void fetch_orders()
    {
        rv_current.setVisibility(View.VISIBLE);
        //modelList = new ArrayList<>();
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                modelList.clear();
                HashMap<String, String> value = (HashMap<String, String>) dataSnapshot.getValue();

                    if (value.get("chicago_date").equals(current_date)){


                        if (!value.get("status").equals("Completed") && !value.get("status").equals("Cancelled")) {
                            //   Toast.makeText(getActivity(), ""+datas.child("order_id").getValue().toString(), Toast.LENGTH_SHORT).show();

                            String o=value.get("order_id");

                            Call<String> call = apiInterface.fetch_order_by_order_id(value.get("order_id"));
                            //progressDialog.show();
                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if (response.body() != null) {
                                        String res = response.body();
                                        try {
                                            JSONObject jsonObject = new JSONObject(res);
                                            String id = value.get("order_id");
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
                                            // String status = jsonObject.getString("status");
                                            // String waiting_status = jsonObject.getString("waiting_status");
                                            String waiting_status = value.get("waiting_status");
                                            String driver_name = jsonObject.getString("driver_name");
                                            String drinks = value.get("drinks");
                                            String dispatcher_id = value.get("dispatcher_id");
                                            String driver_id = value.get("driver_id");
                                            String status2 = value.get("status");
                                            String chicago_date = value.get("chicago_date");
                                            String order_place_time = jsonObject.getString("order_place_time");

                                            CurrentOrder_Model model = new CurrentOrder_Model(id, restaurant_name, delivery_time, address, status2, tip, delivery_fee, checkout_total, restaurant_price, user_name, phone_no, delivery_date, f_ready_time, address2, driver_id, waiting_status, driver_name, drinks,
                                                    dispatcher_id, value.get("priority"), chicago_date,order_place_time);
                                            modelList.add(model);
                                            Collections.sort(modelList, new Comparator() {
                                                @Override
                                                public int compare(Object o1, Object o2) {
                                                    CurrentOrder_Model p1 = (CurrentOrder_Model) o1;
                                                    CurrentOrder_Model p2 = (CurrentOrder_Model) o2;

                                                    Date start = null,end = null;
                                                    try {
                                                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mmaa");

                                                        start = sdf.parse(p1.getTime());
                                                        end = sdf.parse(p2.getTime());

                                                    }catch (ParseException p){

                                                        return 0;
                                                    }


                                                    //return (start.getTime() > end.getTime() ? 1 : -1);

                                                    if (start.getTime() < end.getTime())
                                                        return -1;
                                                    else if (start.getTime() >= end.getTime())
                                                        return 1;
                                                    else
                                                        return 0;
                                                }
                                            });
                                            Collections.sort(modelList, new Comparator() {
                                                @Override
                                                public int compare(Object o1, Object o2) {
                                                    CurrentOrder_Model p1 = (CurrentOrder_Model) o1;
                                                    CurrentOrder_Model p2 = (CurrentOrder_Model) o2;
                                                    return p1.getPriority().compareToIgnoreCase(p2.getPriority());
                                                }
                                            });
                                            adapter.notifyDataSetChanged();

                                            swipyRefreshLayout.setRefreshing(false);

                                            //progressDialog.dismiss();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            swipyRefreshLayout.setRefreshing(false);

                                            //progressDialog.dismiss();
                                            // Toast.makeText(getActivity(), "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getActivity(), "No Response", Toast.LENGTH_SHORT).show();
                                        swipyRefreshLayout.setRefreshing(false);

                                        //progressDialog.dismiss();
                                    }

                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    swipyRefreshLayout.setRefreshing(false);

                                    //progressDialog.dismiss();
                                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                CurrentOrder_Model model = dataSnapshot.getValue(CurrentOrder_Model.class);
                //Toast.makeText(getActivity(), ""+model.getPriority()+" "+model.getDispatcher_id()+" "+model.getStatus(), Toast.LENGTH_SHORT).show();
                for (int i = 0; i < modelList.size(); i++) {
                    if (modelList.get(i).getOrder_id().equals(model.getOrder_id())) {

                        if (modelList.get(i).getChicago_date().equals(current_date)){

                            if (!model.getStatus().equals("Completed") && !model.getStatus().equals("Cancelled")) {
                                Call<String> call = apiInterface.fetch_order_by_order_id(model.getOrder_id());
                                if(progressDialog != null && progressDialog.isShowing())
                                    progressDialog.show();
                                int finalI = i;
                                call.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        if (response.body() != null) {
                                            String res = response.body();
                                            try {
                                                JSONObject jsonObject = new JSONObject(res);

                                                model.setRestaurant(model.getRestaurant());
                                                model.setTime(model.getTime());
                                                model.setCustomer(model.getCustomer());
                                                model.setTip(model.getTip());
                                                model.setDelivery_fee(model.getDelivery_fee());
                                                model.setCheckout_total(model.getCheckout_total());
                                                model.setRestaurant_price(model.getRestaurant_price());
                                                model.setUser_name(model.getUser_name());
                                                model.setPhone_no(model.getPhone_no());
                                                model.setDelivery_date(model.getDelivery_date());
                                                model.setF_ready_time(model.getF_ready_time());
                                                model.setDrinks(model.getDrinks());
                                                model.setDriver_name(model.getDriver_name());
                                                model.setOrder_place_time(model.getOrder_place_time());



                                                modelList.set(finalI, model);
                                                adapter.notifyItemChanged(finalI);

                                                swipyRefreshLayout.setRefreshing(false);

                                                progressDialog.dismiss();

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                swipyRefreshLayout.setRefreshing(false);

                                                progressDialog.dismiss();
                                                //Toast.makeText(getActivity(), "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(getActivity(), "No Response", Toast.LENGTH_SHORT).show();
                                            swipyRefreshLayout.setRefreshing(false);

                                            progressDialog.dismiss();
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        swipyRefreshLayout.setRefreshing(false);

                                        progressDialog.dismiss();
                                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        else if(model.getStatus().equals("Completed")){

                            modelList.remove(i);
                            //adapter.notifyItemRemoved(i);
                            if (modelList.size()==0){
                                adapter.notifyItemRemoved(i);
                                modelList.clear();
                                fetch_orders();

                            }else{
                                adapter.notifyItemRemoved(i);
                            }


                        }

                        break;
                    }

                }

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            int size=0;
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                HashMap<String, String> value = (HashMap<String, String>) snap.getValue();

                                if (value.get("chicago_date").equals(current_date)){

                                    if (!value.get("status").equals("Cancelled") && !value.get("status").equals("Completed")) {
                                        //int size = (int) snapshot.getChildrenCount();
                                        size++;
                                        //  Toast.makeText(MainActivity.this, "" + size, Toast.LENGTH_SHORT).show();

                                        /*if (((MainActivity)getActivity()).badgeDrawable !=null)
                                        {
                                            ((MainActivity)getActivity()).badgeDrawable.setVisible(true);
                                            ((MainActivity)getActivity()).badgeDrawable.setNumber(size);
                                        }
*/

                                    }

                                }

                            }
                        }else {
                            ((MainActivity)getActivity()).badgeDrawable.setVisible(false);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                Toast.makeText(getActivity(), "remove", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Toast.makeText(getActivity(), "moved", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
            }
        });



    }





    public class CurrentOrder_Adapter extends RecyclerView.Adapter<CurrentOrder_Adapter.MyViewHolder> implements View.OnClickListener {

        Context context;
        List<CurrentOrder_Model> models;
        TextView txt_address, txt_custome_phone;
        TextView btn_cancel, btn_approve;
        ApiInterface apiInterface;
        AlertDialog alert;
        int REQUEST_PHONE_CALL =1001;
        RadioGroup rg_cancel_reason;
        RadioButton rb, rb_others;
        String hold_reason="";
        AlertDialog dialog;
        EditText et_comment;

        public CurrentOrder_Adapter(Context context, List<CurrentOrder_Model> models) {
            this.context = context;
            this.models = models;
            apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        }

        @NonNull
        @Override
        public CurrentOrder_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.single_current_order, parent, false);
            CurrentOrder_Adapter.MyViewHolder viewHolder=new CurrentOrder_Adapter.MyViewHolder(view);

            viewHolder.img_expand.setTag(viewHolder);
            viewHolder.img_expand.setOnClickListener(CurrentOrder_Adapter.this);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final CurrentOrder_Adapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

            /*try {
                DateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date date = inputFormatter.parse(models.get(position).getTime());
                DateFormat outputFormatter = new SimpleDateFormat("HH:mm a");
                holder.time.setText(outputFormatter.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }*/

            holder.time.setText(models.get(position).getOrder_place_time());
            holder.tip.setText(models.get(position).getTip());
            holder.customer.setText(models.get(position).getCustomer());

            holder.delivery_fee.setText(models.get(position).getDelivery_fee());
            holder.checkout_total.setText(models.get(position).getCheckout_total());
            holder.restaurant_fee.setText(models.get(position).getRestaurant_price());
            holder.customer_name.setText(models.get(position).getUser_name());
            holder.food_ready_time.setText(models.get(position).getF_ready_time());
            holder.request_delivery_time.setText(models.get(position).getDelivery_date()+ "  " +models.get(position).getTime());

            if (models.get(position).getDriver_name().equals("null"))
            {
                holder.driver.setText("--");
            }
            else
            {
                holder.driver.setText(models.get(position).getDriver_name());
            }

            //holder.linear.setBackgroundColor(Color.parseColor("#B3DDFF"));

            if (models.get(position).getWaiting_status().equals("Waiting"))
            {
                holder.lin_wait.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.lin_wait.setVisibility(View.GONE);
            }

            if (models.get(position).getDrinks().equals("Yes"))
            {
                holder.lin_drinks.setVisibility(View.VISIBLE);
                holder.card_drinks.setVisibility(View.GONE);
            }
            else
            {
                holder.lin_drinks.setVisibility(View.GONE);
                holder.card_drinks.setVisibility(View.VISIBLE);
            }



            if(models.get(position).getStatus().equals("Assign")){

                holder.linear.setBackgroundColor(Color.parseColor("#FFEEB9"));
                //holder.btn_cancel.setVisibility(View.VISIBLE);
            }
            if(models.get(position).getStatus().equals("Unassign")){

                holder.linear.setBackgroundColor(Color.parseColor("#FFC0BB"));
                //holder.btn_cancel.setVisibility(View.VISIBLE);
            }
            if(models.get(position).getStatus().equals("Reached")){

                holder.linear.setBackgroundColor(Color.parseColor("#B3DDFF"));
                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(100); //You can manage the blinking time with this parameter
                anim.setStartOffset(100);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                holder.linear.startAnimation(anim);
            }
            if(models.get(position).getStatus().equals("Accepted")){

                holder.linear.setBackgroundColor(Color.parseColor("#B3DDFF"));
            }
            if(models.get(position).getStatus().equals("Out for Delivery")){

                holder.linear.setBackgroundColor(Color.parseColor("#B2FDB5"));
                //holder.btn_cancel.setVisibility(View.GONE);
            }




            boolean isExpandes =models.get(position).isExpanded();

            if (isExpandes)
            {
                holder.linear_order.setVisibility(View.VISIBLE);

            }
            else
            {
                holder.linear_order.setVisibility(View.GONE);

            }


            databaseReference.child(models.get(position).getOrder_id()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    if (snapshot.exists()) {

                        HashMap<String, String> hashMap = (HashMap<String, String>) snapshot.getValue();

                        if (hashMap.get("food_ready_status") != null) {

                            if (hashMap.get("food_ready_status").equals("Yes")) {
                                holder.ch_f_ready.setChecked(true);
                            } else {
                                holder.ch_f_ready.setChecked(false);
                            }

                        }

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



            holder.ch_f_ready.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                    if (isChecked)
                    {
                        food_ready_status(models.get(position).getOrder_id(),"Yes");
                        databaseReference.child(models.get(position).getOrder_id()).child("food_ready_status").setValue("Yes");

                    }
                    else
                    {
                        food_ready_status(models.get(position).getOrder_id(),"");
                        databaseReference.child(models.get(position).getOrder_id()).child("food_ready_status").setValue("");
                    }

                }
            });



            holder.customer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //checkAutoApprove(position);



                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    final View dialogView = inflater.inflate(R.layout.dialog_customer_adrs, null);

                    TextView edit = dialogView.findViewById(R.id.edit);
                    txt_address = dialogView.findViewById(R.id.address);
                    txt_custome_phone = dialogView.findViewById(R.id.customar_phone);
                    btn_cancel = dialogView.findViewById(R.id.btn_cancel);
                    btn_approve = dialogView.findViewById(R.id.btn_approve);

                    txt_address.setText(models.get(position).getCustomer());
                    txt_custome_phone.setText(models.get(position).getPhone_no());


                    btn_approve.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            approveOrder(models.get(position).getOrder_id());
                        }
                    });


                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                            alertDialog.setTitle("Cancel Reason");
                            alertDialog.setCancelable(false);
                            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                            View view1 = layoutInflater.inflate(R.layout.dialog_cancel_reason,null);

                            RadioGroup rg_cancel_reason = view1.findViewById(R.id.rg_cancel_reason);
                            rb_others = view1.findViewById(R.id.others);
                            et_comment = view1.findViewById(R.id.et_comment);

                            rb_others.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                                    if (b)
                                    {
                                        et_comment.setVisibility(View.VISIBLE);
                                    }
                                    else
                                    {
                                        et_comment.setVisibility(View.GONE);
                                    }
                                }
                            });

                            rg_cancel_reason.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                                    rb = (RadioButton) rg_cancel_reason.findViewById(checkedId);

                                    hold_reason = rb.getText().toString();

                                }
                            });





                            alertDialog.setView(view1);


                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    if (hold_reason.equals(""))
                                    {
                                        Toast.makeText(getContext(), "Please Select Reason", Toast.LENGTH_SHORT).show();

                                    }
                                    else if (hold_reason.equals("Others"))
                                    {
                                        cancelOrder(models.get(position).getOrder_id(), et_comment.getText().toString());
                                    }
                                    else
                                    {
                                        cancelOrder(models.get(position).getOrder_id(), hold_reason);
                                    }

                                }
                            }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    hold_reason="";
                                }
                            });

                            dialog = alertDialog.create();
                            dialog.show();

                            // Get the alert dialog buttons reference
                            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                            Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                            positiveButton.setTextColor(Color.parseColor("#FF0B8B42"));
                            negativeButton.setTextColor(Color.parseColor("#4588f5"));




                        }
                    });


                    txt_custome_phone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:"+txt_custome_phone.getText().toString()));
                            context.startActivity(callIntent);


                        }
                    });

                    edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Bundle bundle = new Bundle();
                            bundle.putString("id",models.get(position).getOrder_id());
                            bundle.putString("update_order","update_order");
                            bundle.putString("phone_no",models.get(position).getPhone_no());
                            bundle.putString("customar_name",models.get(position).getUser_name());
                            bundle.putString("address",models.get(position).getCustomer());
                            bundle.putString("res_price",models.get(position).getRestaurant_price());
                            bundle.putString("delivery_fees",models.get(position).getDelivery_fee());
                            bundle.putString("delivery_tip",models.get(position).getTip());
                            bundle.putString("checkout_total",models.get(position).getCheckout_total());
                            bundle.putString("f_ready_time",models.get(position).getF_ready_time());
                            bundle.putString("delivery_time",models.get(position).getOrder_place_time());
                            bundle.putString("delivery_date",models.get(position).getDelivery_date());

                            context.startActivity(new Intent(context, New_Order.class).putExtras(bundle));
                            ((Activity)context).finish();
                        }
                    });
                    builder.setView(dialogView);
                    alert = builder.create();
                    alert.setCanceledOnTouchOutside(true);
                    alert.show();




                }
            });



            if (models.get(position).getDrinks()!=null) {
                if (models.get(position).getDrinks().equals("Yes") && !models.get(position).getDrinks().equals("null")) {
                    holder.lin_drinks.setVisibility(View.VISIBLE);
                } else {
                    holder.lin_drinks.setVisibility(View.GONE);
                }
            }else {
                holder.lin_drinks.setVisibility(View.GONE);
            }


            holder.btn_drinks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    databaseReference.child(models.get(position).getOrder_id()).child("drinks").setValue("Yes");
                    drinks(models.get(position).getOrder_id(), holder.lin_drinks, holder.card_drinks);

                }
            });


        }

        @Override
        public int getItemCount() {
            return models.size();
        }

        @Override
        public void onClick(View v) {

            MyViewHolder holder = (MyViewHolder) v.getTag();

            if (models.get(holder.getPosition()).isExpanded())
            {
                models.get(holder.getPosition()).setExpanded(false);
                notifyDataSetChanged();
            }
            else
            {
                for (int i=0; i<models.size(); i++)
                {
                    if (models.get(i).isExpanded())
                    {
                        models.get(i).setExpanded(false);
                    }
                }

                models.get(holder.getPosition()).setExpanded(true);
                notifyDataSetChanged();
            }

        }




        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView restaurant,time,customer,driver,tip,edit, delivery_fee, checkout_total, restaurant_fee, customer_name, request_delivery_time,
                    food_ready_time;
            LinearLayout linear,linear_order,rqst_dlvry_tm,lin_drinks,unit, lin_wait;
            ImageView img_expand;
            Button btn_drinks;
            CardView card_drinks;
            CheckBox ch_f_ready;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                driver = itemView.findViewById(R.id.driver);
                time = itemView.findViewById(R.id.time);
                customer = itemView.findViewById(R.id.customer);
                tip = itemView.findViewById(R.id.tip);
                linear = itemView.findViewById(R.id.linear);
                linear_order = itemView.findViewById(R.id.linear_order);
                rqst_dlvry_tm = itemView.findViewById(R.id.rqst_dlvry_tm);
                lin_wait = itemView.findViewById(R.id.lin_wait);
                unit = itemView.findViewById(R.id.unit);
                delivery_fee = itemView.findViewById(R.id.delivery_fee);
                checkout_total = itemView.findViewById(R.id.checkout_total);
                restaurant_fee = itemView.findViewById(R.id.restaurant_fee);
                customer_name = itemView.findViewById(R.id.customer_name);
                request_delivery_time = itemView.findViewById(R.id.request_delivery_time);
                food_ready_time = itemView.findViewById(R.id.food_ready_time);
                img_expand = itemView.findViewById(R.id.img_expand);
                btn_drinks = itemView.findViewById(R.id.btn_drinks);
                lin_drinks = itemView.findViewById(R.id.lin_drinks);
                card_drinks = itemView.findViewById(R.id.card_drinks);
                ch_f_ready = itemView.findViewById(R.id.ch_f_ready);




            }
        }





        private void cancelOrder(String order_id, String reason)
        {
            Call<String> call = apiInterface.cancel_order(order_id,reason);
            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please Wait..");
            progressDialog.setCancelable(false);
            progressDialog.show();
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body());

                        if (jsonObject.getString("rec").equals("1"))
                        {

                            databaseReference.child(order_id).child("status").setValue("Cancelled");
                            Toast.makeText(context, "Order Cancelled", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            alert.dismiss();
                            fetch_orders();

                            hold_reason ="";
                        }
                        else
                        {
                            Toast.makeText(context, "Order Not Cancel", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    } catch (JSONException e) {

                        Toast.makeText(context, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                    Toast.makeText(context, "Slow Network", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }




        private void drinks(String order_id, LinearLayout lin_drinks, CardView card_drinks)
        {
            Call<String> call = apiInterface.drinks(order_id);
            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please Wait..");
            progressDialog.setCancelable(false);
            progressDialog.show();
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body());

                        if (jsonObject.getString("rec").equals("1"))
                        {
                            Toast.makeText(context, "Include Drinks", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            lin_drinks.setVisibility(View.VISIBLE);
                            card_drinks.setVisibility(View.GONE);

                        }
                        else
                        {
                            Toast.makeText(context, "Drinks Not Include", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    } catch (JSONException e) {

                        Toast.makeText(context, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                    Toast.makeText(context, "Slow Network", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }




        private void food_ready_status(String order_id, String status)
        {
            Call<String> call = apiInterface.food_ready_status(order_id,status);
            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please Wait..");
            progressDialog.setCancelable(false);
            //progressDialog.show();
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body());

                        if (jsonObject.getString("rec").equals("1"))
                        {
                            //Toast.makeText(context, "Food Ready", Toast.LENGTH_SHORT).show();
                            //progressDialog.dismiss();

                        }
                        else
                        {
                            Toast.makeText(context, "Food Not Ready", Toast.LENGTH_SHORT).show();
                            //progressDialog.dismiss();
                        }

                    } catch (JSONException e) {

                        Toast.makeText(context, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                        //progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                    Toast.makeText(context, "Slow Network", Toast.LENGTH_SHORT).show();
                    //progressDialog.dismiss();
                }
            });
        }





        private void checkAutoApprove(int position)
        {
            Call<String> call = apiInterface.check_auto_approve(user.getRestaurant_Name());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body());

                        if (jsonObject.getString("rec").equals("1"))
                        {
                            String status = jsonObject.getString("status");

                            if (status.equals("NO"))
                            {


                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                LayoutInflater inflater = LayoutInflater.from(getContext());
                                final View dialogView = inflater.inflate(R.layout.dialog_customer_adrs, null);

                                TextView edit = dialogView.findViewById(R.id.edit);
                                txt_address = dialogView.findViewById(R.id.address);
                                txt_custome_phone = dialogView.findViewById(R.id.customar_phone);
                                btn_cancel = dialogView.findViewById(R.id.btn_cancel);
                                btn_approve = dialogView.findViewById(R.id.btn_approve);

                                txt_address.setText(models.get(position).getCustomer());
                                txt_custome_phone.setText(models.get(position).getPhone_no());


                                btn_approve.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        approveOrder(models.get(position).getOrder_id());
                                    }
                                });


                                btn_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {


                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                                        alertDialog.setTitle("Cancel Reason");
                                        alertDialog.setCancelable(false);
                                        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                                        View view1 = layoutInflater.inflate(R.layout.dialog_cancel_reason,null);

                                        RadioGroup rg_cancel_reason = view1.findViewById(R.id.rg_cancel_reason);
                                        rb_others = view1.findViewById(R.id.others);
                                        et_comment = view1.findViewById(R.id.et_comment);

                                        rb_others.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                                                if (b)
                                                {
                                                    et_comment.setVisibility(View.VISIBLE);
                                                }
                                                else
                                                {
                                                    et_comment.setVisibility(View.GONE);
                                                }
                                            }
                                        });

                                        rg_cancel_reason.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                                                rb = (RadioButton) rg_cancel_reason.findViewById(checkedId);

                                                hold_reason = rb.getText().toString();

                                            }
                                        });





                                        alertDialog.setView(view1);


                                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                if (hold_reason.equals(""))
                                                {
                                                    Toast.makeText(getContext(), "Please Select Reason", Toast.LENGTH_SHORT).show();

                                                }
                                                else if (hold_reason.equals("Others"))
                                                {
                                                    cancelOrder(models.get(position).getOrder_id(), et_comment.getText().toString());
                                                }
                                                else
                                                {
                                                    cancelOrder(models.get(position).getOrder_id(), hold_reason);
                                                }

                                            }
                                        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                hold_reason="";
                                            }
                                        });

                                        dialog = alertDialog.create();
                                        dialog.show();

                                        // Get the alert dialog buttons reference
                                        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                                        positiveButton.setTextColor(Color.parseColor("#FF0B8B42"));
                                        negativeButton.setTextColor(Color.parseColor("#4588f5"));




                                    }
                                });


                                txt_custome_phone.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                                        callIntent.setData(Uri.parse("tel:"+txt_custome_phone.getText().toString()));
                                        context.startActivity(callIntent);


                                    }
                                });

                                edit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        Bundle bundle = new Bundle();
                                        bundle.putString("id",models.get(position).getOrder_id());
                                        bundle.putString("update_order","update_order");
                                        bundle.putString("phone_no",models.get(position).getPhone_no());
                                        bundle.putString("customar_name",models.get(position).getUser_name());
                                        bundle.putString("address",models.get(position).getCustomer());
                                        bundle.putString("delivery_fees",models.get(position).getDelivery_fee());
                                        bundle.putString("delivery_tip",models.get(position).getTip());
                                        bundle.putString("f_ready_time",models.get(position).getF_ready_time());
                                        bundle.putString("checkout_total",models.get(position).getCheckout_total());
                            /*context.startActivity(new Intent(context, New_Order.class).putExtras(bundle));
                            ((Activity)context).finish();*/
                                    }
                                });
                                builder.setView(dialogView);
                                alert = builder.create();
                                alert.setCanceledOnTouchOutside(true);
                                alert.show();

                            }

                        }
                        else
                        {

                        }

                    } catch (JSONException e) {

                        Toast.makeText(getContext(), "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                    Toast.makeText(getContext(), "Slow Network", Toast.LENGTH_SHORT).show();
                }
            });
        }



        private void approveOrder(String order_id)
        {
            Call<String> call = apiInterface.approve_order(order_id);
            progressDialog.show();
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body());

                        if (jsonObject.getString("rec").equals("1"))
                        {
                            Toast.makeText(getContext(), "Approved Successfully", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            alert.dismiss();

                            databaseReference.child(order_id).child("status").setValue("Unassign");
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Not Approved", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    } catch (JSONException e) {

                        Toast.makeText(getContext(), "Somthing went wrong", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                    Toast.makeText(getContext(), "Slow Network", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }



    }









    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PHONE_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //makePhoneCall();
                //Toast.makeText(getActivity(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
