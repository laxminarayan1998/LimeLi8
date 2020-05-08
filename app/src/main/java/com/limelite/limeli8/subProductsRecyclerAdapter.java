package com.limelite.limeli8;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
    private List<SubProductsModel> subProductsModelList;
    static String SUB_PRODUCT_NAME;

    subProductsRecyclerAdapter(Context context, List<SubProductsModel> subProductsModelList) {
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

        if (strikeprice == null) {
            holder.rupeeSymbol.setVisibility(View.GONE);
        }

        final int strikePrice = strikeprice != null ? Integer.parseInt(strikeprice) : 0;
        final int flatOff = strikeprice != null ?  Integer.parseInt(flatoff) : 0;

        Picasso.get()
                .load(subProductsModelList.get(position).getImageUrl())
                .into(holder.subProductImage);

        holder.subProductTitle.setText(subProductsModelList.get(position).getName());

        String strkPriceMinusFlatOff = strikeprice != null && flatoff != null ? String.valueOf(strikePrice - flatOff) : "";
        holder.subProductPrice.setText(strkPriceMinusFlatOff);

        final String get_Pcs = subProductsModelList.get(position).getPieces() != null ? " for"+ subProductsModelList.get(position).getPieces() + "pcs" : "";
        holder.subProductPcs.setText(get_Pcs);

        if (flatoff.equals("0")) {
            holder.subProductStrikeOffer.setText("");
            holder.subProductStrikeThroughPrice.setText("");
        } else {
            String strike_Price = subProductsModelList.get(position).getStrikePrice() != null ? " " + subProductsModelList.get(position).getStrikePrice() + " " : "";
            holder.subProductStrikeThroughPrice.setText(strike_Price);
            holder.subProductStrikeThroughPrice.setPaintFlags(holder.subProductStrikeThroughPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            String flat_Off = subProductsModelList.get(position).getFlatOff() != null ? "flat ₹" + subProductsModelList.get(position).getFlatOff() + " off" : "";
            holder.subProductStrikeOffer.setText(flat_Off);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (subProductsModelList.get(position).getPieces() == null) {
                    Intent intent = new Intent(context, AnotherLayout.class);
                    intent.putExtra("key", holder.subProductTitle.getText());
                    context.startActivity(intent);
                    return;
                }

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

    class CustomViewHolder extends RecyclerView.ViewHolder {


        ImageView subProductImage;
        TextView subProductPrice, subProductPcs, subProductTitle, subProductStrikeThroughPrice, subProductStrikeOffer, rupeeSymbol;

        CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            subProductImage = itemView.findViewById(R.id.sub_product_img);
            subProductTitle = itemView.findViewById(R.id.sub_product_title);
            subProductPrice = itemView.findViewById(R.id.sub_product_price);
            subProductStrikeThroughPrice = itemView.findViewById(R.id.sub_product_strike_through_price);
            subProductPcs = itemView.findViewById(R.id.sub_product_pcs);
            subProductStrikeOffer = itemView.findViewById(R.id.sub_product_strike_offer);
            rupeeSymbol = itemView.findViewById(R.id.rupee_symbol);

        }
    }
}
