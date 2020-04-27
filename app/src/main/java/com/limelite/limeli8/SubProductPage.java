package com.limelite.limeli8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SubProductPage extends AppCompatActivity {

    DatabaseReference databaseReference;
    List<SubProductsModel> subProductsModelList = new ArrayList<>();

    RecyclerView subProductRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_product_page);


        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_button);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setTitleTextAppearance(this, R.style.fontFamily);

        subProductRecyclerView = findViewById(R.id.sub_product_recycler_view);


        Bundle bundle = getIntent().getExtras();
        final String productName = bundle.getString("productName");


        databaseReference = FirebaseDatabase.getInstance().getReference("Products/" + productName + "/subProducts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                subProductsModelList.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    SubProductsModel subProductsModel = postSnapshot.getValue(SubProductsModel.class);
                    subProductsModelList.add(subProductsModel);
                }

                getSupportActionBar().setTitle(productName + "(" + subProductsModelList.size() + ")");


                subProductsRecyclerAdapter subProductsRecyclerAdapter = new subProductsRecyclerAdapter(SubProductPage.this, subProductsModelList);


                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(SubProductPage.this, DividerItemDecoration.HORIZONTAL);
                dividerItemDecoration.setDrawable(SubProductPage.this.getDrawable(R.color.grey_color));
                subProductRecyclerView.addItemDecoration(dividerItemDecoration);

                DividerItemDecoration dividerItemDecoration1 = new DividerItemDecoration(SubProductPage.this, DividerItemDecoration.VERTICAL);
                dividerItemDecoration1.setDrawable(SubProductPage.this.getDrawable(R.color.grey_color));
                subProductRecyclerView.addItemDecoration(dividerItemDecoration1);


                GridLayoutManager gridLayoutManager = new GridLayoutManager(SubProductPage.this, 2);
                subProductRecyclerView.setLayoutManager(gridLayoutManager);

                subProductRecyclerView.setAdapter(subProductsRecyclerAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
