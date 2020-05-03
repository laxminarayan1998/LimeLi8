package com.limelite.limeli8;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

public class MyBookingAdapter extends RecyclerView.Adapter<MyBookingAdapter.CustomViewHolder> {

    private Context context;
    List<MyBookingsModel> myBookingsModels;

    public MyBookingAdapter(Context context, List<MyBookingsModel> myBookingsModels) {
        this.context = context;
        this.myBookingsModels = myBookingsModels;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_booking_view, parent, false);
        return new MyBookingAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, final int position) {

        if (myBookingsModels.get(position).getOrderStatus().equals("Order Placed")) {
            holder.status_icon.setColorFilter(Color.YELLOW);
        } else if (myBookingsModels.get(position).getOrderStatus().equals("Cancelled")) {
            holder.status_icon.setColorFilter(Color.RED);
        }

        holder.product_name.setText(myBookingsModels.get(position).getProductName());
        holder.status_text.setText(myBookingsModels.get(position).getOrderStatus());

        Picasso.get()
                .load(myBookingsModels.get(position).getProductUrl())
                .into(holder.product_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyBookingsDetails.class);
                intent.putExtra("key", myBookingsModels.get(position).getKey());
                Log.i("send key", myBookingsModels.get(position).getKey());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return myBookingsModels.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView product_name, status_text;
        ImageView product_image, status_icon;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            product_image = itemView.findViewById(R.id.product_image);
            product_name = itemView.findViewById(R.id.product_name);
            status_icon = itemView.findViewById(R.id.status_icon);
            status_text = itemView.findViewById(R.id.status_text);

        }
    }
}
