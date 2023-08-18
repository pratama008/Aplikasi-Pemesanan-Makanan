package com.example.fooddelivery;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.adapter.FoodAdapter;
import com.example.fooddelivery.model.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fooddelivery-cdab1-default-rtdb.firebaseio.com/");
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FoodAdapter adapter;
    ArrayList<Food> arrayList;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;

        arrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerFood);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new FoodAdapter(activity, this, arrayList);
        recyclerView.setAdapter(adapter);

        databaseReference.child("foods").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Food food = dataSnapshot.getValue(Food.class);
                    arrayList.add(food);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Button orderButton = findViewById(R.id.orderButton);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Calculate the total order price based on the data in the FoodAdapter
                int totalOrderPrice = adapter.getTotalPrice();

                // Get the quantities of each item
                ArrayList<Integer> quantities = new ArrayList<>();
                ArrayList<Integer> prices = new ArrayList<>();
                ArrayList<String> foodNames = new ArrayList<>();

                boolean hasSelectedItems = false; // Tandai apakah ada item yang dipilih

                for (int i = 0; i < adapter.getQuantities().size(); i++) {
                    int quantity = adapter.getQuantities().get(i);
                    int price = adapter.getPrices().get(i);
                    String foodName = adapter.getFoodName().get(i);

                    if (quantity > 0) {
                        hasSelectedItems = true; // Set hasSelectedItems ke true
                        quantities.add(quantity);
                        prices.add(price);
                        foodNames.add(foodName);
                    }
                }

                if (!hasSelectedItems) {
                    // Jika tidak ada item yang dipilih, tampilkan peringatan
                    Toast.makeText(MainActivity.this, "Harus memilih makanan", Toast.LENGTH_SHORT).show();
                    return; // Keluar dari method onClick
                }

                int totalQuantityAllItem = 0;
                for (int quantity : quantities) {
                    totalQuantityAllItem += quantity;
                }

                Intent orderIntent = new Intent(MainActivity.this, DaftarPesananActivity.class);
                orderIntent.putExtra("totalOrderPrice", totalOrderPrice);
                orderIntent.putExtra("quantities", quantities);
                orderIntent.putExtra("prices", prices);
                orderIntent.putExtra("foodNames", foodNames);
                orderIntent.putExtra("totalQuantityAllItem", totalQuantityAllItem); // Add this line
                startActivity(orderIntent);
            }
        });

        Button riwayatButton = findViewById(R.id.riwayatButton);
        riwayatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(MainActivity.this, RiwayatPesananActivity.class);
                startActivity(mainIntent);
            }
        });


    }

}