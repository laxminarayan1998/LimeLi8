package com.limelite.limeli8;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class HorizontalScrollRecyclerAdapter extends RecyclerView.Adapter<HorizontalScrollRecyclerAdapter.CustomViewHolder> {

    private Context context;
    private List<ProductModel> printSectionItems;
    static String PRODUCT_NAME;
    private int lastPosition = -1;

    public HorizontalScrollRecyclerAdapter(Context context, List<ProductModel> printSectionItems) {
        this.context = context;
        this.printSectionItems = printSectionItems;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.print_item, parent, false);

        if (HomePage.MAKE_ITEM_CENTER == true) {

            LinearLayout linearLayout = view.findViewById(R.id.single_item);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 30, 0, 30);
            linearLayout.setLayoutParams(params);
        }

        return new CustomViewHolder(view);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, final int position) {

        //holder.itemImage.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_anim));
        setAnimation(holder.itemImage, position);

        holder.itemText.setText(printSectionItems.get(position).getName());
        Picasso.get()
                .load(printSectionItems.get(position).getImageUrl())
                .into(holder.itemImage);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SlidePageAdapter.MAINPRODUCTNAMEFROMBANNER = null;
                PRODUCT_NAME = printSectionItems.get(position).getName();
                Intent intent = new Intent(context, SubProductPage.class);
                intent.putExtra("productName", printSectionItems.get(position).getName());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return printSectionItems.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImage;
        TextView itemText;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.item_image);
            itemText = itemView.findViewById(R.id.item_title);

        }
    }
}
