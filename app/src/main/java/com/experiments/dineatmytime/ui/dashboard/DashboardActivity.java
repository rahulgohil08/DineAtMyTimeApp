package com.experiments.dineatmytime.ui.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import com.experiments.dineatmytime.MainActivity;
import com.experiments.dineatmytime.R;
import com.experiments.dineatmytime.adapters.RestaurantAdapter;
import com.experiments.dineatmytime.databinding.ActivityDashboardBinding;
import com.experiments.dineatmytime.model.Restaurant;
import com.experiments.dineatmytime.model.RestaurantDetails;
import com.experiments.dineatmytime.network.Api;
import com.experiments.dineatmytime.network.AppConfig;
import com.experiments.dineatmytime.network.ServerResponse;
import com.experiments.dineatmytime.ui.booking.BookingHistoryActivity;
import com.experiments.dineatmytime.ui.login.Login;
import com.experiments.dineatmytime.ui.restaurant.BookingActivity;
import com.experiments.dineatmytime.ui.restaurant.RestaurantDetailsActivity;
import com.experiments.dineatmytime.utils.Config;
import com.experiments.dineatmytime.utils.SharedPrefManager;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RestaurantAdapter.RestaurantInterface {

    private ActivityDashboardBinding binding;
    private ActionBarDrawerToggle toggle;
    private RestaurantAdapter restaurantAdapter;

    private final Activity activity = this;

    private List<Restaurant> restaurantList = new ArrayList<>();
    private SharedPrefManager sharedPrefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPrefManager = new SharedPrefManager(activity);

        init();
        clickListener();

    }


    /*----------------------------- Init ----------------------------*/


    private void init() {

        toggle = new ActionBarDrawerToggle(this, binding.drawer, R.string.open, R.string.close);

        binding.drawer.addDrawerListener(toggle);
        toggle.syncState();

        setSupportActionBar(binding.includedContent.includedToolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_toggle);

        binding.nav.setNavigationItemSelectedListener(this);

        restaurantAdapter = new RestaurantAdapter(activity, restaurantList, this);

        binding.includedContent.recyclerView.setHasFixedSize(true);
        binding.includedContent.recyclerView.setAdapter(restaurantAdapter);


        getRestaurantDataFromServer();

    }


    /*----------------------------- Get Restaurant Data From Server ----------------------------*/


    private void getRestaurantDataFromServer() {

        Retrofit retrofit = AppConfig.getRetrofit();
        Api service = retrofit.create(Api.class);

        Call<ServerResponse> call = service.getRestaurantList(Config.user_id);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                ServerResponse serverResponse = response.body();
                restaurantList.clear();
                restaurantList.addAll(serverResponse.getRestaurantList());
                restaurantAdapter.notifyDataSetChanged();

                binding.includedContent.loadingSpinner.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Config.showToast(activity, t.getMessage());
            }
        });

    }


    /*----------------------------- Click Listener ----------------------------*/


    private void clickListener() {

    }

    /*----------------------------- On Option Item Selected ----------------------------*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (toggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }


    /*----------------------------- On Navigation Item Selected ----------------------------*/


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.home:
                openActivity(DashboardActivity.class);
                return true;


            case R.id.logout:
                sharedPrefManager.clear();
                openActivity(Login.class);
                return true;


            case R.id.booking_history:
                openActivity(BookingHistoryActivity.class);
                return true;

            default:
                return false;
        }
    }


    /*----------------------------- Navigate Direction ----------------------------*/

    private void openActivity(Class aclass) {
        Intent intent = new Intent(activity, aclass);
        startActivity(intent);
    }


    @Override
    public void onClick(Restaurant restaurant) {
        Intent intent = new Intent(activity, RestaurantDetailsActivity.class);
        intent.putExtra("res_id", Integer.parseInt(restaurant.getResId()));
        startActivity(intent);
    }
}