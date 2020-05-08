package com.limelite.limeli8;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SlidePageAdapter extends PagerAdapter {

    private Context context;
    private List<SlideModel> slideItems;
    String productImageUrl, productName, productFinalPrice, productStrikePrice, productPcs, productStrikeOffer;
    static String MAINPRODUCTNAMEFROMBANNER;

    public SlidePageAdapter(Context context, List<SlideModel> slideItems) {
        this.context = context;
        this.slideItems = slideItems;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View slideLayout = inflater.inflate(R.layout.slide_item, null);

        ImageView slideImg = slideLayout.findViewById(R.id.slide_image);

        Picasso
                .get()
                .load(slideItems.get(position).getUrl())
                .into(slideImg);


        slideLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HorizontalScrollRecyclerAdapter.PRODUCT_NAME = null;
                subProductsRecyclerAdapter.SUB_PRODUCT_NAME = null;

                MAINPRODUCTNAMEFROMBANNER = slideItems.get(position).getProduct();

                if (slideItems.get(position).getProduct() == null && slideItems.get(position).getSubProduct() == null) {
                  //hello added
                } else if (slideItems.get(position).getSubProduct() == null) {

                    Intent intent = new Intent(context, SubProductPage.class);
                    intent.putExtra("productName", slideItems.get(position).getProduct());
                    context.startActivity(intent);

                } else {

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products/" + slideItems.get(position).getProduct() + "/subProducts/" + slideItems.get(position).getSubProduct());
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            productImageUrl = dataSnapshot.child("imageUrl").getValue(String.class);
                            productName = dataSnapshot.child("name").getValue(String.class);

                            String strikePrice = dataSnapshot.child("strikePrice").getValue(String.class);
                            String flatOff = dataSnapshot.child("flatOff").getValue(String.class);

                            int strikePriceInt = Integer.parseInt(strikePrice);
                            int flatOffInt = Integer.parseInt(flatOff);

                            productFinalPrice = String.valueOf(strikePriceInt - flatOffInt);
                            productStrikePrice = strikePrice;
                            productPcs = dataSnapshot.child("pieces").getValue(String.class);
                            productStrikeOffer = dataSnapshot.child("flatOff").getValue(String.class);

                            Intent intent = new Intent(context, SpecificationPage.class);
                            intent.putExtra("product", slideItems.get(position).getProduct());
                            intent.putExtra("subProduct", slideItems.get(position).getSubProduct());
                            intent.putExtra("product_image", productImageUrl);
                            intent.putExtra("product_name", productName);
                            intent.putExtra("product_final_price", productFinalPrice);
                            intent.putExtra("product_strike_price", productStrikePrice);
                            intent.putExtra("products_pcs", " for" + productPcs + "pcs");
                            intent.putExtra("product_strike_offer", "flat ₹" + productStrikeOffer + " off");

                            if (productPcs == null) {
                                Intent newIntent = new Intent(context, AnotherLayout.class);
                                HorizontalScrollRecyclerAdapter.PRODUCT_NAME = slideItems.get(position).getProduct();
                                newIntent.putExtra("key", slideItems.get(position).getSubProduct());
                                context.startActivity(newIntent);
                                return;
                            }

                            context.startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }
        });

        container.addView(slideLayout);


        return slideLayout;

    }

    @Override
    public int getCount() {
        return slideItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
