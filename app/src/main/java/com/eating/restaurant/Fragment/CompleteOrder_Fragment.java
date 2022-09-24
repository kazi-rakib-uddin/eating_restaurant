package com.eating.restaurant.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.eating.restaurant.ApiClient.ApiClient;
import com.eating.restaurant.Interface.ApiInterface;
import com.eating.restaurant.Model.CurrentOrder_Model;
import com.eating.restaurant.Model.User;
import com.eating.restaurant.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompleteOrder_Fragment extends Fragment {

    RecyclerView rv_current;
    List<CurrentOrder_Model> modelList;
    CurrentOrder_Adapter adapter;
    ApiInterface apiInterface;
    User user;
    ProgressDialog progressDialog;
    SwipeRefreshLayout swipyRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complete,container,false);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        user = new User(getContext());
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        rv_current = view.findViewById(R.id.rv_current);
        swipyRefreshLayout = view.findViewById(R.id.swipe_refresh);

        rv_current.setHasFixedSize(true);
        rv_current.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));



        swipyRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                fetchCompletedOrder();
            }
        });




       fetchCompletedOrder();

        return view;
    }




    private void fetchCompletedOrder()
    {

        modelList = new ArrayList<>();

        Call<String> call = apiInterface.fetch_complet_order(user.getRestaurant_id());
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
                        swipyRefreshLayout.setRefreshing(false);
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
                            String driver_name = jsonObject.getString("driver_name");
                            String order_place_time = jsonObject.getString("order_place_time");

                            CurrentOrder_Model model = new CurrentOrder_Model(id,restaurant_name,delivery_time,address,status,tip,delivery_fee,checkout_total,restaurant_price,user_name,phone_no,delivery_date,f_ready_time,address2,"","",driver_name,"","","","",order_place_time);
                            modelList.add(model);
                        }

                        adapter = new CurrentOrder_Adapter(getActivity(),modelList);
                        rv_current.setAdapter(adapter);
                        swipyRefreshLayout.setRefreshing(false);

                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {

                    Toast.makeText(getContext(), "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    swipyRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(getContext(), "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                swipyRefreshLayout.setRefreshing(false);
            }
        });

    }





    public class CurrentOrder_Adapter extends RecyclerView.Adapter<CurrentOrder_Adapter.MyViewHolder> implements View.OnClickListener {

        Context context;
        List<CurrentOrder_Model> models;

        public CurrentOrder_Adapter(Context context, List<CurrentOrder_Model> models) {
            this.context = context;
            this.models = models;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.single_current_order, parent, false);

            MyViewHolder viewHolder=new MyViewHolder(view);
            viewHolder.img_expand.setTag(viewHolder);
            viewHolder.img_expand.setOnClickListener(CurrentOrder_Adapter.this);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

            holder.time.setText(models.get(position).getTime());
            holder.tip.setText(models.get(position).getTip());
            holder.customer.setText(models.get(position).getCustomer());

            holder.delivery_fee.setText(models.get(position).getDelivery_fee());
            holder.checkout_total.setText(models.get(position).getCheckout_total());
            holder.restaurant_fee.setText(models.get(position).getRestaurant_price());
            holder.customer_name.setText(models.get(position).getUser_name());
            holder.food_ready_time.setText(models.get(position).getF_ready_time());
            holder.request_delivery_time.setText(models.get(position).getDelivery_date()+ "  " +models.get(position).getTime());

            holder.linear.setBackgroundColor(Color.parseColor("#52F259"));

            holder.lin_wait.setVisibility(View.GONE);
            holder.card_drinks.setVisibility(View.GONE);
            holder.lin_food_ready.setVisibility(View.GONE);


            if (models.get(position).getDriver_name().equals("null"))
            {
                holder.driver.setText("--");
            }
            else
            {
                holder.driver.setText(models.get(position).getDriver_name());
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
            LinearLayout linear,linear_order,rqst_dlvry_tm,lin_wait,unit, lin_food_ready;
            CardView card_drinks;
            ImageView img_expand;
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
                card_drinks = itemView.findViewById(R.id.card_drinks);
                lin_food_ready = itemView.findViewById(R.id.lin_food_ready);


            }
        }
    }

}
