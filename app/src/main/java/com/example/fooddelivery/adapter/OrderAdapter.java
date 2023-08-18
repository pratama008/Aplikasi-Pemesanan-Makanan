package com.example.fooddelivery.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fooddelivery.R;
import com.example.fooddelivery.model.OrderItem;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<OrderItem> orderItemList; // Ganti OrderItem dengan nama model Anda

    public OrderAdapter(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderItem orderItem = orderItemList.get(position);

        // Mengisi tampilan item_order dengan data dari OrderItem
        holder.foodNameTextView.setText(orderItem.getFoodName());
        holder.quantityTextView.setText(String.valueOf(orderItem.getQuantity()));
        holder.priceTextView.setText("Rp " + String.format("%,d", orderItem.getPrice()));
        int totalItemPrice = orderItem.getQuantity() * orderItem.getPrice();
        holder.totalOrderItemPriceTextView.setText("Rp " + String.format("%,d", totalItemPrice));
    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

    // ViewHolder untuk item dalam RecyclerView
    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView foodNameTextView, quantityTextView, priceTextView, totalOrderItemPriceTextView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            foodNameTextView = itemView.findViewById(R.id.orderNameTextView);
            quantityTextView = itemView.findViewById(R.id.orderQuantityTextView);
            priceTextView = itemView.findViewById(R.id.oderPriceTextView);
            totalOrderItemPriceTextView = itemView.findViewById(R.id.totalOrderItemPriceTextView);
        }
    }
}
