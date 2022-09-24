package com.eating.restaurant.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eating.restaurant.Model.CurrentOrder_Model;
import com.eating.restaurant.Order_Details;
import com.eating.restaurant.R;

import java.util.List;

public class OrderHistory_Adapter extends RecyclerView.Adapter<OrderHistory_Adapter.MyViewHolder> {

    Context context;
    List<CurrentOrder_Model> models;

    public OrderHistory_Adapter(Context context, List<CurrentOrder_Model> models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_order_history, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        //holder.driver.setText(models.get(position).getDriver_name());
        holder.time.setText(models.get(position).getOrder_place_time());
        holder.tip.setText(models.get(position).getTip());
        holder.customer.setText(models.get(position).getCustomer());


        if (models.get(position).getDriver_name().equals("null"))
        {
            holder.driver.setText("--");
        }
        else
        {
            holder.driver.setText(models.get(position).getDriver_name());
        }


        //holder.status.setText(models.get(position).getStatus());

//        holder.linear.setBackgroundColor(Color.parseColor("#B3DDFF"));

       /* if(models.get(position).getDriver().equals("None")){
            holder.linear.setBackgroundColor(Color.parseColor("#FFC0BB"));
        }else if(models.get(position).getDriver().equals("Ken")){
            holder.linear.setBackgroundColor(Color.parseColor("#FFEEB9"));
        }else if(models.get(position).getDriver().equals("Aid")){
            holder.linear.setBackgroundColor(Color.parseColor("#B2FDB5"));
            holder.rqst_dlvry_tm.setVisibility(View.VISIBLE);
            holder.lin_tips.setVisibility(View.VISIBLE);
            holder.unit.setVisibility(View.GONE);
        }else {
            holder.linear.setBackgroundColor(Color.parseColor("#B3DDFF"));
            holder.rqst_dlvry_tm.setVisibility(View.GONE);
            holder.lin_tips.setVisibility(View.GONE);
            holder.unit.setVisibility(View.VISIBLE);
        }*/

        holder.img_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putString("id",models.get(position).getOrder_id());
                b.putString("res_name",models.get(position).getRestaurant());
                b.putString("driver_id",models.get(position).getDriver_id());
                b.putString("customar_name",models.get(position).getUser_name());
                b.putString("customar_address",models.get(position).getCustomer());
                b.putString("customar_phone",models.get(position).getPhone_no());
                context.startActivity(new Intent(context, Order_Details.class).putExtras(b));
            }
        });
        /*holder.customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                final View dialogView = inflater.inflate(R.layout.dialog_customer_adrs, null);

                TextView edit = dialogView.findViewById(R.id.edit);
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(context, New_Order.class));
                    }
                });
                builder.setView(dialogView);
                final AlertDialog alert = builder.create();
                alert.setCanceledOnTouchOutside(true);
                alert.show();
            }
        });*/


    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView time,customer,driver,tip,status;
//        LinearLayout linear,linear_order,rqst_dlvry_tm,lin_tips,unit;
        ImageView img_expand;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            driver = itemView.findViewById(R.id.driver);
            time = itemView.findViewById(R.id.time);
            customer = itemView.findViewById(R.id.customer);
            tip = itemView.findViewById(R.id.tip);
            status = itemView.findViewById(R.id.status);
            img_expand = itemView.findViewById(R.id.img_expand);



        }
    }
}
