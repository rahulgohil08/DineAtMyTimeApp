package com.experiments.dineatmytime.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.experiments.dineatmytime.R;
import com.experiments.dineatmytime.databinding.LayoutRestaurantsBinding;
import com.experiments.dineatmytime.model.Restaurant;
import com.experiments.dineatmytime.utils.Config;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {

    private Context context;
    private List<Restaurant> restaurantList;

    public RestaurantAdapter(Context context, List<Restaurant> restaurantList) {
        this.context = context;
        this.restaurantList = restaurantList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutRestaurantsBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Restaurant restaurant = restaurantList.get(position);

        holder.binding.name.setText(restaurant.getResName());

        Glide.with(holder.itemView)
                .load(Config.restaurantImageUrl + restaurant.getResImage())
                .placeholder(R.drawable.user_logo)
                .into(holder.binding.image);


        holder.itemView.setOnClickListener(view -> {

        });

    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        LayoutRestaurantsBinding binding;

        public ViewHolder(@NonNull LayoutRestaurantsBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }

}
