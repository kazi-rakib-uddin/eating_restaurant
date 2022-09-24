package com.eating.restaurant.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.eating.restaurant.Model.CurrentOrder_Model;
import com.eating.restaurant.R;

import java.util.ArrayList;
import java.util.List;

public class CancelOrder_Fragment extends Fragment {

    RecyclerView rv_current;
    List<CurrentOrder_Model> modelList;
    CurrentOrder_Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cancel,container,false);

        rv_current = view.findViewById(R.id.rv_current);

        modelList = new ArrayList<>();
        rv_current.setHasFixedSize(true);
        rv_current.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
       /* modelList.add(new CurrentOrder_Model("","4:14","2123 1/2 Ridge, Eva","Aid","2.4"));
        modelList.add(new CurrentOrder_Model("","4:21","2147 Ridge, Eva","Gemma","2.49"));
        modelList.add(new CurrentOrder_Model("","4:48","Oakton, Eva","Ken","0"));
        modelList.add(new CurrentOrder_Model("","4:14","4837 louise, Sko","Ken","0"));
        modelList.add(new CurrentOrder_Model("","4:14","2123 1/2 Ridge, Eva","Ken","6"));
        modelList.add(new CurrentOrder_Model("","4:14","2123 1/2 Ridge, Eva","None","2.4"));
        modelList.add(new CurrentOrder_Model("","4:14","2123 1/2 Ridge, Eva","None","2.4"));
        adapter = new CurrentOrder_Adapter(getActivity(),modelList);
        rv_current.setAdapter(adapter);*/

        return view;
    }

    public class CurrentOrder_Adapter extends RecyclerView.Adapter<CurrentOrder_Adapter.MyViewHolder> {

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
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

           /* holder.driver.setText(models.get(position).getDriver());
            holder.time.setText(models.get(position).getTime());
            holder.tip.setText(models.get(position).getTip());
            holder.customer.setText(models.get(position).getCustomer());*/

            holder.linear.setBackgroundColor(Color.parseColor("#FFC0BB"));

            holder.linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.linear_order.setVisibility(View.VISIBLE);
                    holder.button_info.setText("Re-Assign");
                }
            });
//            holder.button_info.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    context.startActivity(new Intent(context, DriverList.class));
//                }
//            });



        }

        @Override
        public int getItemCount() {
            return models.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView restaurant,time,customer,driver,tip;
            LinearLayout linear,linear_order,rqst_dlvry_tm,lin_tips,unit;
            Button button_info;
            CardView card_btn;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                driver = itemView.findViewById(R.id.driver);
                time = itemView.findViewById(R.id.time);
                customer = itemView.findViewById(R.id.customer);
                tip = itemView.findViewById(R.id.tip);
                linear = itemView.findViewById(R.id.linear);
                linear_order = itemView.findViewById(R.id.linear_order);
                rqst_dlvry_tm = itemView.findViewById(R.id.rqst_dlvry_tm);
                //lin_tips = itemView.findViewById(R.id.lin_tips);
                unit = itemView.findViewById(R.id.unit);


            }
        }
    }
}
