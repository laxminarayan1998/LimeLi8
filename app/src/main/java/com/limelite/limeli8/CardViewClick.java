package com.limelite.limeli8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CardViewClick extends AppCompatActivity {

    List<ProductModel> productModelList;

    RecyclerView printRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view_click);

        Toolbar toolbar = findViewById(R.id.homePageToolbar);


        productModelList = new ArrayList<>();
        printRecyclerView = findViewById(R.id.tvc_recycler_view);

        Intent intent = getIntent();
        String item = intent.getStringExtra("item");

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_back_button);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setTitleTextAppearance(this, R.style.fontFamily);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products");

        if (item != null) {

            getSupportActionBar().setTitle(item);

            if (item.equals("Print")) {

                Query printQuery = databaseReference.orderByChild("category").equalTo(item);
                final Query allPrintQuery = databaseReference.orderByChild("category").equalTo("Merchandise Print");

                printQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        productModelList.clear();

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            ProductModel productModel = dataSnapshot1.getValue(ProductModel.class);
                            productModelList.add(productModel);

                        }

                        allPrintQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                    ProductModel productModel = dataSnapshot1.getValue(ProductModel.class);
                                    productModelList.add(productModel);

                                }

                                HorizontalScrollRecyclerAdapter horizontalScrollRecyclerAdapter = new HorizontalScrollRecyclerAdapter(CardViewClick.this, productModelList);
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(CardViewClick.this, 2);
                                printRecyclerView.setLayoutManager(gridLayoutManager);

                                printRecyclerView.setAdapter(horizontalScrollRecyclerAdapter);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            } else {

                Query tvcQuery = databaseReference.orderByChild("category").equalTo(item);

                tvcQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        productModelList.clear();

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            ProductModel productModel = dataSnapshot1.getValue(ProductModel.class);
                            productModelList.add(productModel);

                        }

                        HorizontalScrollRecyclerAdapter horizontalScrollRecyclerAdapter = new HorizontalScrollRecyclerAdapter(CardViewClick.this, productModelList);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(CardViewClick.this, 2);
                        printRecyclerView.setLayoutManager(gridLayoutManager);

                        printRecyclerView.setAdapter(horizontalScrollRecyclerAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        HomePage.MAKE_ITEM_CENTER = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        HomePage.MAKE_ITEM_CENTER = false;
    }
}
