package com.example.fooddelivery.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.R;
import com.example.fooddelivery.model.Food;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.MyViewHolder> {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fooddelivery-cdab1-default-rtdb.firebaseio.com/");

    private Activity mActivity;
    private Context mContext;
    private ArrayList<Food> arrayList = new ArrayList<>();
    private Dialog dialog;

    int totalQuantity = 0;
    int totalPrice = 0;

    private List<MyViewHolder> myViewHolders = new ArrayList<>();



    public FoodAdapter(Activity activity, Context context, ArrayList<Food> arrayList) {
        this.mActivity = activity;
        this.mContext = context;
        this.arrayList = arrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_menu, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view, this);
        myViewHolders.add(viewHolder);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Food food = arrayList.get(position);
        int resourceId = mContext.getResources().getIdentifier(food.getImageResource(), "drawable", mContext.getPackageName());
        holder.imageResource.setImageResource(resourceId);
        holder.name.setText(food.getName());
        holder.description.setText(food.getDescription());
        holder.price.setText(String.valueOf(food.getPrice()));

        holder.originalPrice = food.getPrice();
        holder.updateQuantity();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageResource;
        TextView name, description, price, quantityTextView, updatePriceTextView;
        int quantity = 0;
        int originalPrice;
        private FoodAdapter adapter;



        public MyViewHolder(View itemView, FoodAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            imageResource = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.textViewName);
            description = itemView.findViewById(R.id.textViewDescription);
            price = itemView.findViewById(R.id.textViewPrice);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            updatePriceTextView = itemView.findViewById(R.id.updatePriceTextView);

            // Set initial originalPrice value based on the current Food's price
//            originalPrice = arrayList.get(getAdapterPosition()).getPrice();

            // Calculate and set the initial price
            updateQuantity();

            Button minusButton = itemView.findViewById(R.id.minusButton);
            Button plusButton = itemView.findViewById(R.id.plusButton);

            plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quantity++;
                    updateQuantity();
                }
            });

            minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (quantity > 0) {
                        quantity--;
                        updateQuantity();
                    }
                }
            });
        }

        private void updateQuantity() {
            quantityTextView.setText(String.valueOf(quantity));

            int newPrice = originalPrice * quantity;
            String formattedPrice = "Rp " + String.format("%,d", newPrice);

            updatePriceTextView.setText(formattedPrice);

            // Update total quantity and total price in the adapter only if quantity changes
            totalQuantity = totalQuantity - this.quantity + quantity;
            totalPrice = totalPrice - (originalPrice * this.quantity) + newPrice;

            // Notify the adapter to update the main activity's TextViews
            adapter.updateTotal();
        }


    }

    public int getTotalPrice() {
        totalPrice = 0;

        for (MyViewHolder holder : myViewHolders) {
            totalPrice += holder.originalPrice * holder.quantity;
        }

        return totalPrice;
    }

    public ArrayList<Integer> getQuantities() {
        ArrayList<Integer> quantities = new ArrayList<>();

        for (MyViewHolder holder : myViewHolders) {
            quantities.add(holder.quantity);
        }

        return quantities;
    }

    public ArrayList<Integer> getPrices() {
        ArrayList<Integer> prices = new ArrayList<>();

        for (MyViewHolder holder : myViewHolders) {
            prices.add(holder.originalPrice);
        }

        return prices;
    }

    public ArrayList<String> getFoodName() {
        ArrayList<String> foodNames = new ArrayList<>();

        for (MyViewHolder holder : myViewHolders) {
            foodNames.add(holder.name.getText().toString());
        }

        return foodNames;
    }



    public void updateTotal() {
        totalQuantity = 0;
        totalPrice = 0;

        for (MyViewHolder holder : myViewHolders) {
            totalQuantity += holder.quantity;
            totalPrice += holder.originalPrice * holder.quantity;
        }

        // Update the main activity's TextViews
        TextView totalQuantityTextView = mActivity.findViewById(R.id.totalQuantityTextView);
        TextView totalPriceTextView = mActivity.findViewById(R.id.totalPriceTextView);

        totalQuantityTextView.setText("Total Item: " + totalQuantity);
        totalPriceTextView.setText("Total Harga: Rp " + String.format("%,d", totalPrice));
    }

}
