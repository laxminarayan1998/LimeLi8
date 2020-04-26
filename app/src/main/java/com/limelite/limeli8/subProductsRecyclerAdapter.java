package com.limelite.limeli8;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class subProductsRecyclerAdapter extends RecyclerView.Adapter<subProductsRecyclerAdapter.CustomViewHolder> {

    private Context context;
    List<SubProductsModel> subProductsModelList;
    static String SUB_PRODUCT_NAME;

    public subProductsRecyclerAdapter(Context context, List<SubProductsModel> subProductsModelList) {
        this.context = context;
        this.subProductsModelList = subProductsModelList;
    }

    @NonNull
    @Override
    public subProductsRecyclerAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sub_product_item, parent, false);
        return new subProductsRecyclerAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final subProductsRecyclerAdapter.CustomViewHolder holder, final int position) {

        String strikeprice = subProductsModelList.get(position).getStrikePrice();
        String flatoff = subProductsModelList.get(position).getFlatOff();
        final int strikePrice = Integer.parseInt(strikeprice);
        final int flatOff = Integer.parseInt(flatoff);

        Picasso.get()
                .load(subProductsModelList.get(position).getImageUrl())
                .into(holder.subProductImage);

        holder.subProductTitle.setText(subProductsModelList.get(position).getName());
        holder.subProductPrice.setText(String.valueOf(strikePrice - flatOff));

        holder.subProductPcs.setText(" for"+ subProductsModelList.get(position).getPieces() + "pcs");

        if (flatoff.equals("0")) {
            holder.subProductStrikeOffer.setText("");
            holder.subProductStrikeThroughPrice.setText("");
        } else {
            holder.subProductStrikeThroughPrice.setText(" " + subProductsModelList.get(position).getStrikePrice() + " ");
            holder.subProductStrikeThroughPrice.setPaintFlags(holder.subProductStrikeThroughPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.subProductStrikeOffer.setText("flat ₹" + subProductsModelList.get(position).getFlatOff() + " off");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SUB_PRODUCT_NAME = subProductsModelList.get(position).getName();
                Intent intent = new Intent(context, SpecificationPage.class);
                intent.putExtra("product_image", subProductsModelList.get(position).getImageUrl());
                intent.putExtra("product_name", holder.subProductTitle.getText());
                intent.putExtra("product_final_price", holder.subProductPrice.getText());
                intent.putExtra("product_strike_price", holder.subProductStrikeThroughPrice.getText());
                intent.putExtra("products_pcs", holder.subProductPcs.getText());
                intent.putExtra("product_strike_offer", holder.subProductStrikeOffer.getText());
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return subProductsModelList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {


        ImageView subProductImage;
        TextView subProductPrice, subProductPcs, subProductTitle, subProductStrikeThroughPrice, subProductStrikeOffer;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            subProductImage = itemView.findViewById(R.id.sub_product_img);
            subProductTitle = itemView.findViewById(R.id.sub_product_title);
            subProductPrice = itemView.findViewById(R.id.sub_product_price);
            subProductStrikeThroughPrice = itemView.findViewById(R.id.sub_product_strike_through_price);
            subProductPcs = itemView.findViewById(R.id.sub_product_pcs);
            subProductStrikeOffer = itemView.findViewById(R.id.sub_product_strike_offer);

        }
    }
}
