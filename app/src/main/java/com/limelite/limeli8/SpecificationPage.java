package com.limelite.limeli8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD;

public class SpecificationPage extends AppCompatActivity {

    private ImageView subProductImage;
    private TextView subProductStrikePrice, subProductTitle, subProductPrice, subProductPcs, subProductStrikeOffer;
    private LinearLayout optionLinearLayout;
    private Button continueButton;


    ArrayList optionTitle = new ArrayList();
    List<SubProductDetailsPrice> optionList = new ArrayList();
    List<SubProductDetailsPrice> selectedOptionsPrice = new ArrayList();

    TextView descHeading, description, offerText;

    String productName = "", productFinalPrice = "";
    List<String> optionKey = new ArrayList<>();
    List<String> optionValue = new ArrayList<>();
    String productImageUrl;

    int finalPrice;

    HashSet<String> hashSet = new HashSet<String>();
    List<String> hashy;
    HashSet<SubProductDetailsPrice> gotList = new HashSet<SubProductDetailsPrice>();

    ProgressBar progressBar;
    CustomProgressBar customProgressBar;

    Map<String, String> map;
    LinearLayout offerLayout;
    String deliveryPriceString, offerPrice, offerOnOrderAbove, product, subProduct, productStrikePrice, productPcs, productStrikeOffer;
    String fromOfferIntentProduct, fromOfferIntentSubProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specification_page);

        customProgressBar = new CustomProgressBar(SpecificationPage.this);


        subProductImage = findViewById(R.id.sub_product_img);
        subProductTitle = findViewById(R.id.sub_product_title);
        subProductPrice = findViewById(R.id.sub_product_price);
        subProductPcs = findViewById(R.id.sub_product_pcs);
        subProductStrikeOffer = findViewById(R.id.sub_product_strike_offer);
        subProductStrikePrice = findViewById(R.id.sub_product_strike_through_price);
        optionLinearLayout = findViewById(R.id.radio_button_group);
        descHeading = findViewById(R.id.desc_heading);
        description = findViewById(R.id.description);
        continueButton = findViewById(R.id.continue_button);
        offerLayout = findViewById(R.id.on_order_above_layout);
        offerText = findViewById(R.id.offer_text);


        progressBar = findViewById(R.id.spin_kit);


        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_back_btn_with_red_bg);

        Intent intent = getIntent();
        fromOfferIntentProduct = intent.getStringExtra("product");
        fromOfferIntentSubProduct = intent.getStringExtra("subProduct");


        if (fromOfferIntentProduct == null || fromOfferIntentSubProduct == null) {
            product = HorizontalScrollRecyclerAdapter.PRODUCT_NAME;
            subProduct = subProductsRecyclerAdapter.SUB_PRODUCT_NAME;

            Bundle bundle = getIntent().getExtras();
            productImageUrl = bundle.getString("product_image");
            productName = bundle.getString("product_name");
            productFinalPrice = bundle.getString("product_final_price");
            productStrikePrice = bundle.getString("product_strike_price");
            productPcs = bundle.getString("products_pcs");
            productStrikeOffer = bundle.getString("product_strike_offer");


        } else {
            product = fromOfferIntentProduct;
            subProduct = fromOfferIntentSubProduct;

            productImageUrl = intent.getStringExtra("product_image");
            productName = intent.getStringExtra("product_name");
            productFinalPrice = intent.getStringExtra("product_final_price");
            productStrikePrice = intent.getStringExtra("product_strike_price");
            productPcs = intent.getStringExtra("products_pcs");
            productStrikeOffer = intent.getStringExtra("product_strike_offer");

        }

        DatabaseReference offerPriceRef = FirebaseDatabase.getInstance().getReference("Products/" + product + "/subProducts/" + subProduct);
        offerPriceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                offerPrice = dataSnapshot.child("offerPrice").getValue(String.class);
                offerOnOrderAbove = dataSnapshot.child("offerOnAbove").getValue(String.class);
                if (offerPrice != null || offerOnOrderAbove != null) {
                    offerLayout.setVisibility(View.VISIBLE);

                    DatabaseReference additionalOfferText = FirebaseDatabase.getInstance().getReference("Products/" + product + "/subProducts/" + subProduct + "/offerText");
                    additionalOfferText.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                offerText.setText(Html.fromHtml(dataSnapshot.getValue(String.class)));
                            } else {
                                offerText.setText("Get ₹" + offerPrice + " off on order above " + offerOnOrderAbove + "pc(s)");
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    offerLayout.setVisibility(View.GONE);
                    offerPrice = String.valueOf(0);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        optionCreationFromDatabase();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("data/deliveryFee");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                deliveryPriceString = dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customProgressBar.startProgressBar();
                customProgressBar.dialog.setCanceledOnTouchOutside(false);

                map = new HashMap<>();
                for (int i = 0; i < optionValue.size(); i++) {

                    map.put((String) optionTitle.get(i), optionValue.get(i));

                }

                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Users/" + FirebaseAuth.getInstance().getUid() + "/userDetails");
                databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Intent intent = new Intent(SpecificationPage.this, OrderSummary.class);
                            intent.putExtra("hashMap", (Serializable) map);
                            intent.putExtra("product_name", subProductTitle.getText());
                            intent.putExtra("product_price", subProductPrice.getText());
                            intent.putExtra("product_url", productImageUrl);
                            intent.putExtra("delivery_price", deliveryPriceString);

                            if (fromOfferIntentProduct == null || fromOfferIntentSubProduct == null) {

                            } else {
                                intent.putExtra("product", fromOfferIntentProduct);
                                intent.putExtra("subProduct", fromOfferIntentSubProduct);
                                Log.i("product", fromOfferIntentProduct);
                                Log.i("subProduct", fromOfferIntentSubProduct);
                            }


                            Log.i("hashMap", String.valueOf((Serializable) map));
                            Log.i("product_name", String.valueOf(subProductTitle.getText()));
                            Log.i("product_price", String.valueOf(subProductPrice.getText()));
                            Log.i("product_url", productImageUrl);
                            Log.i("delivery_price", deliveryPriceString);

//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    startActivity(new Intent(getApplicationContext(), LogIn.class));
//                                    finish();
//                                }
//                            }, 2000);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(SpecificationPage.this, UserDetailsActivity.class);
                            customProgressBar.dismissProgressBar();
                            startActivity(intent);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

    }

    void updatePrice(final SubProductDetailsPrice subProductDetailsPrice) {

        finalPrice = Integer.parseInt(subProductPrice.getText().toString());
        gotList.clear();

        gotList.addAll(Collections.singleton(subProductDetailsPrice));
        selectedOptionsPrice.clear();
        selectedOptionsPrice.addAll(gotList);

        int num = 0;

        hashy = new ArrayList(hashSet);
        for (int i = 0; i < selectedOptionsPrice.size(); i++) {
            int sum = Integer.parseInt(selectedOptionsPrice.get(i).getPrice());
            num += sum;
        }

        finalPrice += num;

    }

    View.OnClickListener getOnClickDoSomething(final RadioButton button, final String price, final RadioGroup rgp, final SubProductDetailsPrice subProductDetailsPrice) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = rgp.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectedId);

                //hashSet.addAll(Collections.singleton(String.valueOf(rgp.getId())));

                Log.i("haset", String.valueOf(hashSet));
                Log.i("id", String.valueOf(rgp.getId()));


                try {


                    if (hashSet.contains(String.valueOf(rgp.getId()))) {
                        gotList.clear();
                        optionCreationFromDatabase();
                        optionValue.clear();

                    } else {

                        updatePrice(subProductDetailsPrice);
                        optionValue.add(radioButton.getText().toString());
                        hashSet.add(String.valueOf(rgp.getId()));

                    }

                    //finalPrice += Integer.parseInt(price);


                } catch (Exception e) {
                    e.printStackTrace();

                    updatePrice(subProductDetailsPrice);
                    optionValue.add(radioButton.getText().toString());
                    hashSet.add(String.valueOf(rgp.getId()));


                }

                smallTimer(String.valueOf(finalPrice));


            }


        };
    }

    void smallTimer(final String price) {
        new CountDownTimer(0, 0) {
            @Override
            public void onTick(long millisUntilFinished) {

                //customProgressBar.startProgressBar();
            }

            @Override
            public void onFinish() {

//                try {
//                    customProgressBar.dismissProgressBar();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                //finalPrice += Integer.parseInt(price);
                subProductPrice.setText(String.valueOf(price));
            }
        }.start();
    }

    void optionCreationFromDatabase() {

        customProgressBar.startProgressBar();

        Picasso.get()
                .load(productImageUrl)
                .into(subProductImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        customProgressBar.dismissProgressBar();
                    }

                    @Override
                    public void onError(Exception e) {
                        customProgressBar.dismissProgressBar();
                    }
                });

        subProductTitle.setText(productName);
        subProductPrice.setText(productFinalPrice);
        subProductPcs.setText(productPcs);
        subProductStrikeOffer.setText(productStrikeOffer);
        subProductStrikePrice.setText(productStrikePrice);


        subProductStrikePrice.setPaintFlags(subProductStrikePrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        try {
            finalPrice = Integer.parseInt(subProductPrice.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


        optionLinearLayout.removeAllViews();
        hashSet.clear();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products/Visiting Card/subProducts/" + productName + "/details");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                optionTitle.clear();
                optionList.clear();


                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {


                    optionTitle.add(dataSnapshot1.getKey());


                }


                for (int i = 0; i < optionTitle.size(); i++) {


                    optionList.clear();

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products/Visiting Card/subProducts/" + productName + "/details/" + optionTitle.get(i));

                    final int finalI1 = i;
                    final int finalI = i;
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            optionList.clear();

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {


                                SubProductDetailsPrice subProductDetailsPrice = dataSnapshot1.getValue(SubProductDetailsPrice.class);

                                optionList.add(subProductDetailsPrice);

                            }

                            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                            param.setMargins(20, 20, 15, 20);


                            TextView textView = new TextView(SpecificationPage.this);
                            textView.setText(String.valueOf(optionTitle.get(finalI1)));
                            textView.setTextSize(16);
                            textView.setTextColor(Color.GRAY);
                            textView.setLayoutParams(param);


                            final RadioGroup rgp = new RadioGroup(SpecificationPage.this);
                            rgp.setId(View.generateViewId());
                            rgp.setOrientation(LinearLayout.HORIZONTAL);

                            for (int i = 0; i < optionList.size(); i++) {
                                RadioButton rbn = new RadioButton(SpecificationPage.this);
                                rbn.setId(View.generateViewId());
                                rbn.setText(String.valueOf(optionList.get(i).getOption()));
                                rbn.setTextSize(12);
                                rbn.setTextColor(Color.WHITE);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                                params.setMargins(12, 5, 16, 5);
                                rbn.setButtonDrawable(null);
                                rgp.setGravity(Gravity.CENTER);
                                rbn.setGravity(Gravity.CENTER);
                                rbn.setBackground(ContextCompat.getDrawable(SpecificationPage.this, R.drawable.custom_radio_button));
                                rbn.setLayoutParams(params);
                                rgp.setLayoutParams(params);
                                rgp.addView(rbn);
                                rbn.setOnClickListener(getOnClickDoSomething(rbn, optionList.get(i).getPrice(), rgp, optionList.get(i)));
                                //rgp.setOnCheckedChangeListener(getOnClickDoSomething(rbn, rgp));

                            }


                            optionLinearLayout.setGravity(Gravity.CENTER);
                            optionLinearLayout.addView(textView);
                            optionLinearLayout.addView(rgp);


//                            rgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                                @Override
//                                public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//                                    hashSet.addAll(Collections.singleton(String.valueOf(rgp.getId())));
//                                    Log.i("hassy", String.valueOf(hashy));
//                                    Log.i("id", String.valueOf(rgp.getId()));
//
//
//                                }
//                            });


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Products/Visiting Card/subProducts/" + productName + "/descHead/");
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Products/Visiting Card/subProducts/" + productName + "/description/");

        //Log.i("name", productName);
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                descHeading.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                description.setText(Html.fromHtml(dataSnapshot.getValue(String.class)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onPause() {
        try {
            customProgressBar.dismissProgressBar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }
}
