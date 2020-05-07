package com.limelite.limeli8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchItems extends AppCompatActivity {

    SearchView searchView;
    RecyclerView recyclerView;

    List<ProductModel> productModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_items);

        recyclerView = findViewById(R.id.recycler_view);
        searchView = findViewById(R.id.searchView);

        searchView.requestFocus();

        productModelList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                productModelList.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    ProductModel productModel = dataSnapshot1.getValue(ProductModel.class);
                    productModelList.add(productModel);

                }

//                HorizontalScrollRecyclerAdapter horizontalScrollRecyclerAdapter = new HorizontalScrollRecyclerAdapter(SearchItems.this, productModelList);
//                GridLayoutManager gridLayoutManager = new GridLayoutManager(SearchItems.this, 2);
//                recyclerView.setLayoutManager(gridLayoutManager);
//
//                recyclerView.setAdapter(horizontalScrollRecyclerAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if (searchView != null) {

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return false;
                }
            });

        }

    }

    private void search(String string) {

        List<ProductModel> productModel = new ArrayList<>();

        for (ProductModel productModel1 : productModelList) {

            if (productModel1.getName().toLowerCase().contains(string.toLowerCase())) {
                productModel.add(productModel1);
            } else if (productModel1.getCategory().toLowerCase().contains(string.toLowerCase())) {
                productModel.add(productModel1);
            }

        }
        HorizontalScrollRecyclerAdapter horizontalScrollRecyclerAdapter = new HorizontalScrollRecyclerAdapter(SearchItems.this, productModel);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(SearchItems.this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(horizontalScrollRecyclerAdapter);
    }
}
