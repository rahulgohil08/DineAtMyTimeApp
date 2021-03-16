package com.experiments.dineatmytime.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.experiments.dineatmytime.databinding.ActivityLoginBinding;
import com.experiments.dineatmytime.model.User;
import com.experiments.dineatmytime.network.Api;
import com.experiments.dineatmytime.network.AppConfig;
import com.experiments.dineatmytime.network.ServerResponse;
import com.experiments.dineatmytime.ui.dashboard.DashboardActivity;
import com.experiments.dineatmytime.ui.register.Register;
import com.experiments.dineatmytime.utils.Config;
import com.experiments.dineatmytime.utils.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Login extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        clickListener();

    }



    /*---------------------------------------- Init ----------------------------------------------------------------*/

    private void init() {


    }

    /*---------------------------------------- Click Listeners ----------------------------------------------------------------*/


    public void clickListener() {

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = binding.edtEmail.getText().toString();
                String Password = binding.edtPassword.getText().toString();
                if (TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password)) {
                    binding.edtEmail.setError("All fields are required !!");
                    binding.edtPassword.setError("All fields are required!!");
                    return;
                }
                Log.v("Login ", "It Works");

                doLogin(Email, Password);

            }
        });


        binding.registerbtn.setOnClickListener(v -> {
            Intent obj = new Intent(Login.this, Register.class);
            Login.this.startActivity(obj);

        });
    }



    /*---------------------------------------- Do Login ----------------------------------------------------------------*/


    private void doLogin(String email, String password) {
        Retrofit retrofit = AppConfig.getRetrofit();
        Api service = retrofit.create(Api.class);

        Call<ServerResponse> call = service.login(email, password);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                if (response.body() != null) {

                    ServerResponse serverResponse = response.body();

                    if (!serverResponse.getError()) {

                        sendUserData(serverResponse.getUser());


                    } else {
                        Toast.makeText(Login.this, "Login unSuccessful", Toast.LENGTH_LONG).show();
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




    /*----------------------------------------------- Save User Data -----------------------------------------------*/

    private void sendUserData(User user) {

        SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
        sharedPrefManager.setInt("id", user.getId());
        sharedPrefManager.setString("name", user.getName());

        sharedPref();
    }


    @Override
    protected void onStart() {
        super.onStart();

        sharedPref();

    }

    private void sharedPref() {
        SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
        Config.user_id = sharedPrefManager.getInt("id");

        if (Config.user_id != -1) {

            openActivity(DashboardActivity.class);
            finish();
        }

    }


    private void openActivity(Class aclass) {
        Intent intent = new Intent(context, aclass);
        startActivity(intent);
    }
}