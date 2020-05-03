package com.limelite.limeli8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MyBookingsDetails extends AppCompatActivity {

    TextView orderId, productName, productSubTitle, productAmount, orderDate, userName, userAddress, priceDetailsPrice, gstShow, gstPrice, offerPrice, delivery_price, quantityMultiplePrice, totalAmount, grand_total;
    ImageView statusIcon, productUrl;
    RelativeLayout onOrderAboveLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings_details);

        Toolbar toolbar = findViewById(R.id.homePageToolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Order Details");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_back_button);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setTitleTextAppearance(this, R.style.fontFamily);

        orderId = findViewById(R.id.order_id);
        productName = findViewById(R.id.product_name);
        productSubTitle = findViewById(R.id.product_sub_title);
        productUrl = findViewById(R.id.product_image);
        productAmount = findViewById(R.id.product_price);
        statusIcon = findViewById(R.id.status_icon);
        orderDate = findViewById(R.id.status_text);
        userName = findViewById(R.id.user_name);
        userAddress = findViewById(R.id.user_address);
        quantityMultiplePrice = findViewById(R.id.quantity_multiply_price);
        priceDetailsPrice = findViewById(R.id.quantity_multiply_price_text);
        gstShow = findViewById(R.id.gst_show);
        gstPrice = findViewById(R.id.gst);
        onOrderAboveLayout = findViewById(R.id.on_order_above_layout);
        offerPrice = findViewById(R.id.on_order_above_price);
        delivery_price = findViewById(R.id.deliveryPrice);
        totalAmount = findViewById(R.id.totalAmount);
        grand_total = findViewById(R.id.grand_total);

        Intent intent = getIntent();
        String key = intent.getStringExtra("key");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users/" + FirebaseAuth.getInstance().getUid() + "/myBookings/" + key);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String address, date, _deliveryPrice, _grandTotalPrice, _gstPrice, _gstValue, _name, _offerPrice, _orderId, _orderStatus, _price,
                        _priceWithQuantity, _productName, _productSubTitleValue, _productUrl, _totalPrice, _deliveryDate;


                address = dataSnapshot.child("address").getValue(String.class);
                date = dataSnapshot.child("date").getValue(String.class);
                _deliveryPrice = dataSnapshot.child("deliveryPrice").getValue(String.class);
                _grandTotalPrice = dataSnapshot.child("grandTotalPrice").getValue(String.class);
                _gstPrice = dataSnapshot.child("gstPrice").getValue(String.class);
                _gstValue = dataSnapshot.child("gstValue").getValue(String.class);
                _name = dataSnapshot.child("name").getValue(String.class);
                _offerPrice = dataSnapshot.child("offerPrice").getValue(String.class);
                _orderId = dataSnapshot.child("orderId").getValue(String.class);
                _orderStatus = dataSnapshot.child("orderStatus").getValue(String.class);
                _price = dataSnapshot.child("price").getValue(String.class);
                _priceWithQuantity = dataSnapshot.child("priceWithQuantity").getValue(String.class);
                _productName = dataSnapshot.child("productName").getValue(String.class);
                _productSubTitleValue = dataSnapshot.child("productSubTitleValue").getValue(String.class);
                _productUrl = dataSnapshot.child("productUrl").getValue(String.class);
                _totalPrice = dataSnapshot.child("totalPrice").getValue(String.class);
                _deliveryDate = dataSnapshot.child("deliveryDate").getValue(String.class);


                if (_orderId != null) {
                    orderId.setText("Order Id - " + _orderId);
                }

                if (_productName != null) {
                    productName.setText(_productName);
                }

                if (_productSubTitleValue != null) {
                    productSubTitle.setText(_productSubTitleValue);
                }

                if (_productUrl != null) {
                    Picasso.get()
                            .load(_productUrl)
                            .into(productUrl);
                }

                if (_price != null) {
                    productAmount.setText(_price);
                }

                if (_orderStatus != null) {

                    if (_orderStatus.equals("Order Placed")) {
                        statusIcon.setColorFilter(Color.YELLOW);
                    } else if (_orderStatus.equals("Delivered")) {
                        statusIcon.setColorFilter(Color.GREEN);
                    } else {
                        statusIcon.setColorFilter(Color.RED);
                    }

                }

                if (date != null) {
                    if (_deliveryDate != null) {
                        orderDate.setText(_orderStatus + " on " + _deliveryDate);
                    } else {
                        orderDate.setText(_orderStatus + " on " + date);
                    }
                }

                if (_name != null) {
                    userName.setText(_name);
                }

                if (address != null) {
                    userAddress.setText(address);
                }

                if (_priceWithQuantity != null) {
                    priceDetailsPrice.setText(_priceWithQuantity);
                    quantityMultiplePrice.setText(_price);
                }

                if (!_offerPrice.equals("")) {
                    onOrderAboveLayout.setVisibility(View.VISIBLE);
                    offerPrice.setText(_offerPrice);
                }

                if (_deliveryPrice != null) {
                    delivery_price.setText(_deliveryPrice);
                }

                if (_totalPrice != null) {
                    totalAmount.setText(_totalPrice);
                }

                if (_gstValue != null) {
                    gstShow.setText(_gstValue);
                }

                if (_gstPrice != null) {
                    gstPrice.setText(_gstPrice);
                }

                if (_grandTotalPrice != null) {
                    grand_total.setText(_grandTotalPrice);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
