package com.eating.restaurant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.eating.restaurant.Adapter.OrderPagerAdapter;
import com.eating.restaurant.ApiClient.ApiClient;
import com.eating.restaurant.Interface.ApiInterface;
import com.eating.restaurant.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    NavigationView navview;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    Button button_new;
    ApiInterface apiInterface;
    User user;
    String total_row="0";
    TextView txt_pdt_time;
    DatabaseReference databaseReference, databaseReference2;
    Query query;
    public BadgeDrawable badgeDrawable;
    String current_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);


        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        user = new User(this);
        //databaseReference= FirebaseDatabase.getInstance().getReference("pdt");
        databaseReference2= FirebaseDatabase.getInstance().getReference("order_list1");
        query=databaseReference2.orderByChild("restaurant_name").equalTo(user.getRestaurant_Name().trim());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
        current_date = sdf.format(new Date());

        toolbar.setTitle(user.getRestaurant_Name());
        setSupportActionBar(toolbar);


        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel("MyNotification","MyNotification", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }


        drawer = findViewById(R.id.drawer);
        navview = findViewById(R.id.navview);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabLayout);
        button_new = findViewById(R.id.button_new);
        txt_pdt_time = findViewById(R.id.pdt_time);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();




        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {

                if (task.isSuccessful())
                {
                    String newToken = task.getResult();

                    Call<String> call=apiInterface.insert_token(user.getRestaurant_Name(),newToken);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                }
            }
        });




        navview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem menuItem) {

                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.menu_home:
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                        break;
                    case R.id.order_history:
                        startActivity(new Intent(MainActivity.this, Order_History.class));
                        break;
                    case R.id.drawer_logout:
                        user.removeUser();
                         startActivity(new Intent(MainActivity.this, Login_Activity.class));
                        finish();
                        break;

                    default:
                        return true;
                }
                return true;
            }
        });

        viewPager.setAdapter(new OrderPagerAdapter(this));
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:{
                        tab.setText("Open Orders");
                         badgeDrawable = tab.getOrCreateBadge();

                        badgeDrawable.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.red));
                        badgeDrawable.setVisible(true);
                      //  fetchNomRow(badgeDrawable);
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
                                                badgeDrawable.setVisible(true);
                                                badgeDrawable.setNumber(size);
                                            }

                                         }
                                    }
                                }else {
                                    badgeDrawable.setVisible(false);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        //badgeDrawable.setNumber(Integer.valueOf(total_row));
                        break;
                    }
                    case 1:{
                        tab.setText("Completed");
//                        BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
//                        badgeDrawable.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.red));
//                        badgeDrawable.setVisible(true);
//                        badgeDrawable.setNumber(2);
                        break;
                    }
                }
            }
        }
        );
        tabLayoutMediator.attach();

        button_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, New_Order.class));
            }
        });


        //fetchPDTTime();

    }

    private void fetchNomRow(BadgeDrawable badgeDrawable)
    {
        Call<String> call = apiInterface.fetch_num_row(user.getRestaurant_Name());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {
                        total_row = jsonObject.getString("total_row");
                        badgeDrawable.setNumber(Integer.valueOf(total_row));
                    }
                    else
                    {

                    }

                } catch (JSONException e) {

                    Toast.makeText(MainActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(MainActivity.this, "slow network", Toast.LENGTH_SHORT).show();

            }
        });
    }




    private void fetchPDTTime()
    {
        Call<String> call = apiInterface.fetch_pdt_time(user.getRestaurant_Name().trim());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {
                        String area = jsonObject.getString("area");

                        txt_pdt_time.setText("");
                        databaseReference.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                if(snapshot.getKey().equals(area.trim())) {
                                    HashMap<String, String> value = (HashMap<String, String>) snapshot.getValue();
                                    txt_pdt_time.setText(value.get("time"));
                                }
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                if(snapshot.getKey().equals(area.trim())) {
                                    HashMap<String, String> value = (HashMap<String, String>) snapshot.getValue();
                                    txt_pdt_time.setText(value.get("time"));
                                }
                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                                if(snapshot.getKey().equals(area.trim())) {
                                    txt_pdt_time.setText("");
                                }
                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });



                    }
                    else
                    {
                        txt_pdt_time.setText("");
                    }

                } catch (JSONException e) {

                    Toast.makeText(MainActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(MainActivity.this, "slow network", Toast.LENGTH_SHORT).show();

            }
        });
    }


}