package com.limelite.limeli8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.Objects;

public class AnotherLayout extends AppCompatActivity {

    FrameLayout frameLayout;
    TextView finalPrice, strikePrice, flatOff, name, desc_head;
    WebView desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_another_layout);

        final DecimalFormat decimalFormat = new DecimalFormat("₹###,###.##");

        final CustomProgressBar customProgressBar = new CustomProgressBar(this);
        customProgressBar.startProgressBar();

        Toolbar toolbar = findViewById(R.id.homePageToolbar);
        final YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        final ImageView imageView = findViewById(R.id.image_view);
        frameLayout = findViewById(R.id.sub_product_cover_img);
        finalPrice = findViewById(R.id.final_price);
        strikePrice = findViewById(R.id.strike_price);
        flatOff = findViewById(R.id.flat_off);
        name = findViewById(R.id.name);
        desc_head = findViewById(R.id.desc_heading);
        desc = findViewById(R.id.description);

        desc.getSettings().setJavaScriptEnabled(true);
        desc.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Hello");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_back_button);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setTitleTextAppearance(this, R.style.fontFamily);


        Intent intent = getIntent();
        String key = intent.getStringExtra("key");
        String productName = HorizontalScrollRecyclerAdapter.PRODUCT_NAME;


        if (key != null && productName != null) {

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products/" + productName + "/subProducts/" + key);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                    final String snapshotId = dataSnapshot.child("videoId").getValue(String.class);
                    final String productName = dataSnapshot.child("name").getValue(String.class);
                    final String image = dataSnapshot.child("imageUrl").getValue(String.class);
                    final String description = dataSnapshot.child("description").getValue(String.class);
                    final String flat_off = dataSnapshot.child("flatOff").getValue(String.class);
                    final String strike_price = " " + dataSnapshot.child("strikePrice").getValue(String.class) + " ";

                    if (productName != null) {
                        name.setText(productName);
                    }

                    if (flat_off != null) {

                        if (flat_off.equals("0")) {
                            flatOff.setVisibility(View.GONE);
                            strikePrice.setVisibility(View.GONE);
                        }

                        Double flatOffDouble = Double.parseDouble(flat_off);
                        Double strikePriceDouble = Double.parseDouble(strike_price);
                        Double finalPriceDouble = strikePriceDouble - flatOffDouble;

                        finalPrice.setText(decimalFormat.format(finalPriceDouble));
                        strikePrice.setText(strike_price);
                        strikePrice.setPaintFlags(strikePrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                        String flatOfferText = "Flat ₹" + flat_off + " off";
                        flatOff.setText(flatOfferText);

                    }

                    if (description != null) {
                        desc.loadData(description, "text/html; charset=utf-8", "utf-8");
                    }

                    final String videoId;

                    if (snapshotId != null) {

                        videoId = snapshotId;

                        getLifecycle().addObserver(youTubePlayerView);
                        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                            @Override
                            public void onReady(@NonNull YouTubePlayer youTubePlayer) {

                                youTubePlayer.cueVideo(videoId, 0f);
                                customProgressBar.dismissProgressBar();

                            }
                        });

                    } else {
                        youTubePlayerView.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);
                        Picasso.get()
                                .load(image)
                                .into(imageView);
                        customProgressBar.dismissProgressBar();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }


    }
}
