package com.experiments.dineatmytime.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.experiments.dineatmytime.databinding.LayoutHistoryBinding;
import com.experiments.dineatmytime.model.Booking;

import java.util.List;


public class BookingHistoryAdapter extends RecyclerView.Adapter<BookingHistoryAdapter.ViewHolder> {

    private List<Booking> bookingList;

    public BookingHistoryAdapter(List<Booking> restaurantList) {
        this.bookingList = restaurantList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutHistoryBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Booking booking = bookingList.get(position);

        holder.binding.tvShopName.setText(booking.getResName());


        holder.binding.tvSlotStart.setText("Booking Time : " + booking.getBookingDateTime());
        holder.binding.tvAmount.setText("Rs. " + booking.getAmount());
        holder.binding.tvTableNo.setText("Table No. " + booking.getTableNo());


    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        LayoutHistoryBinding binding;

        public ViewHolder(@NonNull LayoutHistoryBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }


}



