package com.example.fooddelivery.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.R;
import com.example.fooddelivery.model.Order;

import java.util.List;

public class RiwayatPesananAdapter extends RecyclerView.Adapter<RiwayatPesananAdapter.ViewHolder> {

    private List<Order> riwayatPesananList;

    public RiwayatPesananAdapter(List<Order> riwayatPesananList) {
        this.riwayatPesananList = riwayatPesananList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_riwayat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = riwayatPesananList.get(position);

        holder.quantityTextView.setText(order.getTotalQuantityAllItem() + " item");
        holder.priceTextView.setText("Rp " + order.getTotalOrderAllPrice());
        holder.dateTextView.setText(order.getCurrentDate());
        holder.paymentTextView.setText(order.getPaymentOption());
    }

    @Override
    public int getItemCount() {
        return riwayatPesananList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView quantityTextView, priceTextView, dateTextView, paymentTextView;

        ViewHolder(View itemView) {
            super(itemView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            paymentTextView = itemView.findViewById(R.id.paymentTextView);
        }
    }
}
