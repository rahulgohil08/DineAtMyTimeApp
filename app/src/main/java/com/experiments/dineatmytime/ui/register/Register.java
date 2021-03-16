package com.experiments.dineatmytime.ui.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


import com.experiments.dineatmytime.R;
import com.experiments.dineatmytime.databinding.ActivityRegisterBinding;
import com.experiments.dineatmytime.network.Api;
import com.experiments.dineatmytime.network.AppConfig;
import com.experiments.dineatmytime.network.ServerResponse;
import com.experiments.dineatmytime.ui.login.Login;
import com.experiments.dineatmytime.utils.Config;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Register extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        clickLisetner();


    }

    private void clickLisetner() {
        binding.registerbtn.setOnClickListener(v -> {
            String Name = binding.name.getText().toString();
            String Email = binding.email.getText().toString();
            String Password = binding.password.getText().toString();
            String Phone = binding.phone.getText().toString();
            String Address = binding.address.getText().toString();

            Log.v("Name", Name);
            Log.v("Email", Email);
            Log.v("Password", Password);
            Log.v("Phone", Phone);
            Log.v("Address", Address);


            if (TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password) || TextUtils.isEmpty(Name) || TextUtils.isEmpty(Phone) || TextUtils.isEmpty(Address)) {
                binding.password.setError("All fields are required!!");
                return;
            }


            doRegister(Email, Password, Name, Phone, Address);

        });

        binding.btnLogin.setOnClickListener(v -> {
            Intent obj = new Intent(Register.this, Login.class);
            Register.this.startActivity(obj);

        });
    }

    private void doRegister(String email, String password, String name, String phone, String address) {

        Log.v("Name", name);
        Log.v("Email", email);
        Log.v("Password", password);
        Log.v("Phone", phone);
        Log.v("Address", address);


        Retrofit retrofit = AppConfig.getRetrofit();
        Api service = retrofit.create(Api.class);

        Call<ServerResponse> call = service.register(name, email, password, phone, address);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                if (response.body() != null) {
                    ServerResponse serverResponse = response.body();
                    if (!serverResponse.getError()) {
                        Config.showToast(context, serverResponse.getMessage());
                        Intent home = new Intent(Register.this, Login.class);
                        startActivity(home);
                        finish();
                    } else {
                        Config.showToast(context, serverResponse.getMessage());

                    }

                }

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Config.showToast(context, t.getMessage());
            }
        });


    }
}