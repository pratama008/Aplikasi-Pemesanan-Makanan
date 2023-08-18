package com.example.fooddelivery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.adapter.OrderAdapter;
import com.example.fooddelivery.model.Order;
import com.example.fooddelivery.model.OrderItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DaftarPesananActivity extends AppCompatActivity {

    private TextView totalOrderAllPriceTextView;
    private RadioGroup paymentOptionsRadioGroup;
    private RadioButton tunaiRadioButton, debitRadioButton, ovoRadioButton;
    private Button bayarSekarangButton;

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_pesanan);

        totalOrderAllPriceTextView = findViewById(R.id.totalOrderAllPriceTextView);
        paymentOptionsRadioGroup = findViewById(R.id.paymentOptionsRadioGroup);
        tunaiRadioButton = findViewById(R.id.tunaiRadioButton);
        debitRadioButton = findViewById(R.id.debitRadioButton);
        ovoRadioButton = findViewById(R.id.ovoRadioButton);
        bayarSekarangButton = findViewById(R.id.bayarSekarangButton);

        ArrayList<Integer> quantities = getIntent().getIntegerArrayListExtra("quantities");
        ArrayList<Integer> prices = getIntent().getIntegerArrayListExtra("prices");
        ArrayList<String> foodNames = getIntent().getStringArrayListExtra("foodNames"); // Ambil data foodNames

// Buat list OrderItem berdasarkan data yang Anda terima
        List<OrderItem> orderItems = new ArrayList<>();
        for (int i = 0; i < quantities.size(); i++) {
            int quantity = quantities.get(i);
            int price = prices.get(i); // Ambil harga sesuai dengan indeks item
            String foodName = foodNames.get(i); // Ambil nama makanan sesuai dengan indeks item
            orderItems.add(new OrderItem(foodName, quantity, price)); // Sertakan nama makanan dalam OrderItem
        }



        // Set up RecyclerView dengan adapter
        recyclerView = findViewById(R.id.recyclerOrder);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderAdapter = new OrderAdapter(orderItems);
        recyclerView.setAdapter(orderAdapter);


        // Retrieve data from intent
        int totalOrderPrice = getIntent().getIntExtra("totalOrderPrice", 0);
        totalOrderAllPriceTextView.setText("Total : Rp " + String.format("%,d", totalOrderPrice));

        bayarSekarangButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get selected payment option
                int selectedId = paymentOptionsRadioGroup.getCheckedRadioButtonId();

                if (selectedId == -1) {
                    // No payment option selected, show warning toast
                    Toast.makeText(DaftarPesananActivity.this, "Pilih metode pembayaran terlebih dahulu", Toast.LENGTH_SHORT).show();
                    return; // Stop further execution
                }

                String paymentOption = "";
                if (selectedId == R.id.tunaiRadioButton) {
                    paymentOption = "Tunai";
                } else if (selectedId == R.id.debitRadioButton) {
                    paymentOption = "Debit BCA";
                } else if (selectedId == R.id.ovoRadioButton) {
                    paymentOption = "OVO";
                }

                // Get total quantity and total order price from intent
                int totalQuantityAllItem = getIntent().getIntExtra("totalQuantityAllItem", 0);
                int totalOrderAllPrice = getIntent().getIntExtra("totalOrderPrice", 0);

                // Get current date and time in Indonesian format
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                String currentDate = dateFormat.format(new Date());
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String currentTime = timeFormat.format(new Date());


                // Create an Order object
                Order order = new Order(totalQuantityAllItem, totalOrderAllPrice, paymentOption, currentDate, currentTime);

                // Get a reference to the "orders" node in Firebase
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fooddelivery-cdab1-default-rtdb.firebaseio.com/");

                // Push the order data to Firebase
                String finalPaymentOption = paymentOption;
                databaseReference.child("orders").push().setValue(order)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Display a toast message
                                Toast.makeText(DaftarPesananActivity.this, "Pembayaran anda berhasil!", Toast.LENGTH_SHORT).show();

                                // Open the RiwayatPesananActivity
                                Intent riwayatIntent = new Intent(DaftarPesananActivity.this, RiwayatPesananActivity.class);
                                startActivity(riwayatIntent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Display a toast message in case of failure
                                Toast.makeText(DaftarPesananActivity.this, "Gagal menyimpan data pesanan", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }
}
